package net.liukrast.schematicdisplay.mixin;

import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.runtime.EmiFavorites;
import net.liukrast.schematicdisplay.clipboard.ClipboardTreeRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiFavorites.class)
public abstract class EmiFavoritesMixin {
    @Inject(method = "updateSynthetic", at = @At("TAIL"), remap = false)
    private static void emi_create_schematics$hideVirtualRootRecipe(EmiPlayerInventory inv, CallbackInfo ci) {
        if (BoM.tree == null || !ClipboardTreeRecipe.isVirtual(BoM.tree.goal.recipe)) {
            return;
        }

        EmiFavorites.syntheticFavorites.removeIf(favorite -> ClipboardTreeRecipe.isVirtual(favorite.getRecipe()));
    }
}
