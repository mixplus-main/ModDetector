package com.mixplus.plugin.modDetector.check.impl;


import com.mixplus.plugin.modDetector.mod.ModInfo;


public class CheckMod {
    public ModInfo check(String[] lines, ModInfo mod) {

        if (lines == null || lines.length < 4) {
            return null;
        }

        if (
                lines[3].equals("CheckSign")
                        && !lines[1].equals(mod.key())
                        && !lines[2].equals(mod.key())
        ) {
            return mod;
        }

        return null;
    }
}
