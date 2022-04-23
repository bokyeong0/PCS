package vertexid.nhmarket.pcs.cmmn;

import java.io.File;

public class FileUtil {

	/**
	 * 폴더 생성
	 * 
	 * @param path
	 */
	public static void createFolder(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 폴더 삭제
	 * 
	 * @param path
	 *            삭제 경로
	 * @param ignoreName
	 *            무시 파일 이름
	 */
	public static void deleteFolder(String path, String ignoreName) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		File[] tempFile = file.listFiles();
		for (File f : tempFile) {
			if (!f.getName().equalsIgnoreCase(ignoreName)) {
				recursiveDelete(f);
			}
		}
	}

	/**
	 * 하위 디렉토리 까지 모두 삭제
	 * 
	 * @param file
	 */
	public static void recursiveDelete(File file) {
		if (!file.exists())
			return;

		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				recursiveDelete(f);
			}
		}
		file.delete();
	}
}
