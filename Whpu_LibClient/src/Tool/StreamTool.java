package Tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
	/**

	　　* 从输入流中获取数据

	　　* @param inputStream　输入流

	　　* @return 字节数组

	　　* @throws Exception

	　　*/

	public static byte[] readInputStream(InputStream inputStream) throws Exception{
		//实例化一个输出流

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		//一个1024字节的缓冲字节数组
		byte[] buffer = new byte[1024];
		int len = 0;

		//读流的基本知识

		while ((len=inputStream.read(buffer)) != -1) {
		outputStream.write(buffer, 0, len);
		}
		//用完要关，大家都懂的

		inputStream.close();

		return outputStream.toByteArray();
	}
}
