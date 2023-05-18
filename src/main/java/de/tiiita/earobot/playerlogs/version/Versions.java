package de.tiiita.earobot.playerlogs.version;

/**
 * Created on April 04, 2023 | 23:45:22
 * (●'◡'●)
 */
public enum Versions {
    V1_7("1.7", 5),
    V1_8("1.8", 47),
    V1_9("1.9", 107),
    V1_9_1("1.9.1", 108),
    V1_9_2("1.9.2", 109),
    V1_9_3("1.9.3/4", 110),
    V1_10("1.10/1/2", 210),
    V1_11("1.11", 315),
    V1_11_1("1.11.1/2", 316),
    V1_12("1.12", 335),
    V1_12_1("1.12.1", 338),
    V1_12_2("1.12.2", 340),
    V1_13("1.13", 393),
    V1_13_1("1.13.1", 401),
    V1_13_2("1.13.2", 404),
    V1_14("1.14", 477),
    V1_14_1("1.14.1", 480),
    V1_14_2("1.14.2", 485),
    V1_14_3("1.14.3", 490),
    V1_14_4("1.14.4", 498),
    V1_15("1.15", 573),
    V1_15_1("1.15.1", 575),
    V1_15_2("1.15.2", 578),
    V1_16("1.16", 735),
    V1_16_1("1.16.1", 736),
    V1_16_2("1.16.2", 751),
    V1_16_3("1.16.3", 753),
    V1_16_4("1.16.4/5", 754),
    V1_17("1.17", 755),
    V1_17_1("1.17.1", 756),
    V1_18("1.18/1", 757),
    V1_18_2("1.18.2", 758),
    V1_19("1.19", 759),
    V1_19_1("1.19.1/2", 760),
    V1_19_3("1.19.3", 761),
    V1_19_4("1.19.4", 762);

    private final String displayVersion;
    private final int protocolId;

    Versions(String displayVersion, int protocolId) {
        this.displayVersion = displayVersion;
        this.protocolId = protocolId;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }

    public int getProtocolId() {
        return protocolId;
    }

    public static String getVersionById(int protocolId) {
        for (Versions version : values()) {
            if (protocolId == version.protocolId) {
                return version.displayVersion;
            }
        }
        return "Unknown ID (" + protocolId + ")[Error]";
    }
}