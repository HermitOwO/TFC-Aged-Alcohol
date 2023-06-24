package com.hermitowo.tfcagedalcohol;

import com.hermitowo.tfcagedalcohol.common.AgedAlcoholFluids;
import com.hermitowo.tfcagedalcohol.common.Registers;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(TFCAgedAlcohol.MOD_ID)
public class TFCAgedAlcohol
{
    public static final String MOD_ID = "tfcagedalcohol";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFCAgedAlcohol()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Registers.addRegistersToEventBus(eventBus);

        MinecraftForge.EVENT_BUS.addListener(this::onDrink);

        if (FMLEnvironment.dist == Dist.CLIENT)
            MinecraftForge.EVENT_BUS.addListener(this::onTooltip);
    }

    private void onDrink(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntity() instanceof Player player)
        {
            ItemStack stack = event.getItem();
            stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap ->
                AgedAlcoholFluids.getAlcohol(cap).ifPresent(alcohol ->
                    player.addEffect(alcohol.getEffectInstance())));
        }
    }

    private void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack fluidStack = cap.getFluidInTank(0);
            if (!fluidStack.isEmpty())
            {
                event.getToolTip().add(new TranslatableComponent(fluidStack.getTranslationKey()).withStyle(ChatFormatting.GRAY));
                var fluid = AgedAlcoholFluids.AGED_ALCOHOL.inverse().keySet().stream().filter(f ->
                    f.getSource() == fluidStack.getFluid()
                ).findAny();
                fluid.ifPresent(f -> event.getToolTip().add(AgedAlcoholFluids.AGED_ALCOHOL.inverse().get(f).getTooltip()));
            }
        });
    }
}
