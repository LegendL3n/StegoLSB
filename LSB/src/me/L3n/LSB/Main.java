package me.L3n.LSB;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Press E to encode or D to decode: ");
		
		char option;
		
		do {
			option = scanner.nextLine().toUpperCase().charAt(0);
		} while(!(option == 'E' || option == 'D'));
		
		boolean camouflage = (option == 'E');

		JFileChooser fileChooser = new JFileChooser() {
			// Hack to bring the dialog above all other windows
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException {
				// intercept the dialog created by JFileChooser
				JDialog dialog = super.createDialog(parent);
				dialog.setAlwaysOnTop(true);
				return dialog;
			}
		};

		fileChooser.setDialogTitle("Choose a file");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.showOpenDialog(null);

		File fileChosen = fileChooser.getSelectedFile();
		BufferedImage img = ImageIO.read(fileChosen);

		if (camouflage) {
			System.out.print("Enter desired message: ");

			String msg = scanner.nextLine();

			long init = System.currentTimeMillis();

			Stego.encode(img, msg);

			long finit = System.currentTimeMillis();

			System.out.println("Camouflage finished.");
			System.out.println("Time taken was " + (finit - init) + "ms.");

			String filePath = fileChosen.getPath();
			String camouflagedPath = filePath.substring(0, filePath.lastIndexOf(".")) + "_camouflaged.png";

			ImageIO.write(img, "png", new File(camouflagedPath));

			System.out.println("Changes written to " + camouflagedPath + ".");

			scanner.close();
		} else {
			long init = System.currentTimeMillis();

			String message = Stego.decode(img);

			long finit = System.currentTimeMillis();

			System.out.println("Message: " + message);

			System.out.println("Uncamouflage finished.");
			System.out.println("Time taken was " + (finit - init) + "ms.");
		}
	}
}