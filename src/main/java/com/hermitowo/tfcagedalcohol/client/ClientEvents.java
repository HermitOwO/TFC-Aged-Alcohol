package com.hermitowo.tfcagedalcohol.client;

import java.util.Objects;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

public class ClientEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEvents::registerColorHandlerItems);
    }

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        for (Fluid fluid : ForgeRegistries.FLUIDS.getValues())
        {
            if (Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getNamespace().equals(MOD_ID))
            {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }
}
