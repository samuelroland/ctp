// Script used to manage temporary file with static section

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class PostMix {
    static final String STATIC_START = "' STATIC";
    static final String STATIC_END = "' ENDSTATIC";
    static final String STATIC_REMOVE_KW = "' REMOVE";

    public static void print(Object msg) {
        System.out.println(msg);
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.println("No file provided as first argument");
            System.exit(1);
        }
        final String FILE = args[0];
        // Take the content out of the temp file containing only dynamic content
        final String FILETMP = FILE + ".tmp";
        String tmp = readEntireFile(FILETMP);
        if (tmp.trim().isEmpty()) {
            System.err.println("Error: File " + FILETMP + " not found");
            System.exit(1);
        }

        print(">> Starting post generation steps");

        // Try reading an existing file to extract its static section before erasing
        // or just use the default one if it doesn't exist
        String text = readEntireFile(FILE);
        if (text.trim().isEmpty() || !doesContainStaticSection(text)) {
            print("Adding default static section");
            text = pushStaticSection(tmp, defaultStaticSection);
        } else {
            print(">>> Reusing existing static section");
            String staticSection = extractStaticSection(text);
            tmp = removePatterns(tmp, staticSection);
            text = pushStaticSection(tmp, staticSection);
        }

        // Finally write the mixed text to final file (without .tmp)
        writeEntireFile(FILE, text);
    }

    public static String readEntireFile(String path) {
        String text = "";
        try (var reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                text += line + "\n";
            }
        } catch (Exception e) {
        }

        return text;
    }

    public static void writeEntireFile(String path, String content) {
        String text = "";
        try (var writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {

            writer.write(content);
            writer.flush();
        } catch (Exception e) {
        }
    }

    private static String defaultStaticSection = "' STATIC\n" +
            "' Style\n" +
            "\n" +
            "' Missing associations\n" +
            "\n" +
            "' Post operations\n" +
            "' REMOVE\n" +
            "\n" +
            "' ENDSTATIC\n";

    public static String pushStaticSection(String dynamicContent, String staticSection) {
        final String startMarker = "@startuml\n";
        if (dynamicContent.indexOf(startMarker) == -1) {
            System.err
                    .println("Error: temp diagram file has no @startuml directive... Failed to insert static section.");
            System.exit(2);
        }
        return startMarker + "\n" + staticSection + "\n\n" + dynamicContent.substring(startMarker.length());
    }

    public static boolean doesContainStaticSection(String content) {
        int startPos = content.indexOf(STATIC_START);
        int endPos = content.indexOf(STATIC_END, startPos);
        return startPos != -1 && endPos != -1;
    }

    public static String extractStaticSection(String content) {
        if (!doesContainStaticSection(content))
            return "";
        return content.substring(content.indexOf(STATIC_START), content.indexOf(STATIC_END) + STATIC_END.length());
    }

    public static String removePatterns(String tmp, String staticSection) {
        print(">> Removing following patterns");
        String[] lines = staticSection.substring(0, staticSection.indexOf(STATIC_END)).split("\n");
        boolean foundRemoveKeyword = false;

        for (String line : lines) {
            // Search for STATIC_REMOVE_KW
            if (!foundRemoveKeyword) {
                if (line.trim().equals(STATIC_REMOVE_KW)) {
                    foundRemoveKeyword = true;
                }
                continue;
            }

            // If this is a commented line, apply regex replace all on tmp
            if (line.startsWith("' ")) {
                String regex = line.substring(2);
                print(">>> " + regex);
                tmp = tmp.replaceAll(regex, "");
            }
        }

        return tmp;
    }
}
