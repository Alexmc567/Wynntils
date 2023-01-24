/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.screens.lootrun.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wynntils.core.components.Models;
import com.wynntils.models.lootruns.LootrunInstance;
import com.wynntils.models.lootruns.type.LootrunPath;
import com.wynntils.screens.base.widgets.WynntilsButton;
import com.wynntils.screens.lootrun.WynntilsLootrunsScreen;
import com.wynntils.screens.maps.MainMapScreen;
import com.wynntils.utils.colors.CommonColors;
import com.wynntils.utils.colors.CustomColor;
import com.wynntils.utils.mc.KeyboardUtils;
import com.wynntils.utils.mc.McUtils;
import com.wynntils.utils.mc.RenderedStringUtils;
import com.wynntils.utils.render.FontRenderer;
import com.wynntils.utils.render.RenderUtils;
import com.wynntils.utils.render.type.HorizontalAlignment;
import com.wynntils.utils.render.type.TextShadow;
import com.wynntils.utils.render.type.VerticalAlignment;
import java.io.File;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class LootrunButton extends WynntilsButton {
    private static final CustomColor BUTTON_COLOR = new CustomColor(181, 174, 151);
    private static final CustomColor BUTTON_COLOR_HOVERED = new CustomColor(121, 116, 101);
    private static final CustomColor TRACKED_BUTTON_COLOR = new CustomColor(176, 197, 148);
    private static final CustomColor TRACKED_BUTTON_COLOR_HOVERED = new CustomColor(126, 211, 106);

    private final LootrunInstance lootrun;
    private final WynntilsLootrunsScreen screen;

    public LootrunButton(int x, int y, int width, int height, LootrunInstance lootrun, WynntilsLootrunsScreen screen) {
        super(x, y, width, height, Component.literal("Lootrun Button"));
        this.lootrun = lootrun;
        this.screen = screen;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        CustomColor backgroundColor = getButtonBackgroundColor();
        RenderUtils.drawRect(poseStack, backgroundColor, this.getX(), this.getY(), 0, this.width, this.height);

        int maxTextWidth = this.width - 21;
        FontRenderer.getInstance()
                .renderText(
                        poseStack,
                        RenderedStringUtils.getMaxFittingText(
                                lootrun.name(),
                                maxTextWidth,
                                FontRenderer.getInstance().getFont()),
                        this.getX() + 14,
                        this.getY() + 1,
                        0,
                        CommonColors.BLACK,
                        HorizontalAlignment.Left,
                        VerticalAlignment.Top,
                        TextShadow.NONE);
    }

    private CustomColor getButtonBackgroundColor() {
        if (isLoaded()) {
            return isHovered ? TRACKED_BUTTON_COLOR_HOVERED : TRACKED_BUTTON_COLOR;
        } else {
            return isHovered ? BUTTON_COLOR_HOVERED : BUTTON_COLOR;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (isLoaded()) {
                Models.Lootrun.clearCurrentLootrun();
            } else {
                Models.Lootrun.loadFile(lootrun.name());
            }
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            Util.getPlatform().openFile(Models.Lootrun.LOOTRUNS);
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if ((KeyboardUtils.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)
                            || KeyboardUtils.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT))
                    && !isLoaded()) {
                tryDeleteLootrun();
                return true;
            }

            LootrunPath path = lootrun.path();
            Vec3 start = path.points().get(0);

            McUtils.mc().setScreen(MainMapScreen.create((float) start.x, (float) start.z));
            return true;
        }

        return true;
    }

    // Not called
    @Override
    public void onPress() {}

    private void tryDeleteLootrun() {
        File file = new File(Models.Lootrun.LOOTRUNS, lootrun.name() + ".json");
        file.delete();
        screen.reloadElements();
    }

    private boolean isLoaded() {
        LootrunInstance currentLootrun = Models.Lootrun.getCurrentLootrun();
        return currentLootrun != null && Objects.equals(currentLootrun.name(), lootrun.name());
    }

    public LootrunInstance getLootrun() {
        return lootrun;
    }
}