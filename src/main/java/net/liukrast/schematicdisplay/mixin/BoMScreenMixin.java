package net.liukrast.schematicdisplay.mixin;

import dev.emi.emi.bom.BoM;
import dev.emi.emi.bom.MaterialNode;
import dev.emi.emi.bom.ProgressState;
import dev.emi.emi.screen.BoMScreen;
import net.liukrast.schematicdisplay.clipboard.ClipboardTreeRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoMScreen.class)
public abstract class BoMScreenMixin {
    @Inject(method = "recalculateTree", at = @At("TAIL"), remap = false)
    private void emi_create_schematics$markVirtualRootProgress(CallbackInfo ci) {
        if (BoM.tree == null || !ClipboardTreeRecipe.isVirtual(BoM.tree.goal.recipe) || BoM.tree.goal.children == null) {
            return;
        }

        boolean anyProgress = false;
        boolean allComplete = !BoM.tree.goal.children.isEmpty();

        for (MaterialNode child : BoM.tree.goal.children) {
            if (child.progress != ProgressState.UNSTARTED) {
                anyProgress = true;
            }
            if (child.progress != ProgressState.COMPLETED) {
                allComplete = false;
            }
        }

        if (allComplete) {
            BoM.tree.goal.progress = ProgressState.COMPLETED;
        } else if (anyProgress) {
            BoM.tree.goal.progress = ProgressState.PARTIAL;
        } else {
            BoM.tree.goal.progress = ProgressState.UNSTARTED;
        }
    }
}
