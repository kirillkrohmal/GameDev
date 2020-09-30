package com.gamedev;

import java.awt.image.BufferedImage;

public class Ranked extends Card {
    enum Ranks {
        HZ,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE
    }

    private final int LEVEL_1 = 10;
    private final int LEVEL_2 = 15;
    private final int LEVEL_3 = 20;
    private final int LEVEL_4 = 25;
    private final int LEVEL_5 = 30;
    private final int[] levels = new int[]{LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5};
    private int levelForFive = LEVEL_4 - 2;

    private final int PRETTY_WIDE   = 10;
    private final int PRETTY_NARROW = 8;

    private boolean[] holes = new boolean[levels.length];
    private boolean[] wide = new boolean[levels.length];
    private boolean[] narrow = new boolean[levels.length];
    private int[] starts = new int [levels.length];

    private Ranks rank;

    private Ranks getRank(){
        if(starts[1] == starts[2] && starts[1] == starts[3] && starts[1] == starts[4] && starts[0] < starts[1]) return Ranks.TEN;
        if(narrow[0] && narrow[1] && narrow[2] && starts[4] < starts[2]) return Ranks.JACK;
        if(starts[0] == starts[1] && starts[0] == starts[2] && starts[0] == starts[3] && starts[0] == starts[4]) return Ranks.KING;
        if(wide[0] && starts[4] < starts[3] && starts[3] < starts[2] && starts[2] < starts[1]) return Ranks.SEVEN;
        if(wide[4] && starts[4] < starts[3] && starts[3] < starts[2] && starts[0] < starts[2]) return Ranks.TWO;
        if(narrow[0] && holes[2] && wide[3] && holes[4]) return Ranks.ACE;
        if(narrow[0] && narrow[4] && wide[3] && holes[2]) return Ranks.FOUR;
        if(wide[0] && narrow[1] && starts[0] < starts[1] && starts[2] < starts[1] && starts[2] > starts[0]) return Ranks.THREE;
        if(wide[0] && wide[2] && wide[4]){
            if(getMaxWidth(LEVEL_3) > 20) return Ranks.QUEEN;
            if(holes[1] && !holes[2] && holes[3] && starts[3] < starts[1]) return Ranks.EIGHT;
            if(holes[1]) return Ranks.NINE;
            boolean noHoleHere = !hasHole(LEVEL_4 - 2);
            if(noHoleHere && narrow[1]) return Ranks.FIVE;
            return Ranks.SIX;
        }
        return Ranks.HZ;
    }

    private int getMaxWidth(int level){
        int count = image.getWidth() - 5;
        for(int x = 5; x < image.getWidth(); x++){
            if(GameDev.getBackground(image, x, level) != background) break;
            count--;
        }
        for(int x = image.getWidth() - 1; x > -1; x--){
            if(GameDev.getBackground(image, x, level) != background) break;
            count--;
        }
        return count;
    }

    Ranked(BufferedImage img, int bg){
        super(img, bg);
        image = img.getSubimage(0, 0, 40, 35);
        for(int i = 0; i < levels.length; i++){
            int l = levels[i];
            holes[i] = hasHole(l);
            wide[i] = isPrettyWide(l);
            narrow[i] = isPrettyNarrow(l);
            starts[i] = startsWith(l);
        }
        rank = getRank();
    }

    private int startsWith(int level){
        for(int x = 5; x < image.getWidth(); x++) {
            if(GameDev.getBackground(image, x, level) != background) return x;
        }
        return -1;
    }

    private int countConsiderable(int level){
        int count = 0;
        for(int x = 5; x < image.getWidth(); x++) {
            if(GameDev.getBackground(image, x, level) != background) count++;
        }
        return count;
    }

    private boolean isPrettyWide(int level){
        return countConsiderable(level) > PRETTY_WIDE;
    }

    private boolean isPrettyNarrow(int level){
        return countConsiderable(level) < PRETTY_NARROW;
    }

    private boolean hasHole(int level) {
        int x = 5;
        int w = image.getWidth();
        while(x < w && GameDev.getPixel(image, x++, level)[3] == background);
        if(x == w) return false;
        while(x < w && GameDev.getPixel(image, x++, level)[3] != background);
        if(x == w) return false;
        while(x < w && GameDev.getPixel(image, x++, level)[3] == background);
        if(x == w) return false;
        while(x < w && GameDev.getPixel(image, x++, level)[3] != background);
        if(x == w) return false;
        while(x < w && GameDev.getPixel(image, x++, level)[3] == background);
        return x == w;
    }

    @Override
    public String toString() {
        String prefix = "?";
        String postfix = super.toString();
        switch(rank){
            case TWO:
                prefix = "2";
                break;
            case THREE:
                prefix = "3";
                break;
            case FOUR:
                prefix = "4";
                break;
            case FIVE:
                prefix = "5";
                break;
            case SIX:
                prefix = "6";
                break;
            case SEVEN:
                prefix = "7";
                break;
            case EIGHT:
                prefix = "8";
                break;
            case NINE:
                prefix = "9";
                break;
            case TEN:
                prefix = "10";
                break;
            case JACK:
                prefix = "J";
                break;
            case QUEEN:
                prefix = "Q";
                break;
            case KING:
                prefix = "K";
                break;
            case ACE:
                prefix = "A";
                break;
        }
        return prefix + postfix;
    }
}
