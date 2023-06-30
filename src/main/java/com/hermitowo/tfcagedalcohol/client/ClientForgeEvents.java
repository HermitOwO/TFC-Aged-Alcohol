package com.hermitowo.tfcagedalcohol.client;

import com.hermitowo.tfcagedalcohol.common.AgedAlcoholFluids;
import com.hermitowo.tfcagedalcohol.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.dries007.tfc.util.Drinkable;
import net.dries007.tfc.util.Helpers;

public class ClientForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ClientForgeEvents::onTooltip);
    }

    private static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack fluidStack = cap.getFluidInTank(0);
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
                    drinkable.getEffects()
                        .stream()
                        .findAny()
                        .map(effect -> event.getToolTip().add(getTooltip(effect.type(), effect.duration(), effect.amplifier())));
                }
            }
        });
    }

    private static Component getTooltip(MobEffect effect, int duration, int amplifier)
    {
        MobEffectInstance effectInstance = new MobEffectInstance(effect, duration, amplifier);
        return Helpers.literal(effect.getDisplayName().getString() + displayedPotency(amplifier) + "(" + MobEffectUtil.formatDuration(effectInstance, 1) + ")").withStyle(effect.getCategory().getTooltipFormatting());
    }

    private static String displayedPotency(int amplifier)
    {
        return switch (amplifier + 1)
        {
            case 2 -> " II ";
            case 3 -> " III ";
            default -> " ";
        };
    }
}
