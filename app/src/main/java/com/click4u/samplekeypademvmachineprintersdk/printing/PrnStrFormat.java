package com.click4u.samplekeypademvmachineprintersdk.printing;

import android.content.res.AssetManager;
import android.text.Layout;

public class PrnStrFormat {
    private int textSize = 20;
    private boolean underline = false;
    private float textScaleX = 1.0F;
    private float letterSpacing = -0.05F;
    private Layout.Alignment ali;
    private PrnTextStyle style;
    private PrnTextFont font;
    private AssetManager am;
    private String path;

    public PrnStrFormat() {
        this.ali = Layout.Alignment.ALIGN_NORMAL;
        this.style = PrnTextStyle.NORMAL;
        this.font = PrnTextFont.DEFAULT;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public boolean isUnderline() {
        return this.underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public float getTextScaleX() {
        return this.textScaleX;
    }

    public void setTextScaleX(float textScaleX) {
        this.textScaleX = textScaleX;
    }

    public float getLetterSpacing() {
        return this.letterSpacing;
    }

    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    public Layout.Alignment getAli() {
        return this.ali;
    }

    public void setAli(Layout.Alignment ali) {
        this.ali = ali;
    }

    public PrnTextStyle getStyle() {
        return this.style;
    }

    public void setStyle(PrnTextStyle style) {
        this.style = style;
    }

    public PrnTextFont getFont() {
        return this.font;
    }

    public void setFont(PrnTextFont font) {
        this.font = font;
    }

    public AssetManager getAm() {
        return this.am;
    }

    public void setAm(AssetManager am) {
        this.am = am;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
