package tfar.dankstorage.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tfar.dankstorage.client.button.RedGreenToggleButton;
import tfar.dankstorage.client.button.TripleToggleButton;
import tfar.dankstorage.container.PortableDankContainer;
import tfar.dankstorage.network.C2SMessageTagMode;
import tfar.dankstorage.network.CMessageTogglePickup;
import tfar.dankstorage.network.DankPacketHandler;
import tfar.dankstorage.utils.Utils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

import static tfar.dankstorage.client.screens.DockScreen.*;

public class PortableDankStorageScreen extends AbstractDankStorageScreen<PortableDankContainer> {

  public PortableDankStorageScreen(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component, ResourceLocation background) {
    super(container,playerinventory, component,background);
  }

  private TripleToggleButton tripleToggleButton;

  @Override
  protected void init() {
    super.init();
    int start = this.titleX;
    int namelength = font.getStringPropertyWidth(title);
    start += namelength;
    this.addButton(new RedGreenToggleButton(guiLeft + (start += 7), guiTop + 6 ,8,8, b -> {
      ((RedGreenToggleButton)b).toggle();
      DankPacketHandler.INSTANCE.sendToServer(new C2SMessageTagMode());
    }, Utils.oredict(playerInventory.player.getHeldItemMainhand())));
    tripleToggleButton = new TripleToggleButton(guiLeft + (start += 30), guiTop + 6 ,8,8, b -> {
      ((TripleToggleButton)b).toggle();
      DankPacketHandler.INSTANCE.sendToServer(new CMessageTogglePickup());
    }, Utils.getMode(playerInventory.player.getHeldItemMainhand()));
    this.addButton(tripleToggleButton);
  }

  @Override
  protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
    super.renderHoveredTooltip(matrixStack, x, y);
    if (tripleToggleButton.isHovered()) {
      List<ITextComponent> components = new ArrayList<>();
      components.add(new TranslationTextComponent("dankstorage.mode."+tripleToggleButton.mode));
      this.func_243308_b(matrixStack,components,x,y);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack stack,int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(stack,mouseX, mouseY);
    int namelength = font.getStringPropertyWidth(title);
    int start = this.titleX;
    start+= namelength;
    this.font.drawString(stack,"Tag", start += 18, 6, 0x404040);
    this.font.drawString(stack,"Pickup", start += 30, 6 , 0x404040);
    int nbt_size = container.propertyDelegate.get(container.dankHandler.getSlots());
    int color = nbt_size > 1048576 ? 0x800000 : 0x008000;
    this.font.drawString(stack,"NBT: " + nbt_size,70,this.ySize - 94,color);
  }

  public static PortableDankStorageScreen t1(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component) {
    return new PortableDankStorageScreen(container,playerinventory,component,background1);
  }

  public static PortableDankStorageScreen t2(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component) {
    return new PortableDankStorageScreen(container,playerinventory,component,background2);
  }

  public static PortableDankStorageScreen t3(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component) {
    return new PortableDankStorageScreen(container,playerinventory,component,background3);
  }

  public static PortableDankStorageScreen t4(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component) {
    return new PortableDankStorageScreen(container,playerinventory,component,background4);
  }

  public static PortableDankStorageScreen t5(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component) {
    return new PortableDankStorageScreen(container,playerinventory,component,background5);
  }

  public static PortableDankStorageScreen t6(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component) {
    return new PortableDankStorageScreen(container,playerinventory,component,background6);
  }

  public static PortableDankStorageScreen t7(PortableDankContainer container, PlayerInventory playerinventory, ITextComponent component) {
    return new PortableDankStorageScreen(container,playerinventory,component,background7);
  }

}