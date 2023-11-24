package com.hermitowo.tfcagedalcohol.common;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.dries007.tfc.common.fluids.ExtendedFluidType;
import net.dries007.tfc.common.fluids.FluidRegistryObject;
import net.dries007.tfc.common.fluids.FluidTypeClientProperties;
import net.dries007.tfc.common.fluids.MixingFluid;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import static com.hermitowo.tfcagedalcohol.TFCAgedAlcohol.*;
import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class AgedAlcoholFluids
{
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MOD_ID);

    public static final Map<AgedAlcohol, FluidRegistryObject<ForgeFlowingFluid>> AGED_ALCOHOL = Helpers.mapOfKeys(AgedAlcohol.class, fluid -> registerFluid(
        fluid.getId(),
        properties ->
            properties
                .block(Registers.AGED_ALCOHOL.get(fluid))
                .bucket(Registers.FLUID_BUCKETS.get(fluid)),
        waterLike()
            .descriptionId("fluid.tfcagedalcohol." + fluid.getId())
            .canConvertToSource(false),
        new FluidTypeClientProperties(fluid.getColor(), WATER_STILL, WATER_FLOW, WATER_OVERLAY, null),
        MixingFluid.Source::new,
        MixingFluid.Flowing::new
    ));

    private static FluidType.Properties waterLike()
    {
        return FluidType.Properties.create()
            .adjacentPathType(BlockPathTypes.WATER)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .canConvertToSource(true)
            .canDrown(true)
            .canExtinguish(true)
            .canHydrate(false)
            .canPushEntity(true)
            .canSwim(true)
            .supportsBoating(true);
    }

    private static <F extends FlowingFluid> FluidRegistryObject<F> registerFluid(String name, Consumer<ForgeFlowingFluid.Properties> builder, FluidType.Properties typeProperties, FluidTypeClientProperties clientProperties, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory)
    {
        final int index = name.lastIndexOf('/');
        final String flowingName = index == -1 ? "flowing_" + name : name.substring(0, index) + "/flowing_" + name.substring(index + 1);

        return RegistrationHelpers.registerFluid(FLUID_TYPES, FLUIDS, name, name, flowingName, builder, () -> new ExtendedFluidType(typeProperties, clientProperties), sourceFactory, flowingFactory);
    }
}
