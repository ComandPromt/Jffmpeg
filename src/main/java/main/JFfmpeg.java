package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;

import net.bramp.ffmpeg.FFmpeg;

public class JFfmpeg extends JFrame {

	private static final long serialVersionUID = 1L;

	private String os;

	private String separador;

	private static LinkedList<String> comandos;

	private LinkedList<String> command;

	private boolean fallo;

	private boolean marcaDeAgua;

	private boolean posicionarWatermark;

	private boolean buenaCalidad;

	private boolean imagen;

	private boolean parar;

	private static String verSiguienteDato(String search, boolean filtro) {

		String resultado = "";

		try {

			if (filtro) {

				resultado = comandos.get(comandos.indexOf(search) + 1);

			}

			else {

				resultado = comandos.get(comandos.indexOf(search));

			}

			resultado = resultado.trim();

		}

		catch (Exception e) {

		}

		return resultado;

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

	public static String obtenerDimensionesVideo(String rutaVideo, float factor) {

		String resultado = "";

		IContainer container = IContainer.make();

		if (container.open(rutaVideo, IContainer.Type.READ, null) < 0) {

			return "";

		}

		IStream stream = null;

		for (int i = 0; i < container.getNumStreams(); i++) {

			if (container.getStream(i).getStreamCoder()
					.getCodecType() == com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_VIDEO) {

				stream = container.getStream(i);

				i = container.getNumStreams();

			}

		}

		if (stream != null) {

			resultado = (int) (stream.getStreamCoder().getWidth() * factor) + "x"
					+ (int) (stream.getStreamCoder().getHeight() * factor);

		}

		else {

			resultado = "";

		}

		container.close();

		return resultado;

	}

	private int comando(String index) {

		int busqueda = buscarComando(index);

		if (busqueda != -1) {

			try {

				int indice = busqueda;

				indice++;

				if (index.equals("-s")) {

					if (comandos.get(indice).contains(".") && Float.parseFloat(comandos.get(indice)) != 0f) {

						comandos.set(indice,
								obtenerDimensionesVideo(comandos.get(1), Float.parseFloat(comandos.get(indice))));

					}

					else if (comandos.get(indice).contains(":")) {

						comandos.set(indice, redimensionarConProporcion(comandos.get(1), comandos.get(indice)));

					}

				}

			}

			catch (Exception e) {

			}

			command.add(comandos.get(busqueda));

			busqueda++;

			if (!comandos.get(busqueda).contains("-")) {

				command.add(comandos.get(busqueda));

			}

			else {

				command.removeLast();

			}

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

	private void saberSiEsGif() {

		if (marcaDeAgua && (verSiguienteDato("-y", true).endsWith(".gif"))) {

			command.add("-c:v");

			command.add("gif");

		}

	}

	private void ponerCalidad() {

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

	private String salida(String salida) {

		int busqueda = comando("-y");

		if (busqueda == -1) {

			try {

				separador = saberSeparador(os);

				File test = new File(comandos.getLast().substring(0, comandos.getLast().lastIndexOf(separador)));

				if (test.isDirectory()) {

					salida = comandos.getLast();

					if (!command.contains("-y")) {

						command.add("-y");

						command.add(salida);

					}

				}

			}

			catch (Exception e) {

				e.printStackTrace();

				fallo = true;

			}

		}

		else {

			salida = verSiguienteDato("-y", true);

		}

		return salida;

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

	private void bits() {

		if (!verSiguienteDato("-bits", false).equals("")) {

			command.add("-b:v");

			command.add(verSiguienteDato("-bits", true));

		}

	}

	private void calidad() {

		command.add("-q:v");

		if (!verSiguienteDato("-quality", false).equals("")) {

			command.add(verSiguienteDato("-quality", true));

		}

		else {

			command.add("16");

		}

	}

	private void rotate() {

		if (!verSiguienteDato("-rotate", false).equals("")) {

			int num1 = command.indexOf("-filter_complex") + 1;

			if (num1 > 0) {

				String dato = command.get(num1);

				try {
					command.set(num1, dato.substring(0, dato.indexOf(",") + 1) + tipoRotacion()
							+ dato.substring(dato.indexOf(","), dato.length()));
				} catch (Exception e) {

				}
			}

			else {

				if (!command.contains("-vf")) {

					if (num1 > 0) {

						num1--;

						command.set(num1, "-vf");

						num1++;

					}

					else {

						command.add("-vf");

					}

				}

				String dato = command.get(num1);

				if (!dato.contains("-")) {

					if (num1 > 0) {

						command.set(num1, dato.substring(0, dato.indexOf(",") + 1) + tipoRotacion()
								+ dato.substring(dato.indexOf(","), dato.length()));

					}

					else {

						try {

							command.add(dato.substring(0, dato.indexOf(",") + 1) + tipoRotacion()
									+ dato.substring(dato.indexOf(","), dato.length()));

						}

						catch (Exception e) {

							command.add(tipoRotacion());

						}

					}

				}

				else {

					if (!command.contains("-vf")) {

						command.add(tipoRotacion());

					}

					else {

						int index = command.indexOf("-vf") + 1;

						if (index == command.size()) {

							index--;

						}

						System.out.println("entro aaa " + command.get(index));

						if (command.get(index).equals("-vf")) {

						}

						command.set(index, command.get(index) + "," + tipoRotacion());

					}

				}

			}

		}

	}

	private String tipoRotacion() {

		String rotacion = verSiguienteDato("-rotate", true);

		String resultado = "";

		try {

			if (rotacion.contains("#")) {

				rotacion = rotacion.replace("#", "");

				resultado = "rotate=" + rotacion + "*PI/180";

			}

			else {

				int giro = Integer.parseInt(rotacion.substring(rotacion.indexOf("x") + 1, rotacion.length()));

				switch (Integer.parseInt(rotacion.substring(0, rotacion.indexOf("x")))) {

				case 2:

					resultado = "transpose=1,transpose=1";

					break;

				default:

					if (giro > 1) {

						resultado = "transpose=2";

					}

					else {

						resultado = "transpose=1";

					}

					break;

				}

			}

		}

		catch (Exception e) {

		}

		return resultado;

	}

	private void reverse() {

		if (!verSiguienteDato("-reverse", false).equals("")) {

			int num1 = 0;

			if (!buenaCalidad) {

				if (!command.contains("-vf")) {

					command.add("-vf");

				}

				num1 = command.indexOf("-vf") + 1;

			}

			else {

				if (!command.contains("-filter_complex")) {

					command.add("-filter_complex");

				}

				num1 = command.indexOf("-filter_complex") + 1;

			}

			if (!buenaCalidad) {

				if (num1 == command.size()) {

					command.add("reverse");

				}

				else {

					command.set(num1, command.get(num1) + ",reverse");

				}

			}

			else {

				command.set(num1, command.get(num1) + "[reversed];[reversed]reverse");

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

			int indice = command.indexOf("vf");

			if (indice > 0) {

				command.add(indice, "format=gray");

			}

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

		buenaCalidad = true;

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

		ancho = ancho.replace("i", "1");

		String filtro = "[0:v]fps=" + fps + ",scale=w=" + ancho + ":h=" + alto
				+ ",split=2[a][b];[a]palettegen=stats_mode=single[p];[b][p]paletteuse=new=1";

		if (!verSiguienteDato("-crop", false).equals("")) {

			filtro = "[0:v]fps=" + fps + calcularCrop(verSiguienteDato("-crop", true), true) + ",scale=w=" + ancho
					+ ":h=" + alto + ",split=2[a][b];[a]palettegen=stats_mode=single[p];[b][p]paletteuse=new=1";

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

		try {

			if (coma) {

				resultado = ",";

			}

			int[] posiciones = buscarPosiciones("x", dato);

			resultado += "crop=w=" + dato.substring(0, posiciones[0]) + ":h="
					+ dato.substring(posiciones[0] + 1, posiciones[1]) + ":x=";

			resultado += dato.substring(posiciones[1] + 1, posiciones[2]) + ":y="
					+ dato.substring(posiciones[2] + 1, dato.length());

		}

		catch (Exception e) {

		}

		return resultado;

	}

	public String convertSpeedToFilter(int percentage) {

		String resultado = "";

		if (percentage <= 0) {

			imagen = true;

			command.addFirst("-y");

			command.add("-vframes");

			command.add("1");

			command.add(comandos.getLast());

		}

		else {

			if (percentage > 200) {

				percentage = 200;

			}

			double factor = percentage / 100.0;

			if (factor == 1.0) {

				resultado = "setpts=PTS";

			}

			else if (factor < 1.0) {

				resultado = String.format("setpts=PTS*%.1f", 1.0 / factor);

			}

			else {

				resultado = String.format("setpts=PTS/%.1f", factor);

			}

			resultado = resultado.replace(",", ".");

		}

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

	public String redimensionarConProporcion(String rutaVideo, String proporcionDeseada) {

		try {

			String resultado = "";

			IContainer container = IContainer.make();

			if (container.open(rutaVideo, IContainer.Type.READ, null) < 0) {

				return "";

			}

			IStream stream = null;

			for (int i = 0; i < container.getNumStreams(); i++) {

				if (container.getStream(i).getStreamCoder()
						.getCodecType() == com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_VIDEO) {

					stream = container.getStream(i);

					i = container.getNumStreams();

				}

			}

			if (stream != null) {

				int anchoOriginal = stream.getStreamCoder().getWidth();

				int altoOriginal = stream.getStreamCoder().getHeight();

				int nuevoAncho;

				int nuevoAlto;

				String[] proporcion = proporcionDeseada.split(":");

				int proporcionAncho = Integer.parseInt(proporcion[0]);

				int proporcionAlto = Integer.parseInt(proporcion[1]);

				if (anchoOriginal * proporcionAlto > altoOriginal * proporcionAncho) {

					nuevoAlto = altoOriginal;

					nuevoAncho = (altoOriginal * proporcionAncho) / proporcionAlto;

				}

				else {

					nuevoAncho = anchoOriginal;

					nuevoAlto = (anchoOriginal * proporcionAlto) / proporcionAncho;

				}

				resultado = nuevoAncho + "x" + nuevoAlto;

			}

			else {

				resultado = "";

			}

			container.close();

			return resultado;

		}

		catch (Exception e) {

			return null;

		}

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

	}

	private void ponerFiltro(String index, String dato, boolean coma) {

		int indice = command.indexOf(index);

		if (!dato.isEmpty()) {

			if (indice == -1) {

				command.add(index);

				command.add(dato);

			}

			else if (!verSiguienteDato(index, true).equals("") && !command.get(++indice).equals(dato)) {

				if (coma) {

					command.set(indice, command.get(indice) + "," + dato);

				}

				else {

					command.set(indice, command.get(indice) + dato);

				}

			}

		}

	}

	public JFfmpeg(String[] args, boolean folder) throws IOException {

		boolean cambiar = true;

		imagen = false;

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

			try {

				comandos = new LinkedList<>(Arrays.asList(args));

				if (comandos.contains("-h") || comandos.contains("-help")) {

					help();

				}

				else {

					try {

						posicionarWatermark = false;

						buenaCalidad = false;

						FFmpeg ffmpeg = new FFmpeg();

						comando("-i");

						comando("-ss");

						comando("-t");

						comando("-r");

						String box = "";

						if (!verSiguienteDato("--text-box", false).equals("")) {

							String dato = verSiguienteDato("--text-box", true);

							String a = dato.substring(0, dato.indexOf("x"));

							String b = dato.substring(dato.indexOf("x") + 1, dato.lastIndexOf("x"));

							String c = dato.substring(dato.lastIndexOf("x") + 1, dato.length());

							b = b.replace("-", "@");

							box = ":box=" + a + ":boxcolor='" + b + "':boxborderw=" + c;

						}

						String fuente = "";

						if (!verSiguienteDato("--font-text-watermark", false).equals("")) {

							String archivoFuente = verSiguienteDato("--font-text-watermark", true);

							if (new File(archivoFuente).exists()) {

								if (System.getProperty("os.name").contains("indows")) {

									char unidad = archivoFuente.charAt(0);

									archivoFuente = archivoFuente.substring(3);

									archivoFuente = archivoFuente.replace("\\", "/");

									fuente = ":fontfile='" + unidad + "\\\\\\\\:/" + archivoFuente + "'";

								}

								else {

									fuente = ":fontfile=" + archivoFuente;

								}

							}

							else if (System.getProperty("os.name").contains("indows")) {

								fuente = ":fontfile='C\\\\\\\\:/Windows/Fonts/arial.ttf'";

							}

						}

						else if (System.getProperty("os.name").contains("indows")) {

							fuente = ":fontfile='C\\\\\\\\:/Windows/Fonts/arial.ttf'";

						}

						String posWatermarkX = "0";

						String posWatermarkY = "0";

						String datoPosWatermark = verSiguienteDato("--pos-watermark", true);

						datoPosWatermark = limpiar(datoPosWatermark);

						if (!datoPosWatermark.equals("")) {

							if (datoPosWatermark.contains("x")) {

								posWatermarkX = Integer.toString((Integer
										.parseInt(datoPosWatermark.substring(0, datoPosWatermark.indexOf("x")))));

								posWatermarkY = Integer.toString((Integer.parseInt(datoPosWatermark
										.substring(datoPosWatermark.indexOf("x") + 1, datoPosWatermark.length()))));

							}

							else {

								posicionarWatermark = true;

								try {

									String[] pos1 = saberPosicion(
											Integer.parseInt(verSiguienteDato("--pos-watermark", true)));

									posWatermarkX = pos1[0];

									posWatermarkY = pos1[1];

								} catch (Exception e) {

								}

							}

						}

						String fontsize = "250";

						String colorWatermark = "black";

						String texto = "";

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

						String posicionTextWatermark = ":x=(w-text_w)/2:y=(h-text_h)-5";

						if (!verSiguienteDato("--pos-text-watermark", false).equals("")) {

							String cadena = verSiguienteDato("--pos-text-watermark", true);

							cadena = limpiar(cadena);

							if (cadena.contains("x")) {

								posicionTextWatermark = ":x=" + cadena.substring(0, cadena.indexOf("x")) + ":y="
										+ cadena.substring(cadena.indexOf("x") + 1, cadena.length());

							}

							else {

								String[] pos = saberPosicion(
										Integer.parseInt(verSiguienteDato("--pos-text-watermark", true)));

								posicionTextWatermark = ":x=" + pos[0] + ":y=" + pos[1];

							}

						}

						String fps = "[0]fps=25";

						String scale = "";

						if (!verSiguienteDato("-fps", false).equals("")) {

							fps = "[0]fps=" + verSiguienteDato("-fps", true);

						}

						if (!verSiguienteDato("-s", false).equals("")) {

							String dato = verSiguienteDato("-s", true);

							dato = limpiar(dato);

							scale = ",scale=" + dato.replace("x", ":");

						}

						String blur = ",";

						if (!verSiguienteDato("-blur", false).equals("")) {

							String datoBlur = verSiguienteDato("-blur", true);

							int brillo = Integer.parseInt(datoBlur.substring(0, datoBlur.indexOf("x")));

							int color = Integer
									.parseInt(datoBlur.substring(datoBlur.indexOf("x") + 1, datoBlur.length()));

							blur = ",boxblur=luma_radius=" + brillo + ":chroma_radius=" + color + "[blurred];[blurred]";

						}

						String crop = "";

						if (!verSiguienteDato("-crop", false).equals("")) {

							crop = calcularCrop(verSiguienteDato("-crop", true), true);

						}

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

								if (verSiguienteDato("-watermarkSize", false).equals("")) {

									crop = calcularCrop(verSiguienteDato("-crop", true), false);

									if (!crop.isEmpty()) {

										crop += ",";

									}

									command.add(fps + scale + blur + crop + "overlay=x=" + posWatermarkX + ":y="
											+ posWatermarkY + ":format=auto,drawtext=text='" + texto + "'" + fuente
											+ ":fontsize=" + fontsize + box + ":fontcolor=" + colorWatermark
											+ posicionTextWatermark
											+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse" + bn);

								}

								else {

									int widthWatermark = Integer.parseInt(verSiguienteDato("-watermarkSize", true)
											.substring(0, verSiguienteDato("-watermarkSize", true).indexOf("x")));

									int heightWatermark = Integer.parseInt(verSiguienteDato("-watermarkSize", true)
											.substring(verSiguienteDato("-watermarkSize", true).indexOf("x") + 1,
													verSiguienteDato("-watermarkSize", true).length()));

									if (blur.equals(",")) {

										blur = "";

									}

									if (!blur.equals("") && marcaDeAgua) {

										blur = blur.replace("[blurred];[blurred]", "[blurred];");

										scale = ",scale=w="
												+ Integer.parseInt(
														scale.substring(scale.indexOf("=") + 1, scale.indexOf(":")))
												+ ":h=" + Integer.parseInt(
														scale.substring(scale.indexOf(":") + 1, scale.length()));

										if (!verSiguienteDato("-bn", false).equals("")) {

											int opcion = 1;

											try {

												opcion = Integer.parseInt(verSiguienteDato("-bn", true));

											}

											catch (Exception e) {

											}

											switch (opcion) {

											case 2:

												command.add(fps + blur + "[blurred]" + scale.substring(1)

														+ crop + "[gray_frame];[1]scale=" + widthWatermark + ":"
														+ heightWatermark
														+ ",format=rgba,colorchannelmixer=rr=0.3:rg=0.6:rb=0.1:gr=0.3:gg=0.6:gb=0.1:br=0.3:bg=0.6:bb=0.1[watermark_bw];[gray_frame]"

														+ "[watermark_bw]overlay=W-w-" + posWatermarkX + ":H-h-"
														+ posWatermarkY + ":format=auto,drawtext=text='" + texto
														+ "':fontsize=" + fuente + fontsize + box + ":fontcolor="
														+ colorWatermark + ":" + "x=(w-text_w)/2:y=(h-text_h)/2"
														+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

												break;

											case 3:

												bn = bn.substring(1);

												try {

													if (Integer.parseInt(posWatermarkX) > -1
															&& Integer.parseInt(posWatermarkY) > -1) {

														command.add(fps + blur + "[1]scale=" + widthWatermark + ":"
																+ heightWatermark
																+ "[watermark];[blurred][watermark]overlay=x="
																+ posWatermarkX + ":y=" + posWatermarkY
																+ ":format=auto," + bn + crop + ",drawtext=text='"
																+ texto + "':fontsize=" + fuente + fontsize + box
																+ ":fontcolor=" + colorWatermark + posicionTextWatermark
																+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

													}

												}

												catch (Exception e) {

													e.printStackTrace();

													System.out.println(
															"\nPor favor, introduce el valor de --pos-watermark asi-> 20x20\n\n");

													fallo = true;

												}

												break;

											default:

												if (!posicionarWatermark) {

													command.add(fps + blur + "[blurred]" + scale.substring(1)

															+ crop + bn + "[gray_frame];[1]scale=" + widthWatermark
															+ ":" + heightWatermark

															+ "[watermark];[gray_frame][watermark]overlay=W-w-"
															+ posWatermarkX + ":H-h-" + posWatermarkY
															+ ":format=auto,drawtext=text='" + texto + "':fontsize="
															+ fuente + fontsize + box + ":fontcolor=" + colorWatermark
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

											if (marcaDeAgua) {

												if (posicionarWatermark) {

													command.add(fps + scale + blur + "[1]scale=" + widthWatermark + ":"
															+ heightWatermark + "[watermark_resized];[blurred]"
															+ calcularCrop(verSiguienteDato("-crop", true), false)
															+ "[blurred_cropped];[blurred_cropped][watermark_resized]"
															+ calcularPosicionDeMarcaDeAgua(Integer.parseInt(
																	verSiguienteDato("--pos-watermark", true)))
															+ ":format=auto,drawtext=text='" + texto + "':fontsize="
															+ fuente + fontsize + box + ":fontcolor=" + colorWatermark
															+ posicionTextWatermark
															+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

												}

												else {

													command.add(fps + scale + blur + "[1]scale=" + widthWatermark + ":"
															+ heightWatermark + "[watermark_resized];[blurred]"
															+ calcularCrop(verSiguienteDato("-crop", true), false)
															+ "[blurred_cropped];[blurred_cropped][watermark_resized]"
															+ "overlay=x=" + posWatermarkX + ":y=" + posWatermarkY

															+ ":format=auto,drawtext=text='" + texto + "':fontsize="
															+ fuente + fontsize + box + ":fontcolor=" + colorWatermark
															+ posicionTextWatermark
															+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");
												}

											}

											else {

												command.add(fps + scale + blur + "[1]scale=" + widthWatermark + ":"
														+ heightWatermark + "[watermark_resized];[blurred]"
														+ calcularCrop(verSiguienteDato("-crop", true), false)
														+ "[blurred_cropped];[blurred_cropped][watermark_resized]overlay=x="
														+ posWatermarkX + ":y=" + posWatermarkY
														+ ":format=auto,drawtext=text='" + texto + "':fontsize="
														+ fuente + fontsize + box + ":fontcolor=" + colorWatermark
														+ posicionTextWatermark
														+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");
											}

										}

									}

									else if (marcaDeAgua && blur.equals("")) {

										try {

											if (!bn.equals("")) {

												bn = bn.substring(1);

												bn += ",";

												int opcion = 1;

												try {

													opcion = Integer.parseInt(verSiguienteDato("-bn", true));

												}

												catch (Exception e) {

												}

												switch (opcion) {

												case 2:

													command.add(fps + scale + "[base];[1]scale=w=" + widthWatermark
															+ ":h=" + heightWatermark
															+ ",split[watermark][alpha];[watermark]format=gray[watermark_gray];[watermark_gray]colorchannelmixer=rr=0.5:rg=0.5:rb=0.5:gr=0.5:gg=0.5:gb=0.5:br=0.5:bg=0.5:bb=0.5[watermark_bw];[watermark_bw][alpha]alphamerge[watermark_processed];[base][watermark_processed]overlay=x="
															+ posWatermarkX + ":y=" + posWatermarkY + ":format=auto,"
															+ "drawtext=text='" + texto + fuente + "':fontsize="
															+ fontsize + box + ":fontcolor=" + colorWatermark
															+ posicionTextWatermark);

													break;

												case 3:

													command.add(fps + scale + "[base];[1]scale=w=" + widthWatermark
															+ ":h=" + heightWatermark
															+ ",split[watermark][alpha];[watermark]format=gray[watermark_gray];[watermark_gray]colorchannelmixer=rr=0.5:rg=0.5:rb=0.5:gr=0.5:gg=0.5:gb=0.5:br=0.5:bg=0.5:bb=0.5[watermark_bw];[watermark_bw][alpha]alphamerge[watermark_processed];[base][watermark_processed]overlay=x="
															+ posWatermarkX + ":y=" + posWatermarkY + ":format=auto,"
															+ bn + "drawtext=text='" + texto + fuente + "':fontsize="
															+ fontsize + box + ":fontcolor=" + colorWatermark
															+ posicionTextWatermark);

													break;

												default:

													command.add(fps + scale + ",format=gray[gray_frame];[1]scale="
															+ widthWatermark + ":" + heightWatermark

															+ "[watermark];[gray_frame][watermark]overlay=W-w-"
															+ posWatermarkX + ":H-h-" + posWatermarkY
															+ ":format=auto,drawtext=text='" + texto + fuente
															+ "':fontsize=" + fontsize + box + ":fontcolor="
															+ colorWatermark + ":" + "x=(w-text_w)/2:y=(h-text_h)/2"
															+ ",split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse");

													break;

												}

											}

											else {

												command.add(fps + scale + "[base];[1]scale=w=" + widthWatermark + ":h="
														+ heightWatermark + "[watermark];[base][watermark]overlay=x="
														+ posWatermarkX + ":y=" + posWatermarkY
														+ ":format=auto,drawtext=text='" + texto + fuente
														+ "':fontsize=" + fontsize + box + ":fontcolor="
														+ colorWatermark + posicionTextWatermark);

											}

										}

										catch (Exception e) {

											command.add(fps + scale + "[base];[1]scale=w=" + widthWatermark + ":h="
													+ heightWatermark + "[watermark];[base][watermark]overlay=x="
													+ posWatermarkX + ":y=" + posWatermarkY
													+ ":format=auto,drawtext=text='" + texto + fuente + "':fontsize="
													+ fontsize + box + ":fontcolor=" + colorWatermark
													+ posicionTextWatermark);

										}

									}

								}

								if (verSiguienteDato("-y", true).endsWith(".gif")) {

									if (!marcaDeAgua) {

										ponerCalidad();

									}

									saberSiEsGif();

								}

								bits();

								calidad();

								comando("-s");

								if (imagen) {

									salida = comandos.getLast();

								}

								else {

									salida = salida(salida);

								}

								ponerContador();

								if (!verSiguienteDato("-colors", false).equals("")) {

									if (buenaCalidad) {

										int num1 = command.indexOf("-filter_complex") + 1;

										String dato = command.get(num1);

										dato = dato.replace(
												",split[a][b];[a]palettegen=stats_mode=single[p];[b][p]paletteuse=new=1",
												",split[a][b];[a]palettegen=max_colors=256[p];[b][p]paletteuse=new=1");

										command.set(num1, dato);

									}

									else {

										int num1 = command.indexOf("-filter_complex");

										int num2 = command.indexOf("[s0]palettegen");

										if (num1 > -1 && num2 > -1) {

											String datoColores = command.get(command.indexOf(";[s0]palettegen"));

											int indice = datoColores.indexOf(";[s0]palettegen");

											String cabecera = datoColores.substring(0, indice);

											datoColores = cabecera + "=max_colors=256"
													+ datoColores.substring(indice + 15, datoColores.length());

											command.set(command.indexOf(";[s0]palettegen"), datoColores);

										}

										else if (num1 > -1) {

											int indice = num1 + 1;

											command.set(indice,
													command.get(indice) + ",split[s0][s1];[s0]palettegen=max_colors="
															+ Integer.parseInt(verSiguienteDato("-colors", true))
															+ "[p];[s1][p]paletteuse");

										}

									}

								}

							} catch (Exception e) {

								fallo = true;

							}

						}

						else {

							ponerCalidad();

							saberSiEsGif();

							bits();

							calidad();

							comando("-s");

							int index = -1;

							int indice = -1;

							if (!verSiguienteDato("-fps", false).equals("")) {

								fps = "fps=" + verSiguienteDato("-fps", true) + ",";

							}

							if (!verSiguienteDato("-colors", false).equals("")) {

								String dato = verSiguienteDato("-colors", true);

								indice = command.indexOf("-vf");

								if (!dato.isEmpty()) {

									eliminar("-pix_fmt");

									if (!buenaCalidad) {

										buenaCalidad();

									}

									index = command.indexOf("-filter_complex") + 1;

									command.set(index,
											command.get(index).replace("stats_mode=single", "max_colors=" + dato));

								}

							}

							if (!verSiguienteDato("-bn", false).equals("")) {

								if (buenaCalidad) {

									eliminar("-vf");

									eliminar("-map");

									indice = command.indexOf("-filter_complex") + 1;

									int tipoBn = 1;

									try {

										tipoBn = Integer.parseInt(verSiguienteDato("-bn", true));

									}

									catch (Exception e) {

									}

									command.set(indice,
											command.get(indice).substring(0, command.get(indice).indexOf("=new=1") + 6)
													+ "[output];[output]format=gray");

									switch (tipoBn) {

									case 2:

										command.set(indice,
												command.get(indice).substring(0,
														command.get(indice).indexOf("=new=1") + 6)
														+ "[output];[output]format=gray");

										break;

									default:

										if (!texto.isEmpty()) {

											String busqueda = "-filter_complex";

											if (command.contains("-vf")) {

												busqueda = "-vf";

											}

											command.set(indice,
													command.get(command.indexOf(busqueda) + 1) + ",drawtext=text='"
															+ texto + "'" + fuente + ":fontsize=" + fontsize + box
															+ ":fontcolor=" + colorWatermark + posicionTextWatermark);

										}

										cambiar = false;

										break;

									}

									String dato = command.get(indice);

									if (!verSiguienteDato("-colors", false).equals("")) {

										if (!cambiar) {

											try {

												dato = dato.substring(0, dato.indexOf("[a]palettegen=") + 14)
														+ "max_colors=" + verSiguienteDato("-colors", true) + ":"
														+ dato.substring(dato.indexOf("stats_mode=single"),
																dato.length());

											}

											catch (Exception e) {

												ponerModoSingle(indice, dato);

											}

										}

										else {

											ponerModoSingle(indice, dato);

										}

									}

									if (cambiar) {

										System.out.println("aaaaaaaaaaaaaaaaaaa " + dato);

										ponerModoSingle(indice, dato);

										dato = command.get(indice);

										if (comandos.contains("-crop")) {

											String texto2 = dato.substring(dato.indexOf("[output]format=gray") + 19,
													dato.length());

											dato = dato.substring(0, dato.indexOf("[output]format=gray") + 19)
													+ "[cropped];[cropped]crop=w=100:h=200:x=50:y=50,split=2[c][d];[c][d]overlay=format=auto"
													+ texto2;

										}

										command.set(indice, dato);

									}

								}

								else {
									System.out.println("ENTROOOOOOOOOOOOOOOOOOOOO");
//									if (!cambiar && !verSiguienteDato("-colors", false).equals("")) {
//
//										String dato = command.get(indice);
//
//										try {
//											System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//											dato = dato.substring(0, dato.indexOf("[a]palettegen=") + 14)
//													+ "max_colors=" + verSiguienteDato("-colors", true) + ":"
//													+ dato.substring(dato.indexOf("stats_mode=single"), dato.length());
//
//											command.set(indice, dato);
//
//										}
//
//										catch (Exception e) {
//											System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
//											command.set(indice, dato.replace("max_colors=16", "max_colors="
//													+ verSiguienteDato("-colors", true) + ":stats_mode=single"));
//
//										}
//
//									}

									ponerFiltro("-vf", "format=gray", true);

								}

							}

							if (!verSiguienteDato("-blur", false).equals("")) {

								if (buenaCalidad) {

									eliminar("-vf");

									eliminar("-map");

									if (cambiar) {

										indice = command.indexOf("-filter_complex") + 1;

										String dato = verSiguienteDato("-blur", true);

										blur = dato.substring(0, dato.indexOf("x")) + ":"
												+ dato.substring(dato.indexOf("x") + 1, dato.length());

										if (!verSiguienteDato("-bn", false).equals("")) {

											command.set(indice,
													command.get(indice).substring(0,
															command.get(indice).indexOf("=new=1") + 6)
															+ "[output];[output]format=gray,boxblur=" + blur);

										}

										else {

											command.set(indice,
													command.get(indice).substring(0,
															command.get(indice).indexOf("=new=1") + 6)
															+ "[output];[output]boxblur=" + blur);

										}

									}

									else {

										String dato = verSiguienteDato("-blur", true);

										blur = dato.substring(0, dato.indexOf("x")) + ":"
												+ dato.substring(dato.indexOf("x") + 1, dato.length());

										command.set(indice, command.get(indice) + ",boxblur=" + blur
												+ "[blurred_video];[blurred_video]");

									}

								}

								else {

									String dato = verSiguienteDato("-blur", true);

									blur = "boxblur=" + dato.substring(0, dato.indexOf("x")) + ":"
											+ dato.substring(dato.indexOf("x") + 1, dato.length());

									if (verSiguienteDato("-bn", false).equals("")) {

										ponerFiltro("-vf", blur, false);

									}

									else {

										ponerFiltro("-vf", blur, true);

									}

								}

							}

							if (!verSiguienteDato("-text-watermark", false).equals("")) {

								String dato = "drawtext=text='" + texto + "'" + fuente + ":fontsize=" + fontsize + box
										+ ":fontcolor=" + colorWatermark + posicionTextWatermark;

								if (!verSiguienteDato("-bn", false).equals("")) {

									dato += ",colorchannelmixer=rr=0.3:rg=0.59:rb=0.11:gr=0.3:gg=0.59:gb=0.11:br=0.3:bg=0.59:bb=0.11";

								}

								if (buenaCalidad) {

									if (cambiar) {

										eliminar("-vf");

										indice = command.indexOf("-filter_complex") + 1;

										command.set(indice, command.get(indice) + "," + dato);

									}

								}

								else {

									if (command.indexOf("-vf") == -1) {

										ponerFiltro("-vf", dato, false);

									}

									else {

										ponerFiltro("-vf", dato, true);

									}

								}

							}

							if (!verSiguienteDato("-crop", false).equals("")) {

								try {

									if (cambiar) {

										indice = command.indexOf("-filter_complex") + 1;

										String dato = command.get(indice);

										command.set(indice, dato.substring(0, dato.indexOf(",")));

										if (!dato.contains("crop=w=")) {
											command.set(indice, command.get(indice) + crop
													+ dato.substring(dato.indexOf(","), dato.length()));
										}

										else {

											command.set(indice, command.get(indice)
													+ dato.substring(dato.indexOf(","), dato.length()));

										}

									}

									else {

										indice = command.indexOf("-filter_complex") + 1;

										String dato = command.get(indice);

										dato = dato.replace("[gif];[gif]", "[gif];[0:v]");

										dato = dato.replace("[bw];[0:v][bw]", "[bw];[gif][bw]");

										int indexBlur = dato.indexOf("drawtext=");

										crop = crop.substring(1);

										dato = dato.substring(0, indexBlur) + crop + ","
												+ dato.substring(indexBlur, dato.length());

										command.set(indice, dato);

									}

								}

								catch (Exception e) {

								}

							}

							if (!verSiguienteDato("-speed", false).equals("")) {

								String speed = convertSpeedToFilter(Integer.parseInt(verSiguienteDato("-speed", true)));

								if (buenaCalidad) {

									index = command.indexOf("-filter_complex") + 1;

									if (!command.get(index).endsWith("[blurred_video]")) {

										speed = "," + speed;

									}

									command.set(index, command.get(index) + speed);

								}

								else {

									ponerFiltro("-vf", speed, true);

								}

							}

							if (cambiar && verSiguienteDato("-watermark", false).equals("")) {

								String dato = command.get(indice);

								dato = dato.replace(
										",colorchannelmixer=rr=0.3:rg=0.59:rb=0.11:gr=0.3:gg=0.59:gb=0.11:br=0.3:bg=0.59:bb=0.11",
										"");

								command.set(indice, dato);

							}

						}

						reverse();

						volteo();

						rotate();

						if (imagen) {

							salida = comandos.getLast();

						}

						else {

							salida = salida(salida);

						}

						ponerContador();

						if (command.contains("-filter_complex")) {

							String dato = command.get(command.indexOf("-filter_complex") + 1);

							if (dato.contains("scale=w=-1:h=-1")) {

								command.set(command.indexOf("-filter_complex") + 1,
										dato.replace("scale=w=-1:h=-1,", ""));

							}

						}

						System.out.println(saberComandos(command));

						try {

							if (!fallo && !parar) {

								ffmpeg.run(command);

								try {

									Runtime.getRuntime().exec("gifsicle -i " + command.getLast() + " --optimize=3 -o "
											+ command.getLast());

								}

								catch (Exception e) {

								}

							}

						}

						catch (Exception e) {

							e.printStackTrace();

							fallo = true;

							System.out.println("\n" + ayuda + "\n\n");

						}

						if (!parar && !fallo && folder && !salida.isEmpty()) {

							abrirCarpeta("file://" + salida);

						}

					}

					catch (Exception e) {

						fallo = true;

						e.printStackTrace();

					}

				}

			}

			catch (Exception e) {

				fallo = true;

				e.printStackTrace();

			}

		}

	}

	private void ponerModoSingle(int indice, String dato) {

		String salidaDato = dato.substring(dato.indexOf("[p];[b][p]paletteuse"), dato.length());

		dato = dato.substring(0, dato.indexOf("[a]palettegen=") + 14) + "max_colors="
				+ verSiguienteDato("-colors", true) + ":" + "stats_mode=single" + salidaDato;

		command.set(indice, dato);
	}

	private void ponerContador() {

		if (!verSiguienteDato("-loop", false).equals("")) {

			String archivo = command.get(command.indexOf("-y") + 1);

			command.set(command.size() - 1, "-loop");

			command.add(verSiguienteDato("-loop", true));

			command.add(archivo);

		}

	}

	String saberSeparador() {

		if (System.getProperty("os.name").contains("indows")) {

			return "\\";

		}

		else {

			return "/";

		}

	}

	private void volteo() {

		if (!verSiguienteDato("-voltear", false).equals("")) {

			int index = command.indexOf("-filter_complex") + 1;

			if (index > -1) {

				String dato = "hflip";

				try {

					if (Integer.parseInt(verSiguienteDato("-voltear", true)) == 2) {

						dato = "vflip";

					}

				}

				catch (Exception e) {

				}

				if (command.contains("-vf")) {

					if (index > 0) {

						index--;

						command.set(index, "-vf");

						index++;

					}

					else if (!command.contains("-filter_complex") && !command.contains("-vf")) {

						command.add("-vf");

					}

				}

				if (index > 0) {

					command.set(index,
							command.get(index).substring(0, command.get(index).indexOf(",") + 1) + dato + ","
									+ command.get(index).substring(command.get(index).indexOf(",") + 1,
											command.get(index).length()));

				}

				else {

					if (!command.get(index).contains("-")) {

						command.add(command.get(index).substring(0, command.get(index).indexOf(",") + 1) + dato + ","
								+ command.get(index).substring(command.get(index).indexOf(",") + 1,
										command.get(index).length()));

					}

					else {

						if (!command.contains("-vf")) {

							command.add("-vf");

						}

						index = command.indexOf("-vf") + 1;

						if (index == command.size()) {

							index--;

						}

						if (!command.get(index).isEmpty()) {

							if (!command.get(index).isEmpty()) {

								command.set(index, command.get(index) + "," + dato);

							}

							else {

								command.add(index, dato);

							}

						}

						else {

							command.set(index, dato);

						}

					}

				}

			}

		}

	}

	private void eliminar(String dato) {

		int index = command.indexOf(dato);

		if (index > -1) {

			command.remove(index);

			command.remove(index);

		}

	}

}
