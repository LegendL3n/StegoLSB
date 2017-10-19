package me.L3n.LSB;

public class Utils {
	
	public static int getBitAt(int val, int position) {
		return (val >> position) & 1;
	}
	
	public static int changeBitAt(int val, int position, byte bit) {
		return val | (bit << position);
	}
	
}
