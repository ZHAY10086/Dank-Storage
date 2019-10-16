package com.tfar.dankstorage.network;

import com.tfar.dankstorage.DankStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class DankPacketHandler {
  public static SimpleChannel INSTANCE;

  public static void registerMessages(String channelName) {
    int id = 0;

    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(DankStorage.MODID, channelName), () -> "1.0", s -> true, s -> true);

    INSTANCE.registerMessage(id++, CMessageTogglePickup.class,
            (message, buffer) -> {},
            buffer -> new CMessageTogglePickup(),
            CMessageTogglePickup::handle);

    INSTANCE.registerMessage(id++, C2SMessageLockSlot.class,
            C2SMessageLockSlot::encode,
            C2SMessageLockSlot::new,
            C2SMessageLockSlot::handle);

    INSTANCE.registerMessage(id++, CMessageToggleUseType.class,
            (message, buffer) -> {},
            buffer -> new CMessageToggleUseType(),
            CMessageToggleUseType::handle);

    INSTANCE.registerMessage(id++, C2SMessageChangeSlot.class,
            C2SMessageChangeSlot::encode,
            C2SMessageChangeSlot::new,
            C2SMessageChangeSlot::handle);

    INSTANCE.registerMessage(id++, CMessagePickBlock.class,
            (cMessagePickBlock, buffer) -> {},
            buffer -> new CMessagePickBlock(),
            CMessagePickBlock::handle);

    INSTANCE.registerMessage(id++, CMessageTagMode.class,
            (message, buffer) -> {},
            buffer -> new CMessageTagMode(),
            CMessageTagMode::handle);

    INSTANCE.registerMessage(id++, CMessageSort.class,
            (message, buffer) -> {},
            buffer -> new CMessageSort(),
            CMessageSort::handle);

    INSTANCE.registerMessage(id++, S2CMessageSyncExtendedSlotContents.class,
            S2CMessageSyncExtendedSlotContents::encode,
            S2CMessageSyncExtendedSlotContents::new,
            S2CMessageSyncExtendedSlotContents::handle);
  }
}
