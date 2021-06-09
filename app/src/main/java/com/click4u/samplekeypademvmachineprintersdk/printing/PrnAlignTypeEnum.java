package com.click4u.samplekeypademvmachineprintersdk.printing;

public enum PrnAlignTypeEnum {
    ALIGN_LEFT((byte)1),
    ALIGN_CENTER((byte)2),
    ALIGN_RIGHT((byte)3);

    byte type;

    private PrnAlignTypeEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }
}
