package com.mixplus.plugin.modDetector.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.mixplus.plugin.modDetector.ModDetector;
import com.mixplus.plugin.modDetector.check.Check;
import com.mixplus.plugin.modDetector.check.CheckManager;
import org.jspecify.annotations.NonNull;

public class PacketListener extends PacketListenerAbstract {
    private final ModDetector instance;
    private final CheckManager checkManager;


    public PacketListener() {
        super(PacketListenerPriority.NORMAL);
        this.instance = ModDetector.getInstance();
        this.checkManager = instance.getCheckManager();
    }

    @Override
    public void onPacketReceive(@NonNull PacketReceiveEvent event) {
        checkManager.handle(event);
    }

}
