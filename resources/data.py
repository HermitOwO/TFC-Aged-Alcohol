from mcresources import ResourceManager, utils
from mcresources.type_definitions import Json
from typing import NamedTuple, Optional, Dict

ALCOHOLS = ('beer', 'cider', 'rum', 'sake', 'vodka', 'whiskey', 'corn_whiskey', 'rye_whiskey')


class Effect(NamedTuple):
    type: str
    amplifier: int
    duration: int


AGED_ALCOHOLS: Dict[str, Effect] = {
    'aged_beer': Effect('absorption', 1, 24000),
    'aged_cider': Effect('speed', 0, 6400),
    'aged_rum': Effect('speed', 1, 3200),
    'aged_sake': Effect('resistance', 0, 6400),
    'aged_vodka': Effect('resistance', 1, 3200),
    'aged_whiskey': Effect('haste', 1, 3200),
    'aged_corn_whiskey': Effect('haste', 0, 6400),
    'aged_rye_whiskey': Effect('haste', 0, 6400)
}


def generate(rm: ResourceManager):
    for alcohol, effect in AGED_ALCOHOLS.items():
        water_based_fluid(rm, alcohol)
        rm.fluid_tag('aged_alcohols', alcohol)
        drinkable(rm, '%s' % alcohol, 'tfcagedalcohol:%s' % alcohol, 15, 2000, effects=[{'type': 'minecraft:%s' % effect.type, 'duration': effect.duration, 'amplifier': effect.amplifier}])

    for alcohol in ALCOHOLS:
        barrel_sealed_recipe(rm, 'aged_' + alcohol, 'Ageing ' + alcohol.capitalize(), 576000, '100 tfc:' + alcohol, '100 tfcagedalcohol:aged_' + alcohol)

    rm.fluid_tag('tfc:drinkables', '#tfcagedalcohol:aged_alcohols')
    rm.fluid_tag('minecraft:water', '#tfcagedalcohol:aged_alcohols')

    rm.lang("tfcagedalcohol.creative_tab.main", "TFC Aged Alcohol")


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

    item = rm.custom_item_model(('bucket', name), 'forge:fluid_container', {
        'parent': 'forge:item/bucket',
        'fluid': 'tfcagedalcohol:%s' % name
    })
    item.with_lang(lang('%s bucket', name))
    rm.lang('fluid.tfcagedalcohol.%s' % name, lang(name))


def drinkable(rm: ResourceManager, name_parts: utils.ResourceIdentifier, fluid: utils.Json, thirst: Optional[int] = None, intoxication: Optional[int] = None, effects: Optional[utils.Json] = None, food: Optional[utils.Json] = None):
    rm.data(('tfc', 'drinkables', name_parts), {
        'ingredient': fluid_ingredient(fluid),
        'thirst': thirst,
        'intoxication': intoxication,
        'effects': effects,
        'food': food
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
