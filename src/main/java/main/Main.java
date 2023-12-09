package main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		String[] comandos = new String[19];

		comandos[0] = "-i";

		comandos[1] = "C:\\Users\\7\\Documents\\in.mp4";

		comandos[2] = "-ss";

		comandos[3] = "0.0";

		comandos[4] = "-t";

		comandos[5] = "1.0";
//
//		comandos[6] = "-fps";
//
//		comandos[7] = "25";

//		comandos[9] = "-width";
//
//		comandos[10] = "400";
//
//		comandos[11] = "--pos-watermark";
//
//		comandos[12] = "100*100";
//
//		comandos[13] = "--pos-text-watermark";
//
//		comandos[14] = "5";
//
//		comandos[9] = "-text-watermark";
//
//		comandos[10] = "Hola";

//		comandos[17] = "--color-watermark-text";
//
//		comandos[18] = "red";
//
//		comandos[11] = "-s";
//
//		comandos[12] = "1000x800";
////
//		comandos[7] = "-font-size-text-watermark";
//
//		comandos[8] = "50";
//
//		comandos[9] = "--font-text-watermark";
//
//		comandos[10] = "C:\\Users\\7\\Documents\\nueva carpeta\\SecretST.ttf";

		comandos[6] = "-watermark";
		comandos[7] = "C:\\Users\\7\\Downloads\\engranaje.png";

//		comandos[31] = "-watermarkSize";
//
//		comandos[28] = "100x100";

//		comandos[25] = "--text-box";
//
//		comandos[26] = "1x#0000ff@0.5x20";

//		comandos[6] = "-crop";
//
//		comandos[7] = "100x200x50x50";

		comandos[8] = "-s";

		comandos[9] = "500x600";

		comandos[10] = "-rotate";

		comandos[11] = "1x2"; // o #45

		comandos[12] = "-voltear";

		comandos[13] = "1";

		comandos[14] = "-loop";

		comandos[15] = "1";

		// comandos[6] = "-width";

		// comandos[7] = Integer.toString(ancho. cambia el número de colores a un gif en
		// ffmpeg?getValor());
		// comandos[25] = "/media/linux/DATOSWIN/00 tarjeta/eeee/DCIM/Screen
		// Recorder/BLANCO_Y_NEGRO_3.gif";

		comandos[16] = "-bad";

		comandos[17] = "-bn";

//		comandos[17] = "-text-watermark";
//
//		comandos[18] = "Hola";

		comandos[18] = "C:\\Users\\7\\Documents\\out.gif";

		new JFfmpeg(comandos, true);

	}

}
