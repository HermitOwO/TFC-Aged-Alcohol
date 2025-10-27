package com.hermitowo.tfcagedalcohol.common;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.TFCCreativeTabs;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

@SuppressWarnings({"unused", "SameParameterValue"})
public class CreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final TFCCreativeTabs.Id MAIN = register("main", () -> new ItemStack(Registers.FLUID_BUCKETS.get(AgedAlcohol.WHISKEY).get()), CreativeTabs::fillTab);

    private static TFCCreativeTabs.Id register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        return new TFCCreativeTabs.Id(CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
            .icon(icon)
            .title(Component.translatable("tfcagedalcohol.creative_tab." + name))
            .displayItems(displayItems)
            .build()), displayItems);
    }

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        AgedAlcoholFluids.FLUIDS.getEntries().forEach(fluid -> out.accept(fluid.value().getBucket()));
    }
}
