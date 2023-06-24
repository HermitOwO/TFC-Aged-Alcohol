package com.hermitowo.tfcagedalcohol.common;

import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

public class Registers
{
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final Map<AgedAlcohol, RegistryObject<LiquidBlock>> AGED_ALCOHOL = Helpers.mapOfKeys(AgedAlcohol.class, fluid ->
        registerBlock("fluid/" + fluid.getId(), () -> new LiquidBlock(AgedAlcoholFluids.AGED_ALCOHOL.get(fluid).source(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100f).noDrops()))
    );
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final Map<AgedAlcohol, RegistryObject<BucketItem>> FLUID_BUCKETS = Helpers.mapOfKeys(AgedAlcohol.class, fluid ->
        registerItem("bucket/aged_" + fluid.name(), () -> new BucketItem(AgedAlcoholFluids.AGED_ALCOHOL.get(fluid).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(TFCItemGroup.MISC)))
    );
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);

    public static void addRegistersToEventBus(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        FLUIDS.register(eventBus);
    }

    public static <F extends FlowingFluid> FlowingFluidRegistryObject<F> registerFluid(String sourceName, String flowingName, Consumer<ForgeFlowingFluid.Properties> builder, FluidAttributes.Builder attributes, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory)
    {
        return RegistrationHelpers.registerFluid(FLUIDS, sourceName, flowingName, builder, attributes, sourceFactory, flowingFactory);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        return BLOCKS.register(name.toLowerCase(Locale.ROOT), block);
    }

    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
