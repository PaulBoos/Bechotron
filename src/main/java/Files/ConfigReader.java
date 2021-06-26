package Files;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigReader {
	
	public static String firstLine = "";
	
	public ConfigReader() {
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."), path -> path.toFile().isFile());
			for(Path path: stream) {
				if(path.toString().endsWith(".cfg")) {
					//TODO READ CONFIG
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean readTokenFile() {
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."), path -> path.toFile().isFile());
			for(Path path: stream) {
				if(path.getFileName().toString().equals("token")) {
					List<String> lines = Files.readAllLines(path);
					firstLine = lines.get(0);
					return true;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
