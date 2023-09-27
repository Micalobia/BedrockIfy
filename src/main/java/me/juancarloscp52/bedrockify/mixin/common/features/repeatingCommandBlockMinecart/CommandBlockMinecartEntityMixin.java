package me.juancarloscp52.bedrockify.mixin.common.features.repeatingCommandBlockMinecart;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandBlockMinecartEntity.class)
public class CommandBlockMinecartEntityMixin {
    @Inject(method = "getDefaultContainedBlock", at = @At("HEAD"), cancellable = true)
    private void replaceWithRepeatingCommandBlock(CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(Blocks.REPEATING_COMMAND_BLOCK.getDefaultState());
    }
}
