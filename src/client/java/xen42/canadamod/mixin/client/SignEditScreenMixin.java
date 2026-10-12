package xen42.canadamod.mixin.client;

import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.sign.MapleSignEntity;

@Mixin(SignEditScreen.class)
public class SignEditScreenMixin {

    @Shadow
    @Final
    @Mutable
    private Identifier texture;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ucc$setMapleSignTexture(
            SignBlockEntity sign,
            SignTextSlot slot,
            boolean isFrontText,
            CallbackInfo ci) {

        if (sign instanceof MapleSignEntity) {
            this.texture = Identifier.fromNamespaceAndPath(
                CanadaMod.MOD_ID,
                "textures/gui/signs/maple.png"
            );
        }
    }
}
