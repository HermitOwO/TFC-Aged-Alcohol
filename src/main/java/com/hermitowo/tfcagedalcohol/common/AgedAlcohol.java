package com.hermitowo.tfcagedalcohol.common;

import java.util.Locale;
import com.hermitowo.tfcagedalcohol.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraftforge.registries.ForgeRegistries;

import net.dries007.tfc.util.Helpers;

public enum AgedAlcohol
{
    BEER(-3957193, "minecraft:absorption", 1, 24000),

    CIDER(-5198286, "minecraft:speed", 0, 6400),
    RUM(-9567965, "minecraft:speed", 1, 3200),

    SAKE(-4728388, "minecraft:resistance", 0, 6400),
    VODKA(-2302756, "minecraft:resistance", 1, 3200),

    WHISKEY(-10995943, "minecraft:haste", 1, 3200),
    CORN_WHISKEY(-2504777, "minecraft:haste", 0, 6400),
    RYE_WHISKEY(-3703471, "minecraft:haste", 0, 6400);

    private final String id;
    private final int color;
    private final String defaultEffect;
    private final int defaultEffectAmplifier;
    private final int defaultEffectDuration;

    AgedAlcohol(int color, String defaultEffect, int defaultEffectAmplifier, int defaultEffectDuration)
    {
        this.id = "aged_" + this.name().toLowerCase(Locale.ROOT);
        this.color = color;
        this.defaultEffect = defaultEffect;
        this.defaultEffectAmplifier = defaultEffectAmplifier;
        this.defaultEffectDuration = defaultEffectDuration;
    }

    public String getId()
    {
        return this.id;
    }

    public int getColor()
    {
        return this.color;
    }

    public String getDefaultEffect()
    {
        return this.defaultEffect;
    }

    public int getDefaultEffectAmplifier()
    {
        return this.defaultEffectAmplifier;
    }

    public int getDefaultEffectDuration()
    {
        return this.defaultEffectDuration;
    }

    public MobEffect getEffect()
    {
        return ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(Config.SERVER.agedAlcoholEffects.get(this).get()));
    }

    public int getEffectAmplifier()
    {
        return Config.SERVER.agedAlcoholEffectAmplifiers.get(this).get();
    }

    public int getEffectDuration()
    {
        return Config.SERVER.agedAlcoholEffectDurationInTicks.get(this).get();
    }

    public String displayedPotency()
    {
        return switch (getEffectAmplifier() + 1)
        {
            case 2 -> " II ";
            case 3 -> " III ";
            default -> " ";
        };
    }

    public Component getTooltip()
    {
        return Helpers.literal(getEffect().getDisplayName().getString() + displayedPotency() + "(" + MobEffectUtil.formatDuration(getEffectInstance(), 1) + ")").withStyle(getEffect().getCategory().getTooltipFormatting());
    }

    public MobEffectInstance getEffectInstance()
    {
        return new MobEffectInstance(getEffect(), getEffectDuration(), getEffectAmplifier());
    }
}
