/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author lachlan
 */
public class TextRenderer {

    private final BitmapFont font;

    public TextRenderer() {
        //this.font = new BitmapFont(Gdx.files.internal("Calibri.fnt"), Gdx.files.internal("Calibri.png"), false);
        this.font = new BitmapFont();
    }

    public void render(SpriteBatch batch, String text, float x, float y, Color colour) {
        font.setColor(colour);
        font.draw(batch, text, x, y);
    }

    public void render(SpriteBatch batch, String text, float x, float y) {
        render(batch, text, x, y, Color.WHITE);
    }
}
