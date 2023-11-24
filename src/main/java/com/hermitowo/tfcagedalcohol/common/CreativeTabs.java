package com.hermitowo.tfcagedalcohol.common;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

@SuppressWarnings({"unused", "SameParameterValue"})
public class CreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = register("main", () -> new ItemStack(Registers.FLUID_BUCKETS.get(AgedAlcohol.WHISKEY).get()), CreativeTabs::fillTab);

    private static RegistryObject<CreativeModeTab> register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        return CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
            .icon(icon)
            .title(Component.translatable("tfcagedalcohol.creative_tab." + name))
            .displayItems(displayItems)
            .build());
    }

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        consumeOurs(ForgeRegistries.FLUIDS, fluid -> out.accept(fluid.getBucket()));
    }

    private static <T> void consumeOurs(IForgeRegistry<T> registry, Consumer<T> consumer)
    {
        for (T value : registry)
        {
            if (Objects.requireNonNull(registry.getKey(value)).getNamespace().equals(MOD_ID))
            {
                consumer.accept(value);
            }
        }
    }
}
