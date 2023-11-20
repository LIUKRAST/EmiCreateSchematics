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

@Mixin(ItemEmiStack.class)
public class ItemEmiStackMixin {

    @Inject(at = @At("TAIL"), method = "render", remap = false)
    private void render(PoseStack matrices, int x, int y, float delta, int flags, CallbackInfo ci) {
        EmiDrawContext context = EmiDrawContext.wrap(matrices);
        if ((flags) != 0) {
            String count = "";
            if (((ItemEmiStack)(Object)this).getAmount() != 1) {
                count += ((ItemEmiStack)(Object)this).getAmount();
            }
            EmiRenderHelper.renderAmount(context, x, y, EmiPort.literal(count));
        }
    }
}
