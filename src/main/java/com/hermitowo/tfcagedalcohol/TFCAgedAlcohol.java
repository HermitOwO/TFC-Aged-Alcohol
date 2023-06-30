package com.hermitowo.tfcagedalcohol;

import com.hermitowo.tfcagedalcohol.client.ClientForgeEvents;
import com.hermitowo.tfcagedalcohol.common.Registers;
import com.hermitowo.tfcagedalcohol.config.Config;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
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
        Config.init();

        if (FMLEnvironment.dist == Dist.CLIENT)
            ClientForgeEvents.init();
    }
}
