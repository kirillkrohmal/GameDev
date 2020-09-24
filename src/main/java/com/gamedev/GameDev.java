package com.gamedev;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameDev {
    public static void main(String args[]) throws IOException {
        int width = 636;
        int height = 1166;

        BufferedImage image = null;

        try {
            File input_file = new File("C:\\projects\\game\\src\\main\\resources\\imgs\\20180821_055341.782_0x26080126.png"); //image file path

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input_file);

            System.out.println("Reading complete.");
            Card card = new Card(image);

            System.out.println(card.getDeck());
            System.out.println(card.getSuit());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}





