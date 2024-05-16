import java.io.*;
import java.nio.charset.StandardCharsets;

class Util {

	private Util() {
	}

	public static String readEntireStream(InputStream stream) {
		String text = "";
		try (var reader = new BufferedReader(
				new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				text += line + "\n";
			}
		} catch (Exception e) {
			System.out.println("Error reading stream: " + e);
		}

		return text;
	}

	public static String readEntireFile(String path) {
		try (var stream = new FileInputStream(path)) {
			return Util.readEntireStream(stream);
		} catch (Exception e) {
			CLI.fail("Failed to read file " + path);
			return null;
		}
	}

}

abstract class Convertor {
	private String lang;

	// Constants to indicate where to replace the output file and scan folder
	public static final String FILE_INSERT = "{FILE}";
	public static final String FOLDER_INSERT = "{FOLDER}";

	public Convertor(String lang) {
		this.lang = lang;
	}

	abstract String[] args();

	public String lang() {
		return lang;
	}

	public String[] getTransformedArgs(String path, String filename) {
		var args = args();
		for (var arg : args) {
			arg.replace(FILE_INSERT, filename);
			arg.replace(FOLDER_INSERT, path);
		}
		return args;
	}

	// Run convertion via the given external CLI and args
	public boolean convert(String path, String filename) {
		System.out.println("Running convertion in lang " + lang);

		try {
			var process = new ProcessBuilder(getTransformedArgs(path, filename)).start();
			String output = Util.readEntireStream(process.getInputStream());
			System.out.println("OUTPUT:\n" + output);
			return true;
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return false;
		}

	}
}

class JavaConvertor extends Convertor {
	private static final String[] args = new String[] {
			"java", "-jar", "/cli/plantuml-parser-cli.jar",
			"-f", Convertor.FILE_INSERT,
			"-fdef", "-fpri", "-fpro",
			"-fpub", "-mdef", "-mpri", "-mpro",
			"-mpub",
			"-sdctr" };

	JavaConvertor() {
		super("java");
	}

	@Override
	public String[] args() {
		return args;
	}
}

class CppConvertor extends Convertor {
	private static final String[] args = new String[] { "hpp2plantuml",
			"-i",
			Convertor.FOLDER_INSERT + "/*.h",
			"-i",
			Convertor.FOLDER_INSERT + "/**/*.h",
			"-i",
			Convertor.FOLDER_INSERT + "/*.hpp",
			"-i",
			Convertor.FOLDER_INSERT + "/**/*.hpp",
			"-i",
			Convertor.FOLDER_INSERT + "/*.cpp",
			"-i",
			Convertor.FOLDER_INSERT + "/**/*.cpp",
			"-o",
			Convertor.FILE_INSERT };

	CppConvertor() {
		super("cpp");
	}

	@Override
	public String[] args() {
		return args;
	}
}

public class CLI {
	static final String HELP = """
			CTP - Code To PlantUML

			Usage: ctp [lang] [path] [output]

			Available langs: java | cpp
			Path: a path to a folder inside the current directory
			Output: the name of resulting diagram file (generally in .puml)

			Examples:
			ctp cpp src diagram.puml
			ctp cpp . classdiagram.plantuml
			ctp java app/src/main/org/example/stack stack.puml
				""";

	public static void main(String args[]) {
		System.out.println(HELP);

		// Available convertors for various languages
		Convertor[] convertors = new Convertor[] { new JavaConvertor(), new CppConvertor() };

		if (args.length == 0) {
			return;
		}
		if (args.length < 3) {
			fail("expected arguments number to be 3 !");
		}

		// Checking path is a valid directory
		String path = args[1];
		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) {
			fail("Unknown path or is not a directory: " + path);
		}

		// Checking path is a valid directory
		String output = args[2];
		String tmpOutput = output + ".tmp";

		// Checking if lang is supported
		Convertor chosenConvertor = null;
		String givenLang = args[0];
		for (var c : convertors) {
			if (c.lang().equals(givenLang)) {
				chosenConvertor = c;
				break;
			}
		}
		if (chosenConvertor == null)
			fail("unsupported language `" + givenLang + "`");

		// All args are valid, let's run external CLIs with params
		var conversionResult = chosenConvertor.convert(path, tmpOutput);

		if (conversionResult == false)
			fail("An error happened during conversion, the program has stopped");

		// Run PostMix

	}

	public static void fail(String error) {
		System.out.println("\nError: " + error);
		System.exit(1);
	}
}
