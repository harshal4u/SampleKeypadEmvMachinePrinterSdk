package com.click4u.samplekeypademvmachineprintersdk.printing;

public enum PrnTextFont {
    DEFAULT("DEFAULT", 1),
    DEFAULT_BOLD("DEFAULT_BOLD", 2),
    MONOSPACE("MONOSPACE", 3),
    SANS_SERIF("SANS_SERIF", 4),
    SERIF("SERIF", 5),
    CUSTOM("CUSTOM", 6);

    private String value;

    private PrnTextFont(String value, int index) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
