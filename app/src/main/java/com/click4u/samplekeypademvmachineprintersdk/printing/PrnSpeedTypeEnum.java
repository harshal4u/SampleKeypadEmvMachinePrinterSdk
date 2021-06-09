package com.click4u.samplekeypademvmachineprintersdk.printing;

public enum PrnSpeedTypeEnum {
    LOW_SPEED((byte)0),
    MEDIUM_SPEED((byte)1),
    HIGH_SPEED((byte)2);

    byte type;

    private PrnSpeedTypeEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }
}
