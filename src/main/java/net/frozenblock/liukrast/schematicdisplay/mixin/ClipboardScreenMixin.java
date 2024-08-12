package net.frozenblock.liukrast.schematicdisplay.mixin;

import com.simibubi.create.content.equipment.clipboard.ClipboardBlockItem;
import com.simibubi.create.content.equipment.clipboard.ClipboardScreen;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import net.frozenblock.liukrast.schematicdisplay.SchematicDisplay;
import net.frozenblock.liukrast.schematicdisplay.clipboard.ClipboardScreenUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClipboardScreen.class)
public class ClipboardScreenMixin extends Screen {
    @Shadow
    public ItemStack item;

    protected ClipboardScreenMixin(final Component p_96550_) {
        super(p_96550_);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(final CallbackInfo ci) {
        if (ClipboardScreenUtils.load(item.getTag(), false)) {
            final int x = ((AbstractSimiScreenMixin) this).getGuiLeft();
            final int y = ((AbstractSimiScreenMixin) this).getGuiTop() - 8;
            final IconButton customButton = new IconButton(x + 234, y + 197, AllIcons.I_WHITELIST)
                    .withCallback(() -> {
                        if (item.getItem() instanceof ClipboardBlockItem)
                            ClipboardScreenUtils.load(item.getTag(), true);
                    });
            customButton.setToolTip(Components.translatable("gui." + SchematicDisplay.MOD_ID + ".clipboard.favourite"));
            this.addRenderableWidget(customButton);
        }
    }
}
