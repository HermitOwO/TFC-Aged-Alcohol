package com.hermitowo.tfcagedalcohol.client;

import com.hermitowo.tfcagedalcohol.common.AgedAlcoholFluids;
import com.hermitowo.tfcagedalcohol.config.Config;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import net.dries007.tfc.util.data.Drinkable;

public class ClientForgeEvents
{
    public static void init()
    {
        final IEventBus bus = NeoForge.EVENT_BUS;

        bus.addListener(ClientForgeEvents::onTooltip);
    }

    private static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        final IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (handler == null)
            return;
        FluidStack fluidStack = handler.getFluidInTank(0);
        if (!fluidStack.isEmpty())
        {
            Drinkable alcohol = AgedAlcoholFluids.AGED_ALCOHOL
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getSource() == fluidStack.getFluid())
                .findAny()
                .map(entry -> Drinkable.get(entry.getValue().getSource()))
                .orElse(null);
            Drinkable drinkable = Config.CLIENT.showEffectTooltipForAllDrinkables.get() ? Drinkable.get(fluidStack.getFluid()) : alcohol;
            if (drinkable != null)
            {
                drinkable.effects()
                    .stream()
                    .findAny()
                    .map(effect -> event.getToolTip().add(getTooltip(effect.type(), effect.duration(), effect.amplifier())));
            }
        }
    }

    private static Component getTooltip(Holder<MobEffect> effect, int duration, int amplifier)
    {
        MobEffectInstance effectInstance = new MobEffectInstance(effect, duration, amplifier);
        MutableComponent component = Component.empty();

        component.append(Component.translatable(effectInstance.getDescriptionId()));
        component.append(" ");
        component.append(Component.translatable("potion.potency." + effectInstance.getAmplifier()));
        if (effectInstance.getAmplifier() > 0)
            component.append(" ");
        component.append("(" + formatDuration(effectInstance) + ")");
        return component.withStyle(effect.value().getCategory().getTooltipFormatting());
    }

    private static String formatDuration(MobEffectInstance effect)
    {
        return StringUtil.formatTickDuration(Mth.floor(effect.getDuration()), 20);
    }
}
