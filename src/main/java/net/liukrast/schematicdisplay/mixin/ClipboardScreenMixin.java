package net.liukrast.schematicdisplay.mixin;

import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.equipment.clipboard.ClipboardScreen;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.liukrast.schematicdisplay.EMICreateSchematics;
import net.liukrast.schematicdisplay.clipboard.ClipboardScreenUtils;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClipboardScreen.class)
public abstract class ClipboardScreenMixin extends AbstractSimiScreen {
    @Unique
    private static final Component emi_create_schematics$TOOLTIP = Component.translatable("gui." + EMICreateSchematics.MOD_ID + ".clipboard.favourite");

    @Shadow
    List<List<ClipboardEntry>> pages;

    protected ClipboardScreenMixin(final Component ignored) {
        super(ignored);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo ci) {
        final int x = guiLeft;
        final int y = guiTop - 8;
        final IconButton customButton = new IconButton(x + 234, y + 197, AllIcons.I_WHITELIST).withCallback(() -> ClipboardScreenUtils.load(pages));
        customButton.setToolTip(emi_create_schematics$TOOLTIP);
        this.addRenderableWidget(customButton);
    }
}