package me.juancarloscp52.bedrockify.mixin.common.features.fireAspect;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Inject(method = "onUse", at=@At(value = "INVOKE",target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"),cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        if(!Bedrockify.getInstance().settings.fireAspectLight)
            return;
        ItemStack itemStack = player.getStackInHand(hand);
        if(null != itemStack && (itemStack.hasEnchantments() || itemStack.getItem() instanceof EnchantedBookItem) && EnchantmentHelper.get(itemStack).containsKey(Enchantments.FIRE_ASPECT)){
            if(!CampfireBlock.isLitCampfire(state) && CampfireBlock.canBeLit(state)){
                if(world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD)){
                    itemStack.damage(1, player,((e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)));
                    world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                    world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }else{
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }

    // TODO: Consider making separate mixin
    @Inject(method = "onEntityCollision", at=@At("HEAD"))
    private void watchForBurningEntity(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(!Bedrockify.getInstance().settings.fireAspectLight)
            return;
        if(entity.isOnFire() && !state.get(Properties.LIT))
            world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
    }
}
