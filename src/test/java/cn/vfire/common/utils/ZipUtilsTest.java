package cn.vfire.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class ZipUtilsTest {

	@Test
	public void getAbsFileName() throws IOException {

		File base = new File("E:\\BiqugeCrawler\\小说列表");
		File real = new File("E:\\BiqugeCrawler\\小说列表\\末日杀戮游戏\\_Novel_末日杀戮游戏.json");

		String realAbsPath = ZipUtils.getAbsFileName(base.getPath(), real);

		System.out.println(realAbsPath);

	}


	@Test
	public void getRealFileName() throws IOException {
		String baseDir = "/";
		String absFileName = "a/b/c/t/t.txt";

		File t1 = ZipUtils.getRealFileName(new File(baseDir), absFileName);
		System.out.println(t1.getPath());

		// File t = ZipUtils.getRealFileName(baseDir, absFileName);
		// System.out.println(t.getPath());

	}


	@Test
	public void getSubFiles() {
		File base = new File("E:\\BiqugeCrawler\\小说列表\\末日杀戮游戏\\");
		long t1 = System.currentTimeMillis();
		List<File> list = ZipUtils.getSubFiles(base, 1);
		System.out.println(list.size() + "    " + (System.currentTimeMillis() - t1));
	}


	@Test
	public void gzipFile() {

		String source = "E:\\BiqugeCrawler\\BiqugeCrawler.log";

		String target = String.format("%s.gzip", source);

		String untarget = String.format("%s.txt", target);

		try {

			File gzip = new File(target);

			long t1 = System.currentTimeMillis();

			ZipUtils.gzipFile(source, gzip.getPath());

			long t = System.currentTimeMillis() - t1;

			String rsinfo = String.format("Gzip压缩文件完成，状态%s，耗时%s毫秒。File：%s", String.valueOf(gzip.exists()),
					String.valueOf(t), gzip.getPath());

			System.out.println(rsinfo);

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			File ungzip = new File(untarget);

			long t1 = System.currentTimeMillis();

			ZipUtils.unGzipFile(target, untarget);

			long t = System.currentTimeMillis() - t1;

			String rsinfo = String.format("Gzip解压缩文件完成，状态%s，耗时%s毫秒。File：%s", String.valueOf(ungzip.exists()),
					String.valueOf(t), ungzip.getPath());

			System.out.println(rsinfo);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Test
	public void zipFile() {

		String source = "E:\\BiqugeCrawler\\小说列表\\萝莉掠夺之书";

		String target = String.format("%s.zip", source);

		String untarget = String.format("%s_zip", source);

		try {

			File zip = new File(target);

			long t1 = System.currentTimeMillis();

			ZipUtils.zipFile(source, target);

			long t = System.currentTimeMillis() - t1;

			String rsinfo = String.format("zip压缩文件完成，状态%s，耗时%s毫秒。File：%s", String.valueOf(zip.exists()),
					String.valueOf(t), zip.getPath());

			System.out.println(rsinfo);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			File zip = new File(untarget);

			long t1 = System.currentTimeMillis();

			ZipUtils.unZipFile(target, untarget);

			long t = System.currentTimeMillis() - t1;

			String rsinfo = String.format("zip解压缩文件完成，状态%s，耗时%s毫秒。File：%s", String.valueOf(zip.exists()),
					String.valueOf(t), zip.getPath());

			System.out.println(rsinfo);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Test
	public void gzipAllFile() {

		String baseDir = "E:\\BiqugeCrawler";

		String targetDir = "E:\\BiqugeCrawler_Gzip";

		try {

			long t1 = System.currentTimeMillis();

			long size = ZipUtils.gzipAllFiles(baseDir, targetDir);

			long t = System.currentTimeMillis() - t1;

			String rsinfo = String.format("Gzip压缩指定baseDir目录下所有文件完成，共压缩%s文件，耗时%s毫秒。", String.valueOf(size),
					String.valueOf(t));

			System.out.println(rsinfo);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
