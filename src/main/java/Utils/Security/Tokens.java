package Utils.Security;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Tokens {
	
	public static String readToken(String tokenFile) throws TokenFileNotFoundException {
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./tokens/"), path -> path.toFile().isFile());
			for(Path path: stream) {
				if(path.getFileName().toString().equals(tokenFile + ".token")) {
					List<String> lines = Files.readAllLines(path);
					return lines.get(0);
				}
			}
		} catch(NoSuchFileException e) {
			throw new TokenFileNotFoundException();
		} catch(IOException e) {
			e.printStackTrace();
		}
		throw new TokenFileNotFoundException();
	}
	
	public static String readToken(String tokenFile, int line) throws TokenFileNotFoundException {
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./tokens/"), path -> path.toFile().isFile());
			for(Path path: stream) {
				if(path.getFileName().toString().equals(tokenFile + ".token")) {
					List<String> lines = Files.readAllLines(path);
					return lines.get(line);
				}
			}
		} catch(NoSuchFileException e) {
			throw new TokenFileNotFoundException();
		} catch(IOException e) {
			e.printStackTrace();
		}
		throw new TokenFileNotFoundException();
	}
	
	public static List<String> readTokens(String tokenFile) throws TokenFileNotFoundException {
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./tokens/"), path -> path.toFile().isFile());
			for(Path path: stream) {
				if(path.getFileName().toString().equals(tokenFile + ".token")) {
					return Files.readAllLines(path);
				}
			}
		} catch(NoSuchFileException e) {
			throw new TokenFileNotFoundException();
		} catch(IOException e) {
			e.printStackTrace();
		}
		throw new TokenFileNotFoundException();
	}
	
	public static List<String> readTokens(String tokenFile, int... lines) throws TokenFileNotFoundException {
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./tokens/"), path -> path.toFile().isFile());
			for(Path path: stream) {
				if(path.getFileName().toString().equals(tokenFile + ".token")) {
					List<String> list = Files.readAllLines(path);
					List<String> out = new ArrayList<>();
					for(int i = 0; i < lines.length; i++)
						for(int l: lines)
							if(l == i)
								out.add(list.get(i));
					return out;
				}
			}
		} catch(NoSuchFileException e) {
			throw new TokenFileNotFoundException();
		} catch(IOException e) {
			e.printStackTrace();
		}
		throw new TokenFileNotFoundException();
	}
	
	public static class TokenFileNotFoundException extends Exception {}
	
}
