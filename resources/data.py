from mcresources import ResourceManager, utils
from mcresources.type_definitions import ResourceIdentifier, Json

ALCOHOLS = ('beer', 'cider', 'rum', 'sake', 'vodka', 'whiskey', 'corn_whiskey', 'rye_whiskey')
AGED_ALCOHOLS = ('aged_beer', 'aged_cider', 'aged_rum', 'aged_sake', 'aged_vodka', 'aged_whiskey', 'aged_corn_whiskey', 'aged_rye_whiskey')



def generate(rm: ResourceManager):
    for fluid in AGED_ALCOHOLS:
        water_based_fluid(rm, fluid)
        rm.fluid_tag('aged_alcohols', fluid)

    for alcohol in ALCOHOLS:
        barrel_sealed_recipe(rm, 'aged_' + alcohol, 'Aged ' + alcohol.capitalize(), 576000, '100 tfc:' + alcohol, '100 tfcagedalcohol:aged_' + alcohol)

    rm.fluid_tag('tfc:drinkables', '#tfcagedalcohol:aged_alcohols')
    rm.fluid_tag('minecraft:water', '#tfcagedalcohol:aged_alcohols')
    drinkable(rm, 'aged_alcohol', '#tfcagedalcohol:aged_alcohols', 15, 2000)



def fluid_stack(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return data_in
    fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
    assert not tag, 'fluid_stack() cannot be a tag'
    return {
        'fluid': fluid,
        'amount': amount
    }


def fluid_stack_ingredient(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return {
            'ingredient': fluid_ingredient(data_in['ingredient']),
            'amount': data_in['amount']
        }
    if pair := utils.maybe_unordered_pair(data_in, int, object):
        amount, fluid = pair
        return {'ingredient': fluid_ingredient(fluid), 'amount': amount}
    fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
    if tag:
        return {'ingredient': {'tag': fluid}, 'amount': amount}
    else:
        return {'ingredient': fluid, 'amount': amount}

def fluid_ingredient(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return data_in
    elif isinstance(data_in, list):
        return [*utils.flatten_list([fluid_ingredient(e) for e in data_in])]
    else:
        fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
        if tag:
            return {'tag': fluid}
        else:
            return fluid

def water_based_fluid(rm: ResourceManager, name: str):
    rm.blockstate(('fluid', name)).with_block_model({'particle': 'minecraft:block/water_still'}, parent=None).with_lang(lang(name)).with_tag('all_fluids')
    rm.fluid_tag(name, 'tfcagedalcohol:%s' % name, 'tfcagedalcohol:flowing_%s' % name)

    item = rm.custom_item_model(('bucket', name), 'forge:bucket', {
        'parent': 'forge:item/bucket',
        'fluid': 'tfcagedalcohol:%s' % name
    })
    item.with_lang(lang('%s bucket', name))
    rm.lang('fluid.tfcagedalcohol.%s' % name, lang(name))

def drinkable(rm: ResourceManager, name_parts: utils.ResourceIdentifier, fluid: utils.Json, thirst: int, intoxication: int):
    rm.data(('tfc', 'drinkables', name_parts), {
        'ingredient': fluid_ingredient(fluid),
        'thirst': thirst,
        'intoxication': intoxication
    })

def barrel_sealed_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, translation: str, duration: int, input_fluid: Json, output_fluid: Json):
    rm.recipe(('barrel', name_parts), 'tfc:barrel_sealed', {
        'input_fluid': fluid_stack_ingredient(input_fluid),
        'output_fluid': fluid_stack(output_fluid),
        'duration': duration
    })
    res = utils.resource_location('tfcagedalcohol', name_parts)
    rm.lang('tfc.recipe.barrel.' + res.domain + '.barrel.' + res.path.replace('/', '.'), lang(translation))

def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()