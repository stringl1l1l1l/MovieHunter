package com.example.entity;

public enum MovieGenre {
    NIL(0x0),
    STAGE_ART(0x1),       // 舞台艺术
    SCIENCE_FICTION(0x2), // 科幻
    ANCIENT_COSTUME(0x4), // 古装
    CHILDREN(0x8),        // 儿童
    DISASTER(0x10),       // 灾难
    BIOGRAPHY(0x20),      // 传记
    MUSICAL(0x40),        // 歌舞
    ACTION(0x80),         // 动作
    CRIME(0x100),         // 犯罪
    THRILLER(0x200),      // 惊悚
    FAMILY(0x400),        // 家庭
    FILM_NOIR(0x800),     // 黑色电影
    ANIMATION(0x1000),    // 动画
    MARTIAL_ARTS(0x2000), // 武侠
    ADVENTURE(0x4000),    // 冒险
    TALK_SHOW(0x8000),    // 脱口秀
    HISTORY(0x10000),     // 历史
    SHORT_FILM(0x20000),  // 短片
    MUSIC(0x40000),       // 音乐
    DRAMA(0x80000),       // 剧情
    MYSTERY(0x100000),    // 悬疑
    WAR(0x200000),        // 战争
    HORROR(0x400000),     // 恐怖
    FANTASY(0x800000),    // 奇幻
    COMEDY(0x1000000),    // 喜剧
    SPORTS(0x2000000),    // 运动
    ROMANCE(0x4000000),   // 爱情
    WESTERN(0x8000000),   // 西部
    OTHERS(0x80000000);

    private int value;

    MovieGenre(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

