package main;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;

import mthos.JMthos;
import net.bramp.ffmpeg.FFmpeg;

public class JFfmpeg extends JFrame {

	private static final long serialVersionUID = 1L;

	private String os;

	private String separador;

	private LinkedList<String> comandos;

	private LinkedList<String> command;

	private String output;

	private String datoBlur;

	private String fontsize;

	private String texto;

	private String fuente;

	private String numeroColores;

	private String fps;

	private boolean marcaDeAgua;

	private boolean buenaCalidad;

	private boolean blur;

	private boolean bn;

	private String filtro;

	private String salida;

	private String videoSize;

	private boolean reversa;

	private String datoReversa;

	private String datoCrop;

	private String datoBn;

	private boolean crop;

	private int dither;

	private float velocidad;

	private boolean drawBox;

	private String datoScale;

	private boolean redimensionoWatermark;

	private String[] escalaWatermark;

	private String videoRotate;

	private String drawboxRotate;

	private String[] coloresTextoWatermark;

	private boolean horizontalTextWatermark;

	private String dithering;

	private String statsMode;

	private boolean textoSeparado;

	private String transpose;

	private String brilloYContraste;

	private Point redimensionarVideo;

	/**
	 * Obtiene la duración en segundos de un video utilizando FFmpeg.
	 *
	 * @param videoPath Ruta completa del archivo de video.
	 * @return Duración del video en segundos, o -1 si ocurre un error.
	 */

	public static double getVideoDurationInSeconds(String videoPath) {

		double durationInSeconds = -1;

		try {

			String[] command = { "ffmpeg", "-i", videoPath };

			Process process = new ProcessBuilder(command).redirectErrorStream(true).start();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

				String line;

				while ((line = reader.readLine()) != null) {

					if (line.contains("Duration:")) {

						String duration = line.split("Duration:")[1].trim().split(",")[0].trim();

						String[] timeParts = duration.split(":");

						int hours = Integer.parseInt(timeParts[0]);

						int minutes = Integer.parseInt(timeParts[1]);

						double seconds = Double.parseDouble(timeParts[2]);

						durationInSeconds = hours * 3600 + minutes * 60 + seconds;

						break;

					}

				}

			}

			process.waitFor();

		}

		catch (Exception e) {

		}

		return durationInSeconds;

	}

	private void calcularBlur(boolean cambio, String datoDrawBox, String[] colorBlur) {

		String textoBlur[] = verSiguienteDato("-blur", true).split("x");

		String coma = ",";

		if (!datoBn.isEmpty() || !videoRotate.isEmpty()) {

			coma = "";

		}

		if (colorBlur == null || colorBlur.length == 1) {

			colorBlur = new String[2];

			colorBlur[0] = "#000000";

			colorBlur[1] = "0.8";

		}

		String otroSize = datoScale.replace(":", "x");

		if (redimensionoWatermark && escalaWatermark.length == 2) {

			datoBlur = coma + "boxblur=luma_radius=" + textoBlur[0] + ":luma_power=" + textoBlur[1] + ":chroma_radius="
					+ textoBlur[2] + ":alpha_radius=" + textoBlur[3] + brilloYContraste +

					"[blurred];color=c=" + colorBlur[0] + "@" + colorBlur[1] + ":size=" + otroSize
					+ ":d=1[bg];[bg][blurred]overlay=(main_w-overlay_w)/2:0[bg_blur];[1:v]scale=" + escalaWatermark[0]
					+ ":" + escalaWatermark[1] + "[water];[bg_blur][water]" + calcularPosicionWatermark(cambio)
					+ "[bg_blur_box_drawn];[bg_blur_box_drawn]" + datoDrawBox;

		}

		else {

			if (!marcaDeAgua && !crop && texto.isEmpty()) {

				coma = "";

				if (buenaCalidad) {

					coma = ",";

				}

				if (!datoDrawBox.isEmpty()) {

					datoDrawBox = "," + datoDrawBox;

				}

				datoBlur = coma + "boxblur=luma_radius=" + textoBlur[0] + ":luma_power=" + textoBlur[1]
						+ ":chroma_radius=" + textoBlur[2] + ":alpha_radius=" + textoBlur[3] + brilloYContraste
						+ datoDrawBox;

			}

			else {

				datoBlur = coma + "boxblur=luma_radius=" + textoBlur[0] + ":luma_power=" + textoBlur[1]
						+ ":chroma_radius=" + textoBlur[2] + ":alpha_radius=" + textoBlur[3] + brilloYContraste
						+ "[blurred];color=c=" + colorBlur[0] + "@" + colorBlur[1] + ":size=" + otroSize
						+ ":d=1[bg];[bg][blurred]overlay=(main_w-overlay_w)/2:0[bg_blur];[bg_blur][fv];[fv]"
						+ datoDrawBox;

			}

		}

	}

	public int[] getVideoDimensions(String videoPath) throws Exception {

		ProcessBuilder processBuilder = new ProcessBuilder("ffprobe", "-v", "error", "-select_streams", "v:0",
				"-show_entries", "stream=width,height", "-of", "csv=p=0:s=x", videoPath);

		Process process = processBuilder.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String output = reader.readLine();

		if (output == null || output.isEmpty()) {

			throw new Exception("No se pudo obtener las dimensiones del video.");
		}

		String[] dimensions = output.split("x");

		int width = Integer.parseInt(dimensions[0]);

		int height = Integer.parseInt(dimensions[1]);

		return new int[] { width, height };

	}

	public String calcularVideoSize() throws Exception {

		String videoPath = comandos.get(1);

		String aspectRatio = verSiguienteDato("-aspectRatio", true);

		int[] dimensions = getVideoDimensions(videoPath);

		int width = dimensions[0];

		int height = dimensions[1];

		if (aspectRatio.isEmpty()) {

			aspectRatio = width + ":" + height;

		}

		String[] ratioParts = aspectRatio.split(":");

		if (ratioParts.length == 1) {

			ratioParts = aspectRatio.split("x");

			if (ratioParts.length == 1) {

				width = (int) redimensionarVideo.getX();

				height = (int) redimensionarVideo.getY();

			}

			else {

				width = Integer.parseInt(ratioParts[0]);

				height = Integer.parseInt(ratioParts[1]);

			}

		}

		else {

			int aspectWidth = Integer.parseInt(ratioParts[0]);

			int aspectHeight = Integer.parseInt(ratioParts[1]);

			float aspectRatioValue = (float) aspectWidth / aspectHeight;

			if (width / (float) height > aspectRatioValue) {

				width = Math.round(height * aspectRatioValue);

			}

			else {

				height = Math.round(width / aspectRatioValue);

			}

		}

		return width + "x" + height;

	}

	public String calculateCropScale() throws Exception {

		String crop = datoCrop;

		crop = crop.replace("x", ":");

		String dato2 = crop;

		String aspectRatio = "";

		if (verSiguienteDato("-aspectRatio", false).isEmpty()) {

			aspectRatio = crop;

			aspectRatio = aspectRatio.substring(0, aspectRatio.lastIndexOf(":"));

			aspectRatio = aspectRatio.substring(0, aspectRatio.lastIndexOf(":"));

		}

		else {

			aspectRatio = verSiguienteDato("-aspectRatio", true);

		}

		int[] dimensions = new int[2];

		dimensions[0] = Integer.parseInt(crop.substring(0, crop.indexOf(":")));

		crop = crop.substring(crop.indexOf(":") + 1, crop.length());

		dimensions[1] = Integer.parseInt(crop.substring(0, crop.indexOf(":")));

		String[] ratioParts = aspectRatio.split(":");

		if (ratioParts.length == 1 && aspectRatio.contains("x")) {

			ratioParts = aspectRatio.split("x");
		}

		if (ratioParts.length == 1) {

			ratioParts = new String[2];

			ratioParts[0] = Integer.toString(
					(int) (Float.parseFloat(dato2.substring(0, dato2.indexOf(":"))) * Float.parseFloat(aspectRatio)));

			dato2 = dato2.substring(dato2.indexOf(":") + 1);

			ratioParts[1] = Integer.toString(
					(int) (Float.parseFloat(dato2.substring(0, dato2.indexOf(":"))) * Float.parseFloat(aspectRatio)));

			redimensionarVideo.setLocation(Integer.parseInt(ratioParts[0]), Integer.parseInt(ratioParts[1]));

		}

		return ratioParts[0] + ":" + ratioParts[1];

	}

	public String limpiarPosiciones(String texto, boolean filtro, int pos, String posX, String posY, boolean mover) {

		if (filtro) {

			if (mover) {

				texto = texto.replace("(main_w-overlay_w)", "(W-w)" + "+" + posX);

				texto = texto.replace("(main_h-overlay_h)", "(H-h)" + "+" + posY);

				texto = texto.replace("(main_w-overlay_w)/2", "(W-w)/2" + "+" + posX);

				texto = texto.replace("(main_h-overlay_h)/2", "(H-h)/2" + "+" + posY);

			}

			else {

				texto = texto.replace("(main_w-overlay_w)", "(W-w)");

				texto = texto.replace("(main_h-overlay_h)", "(H-h)");

				texto = texto.replace("(main_w-overlay_w)/2", "(W-w)/2");

				texto = texto.replace("(main_h-overlay_h)/2", "(H-h)/2");

			}

		}

		else {

			if (pos == 9) {

				if (mover) {

					texto = texto.replace("main_w-overlay_w", "x=w-text_w" + "+" + posX);

					texto = texto.replace("main_h-overlay_h", "y=h-text_h" + "+" + posY);

				}

				else {

					texto = texto.replace("main_w-overlay_w", "x=w-text_w");

					texto = texto.replace("main_h-overlay_h", "y=h-text_h");

				}

			}

			else {

				if (mover) {

					texto = texto.replace("(w-text_w)/2", "(w-text_w)/2" + "+" + posX);

					texto = texto.replace("(h-text_h)/2", "(h-text_h)/2" + "+" + posY);

					texto = texto.replace("(main_w-overlay_w)", "(w-text_w)" + "+" + posX);

					texto = texto.replace("(main_h-overlay_h)", "(h-text_h)" + "+" + posY);

					texto = texto.replace("(main_w-overlay_w)/2", "(w-text_w)/2" + "+" + posX);

					texto = texto.replace("(main_h-overlay_h)/2", "(h-text_h)/2" + "+" + posY);

				}

				else {

					texto = texto.replace("(main_w-overlay_w)", "(w-text_w)");

					texto = texto.replace("(main_h-overlay_h)", "(h-text_h)");

					texto = texto.replace("(main_w-overlay_w)/2", "(w-text_w)/2");

					texto = texto.replace("(main_h-overlay_h)/2", "(h-text_h)/2");

				}

			}

		}

		texto = texto.replace("=x=", "x=");

		return texto;

	}

	public static List<Integer> encontrarPosiciones(String texto, String cadena) {

		List<Integer> posiciones = new ArrayList<>();

		int indice = texto.indexOf(cadena);

		while (indice >= 0) {

			posiciones.add(indice);

			indice = texto.indexOf(cadena, indice + 1);

		}

		return posiciones;

	}

	public String getOutput() {

		return output;

	}

	private String verSiguienteDato(String search, boolean filtro) {

		String resultado = "";

		int index = comandos.indexOf(search);

		try {

			if (filtro) {

				if (index > -1) {

					resultado = comandos.get(++index);

				}

			}

			else {

				if (index > -1) {

					resultado = comandos.get(index);

				}

			}

			resultado = resultado.trim();

			if (search.startsWith("--pos") || search.startsWith("-pos")) {

				resultado = limpiar(resultado);

			}

		}

		catch (Exception e) {

		}

		return resultado;

	}

	public void abrirCarpeta(String ruta) {

		if (ruta != null && !ruta.isEmpty() && !ruta.isEmpty()) {

			try {

				if (os.contains("indows")) {

					try {

						String[] command = { "cmd", "/c", "C:\\Windows\\explorer.exe", "\"" + ruta + "\"" };

						ProcessBuilder processBuilder = new ProcessBuilder(command);

						processBuilder.start();

					}

					catch (Exception e) {

					}

				}

				else if (os.contains("inux")) {

					ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", ruta);

					processBuilder.redirectErrorStream(true);

					processBuilder.start();

				}

				else {

					try {

						String[] command = { "open", "\"" + ruta + "\"" };

						ProcessBuilder processBuilder = new ProcessBuilder(command);

						processBuilder.start();

					}

					catch (Exception e) {

					}

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

			if (!comandos.get(busqueda).startsWith("-")) {

				if (index.equals("-i") || index.equals("-y")) {

					String dato = comandos.get(busqueda);

					dato = dato.replace("\"", "");

					command.add("\"" + dato + "\"");

				}

				else {

					command.add(comandos.get(busqueda));

				}

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

		if (verSiguienteDato("-y", true).endsWith(".gif")) {

			command.add("-c:v");

			command.add("gif");

		}

	}

	private String salida() {

		command.add("-pix_fmt");

		command.add("rgb8");

		saberSiEsGif();

		int busqueda = comando("-y");

		if (busqueda == -1) {

			try {

				separador = saberSeparador(os);

				File test = new File(comandos.getLast().substring(0, comandos.getLast().lastIndexOf(separador)));

				if (test.isDirectory()) {

					salida = comandos.getLast();

					if (!command.contains("-y")) {

						command.add("-y");

						command.add("\"" + salida + "\"");

					}

				}

			}

			catch (Exception e) {

			}

		}

		else {

			salida = verSiguienteDato("-y", true);

		}

		return salida;

	}

	private void bits() {

		if (!verSiguienteDato("-bits", false).isEmpty()) {

			command.add("-b:v");

			command.add(verSiguienteDato("-bits", true));

		}

	}

	private void calidad() {

		command.add("-q:v");

		if (!verSiguienteDato("-quality", false).isEmpty()) {

			command.add(verSiguienteDato("-quality", true));

		}

		else {

			command.add("16");

		}

	}

	private String calcularPosicionDeMarcaDeAgua(int index, boolean text) {

		String resultado = "";

		switch (index) {

		case 1:

			if (text) {

				resultado = "drawtext=x=0:y=0";

			}

			else {

				resultado = "overlay=x=0:y=0";

			}

			break;

		case 2:

			if (text) {

				resultado = "drawtext=x=(w-text_w)/2:y=0";

			}

			else {

				resultado = "overlay=x=(main_w-overlay_w)/2:y=0";

			}

			break;

		case 3:

			if (text) {

				resultado = "drawtext=x=(w-text_w):y=0";

			}

			else {

				resultado = "overlay=x=(main_w-overlay_w):y=0";

			}

			break;

		case 4:

			if (text) {

				resultado = "drawtext=x=0:y=(h-text_h)/2";

			}

			else {

				resultado = "overlay=x=0:y=(main_h-overlay_h)/2";

			}

			break;

		case 5:

			if (text) {

				resultado = "drawtext=x=(w-text_w)/2:y=(h-text_h)/2";

			}

			else {

				resultado = "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2";

			}

			break;

		case 6:

			if (text) {

				resultado = "drawtext=x=(w-text_w):y=(h-text_h)/2";

			}

			else {

				resultado = "overlay=x=(main_w-overlay_w):y=(main_h-overlay_h)/2";

			}

			break;

		case 7:

			if (text) {

				resultado = "drawtext=x=0:y=(h-text_h)";

			}

			else {

				resultado = "overlay=x=0:y=(main_h-overlay_h)";

			}

			break;

		case 8:

			if (text) {

				resultado = "drawtext=x=(w-text_w)/2:y=(h-text_h)";

			}

			else {

				resultado = "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)";

			}

			break;

		case 9:

			if (text) {

				resultado = "drawtext=x=(w-text_w):y=(h-text_h)";

			}

			else {

				resultado = "overlay=x=(main_w-overlay_w):y=(main_h-overlay_h)";

			}

			break;

		default:

			if (text) {

				resultado = "drawtext=x=(w-text_w):y=(h-text_h)";

			}

			else {

				resultado = "overlay=x=(main_w-overlay_w):y=(main_h-overlay_h)";

			}

			break;

		}

		if (resultado.startsWith("overlay")) {

			resultado = "[1:v]" + resultado;

		}

		if (!text && !buenaCalidad) {

			resultado = "palettegen=max_colors=" + numeroColores + "[p];[fv][p]paletteuse," + resultado;

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

	public String calcularSize(String videoPath, String aspectRatio) throws Exception {

		if (crop) {

			return calculateCropScale();

		}

		int[] dimensions = getVideoDimensions(videoPath);

		int width = dimensions[0];

		int height = dimensions[1];

		int aspectWidth;

		int aspectHeight;

		if (aspectRatio.isEmpty()) {

			aspectWidth = width;

			aspectHeight = height;

		}

		else {

			String[] ratioParts = aspectRatio.split(":");

			aspectWidth = Integer.parseInt(ratioParts[0]);

			aspectHeight = Integer.parseInt(ratioParts[1]);

		}

		float aspectRatioValue = (float) aspectWidth / aspectHeight;

		if (width / (float) height > aspectRatioValue) {

			width = Math.round(height * aspectRatioValue);

		}

		else {

			height = Math.round(width / aspectRatioValue);

		}

		return width + ":" + height;

	}

	private void inicializarVariables() throws Exception {

		texto = "";

		bn = !verSiguienteDato("-bn", false).isEmpty();

		velocidad = 1f;

		videoRotate = "";

		drawboxRotate = "";

		horizontalTextWatermark = true;

		textoSeparado = !verSiguienteDato("-separatedText", false).isEmpty();

		crop = !verSiguienteDato("-crop", false).isEmpty();

		redimensionoWatermark = !verSiguienteDato("-sizeWatermark", false).isEmpty();

		escalaWatermark = verSiguienteDato("-sizeWatermark", true).split("x");

		if (crop) {

			datoCrop = verSiguienteDato("-crop", true).replace("x", ":");

		}

		datoScale = calcularSize(comandos.get(1), verSiguienteDato("-aspectRatio", true));

		drawBox = !verSiguienteDato("-drawbox", false).isEmpty();

		datoReversa = "";

		datoBn = "";

		marcaDeAgua = !verSiguienteDato("-watermark", false).isEmpty();

		blur = !verSiguienteDato("-blur", false).isEmpty();

		buenaCalidad = false;

		fontsize = "250";

		fuente = "";

		numeroColores = "256";

		reversa = !verSiguienteDato("-reverse", false).isEmpty();

		videoSize = calcularVideoSize();

		if (!verSiguienteDato("-drawboxRotate", false).isEmpty()) {

			drawboxRotate = ",rotate=angle=" + verSiguienteDato("-drawboxRotate", true);

		}

		if (!verSiguienteDato("-text-watermark", false).isEmpty()) {

			texto = verSiguienteDato("-text-watermark", true);

		}

		if (!verSiguienteDato("-formatTextWatermark", false).isEmpty()
				&& verSiguienteDato("-formatTextWatermark", true).equals("vertical")) {

			horizontalTextWatermark = false;

		}

		String datoColoresWatermark = verSiguienteDato("-colorsTextWatermark", true);

		if (datoColoresWatermark.isEmpty()) {

			coloresTextoWatermark = new String[1];

			coloresTextoWatermark[0] = "black";

		}

		else {

			if (datoColoresWatermark.equals("random")) {

				generarColoresAleatoriamente();

			}

			else if (!datoColoresWatermark.contains("x")) {

				coloresTextoWatermark = new String[1];

				coloresTextoWatermark[0] = datoColoresWatermark;

			}

			else {

				coloresTextoWatermark = datoColoresWatermark.split("x");

			}

		}

		if (!verSiguienteDato("-videoRotate", false).isEmpty()) {

			if (bn) {

				videoRotate = ",rotate=angle=" + verSiguienteDato("-videoRotate", true);

			}

			else {

				String backgroundRotateColor = "";

				if (verSiguienteDato("-colorBackgroundRotate", false).isEmpty()
						|| verSiguienteDato("-colorBackgroundRotate", true).isEmpty()) {

					backgroundRotateColor = "white";

				}

				else {

					backgroundRotateColor = verSiguienteDato("-colorBackgroundRotate", true);

				}

				videoRotate = ",rotate=angle=" + verSiguienteDato("-videoRotate", true) + ":fillcolor="
						+ backgroundRotateColor;

			}

			if (!verSiguienteDato("-transpose", false).isEmpty()) {

				try {

					int numero = Integer.parseInt(verSiguienteDato("-transpose", true));

					switch (numero) {

					case 4:

						transpose = ",transpose=1,transpose=1,transpose=1";

						break;

					case 5:

						transpose = ",transpose=2,transpose=2";

						break;

					case 6:

						transpose = ",transpose=2,transpose=2,transpose=2";

						break;

					default:

						transpose = ",transpose=" + numero;

						break;

					}

				}

				catch (Exception e) {

				}

			}

		}

		if (!verSiguienteDato("-speed", false).isEmpty()) {

			velocidad = 100f / (Float.parseFloat(verSiguienteDato("-speed", true)));

		}

		if (comandos.contains("-good")) {

			buenaCalidad = true;

		}

		if (bn) {

			datoBn = ",format=gray";

		}

		if (reversa) {

			datoReversa = ",reverse";

		}

		fps = "25";

		if (!verSiguienteDato("-fps", false).isEmpty()) {

			fps = verSiguienteDato("-fps", true);

		}

		if (!verSiguienteDato("-colors", false).isEmpty()) {

			numeroColores = verSiguienteDato("-colors", true);

		}

		if (!verSiguienteDato("--font-text-watermark", false).isEmpty()) {

			String archivoFuente = verSiguienteDato("--font-text-watermark", true);

			if (new File(archivoFuente).exists()) {

				if (System.getProperty("os.name").contains("indows")) {

					char unidad = archivoFuente.charAt(0);

					archivoFuente = archivoFuente.substring(3);

					archivoFuente = archivoFuente.replace("/", "\\");

					fuente = ":fontfile='" + unidad + "\\\\" + archivoFuente + "'";

				}

				else {

					fuente = ":fontfile=" + archivoFuente;

				}

			}

			else if (System.getProperty("os.name").contains("indows")) {

				fuente = ":fontfile='C\\\\Windows\\\\Fonts\\\\arial.ttf'";

			}

		}

		else if (System.getProperty("os.name").contains("indows")) {

			fuente = ":fontfile='C\\\\Windows\\\\Fonts\\\\arial.ttf'";

		}

		if (!verSiguienteDato("-font-size-text-watermark", false).isEmpty()) {

			fontsize = verSiguienteDato("-font-size-text-watermark", true);

		}

		if (!texto.isEmpty() && !texto.replace(" ", "").isEmpty() && fontsize.equals("0")) {

			fontsize = "25";

		}

	}

	private void ponerVueltas() {

		if (!verSiguienteDato("-loop", false).isEmpty()) {

			command.add("-loop");

			command.add(verSiguienteDato("-loop", true));

		}

	}

	private void volteo() {

		if (!verSiguienteDato("-voltear", false).isEmpty()) {

			int index = command.indexOf("-filter_complex") + 1;

			String dato = "hflip";

			String valor = "";

			try {

				valor = verSiguienteDato("-voltear", true);

				if (Integer.parseInt(valor) == 2) {

					dato = "vflip";

				}

			}

			catch (Exception e) {

				if (valor.equals("vflip")) {

					dato = "vflip";

				}

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

	public String convertSpeedToFilter(int percentage) {

		String resultado = "";

		if (percentage <= 0) {

			command.addFirst("-y");

			command.add("-vframes");

			command.add("1");

			command.add(comandos.getLast());

		}

		else {

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

	private void ponerFiltro(String index, String dato, boolean coma) {

		int indice = command.indexOf(index);

		if (!dato.isEmpty()) {

			if (indice == -1) {

				command.add(index);

				command.add(dato);

			}

			else if (!verSiguienteDato(index, true).isEmpty() && !command.get(++indice).equals(dato)) {

				if (coma) {

					command.set(indice, command.get(indice) + "," + dato);

				}

				else {

					command.set(indice, command.get(indice) + dato);

				}

			}

		}

	}

	private String calcularPosicionWatermark(boolean cambio) {

		if (cambio) {

			return "";

		}

		String dato = verSiguienteDato("--pos-watermark", true);

		if (dato.contains("x")) {

			String dato2[] = dato.split("x");

			return "overlay=" + dato2[0] + ":" + dato2[1];

		}

		else {

			return calcularPosicionDeMarcaDeAgua(Integer.parseInt(dato), false);

		}

	}

	private String calcularFiltroWatermarkText(boolean cambio) {

		boolean sombra = !verSiguienteDato("--shadow-text-watermark", true).isEmpty();

		String datoSombra = "";

		String pos = "";

		String posicionTexto = verSiguienteDato("--pos-text-watermark", true);

		String coma = ",";

		if (cambio) {

			coma = "";

		}

		if (sombra) {

			String[] shadow = verSiguienteDato("--shadow-text-watermark", true).split("x");

			if (posicionTexto.contains("x")) {

				pos = "x=" +

						(Integer.parseInt(
								posicionTexto.substring(0, posicionTexto.indexOf("x"))) + Integer.parseInt(shadow[2]))
						+ ":y="
						+ (Integer.parseInt(
								posicionTexto.substring(posicionTexto.indexOf("x") + 1, posicionTexto.length()))
								+ Integer.parseInt(shadow[3]));

			}

			else {

				pos = limpiarPosiciones(
						calcularPosicionDeMarcaDeAgua(Integer.parseInt(posicionTexto), true).substring(8), false,
						Integer.parseInt(posicionTexto), shadow[2], shadow[3], true);

			}

			datoSombra = coma + "drawtext=text='" + texto + "'" + fuente + ":fontsize=" + shadow[1] + ":fontcolor="
					+ shadow[0] + ":" + pos;

		}

		String pos2 = "";

		if (posicionTexto.contains("x")) {

			pos2 = "x=" + posicionTexto.substring(0, posicionTexto.indexOf("x")) + ":y="
					+ posicionTexto.substring(posicionTexto.indexOf("x") + 1, posicionTexto.length());

		}

		else {

			pos2 = limpiarPosiciones(calcularPosicionDeMarcaDeAgua(Integer.parseInt(posicionTexto), true).substring(8),
					false, Integer.parseInt(posicionTexto), "", "", false);

		}

		if (coloresTextoWatermark.length != texto.length()) {

			generarColoresAleatoriamente();

		}

		if (!textoSeparado && (!verSiguienteDato("-formatTextWatermark", false).isEmpty()
				&& verSiguienteDato("-formatTextWatermark", true).equals("vertical"))) {

			textoSeparado = true;

		}

		if (!textoSeparado) {

			return datoSombra + ",drawtext=text='" + texto + "'" + fuente + ":fontsize=" + fontsize + ":fontcolor="
					+ coloresTextoWatermark[0] + ":" + pos2
					+ "[filtered];[filtered]split[a][b];[a]palettegen=max_colors=" + numeroColores + ":stats_mode="
					+ statsMode + "[p];[b][p]paletteuse=dither=" + dithering + ":bayer_scale=" + dither;

		}

		else {

			int equis = 0;

			int ye = 0;

			final int separacion;

			if (!verSiguienteDato("-posSeparatorTextWatermark", false).isEmpty()) {

				separacion = Integer.parseInt(verSiguienteDato("-posSeparatorTextWatermark", true));

			}

			else {

				separacion = Math.round(Integer.parseInt(fontsize) * 1.2f);

			}

			if (!verSiguienteDato("-posXTextWatermark", false).isEmpty()) {

				equis = Integer.parseInt(verSiguienteDato("-posXTextWatermark", true));

			}

			if (!verSiguienteDato("-posYTextWatermark", false).isEmpty()) {

				ye = Integer.parseInt(verSiguienteDato("-posYTextWatermark", true));

			}

			final int YE;

			if (ye > 0) {

				YE = Integer.parseInt(verSiguienteDato("-posYTextWatermark", true));

			}

			else {

				YE = 0;

			}

			if (horizontalTextWatermark) {

				String palabras = "";

				char letra;

				int alturaMinuscula = Integer.parseInt(fontsize) / 10;

				for (int i = 0; i < coloresTextoWatermark.length; i++) {

					if (i == 1) {

						coma = ",";

					}

					letra = texto.charAt(i);

					if (JMthos.esMayuscula(letra) || letra == 'p' || letra == 'q' || letra == 'j' || letra == 'l'
							|| letra == 'i') {

						ye = YE;

					}

					else {

						ye = YE + alturaMinuscula;

					}

					palabras += coma + "drawtext=text='" + letra + "'" + fuente + ":fontsize=" + fontsize
							+ ":fontcolor=" + coloresTextoWatermark[i] + ":x=" + equis + ":y=" + ye;

					if (letra == 'l' || letra == 'j' || letra == 'i' || letra == 'f' || letra == '1' || letra == '.'
							|| letra == '|' || letra == ',' || (int) letra == 59) {

						equis += separacion - 20;

					}

					else {

						equis += separacion;

					}

				}

				palabras += "[texted];[texted]split[a][b];[a]palettegen[p];[b][p]paletteuse=dither=" + dithering
						+ ":bayer_scale=" + dither;

				return palabras;

			}

			else {

				String palabras = "";

				char letra;

				int mover = (JMthos.calcularSucesionAritmeticaAInt("20#40,50#25,100#0,200#-45",
						Integer.parseInt(fontsize)));

				int mitad = Integer.parseInt(fontsize) / 2;

				int pos1 = ((equis + mitad + mitad / 3) + mover) - 42;

				int pos3 = (equis + mitad
						+ (JMthos.calcularSucesionAritmeticaAInt("100#-10,150#-40", Integer.parseInt(fontsize)))) - 42;

				int pos4 = ((equis + mitad) + mover) - 42;

				for (int i = 0; i < coloresTextoWatermark.length; i++) {

					if (i == 1) {

						coma = ",";

					}

					letra = texto.charAt(i);

					if (letra == 'p' || letra == 'q' || letra == 'j' || letra == 'l' || letra == 'i') {

						equis = pos1;

					}

					else if (JMthos.esMayuscula(letra)) {

						equis = pos3;

					}

					else {

						equis = pos4;

					}

					palabras += coma + "drawtext=text='" + letra + "'" + fuente + ":fontsize=" + fontsize
							+ ":fontcolor=" + coloresTextoWatermark[i] + ":x=" + equis + ":y=" + ye;

					ye += separacion;

				}

				palabras += "[texted];[texted]split[a][b];[a]palettegen[p];[b][p]paletteuse=dither=" + dithering
						+ ":bayer_scale=" + dither;

				return palabras;

			}

		}

	}

	private void generarColoresAleatoriamente() {

		coloresTextoWatermark = JMthos.generateUniqueColors(texto.length()).toArray(new String[0]);

	}

	private String limpiar(String datoPosWatermark) {

		datoPosWatermark = datoPosWatermark.replace("X", "x");

		datoPosWatermark = datoPosWatermark.replace("*", "x");

		return datoPosWatermark;

	}

	public JFfmpeg(String[] comandos, boolean openFile) throws IOException {

		this(Arrays.asList(comandos), openFile);

	}

	public JFfmpeg(List<String> comandos, boolean openFile) throws IOException {

		redimensionarVideo = new Point();

		datoCrop = "";

		brilloYContraste = "";

		transpose = "";

		statsMode = "full";

		dithering = "bayer";

		datoReversa = "";

		datoBn = "";

		videoRotate = "";

		datoBlur = "";

		os = System.getProperty("os.name");

		command = new LinkedList<String>();

		this.comandos = new LinkedList<String>(comandos);

		String ayuda = "\n---------------------------------------------------------------\n\nBad arguments\n\nUse the argument -h to view help\n";

		dither = 5;

		marcaDeAgua = false;

		if (comandos.isEmpty()) {

			System.out.flush();

			System.out.println("\n" + ayuda);

		}

		else {

			try {

				if (!verSiguienteDato("-brillo", false).isEmpty() && verSiguienteDato("-contraste", false).isEmpty()) {

					brilloYContraste = ",eq=brightness=" + verSiguienteDato("-brillo", true);

				}

				else if (verSiguienteDato("-brillo", false).isEmpty()
						&& !verSiguienteDato("-contraste", false).isEmpty()) {

					brilloYContraste = ",eq=contrast=" + verSiguienteDato("-contraste", true);

				}

				else if (!verSiguienteDato("-brillo", false).isEmpty()
						&& !verSiguienteDato("-contraste", false).isEmpty()) {

					brilloYContraste = ",eq=brightness=" + verSiguienteDato("-brillo", true) + ":contrast="
							+ verSiguienteDato("-contraste", true);

				}

				if (!verSiguienteDato("-statsMode", false).isEmpty()) {

					statsMode = verSiguienteDato("-statsMode", true);

				}

				if (!verSiguienteDato("-dither", false).isEmpty()) {

					dither = Integer.parseInt(verSiguienteDato("-dither", true));

				}

				if (!verSiguienteDato("-dithering", false).isEmpty()) {

					dithering = verSiguienteDato("-dithering", true);

				}

				if (comandos.contains("-h") || comandos.contains("-help")) {

					help();

				}

				else {

					inicializarVariables();

					FFmpeg ffmpeg = new FFmpeg();

					comando("-i");

					if (!blur && !marcaDeAgua && crop) {

						command.add("-s");

						command.add(videoSize);

					}

					if (marcaDeAgua) {

						command.add("-i");

						command.add(verSiguienteDato("-watermark", true));

					}

					String fv = "[fv];[fv]";

					boolean cambio = false;

					String fEscala = ",scale=" + datoScale;

					if (marcaDeAgua) {

						buenaCalidad = true;

					}

					else {

						if (!marcaDeAgua && !buenaCalidad && !texto.isEmpty()) {

							buenaCalidad = true;

						}

						cambio = true;

					}

					if (!numeroColores.isEmpty()) {

						buenaCalidad = true;

					}

					String datoDrawBox = "";

					String[] colorBlur = verSiguienteDato("-colorBlur", true).split("x");

					String[] colorDrawbox = verSiguienteDato("-colorDrawbox", true).split("x");

					boolean box = !verSiguienteDato("-drawbox", false).isEmpty();

					String[] textoDrawBox = verSiguienteDato("-drawbox", true).split("x");

					if (verSiguienteDato("-colorDrawbox", false).isEmpty()) {

						colorDrawbox = new String[2];

						colorDrawbox[0] = "black";

						colorDrawbox[1] = "0.8";

					}

					if (box && verSiguienteDato("-colorFillDrawbox", false).isEmpty()) {

						datoDrawBox = "drawbox=x=" + textoDrawBox[0] + ":y=" + textoDrawBox[1] + ":w=" + textoDrawBox[2]
								+ ":h=" + textoDrawBox[3] + ":color=" + colorDrawbox[0] + "@" + colorDrawbox[1] + ":t="
								+ textoDrawBox[4] + drawboxRotate + "[bg_blur_box];[bg_blur_box]";

					}

					else {

						String[] fillDrawbox = verSiguienteDato("-colorFillDrawbox", true).split("x");

						if (verSiguienteDato("-colorFillDrawbox", false).isEmpty()) {

							fillDrawbox = new String[2];

							fillDrawbox[0] = "black";

							fillDrawbox[1] = "0.8";

						}

						if (!drawboxRotate.isEmpty()) {

							drawboxRotate = drawboxRotate.substring(1);

						}

						if (box) {

							datoDrawBox = "drawbox=x=" + textoDrawBox[0] + ":y=" + textoDrawBox[1] + ":w="
									+ textoDrawBox[2] + ":h=" + textoDrawBox[3] + ":color=" + fillDrawbox[0] + "@"
									+ fillDrawbox[1] + ":t=fill[bg_blur_box];[bg_blur_box]drawbox=x=" + textoDrawBox[0]
									+ ":y=" + textoDrawBox[1] + ":w=" + textoDrawBox[2] + ":h=" + textoDrawBox[3]
									+ ":color=" + colorDrawbox[0] + "@" + colorDrawbox[1] + ":t=" + textoDrawBox[4]
									+ "[final];[final]" + drawboxRotate + "[bg_blur_box];[bg_blur_box]";

						}

					}

					if (blur) {

						calcularBlur(cambio, datoDrawBox, colorBlur);

					}

					if (crop) {

						buenaCalidad = true;

						if (blur) {

							if (!datoBn.isEmpty()) {

								datoBn += "[gray];[gray]";

							}

							fv = "";

							if (drawBox) {

								if (colorDrawbox.length == 1) {

									colorDrawbox = new String[2];

									colorDrawbox[0] = "black";

									colorDrawbox[1] = "1";

								}

							}

						}

						if (!videoRotate.isEmpty() && !datoBlur.isEmpty()) {

							if (!datoBn.isEmpty()) {

								datoBn = datoBn.substring(0, datoBn.indexOf("gray") + 4);

								videoRotate += "[gray];[gray]";

								if (datoBlur.startsWith(",")) {

									datoBlur = datoBlur.substring(1);

								}

							}

							else {

								videoRotate += ",";

							}

						}

						if (redimensionoWatermark) {

							if (!datoDrawBox.isEmpty()) {

								datoDrawBox = "," + datoDrawBox;

							}

							filtro = "[0:v]fps=" + fps + ",crop=" + datoCrop + ",scale=" + datoScale + ",setpts="
									+ velocidad + "*PTS" + datoReversa + datoBn + videoRotate + transpose + datoBlur
									+ brilloYContraste + fv + ";[1:v]scale=" + escalaWatermark[0] + ":"
									+ escalaWatermark[1] + "[watermark];[fv][watermark]"
									+ calcularPosicionWatermark(cambio) + datoDrawBox +

									calcularFiltroWatermarkText(cambio) + fEscala;

							if (!blur) {

								filtro = filtro.replace("[bg_blur_box];[bg_blur_box],drawtext=text=",
										"[rotated];[rotated]drawtext=text=");

							}

							filtro = filtro.replace("[fv];[fv];[1:v]", "[fv];[1:v]");

						} else {

							filtro = "[0:v]fps=" + fps + ",crop=" + datoCrop + ",scale=" + datoScale + ",setpts="
									+ velocidad + "*PTS" + datoReversa + datoBn + videoRotate + transpose + datoBlur
									+ brilloYContraste + fv + calcularPosicionWatermark(cambio)
									+ calcularFiltroWatermarkText(cambio) + fEscala;

						}

						ponerFiltro("-filter_complex", filtro, false);

					}

					else {

						if (buenaCalidad) {

							if (marcaDeAgua || !verSiguienteDato("-text-watermark", false).isEmpty()) {

								if (!datoBlur.isEmpty() && !datoBlur.startsWith(",")) {

									datoBlur = "," + datoBlur;

								}

								if (datoBlur.isEmpty()
										|| !datoBlur.contains("brightness") && !datoBlur.contains("contrast")) {

									filtro = "[0:v]fps=" + fps + ",scale=" + datoScale + ",setpts=" + velocidad + "*PTS"
											+ datoReversa + datoBn + videoRotate + transpose + datoBlur
											+ brilloYContraste + fv + calcularPosicionWatermark(cambio)
											+ calcularFiltroWatermarkText(cambio) + fEscala;

								}

								else {

									filtro = "[0:v]fps=" + fps + ",scale=" + datoScale + ",setpts=" + velocidad + "*PTS"
											+ datoReversa + datoBn + videoRotate + transpose + datoBlur
											+ brilloYContraste + fv + calcularPosicionWatermark(cambio)
											+ calcularFiltroWatermarkText(cambio) + fEscala;

								}

								filtro = filtro.replace("[fv];[fv][fv];[fv]", "[fv];[fv]");

								filtro = filtro.replace("color=c=", "color=");

								filtro = filtro.replace("[bg_blur][fv];[fv]", "[bg_blur]");

								if (blur) {

									filtro = filtro.replace("[final];[final][bg_blur_box];[bg_blur_box][fv];[fv][1:v]",
											"[final];[final][1:v]");

									filtro = filtro.replace("[final];[final][bg_blur_box];[bg_blur_box][fv];[fv]",
											"[final];[final]");

								}

							}

							else {

								if (datoBlur.isEmpty()
										|| !datoBlur.contains("brightness") && !datoBlur.contains("contrast")) {

									filtro = "[0:v]fps=" + fps + ",scale=" + datoScale + ",setpts=" + velocidad + "*PTS"
											+ datoReversa + datoBn + videoRotate + transpose + datoBlur + fv
											+ brilloYContraste.substring(1) + fEscala;

								}

								else {
////////////////////////////////////////////////////////////
									filtro = "[0:v]fps=" + fps + ",scale=" + datoScale + ",setpts=" + velocidad + "*PTS"
											+ datoReversa + datoBn + videoRotate + transpose + datoBlur
											+ brilloYContraste + fv + fEscala;

								}

								if (filtro.contains("[fv];[fv],scale=")) {

									filtro = filtro.replace("[fv];[fv],scale=", ",scale=");

								}

								filtro = filtro.replace("[final];[final][bg_blur_box];[bg_blur_box],scale=",
										"[final];[final]scale=");

							}

							ponerFiltro("-filter_complex", filtro, false);

						}

						else {

							// datoBn + videoRotate +

							// datoBlur + fv

							// + calcularPosicionWatermark(cambio) + calcularFiltroWatermarkText(cambio) +

							// fEscala;

							if (reversa) {

								datoReversa = JMthos.removeFirstCharacter(datoReversa);

								datoReversa += ",";

							}

							if (blur) {

								datoBlur = "," + datoBlur;

							}

							ponerFiltro("-vf", datoReversa + "fps=" + fps + ",scale=" + datoScale + ",setpts="
									+ velocidad + "*PTS" + datoBlur + videoRotate + transpose + brilloYContraste,
									false);

						}

					}

					volteo();

					bits();

					comando("-ss");

					comando("-t");

					comando("-r");

					calidad();

					ponerVueltas();

					salida = salida();

					output = saberComandos(command);

					System.out.println(output.replace("\"\"", "\""));

					ffmpeg.run(command);

					if (!verSiguienteDato("-gifsicle", false).isEmpty()) {

						String[] comand = new String[10];

						comand[0] = "gifsicle";

						comand[1] = "-i";

						comand[2] = command.getLast();

						if (verSiguienteDato("-optimize", false).isEmpty()) {

							comand[3] = "--unoptimize";

						}

						else {

							comand[3] = "--optimize=" + verSiguienteDato("-optimize", true);

						}

						comand[4] = "--colors";

						comand[5] = numeroColores;

						if (!verSiguienteDato("-lossy", false).isEmpty()) {

							comand[6] = "--lossy=" + verSiguienteDato("-lossy", true);

						}

						else {

							comand[6] = "--lossy=0";

						}

						comand[7] = "--dither";

						comand[8] = "-o";

						comand[9] = command.getLast();

						ProcessBuilder processBuilder = new ProcessBuilder(comand);

						Process process = processBuilder.start();

						try (BufferedReader reader = new BufferedReader(
								new InputStreamReader(process.getInputStream()));
								BufferedReader errorReader = new BufferedReader(
										new InputStreamReader(process.getErrorStream()))) {

							String line;

							while ((line = reader.readLine()) != null) {

								System.out.println(line);

							}

							while ((line = errorReader.readLine()) != null) {

								System.err.println(line);

							}

						}

					}

					if (openFile && !salida.isEmpty()) {

						abrirCarpeta("file://" + salida);

					}

				}

			}

			catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	private void help() {

	}

}
