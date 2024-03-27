package net.frozenblock.liukrast.schematicdisplay.mixin;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEmiStack.class)
public class ItemEmiStackMixin {

    @Inject(at = @At("TAIL"), method = "render", remap = false)
    private void injectRender(GuiGraphics draw, int x, int y, float delta, int flags, CallbackInfo ci) {
        final EmiDrawContext context = EmiDrawContext.wrap(draw);
        if(flags != 0) {
            String count = "";
            if(((ItemEmiStack)(Object)this).getAmount() != 1) {
                count += ((ItemEmiStack)(Object)this).getAmount();
            }
            EmiRenderHelper.renderAmount(context, x, y, EmiPort.literal(count));
        }
    }
}
