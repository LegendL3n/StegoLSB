package me.L3n.LSB;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Press E to encode or D to decode: ");
		
		char option = '\0';
		
		do {
			option = scanner.nextLine().toUpperCase().charAt(0);
		} while(!(option == 'E' || option == 'D'));
		
		boolean camouflage = (option == 'E');
		
		System.out.print("Enter the path of the image: ");

		if (camouflage) {
			String path = scanner.nextLine();

			BufferedImage img = ImageIO.read(new File(path));

			System.out.print("Enter desired message: ");

			String msg = scanner.nextLine();

			long init = System.currentTimeMillis();

			Stego.encode(img, msg);

			long finit = System.currentTimeMillis();

			System.out.println("Camouflage finished.");
			
			System.out.println("Time taken was " + (finit - init) + "ms.");

			String camouflagedPath = path.substring(0, path.lastIndexOf(".")) + "_camouflaged.png";

			ImageIO.write(img, "png", new File(camouflagedPath));

			System.out.println("Changes written to " + camouflagedPath + ".");

			scanner.close();
		} else {
			String path = scanner.nextLine();

			BufferedImage img = ImageIO.read(new File(path));

			long init = System.currentTimeMillis();

			String message = Stego.decode(img);

			long finit = System.currentTimeMillis();

			System.out.println("Message: " + message);

			System.out.println("Uncamouflage finished.");
			System.out.println("Time taken was " + (finit - init) + "ms.");
		}
	}

}