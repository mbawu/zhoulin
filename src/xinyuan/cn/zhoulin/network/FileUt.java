package xinyuan.cn.zhoulin.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.os.Environment;
/**
 * 
 */
public class FileUt {
	public static final String GAME_WORK_IMAGE = Environment
			.getExternalStorageDirectory() + "/kongyitang/image/work/";

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Boolean hasSD() { // 判断是否有SD�?
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	// url-->InputStream--->byte[]--->bitmap

	public static String uploadFile(String uploadUrl, byte[] bt, int i) { //
		String srcPath;
		if (i == 1) {
			srcPath = FileUt.GAME_WORK_IMAGE + System.currentTimeMillis()
					+ ".amr";
		} else {
			srcPath = FileUt.GAME_WORK_IMAGE + System.currentTimeMillis()
					+ ".jpg";
		}

		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		String result = "";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			Thread.sleep(100);
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
					+ srcPath.substring(srcPath.lastIndexOf("/") + 1)
					+ "\""
					+ end);
			dos.writeBytes(end);
			dos.write(bt, 0, bt.length);
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			result = br.readLine();
			dos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 */
	public static void saveFileSD(byte[] src, String filename)
			throws IOException { // 保存到SD�?
		ByteArrayInputStream inputStream = new ByteArrayInputStream(src);
		saveDataByWang(inputStream, filename);
	}
	/**
	 * 
	 */
	public static void saveDataByWang(InputStream is, String fileName)
			throws IOException {
		File file = new File(fileName);
		FileOutputStream os = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		os.flush();
		os.close();
	}

}