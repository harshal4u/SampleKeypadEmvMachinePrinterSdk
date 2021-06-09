package com.click4u.samplekeypademvmachineprintersdk.printing;

public enum PrnFontSizeTypeEnum {
    DEFAULT_SIZE((byte)0),
    DOUBLE_WIDTH((byte)1),
    DOUBLE_HEIGHT((byte)2),
    DOUBLE_WIDTH_HEIGHT((byte)3);

    byte type;

    private PrnFontSizeTypeEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }
}
