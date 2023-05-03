package main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

import net.bramp.ffmpeg.FFmpeg;

public class JFfmpeg extends JFrame {

	private static final long serialVersionUID = 1L;

	private String os = System.getProperty("os.name");

	private String separador;

	private static LinkedList<String> comandos = new LinkedList<String>();

	private LinkedList<String> command = new LinkedList<String>();

	private static String verSiguienteDato(String search, boolean filtro) {

		String resultado;

		try {

			if (filtro) {

				resultado = comandos.get(comandos.indexOf(search) + 1);

			}

			else {

				resultado = comandos.get(comandos.indexOf(search));

			}

		}

		catch (Exception e) {

			resultado = "";

		}

		return resultado;

	}

	private void abrirCarpeta(String ruta) throws IOException {

		if (ruta != null && !ruta.equals("") && !ruta.isEmpty()) {

			try {

				if (os.contains("inux")) {

					Runtime.getRuntime().exec("xdg-open " + ruta);

				}

				else {

					Runtime.getRuntime().exec("cmd /c C:\\Windows\\explorer.exe " + "\"" + ruta + "\"");

				}

			}

			catch (IOException e) {

				System.out.println("Ruta invalida");

			}

		}

	}

	private int comando(String index) {

		int busqueda = buscarComando(index);

		if (busqueda != -1) {

			command.add(comandos.get(busqueda));

			command.add(comandos.get(++busqueda));

		}

		return busqueda;

	}

	private int buscarComando(String filtro) {

		int indice = -1;

		try {

			indice = comandos.indexOf(filtro);

		}

		catch (Exception e) {

			indice = -1;

		}

		return indice;

	}

	public static void main(String[] args) throws IOException {

		new JFfmpeg().jffmpeg(args);

	}

	public void jffmpeg(String[] args) throws IOException {

		command.clear();

		comandos.clear();

		String salida = "";

		String ayuda = "Bad arguments\nUse the argument -h to view help";

		if (args.length == 0) {

			System.out.flush();

			System.out.println(ayuda);

		}

		else {

			for (int i = 0; i < args.length; i++) {

				comandos.add(args[i]);

			}

			if (comandos.contains("-h") || comandos.contains("-help")) {

				System.out.flush();

				System.out.println("\n");

				System.out.println("\t\t  ╔╗ ╔═╗ ╔═╗\n" + "\t\t  ║║ ║╔╝ ║╔╝\n" + "\t\t  ║║╔╝╚╗╔╝╚╗╔╗╔╗╔══╗╔══╗╔══╗\n"
						+ "\t\t╔╗║║╚╗╔╝╚╗╔╝║╚╝║║╔╗║║╔╗║║╔╗║\n" + "\t\t║╚╝║ ║║  ║║ ║║║║║╚╝║║║═╣║╚╝║\n"
						+ "\t\t╚══╝ ╚╝  ╚╝ ╚╩╩╝║╔═╝╚══╝╚═╗║\n" + "\t\t                ║║      ╔═╝║\n"
						+ "\t\t                ╚╝      ╚══╝");

				System.out.println("\n");

				System.out.println("-i  -> Input (path of file).");

				System.out.println("\n");

				System.out.println(
						"-ss -> Time Start \n\n \t Option is considered an actual timestamp,\n\n\t and is not offset by the start time of the file. \n\n\t This matters only for files which do not start\n\n\t from timestamp 0, such as transport streams.");

				System.out.println("\n");

				System.out.println(
						"-t  -> Time Duration \n\n \t Record or transcode \"duration\" seconds of audio/video.");

				System.out.println("\n");

				System.out.println("-r  -> Frame Rate \n\n \t Set frame rate (Hz value, fraction or abbreviation).");

				System.out.println("\n");

				System.out.println("-s  -> Set frame size (WxH or abbreviation)");

				System.out.println("\n");

				System.out.println("-------------------------Watermarks------------------------------------------");

				System.out.println("\n");

				System.out.println("\t-watermark  -> Input (path of watermark file).");

				System.out.println("\n\t---------------------Position----------------------------");

				System.out.println(
						"\n\t\t-pos-watermark -> Position of the watermark file. \n\n\t\t----------------Values---------------------------\n\n \t\t\t0 -> UP - LEFT\n"
								+ "\n" + "\t\t\t1 -> UP - CENTER\n" + "\n" + "\t\t\t2 -> UP - RIGHT\n" + "\n"
								+ "\t\t\t3 -> MIDDLE - LEFT\n" + "\n" + "\t\t\t4 -> MIDDLE - CENTER\n" + "\n"
								+ "\t\t\t5 -> MIDDLE - RIGHT\n" + "\n" + "\t\t\t6 -> DOWN - LEFT\n" + "\n"
								+ "\t\t\t7 -> DOWN - CENTER\n" + "\n" + "\t\t\t8 -> DOWN - RIGHT");

				System.out.println("\n\t\t----------------Color-----------------------------");

				System.out.println(
						"\n \t\t\t--color-watermark -> Watermark text color.\n\n\t\t\tIMPORTANT: You can put colors in html \n\n\t\t---------------Values-----------------------------\n");

				System.out.println(
						"\t\t\tblack\n\n\t\t\twhite\n\n\t\t\tred\n\n\t\t\tblue\n\n\t\t\tyellow\n\n\t\t\tlime\n\n\t\t\tpink\n\n\t\t\tviolet\n\n\t\t\tgray\n\n\t\t\tcyan\n\n\t\t\tdarkblue\n\n\t\t\tlightblue\n\n\t\t\tpurple\n\n\t\t\tMagenta\n\n\t\t\tSilver\n\n\t\t\torange\n\n\t\t\tbrown\n\n\t\t\tmaroon"
								+ "\n\n\t\t\tgreen\n\n\t\t\tolive\n\n\t\t\taquamarine\n\n\t\t\tgold\n\n\t\t\tdarkorange\n\n\t\t\t#CFB53B (Old gold)\n\n\t\t\tchocolate\n\n\t\t\t#D4AF37 (Metallic gold)\n\n\t\t\tturquoise"
								+ "\n\n\t\t\tteal\n\n\t\t\ttseagreen\n\n\t\t\t#78866B (Camouflage green)\n\n\t\t\t#CD7F32(bronze)\n\n\t\t\t#F3E5AB (Medium champagne)");

				System.out.println("\n\t---------------------Text-------------------------------");

				System.out.println("\n\t\t-text-watermark -> Watermark text");

				System.out.println("\n\t\t-font-size-text-watermark -> Watermark Text size");

				System.out.println("\n\t---------------------Quality----------------------------");

				System.out.println("\n\t\t-good -> Set good quality");

				System.out.println("\n\t\t-bad  -> Set low quality\n");

				System.out.println("\t\t-fps  -> Frames per Second\n");

				System.out.println("\t---------------------Output------------------------------");

				System.out.println("\n\t\t-y  -> Overwrite output files");

				System.out.println("\n\t\tYou can put the output file path directly\n\n\t\twithout the -y argument");

				System.out.println(
						"\n\t\tYou can leave this option without arguments\n\n\t\tand without output file, since\n\n\t\tthe program will save the output file\n\n\t\tas \"file-output\" with its corresponding extension");

				System.out.println("\n");

			}

			else {

				FFmpeg ffmpeg = new FFmpeg();

				comando("-i");

				comando("-ss");

				comando("-t");

				comando("-r");

				comando("-s");

				comando("-pix_fmt");

				if (comandos.contains("-watermark")) {

					command.add("-i");

					command.add(verSiguienteDato("-watermark", true));

					command.add("-filter_complex");

					String x = "";

					String y = "";

					try {

						String posicionWatermark = "";

						if (!verSiguienteDato("-pos-watermark", false).equals("")) {

							switch (Integer.parseInt(verSiguienteDato("-pos-watermark", true))) {

							case 0:

								x = "(text_w)/2";

								y = "(text_h)/2";

								break;

							case 1:

								x = "(w-text_w)/2";

								y = "5";

								break;

							case 2:

								x = "(w-text_w)-5";

								y = "(text_h)/2";

								break;

							case 3:

								x = "5";

								y = "(h-text_h)/2";

								break;

							case 4:

								x = "(w-text_w)/2";

								y = "(h-text_h)/2";

								break;

							case 5:

								x = "(w-text_w)-5";

								y = "(h-text_h)/2";

								break;

							case 6:

								x = "5";

								y = "(h-text_h)-5";

								break;

							case 7:

								x = "(w-text_w)/2";

								y = "(h-text_h)-5";

								break;

							case 8:

								x = "(w-text_w)-5";

								y = "(h-text_h)-5";

								break;

							}

							posicionWatermark = ":x=" + x + ":y=" + y;

						}

						String fps = "[0]fps=25,";

						String scale = "scale=480:480,";

						String fontsize = "0";

						String colorWatermark = "black";

						String texto = " ";

						if (!verSiguienteDato("-fps", false).equals("")) {

							fps = "[0]fps=" + verSiguienteDato("-fps", true) + ",";

						}

						if (!verSiguienteDato("-s", false).equals("")) {

							scale = "scale=" + verSiguienteDato("-s", true).replace("x", ":") + ",";

						}

						if (!verSiguienteDato("-font-size-text-watermark", false).equals("")) {

							fontsize = verSiguienteDato("-font-size-text-watermark", true);

						}

						if (!verSiguienteDato("-text-watermark", false).equals("")) {

							texto = verSiguienteDato("-text-watermark", true);

						}

						if (!texto.isEmpty() && !texto.replace(" ", "").isEmpty() && fontsize.equals("0")) {

							fontsize = "25";

						}

						if (!verSiguienteDato("--color-watermark", false).equals("")) {

							colorWatermark = verSiguienteDato("--color-watermark", true);

						}

						command.add(fps + scale
								+ "overlay=main_w-overlay_w-10:main_h-overlay_h-10:format=auto,drawtext=text='" + texto
								+ "':fontsize=" + fontsize + ":fontcolor=" + colorWatermark + posicionWatermark
								+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

					}

					catch (Exception e) {

					}

				}

				else {

					if (verSiguienteDato("-y", true).endsWith(".gif")) {

						if (comandos.contains("-good") || comandos.contains("-bad")) {

							if (comandos.contains("-good")) {

								buenaCalidad();

							}

							if (comandos.contains("-bad")) {

								malaCalidad();

							}

						}

						else {

							buenaCalidad();

						}

					}

					else {

						if (comandos.contains("-good") || comandos.contains("-bad")) {

							if (comandos.contains("-good")) {

								malaCalidad();

							}

							if (comandos.contains("-bad")) {

								buenaCalidad();

							}

						}

						else {

							malaCalidad();

						}

					}

				}

				int busqueda = comando("-y");

				separador = saberSeparador(os);

				if (busqueda == -1) {

					command.add("-y");

					try {

						if (verSiguienteDato("-watermark", false).equals("")) {

							File test = new File(
									comandos.getLast().substring(0, comandos.getLast().lastIndexOf(separador)));

							if (test.isDirectory()) {

								salida = comandos.getLast();

							}

						}

						else {

							salida = calcularNombre();
						}

					}

					catch (Exception e) {

						salida = calcularNombre();

					}

					command.add(salida);

				}

				else {

					salida = verSiguienteDato("-y", true);

				}

				try {

					ffmpeg.run(command);

					abrirCarpeta("file://" + salida);

				}

				catch (Exception e) {

					System.out.println(ayuda);

				}

			}

		}

	}

	private void malaCalidad() {

		command.add("-pix_fmt");

		command.add("rgb24");

	}

	private void buenaCalidad() {

		command.add("-filter_complex");

		String fps = "25";

		if (!verSiguienteDato("-fps", false).equals("")) {

			fps = verSiguienteDato("-fps", true);

		}

		String ancho = verSiguienteDato("-width", true);

		if (ancho == null || ancho.isEmpty()) {

			ancho = "720";

		}

		command.add("[0:v] fps=" + fps + ",scale=w=" + ancho
				+ ":h=-1,split [a][b];[a] palettegen=stats_mode=single [p];[b][p] paletteuse=new=1");

	}

	private static String calcularNombre() {

		String salida = "";

		try {

			salida = verSiguienteDato("-i", true).substring(0, verSiguienteDato("-i", true).lastIndexOf("."))
					+ "-output" + verSiguienteDato("-i", true).substring(verSiguienteDato("-i", true).lastIndexOf("."),
							verSiguienteDato("-i", true).length());

		}

		catch (Exception e) {

		}

		return salida;

	}

	private String saberSeparador(String os2) {

		if (os.contains("indows")) {

			return "\\";

		}

		else {

			return "/";

		}

	}

}
