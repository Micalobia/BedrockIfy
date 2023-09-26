package me.juancarloscp52.bedrockify.mixin.client.features.salmonSizes;

import me.juancarloscp52.bedrockify.common.features.salmonSizes.SalmonSized;
import net.minecraft.client.render.entity.SalmonEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SalmonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SalmonEntityRenderer.class)
public abstract class SalmonEntityRendererMixin {
    @Inject(method = "setupTransforms(Lnet/minecraft/entity/passive/SalmonEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("TAIL"))
    private void changeSize(SalmonEntity salmonEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo ci) {
        if (!(salmonEntity instanceof SalmonSized sized)) return;
        var size = sized.bedrockify$getSize();
        float scale = SalmonSized.calculateScale(size);
        matrixStack.scale(scale, scale, scale);
    }
}
