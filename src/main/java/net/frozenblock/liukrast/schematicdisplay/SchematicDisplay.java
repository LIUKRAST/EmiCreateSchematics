package net.frozenblock.liukrast.schematicdisplay;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(SchematicDisplay.MOD_ID)
public final class SchematicDisplay {
    public static final String MOD_ID = "emi_create_schematics";

    public SchematicDisplay() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
