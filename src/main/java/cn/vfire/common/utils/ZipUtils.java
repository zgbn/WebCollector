package cn.vfire.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 对文件的压缩和解压缩工具类
 * 
 * @author ChenGang
 *
 */
public class ZipUtils {

	private final static int BUFFER = 1024;// 缓存大小

	private final static ThreadLocal<Long> CountLocal = new ThreadLocal<Long>() {

		@Override
		protected Long initialValue() {
			return 0L;
		}
	};


	/**
	 * 给定根目录，返回另一个文件名的相对路径.
	 * 
	 * @param baseDir
	 *            java.lang.String 根目录
	 * @param realFileName
	 *            java.io.File 实际的文件名
	 * @return 相对文件名
	 */
	public static String getAbsFileName(String baseDir, File realFileName) {
		File real = realFileName;
		File base = new File(baseDir);
		StringBuffer ret = new StringBuffer(real.getName());
		while (true) {
			real = real.getParentFile();
			if (real == null) break;
			if (real.equals(base)) break;
			else ret.insert(0, "/").insert(0, real.getName());
		}
		return ret.toString();
	}


	/**
	 * 给定一个实际存在
	 * 
	 * @param baseDir
	 *            File 指定根目录
	 * @param absFileName
	 *            String 文件的相对路径名
	 * @return
	 * @throws RuntimeException
	 *             当指定的根目录不是一个目录的时候抛出异常。
	 */
	public static File getRealFileName(File baseDir, String absFileName) {

		if (baseDir.isFile()) { throw new RuntimeException(
				String.format("Parameter File it must be a directory.%s", baseDir.getPath())); }

		String retPath = String.format("%s/%s", baseDir.getPath(), absFileName);

		retPath = retPath.replaceAll("\\\\", "/").replaceAll("/+", "/");

		File ret = new File(retPath);

		ret.getParentFile().mkdirs();

		return ret;

	}


	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 * @see #getRealFileName(File, String)
	 */
	@Deprecated
	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				ret = new File(ret, dirs[i]);
			}
			if (!ret.exists()) ret.mkdirs();
			ret = new File(ret, dirs[dirs.length - 1]);
			return ret;
		}
		return ret;
	}


	/**
	 * 取得指定目录下的所有文件列表，包括子目录.
	 * 
	 * @param baseDir
	 *            File 指定的目录
	 * @return 包含java.io.File的List
	 * @throws RuntimeException
	 *             当指定更目录不存在或者是一个文件的时候抛出异常。
	 */
	public static List<File> getSubFiles(File baseDir, int depth) {

		List<File> ret = new ArrayList<File>();

		if (depth == 0) {
			return ret;
		} else if (depth < 0) {
			depth = -1;
		}

		if (baseDir.exists() == false || baseDir.isFile()) { throw new RuntimeException(
				String.format("Parameter File it must be a directory.%s", baseDir.getPath())); }

		File[] tmp = baseDir.listFiles();

		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].isFile()) ret.add(tmp[i]);
			if (tmp[i].isDirectory()) ret.addAll(getSubFiles(tmp[i], depth - 1));
		}

		return ret;
	}


	public static List<File> getSubFiles(File baseDir) {
		return getSubFiles(baseDir, -1);
	}


	/**
	 * Member cache Gzip文件压缩处理
	 * 
	 * @param val
	 *            byte[] 数据二进制字节
	 * @return byte[] Gzip压缩后的数据二进制字节
	 * @throws IOException
	 */
	public static byte[] gzip(byte[] val) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(val.length);
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(bos);
			gos.write(val, 0, val.length);
			gos.finish();
			gos.flush();
			bos.flush();
			val = bos.toByteArray();
		} finally {
			try {
				if (gos != null) gos.close();
			} finally {
				if (bos != null) bos.close();
			}
		}
		return val;
	}


	/**
	 * 对指定目录下的所有文件进行Gzip压缩到目标根目录
	 * 
	 * @param baseDir
	 *            String 指定根目录
	 * @param targetDir
	 *            String 指定Gzip压缩有根目录
	 * @throws IOException
	 */
	public static Long gzipAllFiles(String baseDir, String targetDir) throws IOException {

		File base = new File(baseDir);

		File target = new File(targetDir);

		if (base.exists() == false || base.isDirectory() == false) { throw new RuntimeException(
				String.format("Parameter baseDir File it must be a directory.%s", base.getPath())); }

		File[] list = base.listFiles();

		for (int i = 0; i < list.length; i++) {

			File file = list[i];

			String _targetDir = file.getPath().replace(base.getPath(), target.getPath()) + ".gzip";

			if (file.isFile()) {

				System.out.println("Gzip file end num " + CountLocal.get());

				if (new File(_targetDir).exists()) {
					continue;
				}

				gzipFile(file.getPath(), _targetDir);

				CountLocal.set(CountLocal.get() + 1L);

			}

			if (file.isDirectory()) {

				gzipAllFiles(file.getPath(), _targetDir);

			}

		}

		return CountLocal.get();

	}


	/**
	 * 对文件进行Gzip压缩到目标文件
	 * 
	 * @param source
	 *            String 被压缩的源文件
	 * @param target
	 *            String Gzip压缩后的目标文件
	 * @throws IOException
	 */
	public static void gzipFile(String source, String target) throws IOException {

		File targetFile = new File(target);

		if (targetFile.exists() == false) {
			targetFile.getParentFile().mkdirs();
			targetFile.createNewFile();
		}

		FileInputStream fin = null;
		FileOutputStream fout = null;
		GZIPOutputStream gzout = null;
		try {
			fin = new FileInputStream(source);
			fout = new FileOutputStream(target);
			gzout = new GZIPOutputStream(fout);
			byte[] buf = new byte[1024];
			int num;
			while ((num = fin.read(buf)) != -1) {
				gzout.write(buf, 0, num);
			}
		} finally {
			try {
				if (gzout != null) gzout.close();
			} finally {
				try {
					if (fout != null) fout.close();
				} finally {
					if (fin != null) fin.close();
				}

			}
		}
	}


	/**
	 * 对文件进行Gzip压缩到目标文件
	 * 
	 * @param source
	 *            String 被压缩的源文件
	 * @return
	 * @throws IOException
	 */
	public static File gzipFile(String source) throws IOException {

		final String target = String.format("%s.gzip", source);

		gzipFile(source, target);

		File gzip = new File(target);

		if (gzip.exists() && gzip.isFile()) { return gzip; }

		return null;

	}


	/**
	 * Member cache 文件解压处理
	 * 
	 * @param buf
	 * @return
	 * @throws IOException
	 */
	public static byte[] unGzip(byte[] buf) throws IOException {
		GZIPInputStream gzi = null;
		ByteArrayOutputStream bos = null;
		try {
			gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
			bos = new ByteArrayOutputStream(buf.length);
			int count = 0;
			byte[] tmp = new byte[2048];
			while ((count = gzi.read(tmp)) != -1) {
				bos.write(tmp, 0, count);
			}
			buf = bos.toByteArray();
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
			} finally {
				if (gzi != null) gzi.close();
			}
		}
		return buf;
	}


	/**
	 * 解压Gzip文件到目标文件
	 * 
	 * @param source
	 *            String Gzip压缩的源文件
	 * @param target
	 *            String Gzip解压后的目标文件
	 * @throws IOException
	 */
	public static void unGzipFile(String source, String target) throws IOException {
		FileInputStream fin = null;
		GZIPInputStream gzin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(source);
			gzin = new GZIPInputStream(fin);
			fout = new FileOutputStream(target);
			byte[] buf = new byte[1024];
			int num;
			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, num);
			}
		} finally {
			try {
				if (fout != null) fout.close();
			} finally {
				try {
					if (gzin != null) gzin.close();
				} finally {
					if (fin != null) fin.close();
				}

			}

		}
	}


	/**
	 * 解压缩功能. 将zipFileName文件解压到zipDir目录下.
	 * 
	 * @param zipFileName
	 * @param zipDir
	 * @throws Exception
	 */
	public static void unZipFile(String zipFileName, String zipDir) throws Exception {

		ZipFile zfile = new ZipFile(zipFileName);
		Enumeration<?> zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[BUFFER];
		try {
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory()) {
					File f = new File(zipDir + ze.getName());
					f.mkdir();
					continue;
				}
				OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(zipDir, ze.getName())));
				InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
				int readLen = 0;
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					os.write(buf, 0, readLen);
				}
				is.close();
				os.close();
			}
		} finally {
			zfile.close();
		}

	}


	/**
	 * zip压缩功能. 压缩baseDir(文件夹目录)下所有文件，包括子目录
	 * 
	 * @param baseDir
	 *            String 待压缩的文件或目录
	 * @param fileName
	 *            String 压缩有的目标文件
	 * @throws Exception
	 */
	public static void zipFile(String baseDir, String fileName) throws Exception {

		List<File> fileList = getSubFiles(new File(baseDir));
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
		ZipEntry ze = null;
		byte[] buf = new byte[BUFFER];
		int readLen = 0;
		try {
			for (int i = 0; i < fileList.size(); i++) {
				File f = (File) fileList.get(i);
				ze = new ZipEntry(getAbsFileName(baseDir, f));
				ze.setSize(f.length());
				ze.setTime(f.lastModified());
				zos.putNextEntry(ze);
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				while ((readLen = is.read(buf, 0, BUFFER)) != -1) {
					zos.write(buf, 0, readLen);
				}
				is.close();
			}
		} finally {
			zos.close();
		}

	}


	public static File zipFile(String baseDir) throws Exception {

		final String fileName = String.format("%s.zip", baseDir);

		zipFile(baseDir, fileName);

		File zip = new File(fileName);

		if (zip.exists() && zip.isFile()) { return zip; }

		return null;

	}


	private ZipUtils() {
	}

}
