package com.hermitowo.tfcagedalcohol.config;

import java.util.function.Function;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

public class ClientConfig
{
    public final BooleanValue showEffectTooltipForAllDrinkables;

    ClientConfig(Builder innerBuilder)
    {
        Function<String, Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.client." + name);

        innerBuilder.push("display");

        showEffectTooltipForAllDrinkables = builder.apply("showEffectTooltipForAllDrinkables").comment(
            "If false, only the effects of aged alcohol will be shown on fluid container items like jugs and buckets.",
            "Otherwise, effects like Thirst for Salt Water Ceramic Jug will also be shown"
        ).define("showEffectTooltipForAllDrinkables", false);

        innerBuilder.pop();
    }
}
