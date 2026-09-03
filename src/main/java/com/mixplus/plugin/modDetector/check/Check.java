package com.mixplus.plugin.modDetector.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public interface Check {

    PacketType.Play.Client getPacketType();

    void check(PacketReceiveEvent event);
}
