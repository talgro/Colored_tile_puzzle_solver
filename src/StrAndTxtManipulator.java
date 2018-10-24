import java.io.BufferedReader;
import java.util.LinkedList;

public class StrAndTxtManipulator {
	
	public static String[] splitAndLoseSpaces(String line, String character) {
		String ans[] = line.split(character);
		for (int str = 0; str < ans.length; str++) {
			String temp = ans[str].replace(" ", "");
			ans[str] = temp;
		}
		return ans;
	}
	
	public static LinkedList<String> readTextUntill( BufferedReader reader, String endLine) throws Exception {
		LinkedList<String> ans = new LinkedList<>();
		String currentLine = reader.readLine();
		while (!currentLine.equals(endLine)) {
			ans.add(currentLine);
		}
		return ans;
	}
}
