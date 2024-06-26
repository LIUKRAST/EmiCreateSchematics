package net.frozenblock.liukrast.schematicdisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.runtime.EmiDrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("all")
@Mixin(ItemEmiStack.class)
public final class ItemEmiStackMixin {
    @Inject(at = @At("TAIL"), method = "render", remap = false)
    private void render(final PoseStack matrices,
                        final int x, final int y,
                        final float delta,
                        final int flags,
                        final CallbackInfo ci) {
        final EmiDrawContext context = EmiDrawContext.wrap(matrices);
        if ((flags) != 0) {
            final StringBuilder bob = new StringBuilder();
            if (((ItemEmiStack)(Object)this).getAmount() != 1)
                bob.append(((ItemEmiStack)(Object)this).getAmount());
            EmiRenderHelper.renderAmount(context, x, y, EmiPort.literal(bob.toString()));
        }
    }
}
