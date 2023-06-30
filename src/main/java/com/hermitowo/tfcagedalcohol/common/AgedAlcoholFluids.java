package com.hermitowo.tfcagedalcohol.common;

import java.util.Map;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;

import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.common.fluids.MixingFluid;
import net.dries007.tfc.util.Helpers;

import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class AgedAlcoholFluids
{
    public static final Map<AgedAlcohol, FlowingFluidRegistryObject<MixingFluid>> AGED_ALCOHOL;

    static
    {
        AGED_ALCOHOL = Helpers.mapOfKeys(AgedAlcohol.class, (fluid) -> Registers.registerFluid(fluid.getId(), "flowing_" + fluid.getId(), properties ->
                properties.block(Registers.AGED_ALCOHOL.get(fluid)).bucket(Registers.FLUID_BUCKETS.get(fluid)),
            FluidAttributes.builder(WATER_STILL, WATER_FLOW)
                .translationKey("fluid.tfcagedalcohol." + fluid.getId())
                .color(fluid.getColor())
                .overlay(WATER_OVERLAY)
                .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY),
            MixingFluid.Source::new, MixingFluid.Flowing::new));
    }
}
