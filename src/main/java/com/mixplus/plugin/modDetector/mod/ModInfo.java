package com.mixplus.plugin.modDetector.mod;

import com.mixplus.plugin.modDetector.action.ActionMode;

public record ModInfo(
        String id,
        String name,
        String key,
        ActionMode mode,
        String action
) {
}
