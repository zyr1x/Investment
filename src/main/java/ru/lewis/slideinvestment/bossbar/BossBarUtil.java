package ru.lewis.slideinvestment.bossbar;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BossBarUtil {

    private final UUID bossBarUUID;
    private final Player player;
    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private final float health;
    private final String defaultTitle;

    public BossBarUtil(Player player, String defaultTitle, float health) {
        this.player = player;
        this.health = health;
        this.defaultTitle = defaultTitle;
        this.bossBarUUID = UUID.randomUUID();
    }

    public void sendPacketCreateBossBar() {
        PacketContainer bossBarPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BOSS);
        bossBarPacket.getUUIDs().writeSafely(0, bossBarUUID);
        bossBarPacket.getIntegers().writeSafely(0, 0); // Action: Add
        bossBarPacket.getChatComponents().writeSafely(0, WrappedChatComponent.fromLegacyText(defaultTitle));
        bossBarPacket.getFloat().writeSafely(0, health); // set health
        bossBarPacket.getIntegers().writeSafely(1, 0); // Color: Pink
        bossBarPacket.getIntegers().writeSafely(2, 0); // Division: No divisions
        bossBarPacket.getBytes().writeSafely(0, (byte) 0); // Flags: No flags
        protocolManager.sendServerPacket(player, bossBarPacket);
    }

    public void hide() {
        try {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BOSS);
            packet.getUUIDs().writeSafely(0, bossBarUUID);
            try {
                Class<?> innerEnumClass = Class.forName("net.minecraft.server.v1_16_R3.PacketPlayOutBoss$Action");
                Object[] enumConstants = innerEnumClass.getEnumConstants();
                if (enumConstants.length >= 2) {
                    Object secondEnumValue = enumConstants[1];
                    packet.getModifier().writeSafely(1, secondEnumValue);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTitle(String text) {
        PacketContainer updateTitlePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BOSS);
        updateTitlePacket.getUUIDs().writeSafely(0, bossBarUUID);
        updateTitlePacket.getIntegers().writeSafely(0, 3);
        updateTitlePacket.getChatComponents().writeSafely(0, WrappedChatComponent.fromLegacyText(text));
        updateTitlePacket.getFloat().writeSafely(0, this.health);

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, updateTitlePacket);
    }
}