package xt9.deepmoblearningbm.client.gui.buttons;

import net.minecraft.client.audio.SoundHandler;

/**
 * Created by xt9 on 2018-07-05.
 */
public class ZoneButton extends ClickableZoneButton {
    public ZoneButton(int buttonId, int x, int y, int widthIn, int heightIn, int screenWidth, int screenHeight) {
        super(buttonId, x, y, widthIn, heightIn, screenWidth, screenHeight);
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {}
}
