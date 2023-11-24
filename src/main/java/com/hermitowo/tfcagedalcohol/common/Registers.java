package com.hermitowo.tfcagedalcohol.common;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.util.Helpers;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

public class Registers
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final Map<AgedAlcohol, RegistryObject<LiquidBlock>> AGED_ALCOHOL = Helpers.mapOfKeys(AgedAlcohol.class, fluid ->
        registerBlock("fluid/" + fluid.getId(), () -> new LiquidBlock(AgedAlcoholFluids.AGED_ALCOHOL.get(fluid).source(), BlockBehaviour.Properties.copy(Blocks.WATER)))
    );
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final Map<AgedAlcohol, RegistryObject<BucketItem>> FLUID_BUCKETS = Helpers.mapOfKeys(AgedAlcohol.class, fluid ->
        registerItem("bucket/aged_" + fluid.name(), () -> new BucketItem(AgedAlcoholFluids.AGED_ALCOHOL.get(fluid).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        return BLOCKS.register(name.toLowerCase(Locale.ROOT), block);
    }

    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
