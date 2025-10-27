package com.hermitowo.tfcagedalcohol.config;

import java.util.function.Supplier;

import net.dries007.tfc.config.BaseConfig;

public class ClientConfig extends BaseConfig
{
    public final Supplier<Boolean> showEffectTooltipForAllDrinkables;

    ClientConfig(ConfigBuilder builder)
    {
        builder.push("display");

        showEffectTooltipForAllDrinkables = builder.comment(
            "If false, only the effects of aged alcohol will be shown on fluid container items like jugs and buckets.",
            "Otherwise, effects like Thirst for Salt Water Ceramic Jug will also be shown"
        ).define("showEffectTooltipForAllDrinkables", false);

        builder.pop();
    }
}
