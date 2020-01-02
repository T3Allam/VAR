package com.T3Allam;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;



public class VAR {
    public static final String SOURCE_FILE = "./resources/var.jpg";
    public static final String DESTINATION_FILE = "./out/var.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        recolorSingleThreaded(originalImage, resultImage);
        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height) {
        int ystriker=originalImage.getHeight();
        int ydefender=originalImage.getHeight();
        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < originalImage.getHeight() ; y++) {
                recolorPixel(originalImage, resultImage, x , y);
                int red = getRed(originalImage.getRGB(x, y));
                int green = getGreen(originalImage.getRGB(x, y));
                int blue = getBlue(originalImage.getRGB(x, y));
                if (isRed(red, green, blue)) {
                    if (y < ystriker) {
                        ystriker = y;
                    }
                }
                if (isBlue(red, green, blue)) {
                    if (y < ydefender) {
                        ydefender = y;
                    }
                }
            }
        }
        System.out.println(ystriker);
        System.out.println(ydefender);

        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            recolorPixelHorizontal(resultImage, x , ystriker);
        }
        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            recolorPixelHorizontal(resultImage, x , ydefender);
        }
    }


    public static void recolorPixelHorizontal(BufferedImage resultImage, int x, int y) {
        int newRed=0;
        int newGreen=0;
        int newBlue=0;
        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if(isRed(red, green, blue)) {
            newRed = 255;
            newGreen = 0;
            newBlue = 0;
        } else if (isBlue(red, green, blue)){
            newRed = 0;
            newGreen = 0;
            newBlue = 255;
        } else {
            newRed = 255;
            newGreen = 255;
            newBlue = 255;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static boolean isRed(int red, int green, int blue) {
        return Math.abs(red - green) > 160 && Math.abs(red - blue) > 160 && Math.abs( green - blue) < 100;
    }

    public static boolean isBlue(int red, int green, int blue) {
        return Math.abs(blue - green) > 160 && Math.abs(blue - red) > 160 && Math.abs( green - red) < 100;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }
}
