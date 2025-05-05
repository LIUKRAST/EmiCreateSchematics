package net.liukrast.schematicdisplay.mixin;

import net.createmod.catnip.gui.AbstractSimiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSimiScreen.class)
public interface AbstractSimiScreenMixin {
    @Accessor(value = "guiLeft")
    int getGuiLeft();
    @Accessor(value = "guiTop")
    int getGuiTop();
}
