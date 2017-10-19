package me.L3n.LSB;

import java.awt.image.BufferedImage;

public class Stego {

	public static void encode(BufferedImage img, String msg) {
		int charCount = 0;
		int bitCount = 0;

		for (int x = 0; x < img.getWidth() && charCount <= msg.length(); x++) {
			for (int y = 0; y < img.getHeight() && charCount <= msg.length(); y++) {
				int pixelColor = img.getRGB(x, y);

				if (charCount < msg.length()) { // if we are still writing msg chars
					// grabs the bit at bitCount of the char we're currently inserting
					int bit = Utils.getBitAt(msg.charAt(charCount), bitCount);

					/* set the bit at the current pixel to the current msg char bit
					 * the ternary operator is basically doing:
					 * if the current char bit is not set then:
					 * - do a AND with the inverted of 1 to unset only the LSB
					 * if it isn't set:
					 * - do a OR with 1 to set the LSB
					 */
					img.setRGB(x, y, (((bit & 1) == 0) ? pixelColor & ~(0b1) : pixelColor | 1));					
				} else { // if we already finished writing the msg chars then proceed to put a null terminator
					// again, unset the LSB
					img.setRGB(x, y, pixelColor & ~(0b1));
				}

				bitCount++;

				if (bitCount == 8) { // if a char has already been written in 8 pixels
					bitCount = 0;
					charCount++; // go to next char
				}
			}
		}
	}

	public static String decode(BufferedImage img) {
		String msg = "";

		int currentChar = 0;
		int bitCount = 0;
		int zeroBitsCount = 0;

		// iterate the image's pixels until we either hit the end of it
		// or we encounter a null terminator(8 pixels with the LSB unset/0)
		for (int x = 0; x < img.getWidth() && zeroBitsCount < 7; x++) {
			for (int y = 0; y < img.getHeight() && zeroBitsCount < 7; y++) {
				int pixelColor = img.getRGB(x, y);

				// if the current pixel's LSB is zero
				if ((pixelColor & 1) == 0) {
					zeroBitsCount++;
				}

				// change the bit of the char we're going to insert to the current pixel's LSB
				currentChar = Utils.changeBitAt(currentChar, bitCount++, (byte) Utils.getBitAt(pixelColor, 0)) & 0xFF;

				if (bitCount == 8) {
					msg += (char)currentChar;

					// look for a new character
					currentChar = 0;
					bitCount = 0;
					zeroBitsCount = 0;
				}
			}
		}

		return msg;
	}
}
