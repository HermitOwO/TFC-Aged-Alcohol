package com.hermitowo.tfcagedalcohol.common;

import java.util.Locale;

public enum AgedAlcohol
{
    BEER(-3957193),
    CIDER(-5198286),
    RUM(-9567965),
    SAKE(-4728388),
    VODKA(-2302756),
    WHISKEY(-10995943),
    CORN_WHISKEY(-2504777),
    RYE_WHISKEY(-3703471);

    private final String id;
    private final int color;

    AgedAlcohol(int color)
    {
        this.id = "aged_" + this.name().toLowerCase(Locale.ROOT);
        this.color = color;
    }

    public String getId()
    {
        return this.id;
    }

    public int getColor()
    {
        return this.color;
    }
}
