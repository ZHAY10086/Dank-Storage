package com.tfar.dankstorage.block;

import com.tfar.dankstorage.DankStorage;
import com.tfar.dankstorage.client.Client;
import com.tfar.dankstorage.inventory.DankHandler;
import com.tfar.dankstorage.inventory.PortableDankHandler;
import com.tfar.dankstorage.util.DankConstants;
import com.tfar.dankstorage.util.Utils;
import com.tfar.dankstorage.tile.AbstractDankStorageTile;
import com.tfar.dankstorage.tile.DankTiles;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class DankBlock extends Block {
  public DankBlock(Material p_i48440_1_) {
    super(p_i48440_1_);
  }


  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);
      if (tileEntity instanceof AbstractDankStorageTile) {
      player.openGui(DankStorage.instance, DankConstants.TILE_GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
    return true;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.AIR;
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    final TileEntity tile = world.getTileEntity(pos);
    if (tile instanceof AbstractDankStorageTile && !world.isRemote){
      ItemStack dank = new ItemStack(((AbstractDankStorageTile) tile).getDank());
      NBTTagCompound nbt = ((AbstractDankStorageTile) tile).itemHandler.serializeNBT();
      nbt.setBoolean("pickup",((AbstractDankStorageTile) tile).pickup);
      nbt.setBoolean("void",((AbstractDankStorageTile) tile).isVoid);
      nbt.setInteger("selectedSlot",((AbstractDankStorageTile) tile).selectedSlot);
      dank.setTagCompound(nbt);
      EntityItem itemEntity = new EntityItem(world,pos.getX()+ .5,pos.getY() + .5,pos.getZ()+.5,dank);
      world.spawnEntity(itemEntity);
    }
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase entity, ItemStack stack) {
    TileEntity te = world.getTileEntity(pos);
    if (te instanceof AbstractDankStorageTile && !world.isRemote && entity != null) {
      if (stack.hasTagCompound()){
        ((AbstractDankStorageTile) te).setContents(stack.getTagCompound());
        ((AbstractDankStorageTile) te).pickup = stack.getTagCompound().getBoolean("pickup");
        ((AbstractDankStorageTile) te).isVoid = stack.getTagCompound().getBoolean("void");
        ((AbstractDankStorageTile) te).selectedSlot = stack.getTagCompound().getInteger("selectedSlot");
      }
    }
  }


  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    ItemStack bag = placer.getHeldItem(hand);
    if (!Utils.construction(bag))
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);

    Block block = Block.getBlockFromItem(bag.getItem());
    if (block instanceof DankBlock)return block.getDefaultState();
    return block.isAir(block.getDefaultState(),null,null) ? null : block.getStateForPlacement(world,pos,facing,hitX,hitY,hitZ,meta,placer,hand);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    int type = Utils.getTier(this.getRegistryName());
    switch (type) {
      case 1:
      default:
        return new DankTiles.DankStorageTile1();
      case 2:
        return new DankTiles.DankStorageTile2();
      case 3:
        return new DankTiles.DankStorageTile3();
      case 4:
        return new DankTiles.DankStorageTile4();
      case 5:
        return new DankTiles.DankStorageTile5();
      case 6:
        return new DankTiles.DankStorageTile6();
      case 7:
        return new DankTiles.DankStorageTile7();
    }
  }




  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack bag, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
    //if (bag.hasTag())tooltip.add(new StringTextComponent(bag.getTagCompound().toString()));

    if (!GuiScreen.isShiftKeyDown()){
      tooltip.add(I18n.format("text.dankstorage.shift", Minecraft.getMinecraft().gameSettings.keyBindSneak.getDisplayName()));
    }

    if (GuiScreen.isShiftKeyDown()) {
      if (Utils.autoVoid(bag)) tooltip.add(I18n.format("text.dankstorage.disablevoid", Client.AUTO_VOID.getDisplayName()));
              else tooltip.add(I18n.format("text.dankstorage.enablevoid", Client.AUTO_VOID.getDisplayName()));
      if (Utils.autoPickup(bag)) tooltip.add(
              I18n.format("text.dankstorage.disablepickup", Client.AUTO_PICKUP.getDisplayName()));
      else tooltip.add(
              I18n.format("text.dankstorage.enablepickup", Client.AUTO_PICKUP.getDisplayName()));
      DankHandler handler = Utils.getHandler(bag);

      if (handler.isEmpty()){
        tooltip.add(I18n.format("text.dankstorage.empty"));
        return;
      }

      for (int i = 0; i < handler.getSlots(); i++) {
        ItemStack item = handler.getStackInSlot(i);
        if (item.isEmpty())continue;
          String count = Integer.toString(item.getCount());
        tooltip.add(I18n.format("text.dankstorage.formatcontaineditems",TextFormatting.AQUA+count,item.getItem().getForgeRarity(item).getColor()+item.getDisplayName()));


      }
    }
  }

  public static boolean onItemPickup(EntityItemPickupEvent event, ItemStack bag) {
    // 1. 检查自动拾取是否开启
    if (!bag.hasTagCompound() || !bag.getTagCompound().getBoolean("pickup")) {
        return false;
    }

    // 2. 获取待拾取物品
    ItemStack toPickup = event.getItem().getItem().copy();
    final boolean isVoid = Utils.autoVoid(bag);
    PortableDankHandler inv = Utils.getHandler(bag);

    int initialCount = toPickup.getCount();

    // 3. 扫描存储确认是否有匹配物品（关键修复：区分新物品类型）
    boolean hasMatchingSlot = false;
    for (int i = 0; i < inv.getSlots(); i++) {
        ItemStack stackInSlot = inv.getStackInSlot(i);
        if (!stackInSlot.isEmpty() && canAddItemToSlot(inv, stackInSlot, toPickup, true)) {
            hasMatchingSlot = true;
            break;
        }
    }

    // 4. 没有匹配槽位时：根据虚空模式决定行为
    if (!hasMatchingSlot) {
        // 关键修复：不匹配物品不拾取也不销毁
        return false;
    }

    // 5. 仅处理匹配物品的拾取
    boolean itemAdded = false;
    for (int i = 0; i < inv.getSlots(); i++) {
        ItemStack stackInSlot = inv.getStackInSlot(i);

        // 5.1 只处理匹配的槽位（非空且物品匹配）
        if (stackInSlot.isEmpty() || !canAddItemToSlot(inv, stackInSlot, toPickup, false)) {
            continue;
        }

        // 5.2 计算可添加数量
        int remainingSpace = inv.stacklimit - stackInSlot.getCount();
        if (remainingSpace <= 0) {
            continue; // 槽位已满，跳过
        }

        int addAmount = Math.min(toPickup.getCount(), remainingSpace);
        stackInSlot.grow(addAmount);
        toPickup.shrink(addAmount);
        inv.setStackInSlot(i, stackInSlot); // 显式更新槽位
        itemAdded = true;

        if (toPickup.isEmpty()) {
            break; // 物品已完全处理
        }
    }

    // 6. 处理未拾取的匹配物品
    boolean shouldPlaySound = false;
    if (!toPickup.isEmpty()) {
        if (isVoid) {
            // 关键修复：仅销毁匹配物品的溢出部分
            toPickup.setCount(0);
            shouldPlaySound = itemAdded; // 如果有部分被添加才播放声音
        }
        // 非虚空模式：物品保留在地面上
    } else {
        shouldPlaySound = true;
    }

    // 7. 更新状态
    if (itemAdded || (isVoid && toPickup.isEmpty())) {
        event.getItem().setItem(toPickup);
        inv.writeItemStack();
    }

    if (shouldPlaySound) {
        EntityPlayer player = event.getEntityPlayer();
        player.world.playSound(null, player.posX, player.posY, player.posZ, 
                SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, 
                (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F);
    }

    return toPickup.isEmpty();
  }

  public static boolean canAddItemToSlot(PortableDankHandler handler, ItemStack stackInSlot, ItemStack pickup, boolean stackSizeMatters) {
  if (stackInSlot.isEmpty()) return true; // 空槽位始终可放入

  // 物品类型、元数据、标签必须完全匹配
  if (!ItemStack.areItemsEqual(stackInSlot, pickup) || !ItemStack.areItemStackTagsEqual(stackInSlot, pickup)) {
    return false;
  }

  // 检查堆叠数量是否超过限制
  int total = stackInSlot.getCount() + (stackSizeMatters ? 0 : pickup.getCount());
  return total <= handler.stacklimit;
  }
}
