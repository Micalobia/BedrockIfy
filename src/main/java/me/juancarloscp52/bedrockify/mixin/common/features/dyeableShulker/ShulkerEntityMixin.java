package me.juancarloscp52.bedrockify.mixin.common.features.dyeableShulker;

import me.juancarloscp52.bedrockify.common.features.recipes.DyeHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin extends GolemEntity implements Monster {
    @Shadow
    @Final
    protected static TrackedData<Byte> COLOR;


    protected ShulkerEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        // Maybe add a config to redye entity in survival? Not originally in Bedrock
        if (!player.getAbilities().creativeMode)
            return super.interactMob(player, hand);
        var handStack = player.getStackInHand(hand);
        var item = handStack.getItem();
        if (!(DyeHelper.isDyeableItem(item)))
            return super.interactMob(player, hand);
        if (player.getWorld().isClient) return ActionResult.SUCCESS;
        getDataTracker().set(COLOR, (byte) DyeHelper.getDyeItem(item).getColor().ordinal());
        return ActionResult.CONSUME;
    }
}
