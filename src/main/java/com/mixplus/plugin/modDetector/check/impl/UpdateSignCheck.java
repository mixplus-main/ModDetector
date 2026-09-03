package com.mixplus.plugin.modDetector.check.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import com.mixplus.plugin.modDetector.ModDetector;
import com.mixplus.plugin.modDetector.check.Check;
import com.mixplus.plugin.modDetector.mod.ModInfo;

import org.bukkit.entity.Player;

import java.util.*;

public class UpdateSignCheck implements Check {
    private final ModDetector instance;
    private final CheckMod checkMod;

    private static final Map<UUID, ModInfo> targets = new HashMap<>();

    private static final Map<UUID, List<ModInfo>> detectedMods = new HashMap<>();

    public UpdateSignCheck() {
        this.instance = ModDetector.getInstance();
        this.checkMod = instance.getCheckMod();
    }


    @Override
    public PacketType.Play.Client getPacketType() {
        return PacketType.Play.Client.UPDATE_SIGN;
    }

    @Override
    public void check(PacketReceiveEvent event) {
        Player player = event.getPlayer();
        WrapperPlayClientUpdateSign packet =
                new WrapperPlayClientUpdateSign(event);


        String[] lines = packet.getTextLines();
        if (lines.length != 4) {
            return;
        }


        if (!"CheckSign".equals(lines[3])) {
            return;
        }


        ModInfo target = targets.get(player.getUniqueId());

        if (target == null) {
            return;
        }

        ModInfo detected = checkMod.check(lines, target);
        addDetected(player.getUniqueId(), detected);

    }

    public static void setTargetMod(UUID uuid, ModInfo mod) {
        targets.put(uuid, mod);
    }

    public static void addDetected(UUID uuid, ModInfo mod) {
        if (mod == null) {
            return;
        }
        detectedMods
                .computeIfAbsent(uuid, key -> new ArrayList<>())
                .add(mod);
    }

    public static List<ModInfo> getDetected(UUID uuid) {
        return detectedMods.getOrDefault(uuid, List.of());
    }

    public static void removeDetected(UUID uuid) {
        detectedMods.remove(uuid);
    }
}
