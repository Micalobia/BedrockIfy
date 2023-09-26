package me.juancarloscp52.bedrockify.mixin.common.features.salmonSizes;

import me.juancarloscp52.bedrockify.common.features.salmonSizes.SalmonSized;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SalmonEntity.class)
public abstract class SalmonEntityMixin extends SchoolingFishEntity implements SalmonSized {
    private static final TrackedData<Integer> SIZE = DataTracker.registerData(SalmonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public SalmonEntityMixin(EntityType<? extends SchoolingFishEntity> entityType, World world) {
        super(entityType, world);
    }

    public int bedrockify$getSize() {
        return this.dataTracker.get(SIZE);
    }

    @Unique
    public void bedrockify$setSize(int size) {
        this.dataTracker.set(SIZE, MathHelper.clamp(size, -1, 1));
    }

    @Unique
    private void bedrockify$onSizeChanged() {
        this.calculateDimensions();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SIZE, 0);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (SIZE.equals(data)) this.bedrockify$onSizeChanged();
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        var rng = random.nextFloat();
        // https://minecraft.wiki/w/Salmon#Bedrock_Edition
        if (rng <= 0.315f) bedrockify$setSize(-1);
        else if (rng >= 0.843) bedrockify$setSize(1);
        else bedrockify$setSize(0);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Size", this.bedrockify$getSize());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.bedrockify$setSize(nbt.getInt("Size"));
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        var dimensions = super.getDimensions(pose);
        var size = this.bedrockify$getSize();
        var scale = SalmonSized.calculateScale(size);
        return dimensions.scaled(scale);
    }
}
