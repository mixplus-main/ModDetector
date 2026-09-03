package com.mixplus.plugin.modDetector.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.mixplus.plugin.modDetector.check.impl.UpdateSignCheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckManager {
    private final List<Check> checks = new ArrayList<>();

    public CheckManager() {
        registerChecks();
    }

    public void handle(PacketReceiveEvent event) {
        for (Check check : checks) {
            if (check.getPacketType() == event.getPacketType()) {
                check.check(event);
            }
        }
    }

    public List<Check> getChecks() {
        return Collections.unmodifiableList(checks);
    }

    private void registerChecks() {
        checks.add(new UpdateSignCheck());
    }
}
