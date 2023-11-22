package main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		String[] comandos = new String[36];

		comandos[0] = "-i";

		comandos[1] = "/home/linux/in.mp4";
		// comandos[1] = "/media/linux/DATOSWIN/00 tarjeta/eeee/DCIM/Screen
		// Recorder/2021_09_05_02_10_35.mp4";

		comandos[2] = "-ss";

		comandos[3] = "0.0";

		comandos[4] = "-t";

		comandos[5] = "1.0";

		comandos[6] = "-fps";

		comandos[7] = "25";

		comandos[8] = "-good";

		comandos[9] = "-width";

		comandos[10] = "400";

		comandos[11] = "-watermark";

		comandos[12] = "/home/linux/tijeras.png";

		comandos[13] = "--pos-watermark";

		comandos[14] = "100*100";

		comandos[15] = "--pos-text-watermark";

		comandos[16] = "5";

		comandos[17] = "-text-watermark";

		comandos[18] = "Hola";

		comandos[19] = "--color-watermark-text";

		comandos[20] = "white";

		comandos[21] = "-blur";

		comandos[22] = "-crop";

		comandos[23] = "100x200x50x50";

		comandos[24] = "-s";

		comandos[25] = "1000x800";

		comandos[26] = "-y";

		comandos[27] = "/home/linux/out_31.gif";

		comandos[28] = "-font-size-text-watermark";

		comandos[29] = "50";

		comandos[30] = "-watermarkSize";

		comandos[31] = "1000x800";

		comandos[32] = "-bn";

		comandos[33] = "2";

		comandos[34] = "--font-text-watermark";

		comandos[35] = "/home/linux/0/theboldfont.ttf";

		// comandos[6] = "-width";

		// comandos[7] = Integer.toString(ancho.getValor());
		// comandos[25] = "/media/linux/DATOSWIN/00 tarjeta/eeee/DCIM/Screen
		// Recorder/BLANCO_Y_NEGRO_3.gif";

		new JFfmpeg().jffmpeg(comandos, true);

	}

}
