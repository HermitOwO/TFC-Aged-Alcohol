package com.hermitowo.tfcagedalcohol.config;

import java.util.EnumMap;
import java.util.function.Function;
import com.hermitowo.tfcagedalcohol.common.AgedAlcohol;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;

public class ServerConfig
{
    public final EnumMap<AgedAlcohol, ForgeConfigSpec.ConfigValue<String>> agedAlcoholEffects;
    public final EnumMap<AgedAlcohol, ForgeConfigSpec.IntValue> agedAlcoholEffectAmplifiers;
    public final EnumMap<AgedAlcohol, ForgeConfigSpec.IntValue> agedAlcoholEffectDurationInTicks;

    ServerConfig(Builder innerBuilder)
    {
        Function<String, Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.server." + name);

        innerBuilder.push("agedAlcohol");

        agedAlcoholEffects = new EnumMap<>(AgedAlcohol.class);
        agedAlcoholEffectAmplifiers = new EnumMap<>(AgedAlcohol.class);
        agedAlcoholEffectDurationInTicks = new EnumMap<>(AgedAlcohol.class);

        for (AgedAlcohol alcohol : AgedAlcohol.values())
        {
            final String alcoholEffect = String.format("%s_effect", alcohol.getId());
            agedAlcoholEffects.put(alcohol, builder.apply(alcoholEffect).comment("Registry name of the potion effect").define(alcoholEffect, alcohol.getDefaultEffect()));

            final String alcoholEffectAmplifier = String.format("%s_effectAmplifier", alcohol.getId());
            agedAlcoholEffectAmplifiers.put(alcohol, builder.apply(alcoholEffectAmplifier).defineInRange(alcoholEffectAmplifier, alcohol.getDefaultEffectAmplifier(), 0, Integer.MAX_VALUE));

            final String alcoholEffectDurationInTicks = String.format("%s_effectDurationInTicks", alcohol.getId());
            agedAlcoholEffectDurationInTicks.put(alcohol, builder.apply(alcoholEffectDurationInTicks).defineInRange(alcoholEffectDurationInTicks, alcohol.getDefaultEffectDuration(), 0, Integer.MAX_VALUE));
        }

        innerBuilder.pop();
    }
}
