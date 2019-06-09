package com.utils;



import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceLoader
{

    public static String loadFileAsString(String filePath) throws Exception {
        return loadFileAsString(new File(filePath));
    }

	public static String loadFileAsString(File file) throws Exception {
		StringBuilder contents = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		String currentLine;
		while ((currentLine = bufferedReader.readLine()) != null){
			contents.append(currentLine).append("\n");
		}

		bufferedReader.close();

		return contents.toString();
	}

	public static List<String> readAllLines(String filepath) throws IOException {
		List<String> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(ResourceLoader.class.getClass().getResourceAsStream(filepath)))) {
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		}
		return list;
	}

	public static int getGLSLConstIntValue(String constName, String shaderCode) {
		Pattern pattern = Pattern.compile(String.format("const\\s+int\\s+%s\\s*=\\s*\\d+\\s*;", constName));
		Matcher matcher = pattern.matcher(shaderCode);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group().replaceAll("\\D+", ""));
		} else {
			return 0;
		}
	}
}
