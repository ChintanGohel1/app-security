package com.test.util;

import java.io.File;
import java.util.function.Function;

/**
 * @author Vinit Solanki
 *
 */
public class FileUtil {
	public static Function<String, Boolean> purgeFolder() {
		return (folderName) -> {
			purgeFolder(new File(folderName), true);

			return true;
		};
	}

	public static Function<String, Boolean> emptyFolder() {
		return (folderName) -> {
			purgeFolder(new File(folderName), false);

			return true;
		};
	}

	private static void purgeFolder(File file, boolean delete) {

		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();

				for (File f : files) {
					purgeFolder(f, true);
				}
			}
			if (delete) {
				file.delete();
			}
		}
	}
}
