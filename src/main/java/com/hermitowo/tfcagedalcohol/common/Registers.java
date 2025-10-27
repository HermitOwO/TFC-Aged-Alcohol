package com.hermitowo.tfcagedalcohol.common;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

public class Registers
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);
    public static final Map<AgedAlcohol, TFCBlocks.Id<LiquidBlock>> AGED_ALCOHOL = Helpers.mapOf(AgedAlcohol.class, fluid ->
        registerBlock("fluid/" + fluid.getId(), () -> new LiquidBlock(AgedAlcoholFluids.AGED_ALCOHOL.get(fluid).getSource(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).noLootTable()))
    );
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);
    public static final Map<AgedAlcohol, TFCItems.ItemId> FLUID_BUCKETS = Helpers.mapOf(AgedAlcohol.class, fluid ->
        registerItem("bucket/aged_" + fluid.name(), () -> new BucketItem(AgedAlcoholFluids.AGED_ALCOHOL.get(fluid).getSource(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    private static <T extends Block> TFCBlocks.Id<T> registerBlock(String name, Supplier<T> block)
    {
        return new TFCBlocks.Id<>(BLOCKS.register(name.toLowerCase(Locale.ROOT), block));
    }

    private static TFCItems.ItemId registerItem(String name, Supplier<Item> item)
    {
        return new TFCItems.ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }
}
