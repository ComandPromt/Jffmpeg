package main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		String[] comandos = new String[32];

		comandos[0] = "-i";

		comandos[1] = "C:\\Users\\7\\Documents\\conteo.mp4";

		comandos[2] = "-ss";

		comandos[3] = "0.0";

		comandos[4] = "-t";

		comandos[5] = "1.0";

		comandos[6] = "-good";

		comandos[7] = "-s";

		comandos[8] = "500x600";

		comandos[9] = "-voltear";

		comandos[10] = "1";

		comandos[11] = "-reverse";

		comandos[12] = "-rotate";

		comandos[13] = "1x2"; // o #45

		comandos[14] = "-crop";

		comandos[15] = "100x200x50x50";

		comandos[16] = "-font-size-text-watermark";

		comandos[17] = "50";

		comandos[18] = "-fps";

		comandos[19] = "25";

		comandos[20] = "--color-watermark-text";

		comandos[21] = "red";

		comandos[22] = "-text-watermark";

		comandos[23] = "Hola";

		comandos[24] = "--pos-text-watermark";

		comandos[25] = "5";

		// comandos[26] = "-bn";

		comandos[26] = "2";

		comandos[27] = "-colors";

		comandos[28] = "20";

		comandos[29] = "--text-box";

		comandos[30] = "1x#0000ff@0.5x20";

		comandos[31] = "C:\\Users\\7\\Desktop\\aaaa.gif";

//		comandos[11] = "--pos-watermark";
//
//		comandos[12] = "100*100";
//
//		comandos[9] = "--font-text-watermark";
//
//		comandos[10] = "C:\\Users\\7\\Documents\\nueva carpeta\\SecretST.ttf";

//		comandos[6] = "-watermark";

//		comandos[7] = "C:\\Users\\7\\Downloads\\engranaje.png";

//		comandos[31] = "-watermarkSize";
//
//		comandos[28] = "100x100";

		new JFfmpeg(comandos, true);

	}

}