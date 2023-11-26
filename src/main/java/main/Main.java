package main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		String[] comandos = new String[31];

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

		comandos[8] = "-bad";

		comandos[9] = "-width";

		comandos[10] = "400";

		comandos[11] = "--pos-watermark";

		comandos[12] = "100*100";

		comandos[13] = "--pos-text-watermark";

		comandos[14] = "5";

		comandos[15] = "-text-watermark";

		comandos[16] = "Hola";

		comandos[17] = "--color-watermark-text";

		comandos[18] = "red";

		comandos[19] = "-s";

		comandos[20] = "1000x800";

		comandos[21] = "-font-size-text-watermark";

		comandos[22] = "50";

		comandos[23] = "--font-text-watermark";

		comandos[24] = "/home/linux/0/theboldfont.ttf";

//		comandos[11] = "-watermark";
//
//		comandos[12] = "/home/linux/tijeras.png";

//		comandos[31] = "-watermarkSize";
//
//		comandos[28] = "100x100";

		// comandos[21] = "-blur";

		comandos[25] = "-crop";

		comandos[26] = "100x200x50x50";

		comandos[27] = "-colors";

		comandos[28] = "256";

		comandos[29] = "-bn";

		comandos[30] = "/home/linux/out_41.gif";

		// comandos[6] = "-width";

		// comandos[7] = Integer.toString(ancho. cambia el número de colores a un gif en
		// ffmpeg?getValor());
		// comandos[25] = "/media/linux/DATOSWIN/00 tarjeta/eeee/DCIM/Screen
		// Recorder/BLANCO_Y_NEGRO_3.gif";

		new JFfmpeg(comandos, true);

	}

}
