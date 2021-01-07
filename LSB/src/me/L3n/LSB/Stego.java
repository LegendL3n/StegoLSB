package me.L3n.LSB;

import java.awt.image.BufferedImage;

public class Stego {

    private static final int UNSET_LSB_BITMASK = ~(0b1);
    private static final int SET_LSB_BITMASK = 1; // just because there's the unset :P

    public static void encode(BufferedImage img, String msg) {
        int charCount = 0;
        int bitCount = 0;

        // Iterate all pixels of the image until there are not chars left to encode
        for (int x = 0; x < img.getWidth() && charCount <= msg.length(); x++) {
            for (int y = 0; y < img.getHeight() && charCount <= msg.length(); y++) {
                int pixelColor = img.getRGB(x, y);

                if (charCount < msg.length()) {
                    int bit = BitUtils.getBitAt(msg.charAt(charCount), bitCount);

                    /* Set the bit at the current pixel to the current msg char bit
                     * If the current char bit is not set then:
                     * - do an AND with the inverted of 1 to unset only the LSB
                     * Else:
                     * - do an OR with 1 to set the LSB
                     */
                    img.setRGB(x, y, (((bit & SET_LSB_BITMASK) == 0) ? pixelColor & UNSET_LSB_BITMASK : pixelColor | SET_LSB_BITMASK));
                } else {
                    // If we already finished writing the msg chars then proceed to put a null terminator
                    // again, unset the LSB
                    img.setRGB(x, y, pixelColor & UNSET_LSB_BITMASK);
                }

                bitCount++;

                if (bitCount == 8) { // if a char has already been written in 8 pixels
                    bitCount = 0;
                    charCount++;
                }
            }
        }
    }

    public static String decode(BufferedImage img) {
        StringBuilder msg = new StringBuilder();

        int currentChar = 0;
        int bitCount = 0;
        int zeroBitsCount = 0;

        // iterate the image's pixels until we either hit the end of it
        // or we encounter a null terminator (8 pixels with the LSB unset/at 0)
        for (int x = 0; x < img.getWidth() && zeroBitsCount < 7; x++) {
            for (int y = 0; y < img.getHeight() && zeroBitsCount < 7; y++) {
                int pixelColor = img.getRGB(x, y);

                // if the current pixel's LSB is zero
                if ((pixelColor & SET_LSB_BITMASK) == 0) {
                    zeroBitsCount++;
                }

                // change the  bit of the char we're going to insert to the current pixel's LSB
                currentChar = BitUtils.changeBitAt(
                        currentChar,
                        bitCount++,
                        (byte) BitUtils.getBitAt(pixelColor, 0)) & 0xFF;

                if (bitCount == 8) {
                    msg.append((char) currentChar);

                    // look for a new character
                    currentChar = 0;
                    bitCount = 0;
                    zeroBitsCount = 0;
                }
            }
        }

        return msg.toString();
    }
}
