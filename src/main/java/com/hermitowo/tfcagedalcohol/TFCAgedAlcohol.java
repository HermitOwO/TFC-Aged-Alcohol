package com.hermitowo.tfcagedalcohol;

import com.hermitowo.tfcagedalcohol.client.ClientEvents;
import com.hermitowo.tfcagedalcohol.client.ClientForgeEvents;
import com.hermitowo.tfcagedalcohol.common.AgedAlcoholFluids;
import com.hermitowo.tfcagedalcohol.common.CreativeTabs;
import com.hermitowo.tfcagedalcohol.common.Registers;
import com.hermitowo.tfcagedalcohol.config.Config;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(TFCAgedAlcohol.MOD_ID)
public class TFCAgedAlcohol
{
    public static final String MOD_ID = "tfcagedalcohol";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFCAgedAlcohol(ModContainer mod, IEventBus bus)
    {
        mod.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT.spec());

        AgedAlcoholFluids.FLUIDS.register(bus);
        AgedAlcoholFluids.FLUID_TYPES.register(bus);
        Registers.BLOCKS.register(bus);
        Registers.ITEMS.register(bus);
        CreativeTabs.CREATIVE_TABS.register(bus);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEvents.init(bus);
            ClientForgeEvents.init();
        }
    }
}
