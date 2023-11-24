package com.hermitowo.tfcagedalcohol.client;

import com.hermitowo.tfcagedalcohol.common.AgedAlcoholFluids;
import com.hermitowo.tfcagedalcohol.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;

import net.dries007.tfc.util.Drinkable;

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
        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(cap -> {
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
        return Component.literal(effect.getDisplayName().getString() + displayedPotency(amplifier) + "(" + formatDuration(effectInstance) + ")").withStyle(effect.getCategory().getTooltipFormatting());
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

    private static String formatDuration(MobEffectInstance effect)
    {
        return StringUtil.formatTickDuration(Mth.floor(effect.getDuration()));
    }
}
