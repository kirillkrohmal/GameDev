package com.gamedev;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameDev {
    static int getBackground(BufferedImage image, int x, int y){
        return getPixel(image, x, y)[3];
    }

    static int[] getPixel(BufferedImage image, int x, int y){
        int p = image.getRGB(x, y);
        int a = (p>>24) & 0xff;
        int r = (p>>16) & 0xff;
        int g = (p>>8) & 0xff;
        int b = p & 0xff;
        return new int[]{r, g, b, p};
    }

    private static Ranked[] parseCards(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        int areaStartX = (int) (width * .25);
        int areaEndX   = (int) (width * .25 + width * .05);
        int areaStartY = (int) (height / 2);
        int areaEndY   = areaStartY + (int) (height * .1);

        int[] lightestRGB = new int[]{0, 0, 0};
        int lightestPixel = image.getRGB(0,0);
        int lightestX = 0;
        int lightestY = 0;

        Ranked[] cards = new Ranked[5];

        for(int y = areaStartY; y < areaEndY; y++){
            for(int x = areaStartX; x < areaEndX; x++){
                int[] rgb = getPixel(image, x, y);
                if((rgb[0] + rgb[1] + rgb[2]) > (lightestRGB[0] + lightestRGB[1] + lightestRGB[2])){
                    lightestRGB = rgb;
                    lightestPixel = rgb[3];
                    lightestX = x;
                    lightestY = y;
                }
            }
        }

        int maxWidth = 0;

        class WidthAndXY {
            WidthAndXY(int w, int _x, int _y){
                width = w;
                x = _x;
                y = _y;
            }
            int width, x, y;
        }

        Set<WidthAndXY> widths = new HashSet<WidthAndXY>();

        for(int y = areaStartY; y < areaEndY; y++){
            int p = image.getRGB(lightestX, y);
            if(p != lightestPixel) continue;
            int w = 1;
            int x = lightestX;
            while(true){
                p = image.getRGB(++x, y);
                if(p != lightestPixel) break;
                w++;
            }
            x = lightestX;
            while(true){
                p = image.getRGB(--x, y);
                if(p != lightestPixel) break;
                w++;
            }
            widths.add(new WidthAndXY(w, ++x, y));
        }

        int widestW = 0;
        int widestX = 0;
        int widestY = 0;

        Iterator<WidthAndXY> iter = widths.iterator();

        while(iter.hasNext()){
            WidthAndXY way = iter.next();
            if(way.width > widestW) {
                widestW = way.width;
                widestY = way.y;
                widestX = way.x;
            }
        }

        cards[0] = new Ranked(image.getSubimage(widestX, areaStartY, widestW, (int)(height * .15)), lightestPixel); // высоту берем с запасом, теперь точность не важна

        int middleOfTheCard = widestX + (int)(widestW / 2);
        int[] onWidest = getPixel(image, widestX + 10, widestY);
        for(int i = 1; i < 5; i++) {
            int x = middleOfTheCard + widestW;
            int[] p = getPixel(image, x, widestY);
            while(p[3] == getPixel(image, --x, widestY)[3]);
            middleOfTheCard = x + (int)(widestW / 2);
            int brightness = p[0] + p[1] + p[2];
            if(brightness > 200) cards[i] = new Ranked(image.getSubimage(x, areaStartY, widestW, (int)(height * .15)), p[3]);
        }
        return cards;
    }

    public static void main(String args[]) throws IOException {
        List<String> fileNames = listFilesUsingDirectoryStream(args[0]);
        for(String file : fileNames){
            String fullName = args[0] + File.separator + file;
            parseFile(fullName);
        }
    }

    private static List<String> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if(Files.isDirectory(path)) continue;
                if(!path.getFileName().toString().endsWith(".png")) continue;
                fileList.add(path.getFileName().toString());
            }
        }
        List<String> sortedList = new ArrayList<String>(fileList);
        Collections.sort(sortedList);
        return sortedList;
    }

    private static final int WIDTH = 636;
    private static final int HEIGHT = 1166;

    private static void parseFile(String fileName) throws IOException {

        BufferedImage image = null;

        try {
            File input_file = new File(fileName);

            image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input_file);

            String result = "";
            for(Card card : parseCards(image)) {
                if(card == null) break;
                result += card.toString();
            }
            if(result.indexOf('?') > -1) System.err.println("The file " + fileName + " is not recognized: " + result + ".");
            else {
                System.out.print(fileName + ": " + result);
            }
            System.out.println();

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}






