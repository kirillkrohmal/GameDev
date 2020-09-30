package com.gamedev;

import java.awt.image.BufferedImage;

public class Card {
    enum Suits {
        HZ,
        BLACK,
        RED,
        DIAMONDS,
        SPADES,
        HEARTS,
        CLUBS
    }

    private final int SUIT_CENTER_X  = 40;
    private final int SUIT_CENTER_Y  = 67;
    private final int DIAMONT_HEART_LINE = 54;
    private final int CLUB_SPADE_LINE = 60;
    private final int SPADE_WIDTH = 18;

    private Suits suit = Suits.HZ;
    protected BufferedImage image;
    protected int background;

    private Suits detectSuit(){
        int[] dp = GameDev.getPixel(image, SUIT_CENTER_X, SUIT_CENTER_Y);
        Suits _suit = dp[0] > dp[1] * 1.5 && dp[0] > dp[2] * 1.5 ? Suits.RED : Suits.BLACK;
        if(_suit == Suits.RED){
            int dhlc = countPixels(DIAMONT_HEART_LINE);
            _suit = dhlc == 1 ? Suits.HEARTS : Suits.DIAMONDS;
        }
        else _suit =  countPixels(CLUB_SPADE_LINE ) < SPADE_WIDTH ? Suits.CLUBS: Suits.SPADES;
        return _suit;
    }

    protected int countPixels(int level){
        int count = 0;
        for(int i = 0; i < this.image.getWidth(); i++){
            int[] p = GameDev.getPixel(image, i, level);
            if(p[3] != background) count++;
        }
        return count;
    }

    public Card (BufferedImage img, int bg) {
        image = img;
        background = bg;
        this.suit = detectSuit();
    }

    @Override
    public String toString() {
        switch(this.suit){
            case CLUBS:
                return "c";
            case DIAMONDS:
                return "d";
            case HEARTS:
                return "h";
            case SPADES:
                return "s";
            default:
                return "?";
        }
    }
}


