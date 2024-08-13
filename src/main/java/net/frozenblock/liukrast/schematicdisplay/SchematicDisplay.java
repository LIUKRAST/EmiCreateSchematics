package net.frozenblock.liukrast.schematicdisplay;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.logging.LogManager;
import java.util.logging.Logger;

@Mod(SchematicDisplay.MOD_ID)
public final class SchematicDisplay {
    public static final String MOD_ID = "emi_create_schematics";

    public static final Logger LOGGER = LogManager.getLogManager().getLogger(MOD_ID);

    public SchematicDisplay() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
