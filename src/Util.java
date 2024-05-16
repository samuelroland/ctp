import java.io.*;
import java.nio.charset.StandardCharsets;

class Util {
    private static final String HELP = """
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

    public static void printHelp() {
        System.out.println(HELP);
    }

}