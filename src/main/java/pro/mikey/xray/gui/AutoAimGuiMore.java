package pro.mikey.xray.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.apache.commons.lang3.tuple.Pair;
import pro.mikey.xray.ClientController;
import pro.mikey.xray.Configuration;
import pro.mikey.xray.gui.GuiSelectionScreen;
import pro.mikey.xray.gui.utils.GuiBase;
import pro.mikey.xray.utils.BlockData;
import pro.mikey.xray.xray.Controller;

import java.util.ArrayList;
import java.util.UUID;

public class AutoAimGuiMore extends GuiBase {

    private ForgeSlider redSlider;
    private ForgeSlider greenSlider;
    private ForgeSlider blueSlider;
    private ForgeSlider whiteSlider;


    public AutoAimGuiMore() {
        super(true); // Has a sidebar
        this.setSideTitle(I18n.get("xray.single.tools"));
    }

    @Override
    public void init() {


        addRenderableWidget(new Button((getWidth() / 2) + 78, getHeight() / 2 + 58, 120, 20, Component.translatable("xray.single.cancel"), b -> {
            this.onClose();
            this.getMinecraft().setScreen(new GuiSelectionScreenEntity());
        }));
        addRenderableWidget(new Button(getWidth() / 2 - 138, getHeight() / 2 + 83, 202, 20, Component.translatable("xray.single.save"), b -> {
//            BlockData block = new BlockData(
//                    this.oreName.getValue(),
//                    this.block.getBlockName(),
//                    (((int) (redSlider.getValue()) << 16) + ((int) (greenSlider.getValue()) << 8) + (int) (blueSlider.getValue())),
//                    this.block.getItemStack(),
//                    this.block.isDrawing(),
//                    this.block.getOrder()
//            );
            Configuration.store.scrollingspeed.set(redSlider.getValue()/100);
            Configuration.store.angleofsearch.set(greenSlider.getValue());
            Configuration.store.delayframe.set((int) blueSlider.getValue());
            Configuration.store.eyerefactor.set(whiteSlider.getValue()/100);

            this.onClose();
            getMinecraft().setScreen(new GuiSelectionScreenEntity());
        }));
        addRenderableWidget(blueSlider = new ForgeSlider(getWidth() / 2 - 138, getHeight() / 2 - 16,202, 20,  Component.translatable("entity.AA.eyerefactor"), Component.empty(), 0, 150, (int)(Configuration.store.eyerefactor.get()*100), true));
        addRenderableWidget(redSlider = new ForgeSlider(getWidth() / 2 - 138, getHeight() / 2 + 7, 202, 20, Component.translatable("entity.AA.scrollspeed"), Component.empty(), 0, 100, (int)(Configuration.store.scrollingspeed.get()*100), true));
        addRenderableWidget(greenSlider = new ForgeSlider(getWidth() / 2 - 138, getHeight() / 2 + 30, 202, 20, Component.translatable("entity.AA.angleofsearch"), Component.empty(), 0, 180, (int)(Configuration.store.angleofsearch.get()+0.0), true));
        addRenderableWidget(whiteSlider = new ForgeSlider(getWidth() / 2 - 138, getHeight() / 2 + 53,202, 20,  Component.translatable("entity.AA.delay"), Component.empty(), 0, 200, (Configuration.store.delayframe.get()), true));


    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public void renderExtra(PoseStack stack, int x, int y, float partialTicks) {

    }

    @Override
    public boolean mouseClicked(double x, double y, int mouse) {
        return super.mouseClicked(x, y, mouse);
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    public String title() {
        return I18n.get("xray.title.edit");
    }
}
