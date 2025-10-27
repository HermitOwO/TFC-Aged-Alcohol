from mcresources import ResourceManager, utils, RecipeContext
from mcresources.type_definitions import Json, ResourceIdentifier, JsonObject
from typing import NamedTuple, Optional, Dict

ALCOHOLS = ('beer', 'cider', 'rum', 'sake', 'vodka', 'whiskey', 'corn_whiskey', 'rye_whiskey')


class Effect(NamedTuple):
    type: str
    amplifier: int
    duration: int


AGED_ALCOHOLS: Dict[str, Effect] = {
    'aged_beer': Effect('absorption', 1, 6400),
    'aged_cider': Effect('speed', 0, 6400),
    'aged_rum': Effect('speed', 1, 3200),
    'aged_sake': Effect('resistance', 0, 6400),
    'aged_vodka': Effect('resistance', 1, 3200),
    'aged_whiskey': Effect('haste', 1, 3200),
    'aged_corn_whiskey': Effect('haste', 0, 6400),
    'aged_rye_whiskey': Effect('haste', 0, 6400),
    'aged_mead': Effect('regeneration', 0, 6400)
}


def generate(rm: ResourceManager):
    for alcohol, effect in AGED_ALCOHOLS.items():
        water_based_fluid(rm, alcohol)
        rm.fluid_tag('aged_alcohols', alcohol)
        drinkable(rm, '%s' % alcohol, 'tfcagedalcohol:%s' % alcohol, 15, 2000, may_drink_when_full=True, effects=[{'effect': 'minecraft:%s' % effect.type, 'duration': effect.duration, 'amplifier': effect.amplifier, 'chance': 1.0}])

    for alcohol in ALCOHOLS:
        barrel_sealed_recipe(rm, 'aged_' + alcohol, 'Ageing ' + alcohol.capitalize(), 691200, '100 tfc:' + alcohol, '100 tfcagedalcohol:aged_' + alcohol)
    barrel_sealed_recipe(rm, 'aged_mead', 'Ageing Mead', 691200, '100 firmalife:mead', '100 tfcagedalcohol:aged_mead', {'type': 'neoforge:mod_loaded', 'modid': 'firmalife'})

    rm.fluid_tag('tfc:drinkables', '#tfcagedalcohol:aged_alcohols')
    rm.block_tag('minecraft:replaceable', '#tfcagedalcohol:all_fluids')

    rm.lang("tfcagedalcohol.creative_tab.main", "TFC Aged Alcohol")


def fluid_stack(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return data_in
    fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
    assert not tag, 'fluid_stack() cannot be a tag'
    return {
        'id': fluid,
        'amount': amount
    }


def fluid_stack_ingredient(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return {
            'fluid': fluid_ingredient(data_in['ingredient']),
            'amount': data_in['amount']
        }
    if pair := utils.maybe_unordered_pair(data_in, int, object):
        amount, fluid = pair
        return {'fluid': fluid_ingredient(fluid), 'amount': amount}
    fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
    if tag:
        return {'tag': fluid, 'amount': amount}
    else:
        return {'fluid': fluid, 'amount': amount}


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
            return {'fluid': fluid}


def water_based_fluid(rm: ResourceManager, name: str):
    rm.blockstate(('fluid', name)).with_block_model({'particle': 'minecraft:block/water_still'}, parent=None).with_lang(lang(name)).with_tag('all_fluids')

    item = rm.custom_item_model(('bucket', name), 'neoforge:fluid_container', {
        'parent': 'neoforge:item/bucket',
        'fluid': 'tfcagedalcohol:%s' % name
    })
    item.with_lang(lang('%s bucket', name))
    rm.lang('fluid.tfcagedalcohol.%s' % name, lang(name))


def drinkable(rm: ResourceManager, name_parts: utils.ResourceIdentifier, fluid: utils.Json, water: Optional[int] = None, intoxication: Optional[int] = None, effects: Optional[utils.Json] = None, food: Optional[utils.Json] = None, may_drink_when_full: Optional[bool] = None):
    rm.data(('tfc', 'drinkable', name_parts), {
        'ingredient': fluid_ingredient(fluid),
        'water': water,
        'intoxication': intoxication,
        'effects': effects,
        'food': food,
        'may_drink_when_full': may_drink_when_full
    })


def barrel_sealed_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, translation: str, duration: int, input_fluid: Json, output_fluid: Json, conditions: Optional[Json] = None):
    recipe(rm, ('barrel', name_parts), 'tfc:barrel_sealed', {
        'input_fluid': fluid_stack_ingredient(input_fluid),
        'output_fluid': fluid_stack(output_fluid),
        'duration': duration,
    }, conditions=conditions)
    res = utils.resource_location('tfcagedalcohol', name_parts)
    rm.lang('tfc.recipe.barrel.' + res.domain + '.barrel.' + res.path.replace('/', '.'), lang(translation))


def recipe(self, name_parts: ResourceIdentifier, type_in: Optional[str], data_in: JsonObject, group: Optional[str] = None, conditions: Json = None) -> RecipeContext:
    """
    Creates a non-crafting recipe file, used for custom mod recipes using vanilla's data pack system
    :param name_parts: The resource location, including path elements.
    :param type_in: The type of the recipe.
    :param data_in: Data required by the recipe, as present in json
    :param group: The group.
    :param conditions: Any conditions for the recipe to be enabled.
    """
    res = utils.resource_location(self.domain, name_parts)
    self.write(('data', res.domain, 'recipe', res.path), {
        'type': type_in,
        'group': group,
        **data_in,
        'neoforge:conditions': utils.recipe_condition(conditions)
    })
    return RecipeContext(self, res)


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()
