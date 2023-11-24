package com.hermitowo.tfcagedalcohol;

import com.hermitowo.tfcagedalcohol.client.ClientEvents;
import com.hermitowo.tfcagedalcohol.client.ClientForgeEvents;
import com.hermitowo.tfcagedalcohol.common.AgedAlcoholFluids;
import com.hermitowo.tfcagedalcohol.common.CreativeTabs;
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
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        AgedAlcoholFluids.FLUIDS.register(bus);
        AgedAlcoholFluids.FLUID_TYPES.register(bus);
        Registers.BLOCKS.register(bus);
        Registers.ITEMS.register(bus);
        CreativeTabs.CREATIVE_TABS.register(bus);

        Config.init();

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEvents.init();
            ClientForgeEvents.init();
        }
    }
}
