package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import net.bramp.ffmpeg.FFmpeg;

public class JFfmpeg extends JFrame {

	private static final long serialVersionUID = 1L;

	private String os;

	private String separador;

	private static LinkedList<String> comandos;

	private LinkedList<String> command;

	private boolean fallo;

	private boolean marcaDeAgua;

	private int ancho;

	private int alto;

	private boolean posicionarWatermark;

	public JFfmpeg() {

		os = System.getProperty("os.name");

	}

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

		return resultado.trim();

	}

	public void abrirCarpeta(String ruta) {

		if (ruta != null && !ruta.equals("") && !ruta.isEmpty()) {

			try {

				if (os.contains("indows")) {

					Runtime.getRuntime().exec("cmd /c C:\\Windows\\explorer.exe " + "\"" + ruta + "\"");

				}

				else if (os.contains("inux")) {

					ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", ruta);

					processBuilder.redirectErrorStream(true);

					processBuilder.start();

				}

				else {

					Runtime.getRuntime().exec("open " + "\"" + ruta + "\"");

				}

			}

			catch (IOException e) {

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

	public void help() {

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

		System.out.println("-t  -> Time Duration \n\n \t Record or transcode \"duration\" seconds of audio/video.");

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
				"\n\t\t--pos-text-watermark -> Position of the watermark text. \n\n\t\t----------------Values---------------------------\n\n \t\t\t1 -> UP - LEFT\n"
						+ "\n" + "\t\t\t2 -> UP - CENTER\n" + "\n" + "\t\t\t3 -> UP - RIGHT\n" + "\n"
						+ "\t\t\t4 -> MIDDLE - LEFT\n" + "\n" + "\t\t\t5 -> MIDDLE - CENTER\n" + "\n"
						+ "\t\t\t6 -> MIDDLE - RIGHT\n" + "\n" + "\t\t\t7 -> DOWN - LEFT\n" + "\n"
						+ "\t\t\t8 -> DOWN - CENTER\n" + "\n" + "\t\t\t9 -> DOWN - RIGHT");

		System.out.println("\n\t\t----------------Color-----------------------------");

		System.out.println(
				"\n \t\t\t--color-watermark-text -> Watermark text color.\n\n\t\t\tIMPORTANT: You can put colors in html \n\n\t\t---------------Values-----------------------------\n");

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

	private String[] saberPosicion(int posicion) {

		String[] pos = new String[2];

		String x;

		String y;

		switch (posicion) {

		case 1:

			x = "(text_w)/2";

			y = "(text_h)/2";

			break;

		case 2:

			x = "(w-text_w)/2";

			y = "5";

			break;

		case 3:

			x = "(w-text_w)-5";

			y = "(text_h)/2";

			break;

		case 4:

			x = "5";

			y = "(h-text_h)/2";

			break;

		case 5:

			x = "(w-text_w)/2";

			y = "(h-text_h)/2";

			break;

		case 6:

			x = "(w-text_w)-5";

			y = "(h-text_h)/2";

			break;

		case 7:

			x = "5";

			y = "(h-text_h)-5";

			break;

		case 9:

			x = "(w-text_w)-5";

			y = "(h-text_h)-5";

			break;

		default:

			x = "(w-text_w)/2";

			y = "(h-text_h)-5";

			break;

		}

		pos[0] = x;

		pos[1] = y;

		return pos;

	}

	public void marcaDeAgua() {

//
//			switch (colorWatermark.getSelectedIndex()) {
//
//			case 0:
//
//				color = "black";
//
//				break;
//
//			case 1:
//
//				color = "white";
//
//				break;
//
//			case 2:
//
//				color = "red";
//
//				break;
//
//			case 3:
//
//				color = "blue";
//
//				break;
//
//			case 4:
//
//				color = "yellow";
//
//				break;
//			case 5:
//
//				color = "lime";
//
//				break;
//
//			case 6:
//
//				color = "pink";
//
//				break;
//
//			case 7:
//
//				color = "violet";
//
//				break;
//
//			case 8:
//
//				color = "gray";
//
//				break;
//
//			case 9:
//
//				color = "cyan";
//
//				break;
//
//			case 10:
//
//				color = "darkblue";
//
//				break;
//
//			case 11:
//
//				color = "lightblue";
//
//				break;
//
//			case 12:
//
//				color = "Purple";
//
//				break;
//
//			case 13:
//
//				color = "Magenta";
//
//				break;
//
//			case 14:
//
//				color = "Silver";
//
//				break;
//
//			case 15:
//
//				color = "orange";
//
//				break;
//
//			case 16:
//
//				color = "brown";
//				break;
//
//			case 17:
//
//				color = "maroon";
//
//				break;
//
//			case 18:
//				color = "green";
//
//				break;
//			case 19:
//
//				color = "olive";
//
//				break;
//
//			case 20:
//
//				color = "aquamarine";
//
//				break;
//
//			case 21:
//
//				color = "gold";
//
//				break;
//
//			case 22:
//
//				color = "darkorange";
//
//				break;
//
//			case 23:
//
//				color = "#CFB53B";
//
//				break;
//
//			case 24:
//				color = "chocolate";
//				break;
//
//			case 25:
//				color = "#D4AF37";
//				break;
//
//			case 26:
//				color = "turquoise";
//				break;
//
//			case 27:
//				color = "teal";
//				break;
//
//			case 28:
//				color = "seagreen";
//				break;
//
//			case 29:
//				color = "#78866B";
//				break;
//
//			case 30:
//				color = "#CD7F32";
//				break;
//
//			case 31:
//				color = "#F3E5AB";
//				break;
//
//			}
//

	}

	public void jffmpeg(String[] args, boolean folder) throws IOException {

		os = System.getProperty("os.name");

		comandos = new LinkedList<String>();

		command = new LinkedList<String>();

		String salida = "";

		String ayuda = "\n---------------------------------------------------------------\n\nBad arguments\n\nUse the argument -h to view help\n";

		marcaDeAgua = false;

		if (args.length == 0) {

			System.out.flush();

			System.out.println(args + "\n" + ayuda);

		}

		else {

			for (int i = 0; i < args.length; i++) {

				comandos.add(args[i]);

			}

			if (comandos.contains("-h") || comandos.contains("-help")) {

				help();

			}

			else {

				posicionarWatermark = false;

				FFmpeg ffmpeg = new FFmpeg();

				comando("-i");

				comando("-ss");

				comando("-t");

				comando("-r");

				if (comandos.contains("-watermark")) {

					marcaDeAgua = true;

					command.add("-i");

					command.add(verSiguienteDato("-watermark", true));

					command.add("-filter_complex");

					String bn = "";

					if (!verSiguienteDato("-bn", false).equals("")) {

						bn = ",format=gray";

					}

					try {

						String posicionWatermark = ":x=(w-text_w)/2:y=(h-text_h)-5";

						if (!verSiguienteDato("--pos-text-watermark", false).equals("")) {

							String cadena = verSiguienteDato("--pos-text-watermark", true);

							cadena = limpiar(cadena);

							if (cadena.contains("x")) {

								try {

									posicionWatermark = ":x=" + cadena.substring(0, cadena.indexOf("x")) + ":y="
											+ cadena.substring(cadena.indexOf("x") + 1, cadena.length());

								}

								catch (Exception e1) {

									fallo = true;

								}

							}

							else {

								try {

									String[] pos = saberPosicion(
											Integer.parseInt(verSiguienteDato("--pos-text-watermark", true)));

									posicionWatermark = ":x=" + pos[0] + ":y=" + pos[1];

								}

								catch (Exception e) {

									fallo = true;

								}

							}

						}

						String fps = "[0]fps=25";

						String scale = "";

						String fontsize = "250";

						String colorWatermark = "black";

						String texto = "";

						if (!verSiguienteDato("-fps", false).equals("")) {

							fps = "[0]fps=" + verSiguienteDato("-fps", true);

						}

						if (!verSiguienteDato("-s", false).equals("")) {

							String dato = verSiguienteDato("-s", true);

							dato = limpiar(dato);

							scale = ",scale=" + dato.replace("x", ":");

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

						if (!verSiguienteDato("--color-watermark-text", false).equals("")) {

							colorWatermark = verSiguienteDato("--color-watermark-text", true);

						}

						String posWatermarkX = "0";

						String posWatermarkY = "0";

						String datoPosWatermark = verSiguienteDato("--pos-watermark", true);

						datoPosWatermark = limpiar(datoPosWatermark);

						if (!datoPosWatermark.equals("")) {

							if (datoPosWatermark.contains("x")) {

								try {

									posWatermarkX = Integer.toString((Integer
											.parseInt(datoPosWatermark.substring(0, datoPosWatermark.indexOf("x")))));

									posWatermarkY = Integer.toString((Integer.parseInt(datoPosWatermark
											.substring(datoPosWatermark.indexOf("x") + 1, datoPosWatermark.length()))));

								}

								catch (Exception e) {

									fallo = true;

								}

							}

							else {

								posicionarWatermark = true;

								String[] pos1 = saberPosicion(
										Integer.parseInt(verSiguienteDato("--pos-watermark", true)));

								posWatermarkX = pos1[0];

								posWatermarkY = pos1[1];

							}

						}

						String fuente = "";

						if (!verSiguienteDato("--font-text-watermark", false).equals("")) {

							String archivoFuente = verSiguienteDato("--font-text-watermark", true);

							if (new File(archivoFuente).exists()) {

								fuente = ":fontfile=" + archivoFuente;

							}

						}

						String blur = ",";

						if (!verSiguienteDato("-blur", false).equals("")) {

							blur = ",boxblur=luma_radius=10:chroma_radius=10[blurred];[blurred]";

						}

						String crop = "";

						if (!verSiguienteDato("-crop", false).equals("")) {

							crop = calcularCrop(verSiguienteDato("-crop", true), true);

						}

						if (verSiguienteDato("-watermarkSize", false).equals("")) {

							crop = calcularCrop(verSiguienteDato("-crop", true), false) + ",";

							command.add(fps + scale + blur + crop + "overlay=x=" + posWatermarkX + ":y=" + posWatermarkY
									+ ":format=auto,drawtext=text='" + texto + "':fontsize=" + fontsize + fuente
									+ ":fontcolor=" + colorWatermark + posicionWatermark
									+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse" + bn);

						}

						else {

							int widthWatermark = Integer.parseInt(verSiguienteDato("-watermarkSize", true).substring(0,
									verSiguienteDato("-watermarkSize", true).indexOf("x")));

							int heightWatermark = Integer.parseInt(verSiguienteDato("-watermarkSize", true).substring(
									verSiguienteDato("-watermarkSize", true).indexOf("x") + 1,
									verSiguienteDato("-watermarkSize", true).length()));

							if (blur.equals(",")) {

								blur = "";

							}

							if (!blur.equals("") && marcaDeAgua) {

								blur = blur.replace("[blurred];[blurred]", "[blurred];");

								String comando = fps + blur + "[1]scale=" + widthWatermark + ":" + heightWatermark
										+ "[watermark];";

								String comprobacion = "";

								if (!blur.isEmpty()) {

									comprobacion = "[blurred]";

								}

								scale = ",scale=w="
										+ Integer.parseInt(scale.substring(scale.indexOf("=") + 1, scale.indexOf(":")))
										+ ":h="
										+ Integer.parseInt(scale.substring(scale.indexOf(":") + 1, scale.length()));

								if (!verSiguienteDato("-bn", false).equals("")) {

									int opcion = 1;

									try {

										opcion = Integer.parseInt(verSiguienteDato("-bn", true));

									}

									catch (Exception e) {

									}

									switch (opcion) {

									case 3:

										bn = bn.substring(1);

										try {

											if (Integer.parseInt(posWatermarkX) > -1
													&& Integer.parseInt(posWatermarkY) > -1) {

												command.add(fps + blur + "[1]scale=" + widthWatermark + ":"
														+ heightWatermark + "[watermark];[blurred][watermark]overlay=x="
														+ posWatermarkX + ":y=" + posWatermarkY + ":format=auto," + bn
														+ crop + ",drawtext=text='" + texto + "':fontsize=" + fontsize
														+ fuente + ":fontcolor=" + colorWatermark + posicionWatermark
														+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

											}

										}

										catch (Exception e) {

											System.out.println(
													"\nPor favor, introduce el valor de --pos-watermark asi-> 20x20\n\n");

											fallo = true;

										}

										break;

									case 2:

										command.add(fps + blur + "[blurred]" + scale.substring(1)

												+ crop + "[gray_frame];[1]scale=" + widthWatermark + ":"
												+ heightWatermark
												+ ",format=rgba,colorchannelmixer=rr=0.3:rg=0.6:rb=0.1:gr=0.3:gg=0.6:gb=0.1:br=0.3:bg=0.6:bb=0.1[watermark_bw];[gray_frame]"

												+ "[watermark_bw]overlay=W-w-" + posWatermarkX + ":H-h-" + posWatermarkY
												+ ":format=auto,drawtext=text='" + texto + "':fontsize=" + fontsize
												+ fuente + ":fontcolor=" + colorWatermark + ":"
												+ "x=(w-text_w)/2:y=(h-text_h)/2"
												+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

										break;

									default:

										if (!posicionarWatermark) {

											command.add(fps + blur + "[blurred]" + scale.substring(1)

													+ crop + bn + "[gray_frame];[1]scale=" + widthWatermark + ":"
													+ heightWatermark

													+ "[watermark];[gray_frame][watermark]overlay=W-w-" + posWatermarkX
													+ ":H-h-" + posWatermarkY + ":format=auto,drawtext=text='" + texto
													+ "':fontsize=" + fontsize + fuente + ":fontcolor=" + colorWatermark
													+ ":" + "x=(w-text_w)/2:y=(h-text_h)/2"
													+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

										}

										else {

											System.out.println(
													"\nPor favor, introduce el valor de --pos-watermark asi-> 20x20\n\n");

											fallo = true;

										}

										break;

									}

								}

								else {

									try {

										if (marcaDeAgua) {

											if (posicionarWatermark) {

												command.add(fps + scale + blur + "[1]scale=" + widthWatermark + ":"
														+ heightWatermark + "[watermark_resized];[blurred]"
														+ calcularCrop(verSiguienteDato("-crop", true), false)
														+ "[blurred_cropped];[blurred_cropped][watermark_resized]"
														+ calcularPosicionDeMarcaDeAgua(Integer
																.parseInt(verSiguienteDato("--pos-watermark", true)))
														+ ":format=auto,drawtext=text='" + texto + "':fontsize="
														+ fontsize + fuente + ":fontcolor=" + colorWatermark
														+ posicionWatermark
														+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

											}

											else {

												command.add(fps + scale + blur + "[1]scale=" + widthWatermark + ":"
														+ heightWatermark + "[watermark_resized];[blurred]"
														+ calcularCrop(verSiguienteDato("-crop", true), false)
														+ "[blurred_cropped];[blurred_cropped][watermark_resized]"
														+ "overlay=x=" + posWatermarkX + ":y=" + posWatermarkY

														+ ":format=auto,drawtext=text='" + texto + "':fontsize="
														+ fontsize + fuente + ":fontcolor=" + colorWatermark
														+ posicionWatermark
														+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");
											}

										}

										else {

											command.add(fps + scale + blur + "[1]scale=" + widthWatermark + ":"
													+ heightWatermark + "[watermark_resized];[blurred]"
													+ calcularCrop(verSiguienteDato("-crop", true), false)
													+ "[blurred_cropped];[blurred_cropped][watermark_resized]overlay=x="
													+ posWatermarkX + ":y=" + posWatermarkY
													+ ":format=auto,drawtext=text='" + texto + "':fontsize=" + fontsize
													+ fuente + ":fontcolor=" + colorWatermark + posicionWatermark
													+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");
										}

									}

									catch (Exception e) {

										fallo = true;

									}

								}

							}

							else if (marcaDeAgua && blur.equals("")) {

								command.add(fps + scale + "[base];[1]scale=w=" + widthWatermark + ":h="
										+ heightWatermark + "[watermark];[base][watermark]overlay=x=" + posWatermarkX
										+ ":y=" + posWatermarkY + ":format=auto,drawtext=text='" + texto + "':fontsize="
										+ fontsize + fuente + ":fontcolor=" + colorWatermark + posicionWatermark);
							}

						}

					}

					catch (Exception e) {

						fallo = true;

					}

				}

				if (verSiguienteDato("-y", true).endsWith(".gif")) {

					if (!marcaDeAgua) {

						if (comandos.contains("-good") || comandos.contains("-bad")) {

							if (comandos.contains("-good")) {

								buenaCalidad();

							}

							else if (comandos.contains("-bad")) {

								malaCalidad();

							}

						}

						else {

							buenaCalidad();

						}

					}

					if (marcaDeAgua) {

						command.add("-c:v");

						command.add("gif");

					}

				}

				if (!verSiguienteDato("-bits", false).equals("")) {

					command.add("-b:v");

					command.add(verSiguienteDato("-bits", true));

				}

				command.add("-q:v");

				if (!verSiguienteDato("-quality", false).equals("")) {

					command.add(verSiguienteDato("-quality", true));

				}

				else {

					command.add("16");

				}

				comando("-s");

				int busqueda = comando("-y");

				separador = saberSeparador(os);

				if (busqueda == -1) {

					command.add("-y");

					try {

						File test = new File(
								comandos.getLast().substring(0, comandos.getLast().lastIndexOf(separador)));

						if (test.isDirectory()) {

							salida = comandos.getLast();

						}

					}

					catch (Exception e) {

						fallo = true;

					}

				}

				else {

					salida = verSiguienteDato("-y", true);

				}

				System.out.println(saberComandos(command));

				command.add(salida);

				try {

					if (!fallo) {

						ffmpeg.run(command);

					}

					else {

						System.out.println("\n" + ayuda + "\n\n");

					}

				}

				catch (Exception e) {

					fallo = true;

					System.out.println("\n" + ayuda + "\n\n");

				}

				if (!fallo && folder && !salida.isEmpty()) {

					abrirCarpeta("file://" + salida);

				}

			}

		}

	}

	private String limpiar(String datoPosWatermark) {

		datoPosWatermark = datoPosWatermark.replace("X", "x");

		datoPosWatermark = datoPosWatermark.replace("*", "x");

		return datoPosWatermark;

	}

	private String calcularPosicionDeMarcaDeAgua(int index) {

		String resultado = "";

		switch (index) {

		case 1:

			resultado = "overlay=x=10:y=5";

			break;

		case 2:

			resultado = "overlay=x=(main_w-overlay_w)/2:y=5";

			break;

		case 3:

			resultado = "overlay=x=(main_w-overlay_w-10):y=5";

			break;

		case 4:

			resultado = "overlay=x=10:y=(main_h-overlay_h)/2";

			break;

		case 5:

			resultado = "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2";

			break;

		case 6:

			resultado = "overlay=x=(main_w-overlay_w-10):y=(main_h-overlay_h)/2";

			break;

		case 7:

			resultado = "overlay=x=10:y=(main_h-overlay_h-5)";

			break;

		case 8:

			resultado = "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h-5)";

			break;

		default:

			resultado = "overlay=main_w-overlay_w-10:main_h-overlay_h-5";

			break;

		}

		return resultado;

	}

	private String saberComandos(LinkedList<String> lista) {

		String salida = "ffmpeg ";

		String texto;

		for (int i = 0; i < lista.size(); i++) {

			texto = lista.get(i).trim();

			if (!texto.startsWith("-")) {

				texto = "\"" + texto + "\"";

			}

			salida += texto + " ";

		}

		salida = salida.trim();

		return salida;

	}

	private void malaCalidad() {

		boolean entro = false;

		if (!verSiguienteDato("-crop", false).equals("")) {

			entro = true;

			command.add("-vf");

		}

		if (entro && !verSiguienteDato("-bn", false).equals("")) {

			command.add("crop=" + verSiguienteDato("-crop", true).replace("x", ":") + ",format=gray");

		}

		else if (entro) {

			command.add("crop=" + verSiguienteDato("-crop", true).replace("x", ":"));

		}

		else if (!entro && !verSiguienteDato("-bn", false).equals("")) {

			command.add("-vf");

			command.add("format=gray");

		}

		else {

			command.add("-pix_fmt");

			command.add("rgba");

		}

		if (!verSiguienteDato("-width", false).equals("") && !verSiguienteDato("-height", false).equals("")) {

			int ancho = Integer.parseInt(verSiguienteDato("-width", true));

			int alto = Integer.parseInt(verSiguienteDato("-height", true));

			if (ancho > 0 && alto > 0) {

				command.add("-s");

				command.add(ancho + "x" + alto);

			}

		}

	}

	private void buenaCalidad() {

		command.add("-filter_complex");

		String fps = "25";

		if (!verSiguienteDato("-fps", false).equals("")) {

			fps = verSiguienteDato("-fps", true);

		}

		boolean entro = false;

		String ancho = verSiguienteDato("-width", true);

		String alto = verSiguienteDato("-height", true);

		if (ancho == null || ancho.isEmpty()) {

			ancho = "720";

		}

		if (alto == null || alto.isEmpty() || alto.equals("-i")) {

			alto = "-1";

		}

		String filtro = "[0:v]fps=" + fps + ",scale=w=" + ancho + ":h=" + alto
				+ ",split[a][b];[a]palettegen=stats_mode=single[p];[b][p]paletteuse=new=1";

		if (!verSiguienteDato("-crop", false).equals("")) {

			filtro += calcularCrop(verSiguienteDato("-crop", true), true);

		}

		if (!verSiguienteDato("-bn", false).equals("")) {

			entro = true;

			filtro += ",format=gray[output]";

		}

		command.add(filtro);

		if (entro) {

			command.add("-map");

			command.add("[output]");

		}

	}

	public static int[] buscarPosiciones(String cadenaBuscar, String cadenaPrincipal) {

		List<Integer> posiciones = new ArrayList<>();

		int posicion = cadenaPrincipal.indexOf(cadenaBuscar);

		while (posicion != -1) {

			posiciones.add(posicion);

			posicion = cadenaPrincipal.indexOf(cadenaBuscar, posicion + 1);

		}

		int[] resultado = new int[posiciones.size()];

		for (int i = 0; i < resultado.length; i++) {

			resultado[i] = posiciones.get(i);

		}

		return resultado;

	}

	private String calcularCrop(String dato, boolean coma) {

		String resultado = "";

		if (coma) {

			resultado = ",";

		}

		int[] posiciones = buscarPosiciones("x", dato);

		resultado += "crop=x=" + dato.substring(0, posiciones[0]) + ":h="
				+ dato.substring(posiciones[0] + 1, posiciones[1]) + ":x=";

		resultado += dato.substring(posiciones[1] + 1, posiciones[2]) + ":y="
				+ dato.substring(posiciones[2] + 1, dato.length());

		return resultado;

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
