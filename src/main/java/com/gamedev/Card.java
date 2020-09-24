package com.gamedev;

import java.awt.image.BufferedImage;

public class Card {



    enum Suits {
        DIAMONDS,
        SPIKES,
        HEARTS,
        CROSSES
    }

    enum Deck {
        SIX,
        SEVEN,
        EIHGT,
        NINE,
        TEN,
        JACK,
        QUENN,
        KING,
        ACE
    }

    Suits suit;
    Deck deck;

    static int TestHight = 615;

    public Card (BufferedImage pixel) {

    }

    public String getSuit() {
        return null;
    }

    public String getDeck() {
        return null;
    }
}

