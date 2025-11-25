package net.liukrast.schematicdisplay;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EMICreateSchematics.MOD_ID)
public final class EMICreateSchematics {
    public static final String MOD_ID = "emi_create_schematics";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public EMICreateSchematics() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
