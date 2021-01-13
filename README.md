# StegoLSB

This is a Java version of the Lowest Significant Bit steganography technique.

It demonstrates how to hide an ASCII text message inside an image, making it unnoticeable displaying it.

Basically this works by changing an Image so that each pixel's [LSB](https://www.computerhope.com/jargon/l/leastsb.htm "What is LSB") (of its RGB value) is equal to each bit the message.

To know the length of the message when decoding, I chose to end the message with a null terminator, stopping when it
 encounters 8 zero-bit LSBs.
 
 ## Limitations
 
 Currently there's no way to know if an image has something encoded or not.
 (except for the name, which is obviously unreliable)
 
## Note

I used the LSB of the whole color, so it's essentially just the blue byte (due to how Java handles RGB), but you could
use each byte of the pixel, name the red, blue, green and alpha channels.
