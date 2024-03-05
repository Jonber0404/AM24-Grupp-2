package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class TextUtil {

    /**
     * Creates a BitmapFont object with the specified font file, size and color.
     * @param fontPath The filename of the font located in internal assets
     * @param size the font size
     * @param color the color of the font
     * @return A new BitmapFont. Remember to dispose!
     */
    public static BitmapFont generateFont(String fontPath, int size, Color color) {
        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
        fontParam.size = size;
        fontParam.color = color;
        return fontGen.generateFont(fontParam);
    }
}
