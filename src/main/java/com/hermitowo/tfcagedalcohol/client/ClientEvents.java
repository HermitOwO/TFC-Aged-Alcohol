package com.hermitowo.tfcagedalcohol.client;

import java.util.Objects;

import com.hermitowo.tfcagedalcohol.common.AgedAlcoholFluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

import net.dries007.tfc.client.extensions.FluidRendererExtension;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;
import static net.dries007.tfc.client.ClientEventHandler.*;

public class ClientEvents
{
    public static void init(IEventBus bus)
    {
        bus.addListener(ClientEvents::registerColorHandlerItems);
        bus.addListener(ClientEvents::registerExtensions);
    }

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        for (Fluid fluid : BuiltInRegistries.FLUID)
        {
            if (Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(fluid)).getNamespace().equals(MOD_ID))
            {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }

    public static void registerExtensions(RegisterClientExtensionsEvent event)
    {
        AgedAlcoholFluids.AGED_ALCOHOL.forEach((fluid, holder) -> event.registerFluidType(
            new FluidRendererExtension(fluid.getColor(), WATER_STILL, WATER_FLOW, WATER_OVERLAY, UNDERWATER_LOCATION),
            holder.getType()
        ));
    }
}
