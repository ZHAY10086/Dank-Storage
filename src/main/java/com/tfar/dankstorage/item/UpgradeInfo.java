package com.tfar.dankstorage.item;

import com.tfar.dankstorage.block.DankBlock;
import com.tfar.dankstorage.utils.Utils;

public class UpgradeInfo {

  private final int start;
  public final int end;

  public UpgradeInfo(int start,int end) {
    this.start = start;
    this.end = end;
  }

  public boolean canUpgrade(DankBlock dank){
    return Utils.getTier(dank.getRegistryName()) == start;
  }
}