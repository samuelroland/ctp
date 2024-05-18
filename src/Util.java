import java.io.*;
import java.nio.charset.StandardCharsets;

// Various util functions and constants
class Util {

    public static final String PUML_EXT = ".puml";
    public static final String TMP_EXT = ".tmp";
    // Help text shown on invalid args
    private static final String HELP = """
            CTP - Code To PlantUML

            Usage: ctp [lang] [path] [output]

            Languages: java | cpp
            Path: a path to a folder to scan (must be inside the current directory)
            Output: the name of output diagram file (extension .puml is added if missing)

            Examples:
            ctp cpp src diagram
            ctp cpp . classdiagram
            ctp java app/src/main/org/example/stack stack
            ctp java src/ project.puml

            Repository: https://github.com/samuelroland/ctp
            Docs: See README.md in repository
            """;

    // Delimiters for static section
    public static final String STATIC_START = "STATIC";
    public static final String STATIC_END = "ENDSTATIC";
    public static final String STATIC_REMOVE_KW = "REMOVE";
    public static final String STATIC_REPLACE_KW = "REPLACE"; // TODO: implement this
    public static final String START_MARKER_PUML = "@startuml\n";
    public static final String AUTO_GENERATED_TEXT = "Generated by CTP - https://github.com/samuelroland/ctp";

    // Default content of the static section in output file
    public static final String getDefaultContentWithDefaultStyle(String defaultStyle) {
        return "' " + STATIC_START
                + "\n' " + AUTO_GENERATED_TEXT
                + "\n' Style\n" + defaultStyle
                + "\n' Additions\n' "
                + STATIC_REMOVE_KW + "\n\n' " + STATIC_END + "\n";
    }

    private Util() {
    }

    public static void showHelp() {
        System.out.println(HELP);
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
            Util.fail("Failed to read file " + path);
            return null;
        }
    }

    // Write given content on given file path
    public static void writeEntireFile(String path, String content) {
        try (var writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {

            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            Util.fail(e.toString());
        }
    }

    public static void deleteFile(String path) {
        new File(path).delete();
    }

    public static boolean fileExist(String path) {
        return new File(path).exists();
    }

    public static void fail(String error) {
        System.err.println("\nError: " + error);
        System.exit(1);
    }

    public static void print(Object msg) {
        System.out.println(msg);
    }
}