package com.hermitowo.tfcagedalcohol.config;

import java.util.function.Function;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import net.dries007.tfc.config.BaseConfig;

@SuppressWarnings("SameParameterValue")
public class Config
{
    public static final ClientConfig CLIENT = register(ClientConfig::new, ConfigBuilder.ClientValue::new, "client");

    private static <C extends BaseConfig> C register(Function<ConfigBuilder, C> factory, ConfigBuilder.Factory value, String prefix)
    {
        final Pair<C, ModConfigSpec> pair = new ModConfigSpec.Builder()
            .configure(builder -> factory.apply(new ConfigBuilder(builder, value, prefix)));
        pair.getKey().updateSpec(pair.getValue());
        return pair.getKey();
    }
}
