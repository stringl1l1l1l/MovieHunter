package com.example.entity;

public enum Regions {
    NIL(0x0),               // 未指定地区
    CHINA_MAINLAND(0x1),    // 中国大陆
    CHINA_TAIWAN(0x2),      // 中国台湾
    CHINA_HONGKONG(0x4),    // 中国香港
    UK(0x8),                // 英国
    FRANCE(0x10),           // 法国
    GERMANY(0x20),          // 德国
    ITALY(0x40),            // 意大利
    SPAIN(0x80),            // 西班牙
    CANADA(0x100),          // 加拿大
    ARGENTINA(0x200),       // 阿根廷
    DENMARK(0x400),         // 丹麦
    POLAND(0x800),          // 波兰
    SOUTH_KOREA(0x1000),    // 韩国
    MALAYSIA(0x2000),       // 马来西亚
    NETHERLANDS(0x4000),    // 荷兰
    NORWAY(0x8000),         // 挪威
    BELGIUM(0x10000),       // 比利时
    MEXICO(0x20000),        // 墨西哥
    RUSSIA(0x40000),        // 俄罗斯
    SWEDEN(0x80000),        // 瑞典
    BRAZIL(0x100000),       // 巴西
    CZECH(0x200000),        // 捷克
    AUSTRALIA(0x400000),    // 澳大利亚
    INDIA(0x800000),        // 印度
    JAPAN(0x1000000),       // 日本
    HUNGARY(0x2000000),     // 匈牙利
    USA(0x4000000),         // 美国
    ISRAEL(0x80000000),     // 以色列
    FINLAND(0x10000000),    // 芬兰
    IRAN(0x20000000),       // 伊朗
    IRELAND(0x40000000),    // 爱尔兰
    OTHERS(0x80000000);     // 其他

    private int value;

    Regions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

