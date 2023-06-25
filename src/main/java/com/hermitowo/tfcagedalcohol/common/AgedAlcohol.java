package com.hermitowo.tfcagedalcohol.common;

import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;

import net.dries007.tfc.util.Helpers;

public enum AgedAlcohol
{
    BEER(-3957193, MobEffects.ABSORPTION, 1, 24000),

    CIDER(-5198286, MobEffects.MOVEMENT_SPEED, 0, 6400),
    RUM(-9567965, MobEffects.MOVEMENT_SPEED, 1, 3200),

    SAKE(-4728388, MobEffects.DAMAGE_RESISTANCE, 0, 6400),
    VODKA(-2302756, MobEffects.DAMAGE_RESISTANCE, 1, 3200),

    WHISKEY(-10995943, MobEffects.DIG_SPEED, 1, 3200),
    CORN_WHISKEY(-2504777, MobEffects.DIG_SPEED, 0, 6400),
    RYE_WHISKEY(-3703471, MobEffects.DIG_SPEED, 0, 6400);

    private final String id;
    private final int color;
    private final MobEffect effect;
    private final int effectPotency;
    private final int effectDuration;

    AgedAlcohol(int color, MobEffect effect, int effectPotency, int effectDuration)
    {
        this.id = "aged_" + this.name().toLowerCase(Locale.ROOT);
        this.color = color;
        this.effect = effect;
        this.effectPotency = effectPotency;
        this.effectDuration = effectDuration;
    }

    public String getId()
    {
        return this.id;
    }

    public int getColor()
    {
        return this.color;
    }

    public MobEffect getEffect()
    {
        return effect;
    }

    public int getEffectDuration()
    {
        return effectDuration;
    }

    public int getEffectPotency()
    {
        return effectPotency;
    }

    public String displayedPotency()
    {
        return switch (effectPotency + 1)
            {
                case 2 -> " II " ;
                case 3 -> " III ";
                default -> " ";
            };
    }

    public Component getTooltip()
    {
        return Helpers.literal(effect.getDisplayName().getString() + displayedPotency() + "(" + MobEffectUtil.formatDuration(getEffectInstance(), 1) + ")").withStyle(ChatFormatting.BLUE);
    }

    public MobEffectInstance getEffectInstance()
    {
        return new MobEffectInstance(this.effect, this.effectDuration, this.effectPotency);
    }
}
