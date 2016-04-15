import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class OpenDesktopApplicationApplet extends javax.swing.JApplet {

	private static final long serialVersionUID = -3699522086924040967L;

	/**
	 * Applet Initialization
	 */
	public void init() {    	

		String APPLICATION_NAME = this.getParameter("app");
		String FILE_URL = this.getParameter("fileURL");
		String TMP_DIRECTORY = this.getParameter("tmpDir");

		String USER = "Administrator";
		String PASSWORD = "Administrator";
		
//		APPLICATION_NAME = "C:\\Program Files\\Autodesk\\AutoCAD 2014\\acad.exe";
//		TMP_DIRECTORY = "C:\\Temp";
//		FILE_URL = 
		
//		String PATH_TO_SAVE_FILE = TMP_DIRECTORY + "\\" + UUID.randomUUID() + ".dwg";
		
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile(UUID.randomUUID().toString(), ".dwg");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String PATH_TO_SAVE_FILE = tmpFile.getAbsolutePath();

		// FILE_URL = "http://localhost:8080/nuxeo/restAPI/default/c8b08e36-6ef1-40c9-9c39-a58c8c3470b6/downloadFileAthento";

		// We download file with authentication
		downloadFileWithAuthentication(FILE_URL, USER, PASSWORD, PATH_TO_SAVE_FILE);

		StringBuffer COMMAND = new StringBuffer();
		COMMAND.append("\"" + APPLICATION_NAME + "\"");
		COMMAND.append(" ");
		COMMAND.append("\"" + PATH_TO_SAVE_FILE + "\"");

		System.out.println(COMMAND);
		
		try {
		    File file = new File(PATH_TO_SAVE_FILE);
		    Desktop.getDesktop().open(file);
		} catch(Exception e) {
		    e.printStackTrace();
		}
		
//		try {
//			Process p = Runtime.getRuntime().exec(COMMAND.toString());            
//		} catch (IOException e) {
//			System.out.println("Could not open desktop application '" + APPLICATION_NAME + "'");
//		}
	}

	/**
	 * Downloads file with authentication from Nuxeo
	 * @param webPage
	 * @param name
	 * @param password
	 * @param filePath
	 */
	private static void downloadFileWithAuthentication(String webPage,
			String name, String password, String filePath) {
		try {
			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
//			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
//			String authStringEnc = new String(authEncBytes);
			String authStringEnc = encode(authString);
			System.out.println("Base64 encoded auth string: [" + authStringEnc + "]");

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

			InputStream inputStream = urlConnection.getInputStream();

			OutputStream outputStream = null;

			// write the inputStream to a FileOutputStream
			outputStream = 
					new FileOutputStream(new File(filePath));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.close();

			System.out.println("Done!");

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Encodes in Base64
	 * @param string
	 * @return
	 */
	public static String encode(String string) { 

		String encoded = ""; 
		byte[] stringArray; 
		try { 
			stringArray = string.getBytes("UTF-8"); // utiliza la cadena de código correcta! 
		} catch (Exception ignored) { 
			stringArray = string.getBytes(); //utiliza locale como default en lugar de croak 
		} 
		// determina cuantos bytes de padding se deben agregar a la salida 
		int paddingCount = (3 - (stringArray.length % 3)) % 3; 
		// agrega los padding necesarios a la entrada 
		stringArray = zeroPad(stringArray.length + paddingCount, stringArray); 
		// procesa 3 bytes a la vez, produciendo en serie 4 salidas de bytes 
		// preocúpate acerca de las inserciones CRLF después 
		for (int i = 0; i < stringArray.length; i += 3) { 
			int j = ((stringArray[i] & 0xff) << 16) + 
					((stringArray[i + 1] & 0xff) << 8) +  
					(stringArray[i + 2] & 0xff); 
			encoded = encoded + base64code.charAt((j >> 18) & 0x3f) + 
					base64code.charAt((j >> 12) & 0x3f) + 
					base64code.charAt((j >> 6) & 0x3f) + 
					base64code.charAt(j & 0x3f); 
		} 
		// reemplaza los padding codificados en null con "=" 
		return splitLines(encoded.substring(0, encoded.length() - 
				paddingCount) + "==".substring(0, paddingCount)); 

	} 
	
	private static final String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" 
			+ "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "+/"; 

	private static final int splitLinesAt = 76; 

	public static byte[] zeroPad(int length, byte[] bytes) { 
		byte[] padded = new byte[length]; // initialized to zero by JVM 
		System.arraycopy(bytes, 0, padded, 0, bytes.length); 
		return padded; 
	} 
	
	public static String splitLines(String string) { 

		String lines = ""; 
		for (int i = 0; i < string.length(); i += splitLinesAt) { 

			lines += string.substring(i, Math.min(string.length(), i + splitLinesAt)); 
//			lines += "\r\n"; 

		} 
		return lines; 

	} 
}
