// Post generation mix between static and dynamic content, taking back the previous static version

public class PostMix {
    static final String[] DEFAULT_STYLE = new String[] {
    };

    // Given an outfile (without TMP_EXT), read it, add previous or default static
    // section and save the final file (without TMP_EXT)
    public static void run(String outfile, String defaultStyle) {
        String outfiletmp = outfile + Util.TMP_EXT;
        String tmpText = Util.readEntireFile(outfiletmp);
        if (tmpText.trim().isEmpty())
            Util.fail("Error: File " + outfile + " not found or is empty but should be present.");

        Util.print("> Starting post mix steps");

        // Try reading an existing file to extract its static section before erasing
        // or just use the default one if it doesn't exist
        String text = "";
        if (Util.fileExist(outfile)) {
            text = Util.readEntireFile(outfile);
        }
        if (text.trim().isEmpty() || !doesContainStaticSection(text)) {
            Util.print(">> Adding default static section");
            text = pushStaticSection(tmpText, Util.getDefaultContentWithDefaultStyle(defaultStyle));
            // note: no need to call removePatterns() as we have no existing static section
        } else {
            Util.print(">> Reusing existing static section");
            String staticSection = extractStaticSection(text);
            tmpText = removePatterns(tmpText, staticSection);
            text = pushStaticSection(tmpText, staticSection);
        }

        // Finally write the mixed text to final file (without)
        Util.writeEntireFile(outfile, text);
        Util.deleteFile(outfiletmp);
    }

    // Push the static section (just add id after @startuml) in given PUML schema
    public static String pushStaticSection(String schema, String sectionToPush) {
        if (schema.indexOf(Util.START_MARKER_PUML) == -1)
            Util.fail("Error: temp diagram file has no @startuml directive... Failed to insert static section.");

        return Util.START_MARKER_PUML + "\n" + sectionToPush + "\n"
                + schema.substring(Util.START_MARKER_PUML.length());
    }

    // Look in given schema if already contains a static section to know if we need
    // to insert a default one
    public static boolean doesContainStaticSection(String schema) {
        int startPos = schema.indexOf(Util.STATIC_START);
        int endPos = schema.indexOf(Util.STATIC_END, startPos);
        return startPos != -1 && endPos != -1;
    }

    // Extract static section in a given schema to use it in new generation
    public static String extractStaticSection(String schema) {
        if (!doesContainStaticSection(schema))
            return "";
        return schema.substring(schema.indexOf(Util.STATIC_START),
                schema.indexOf(Util.STATIC_END) + Util.STATIC_END.length());
    }

    // Run patterns deletion (via regex given after Util.STATIC_REMOVE_KW)
    public static String removePatterns(String tmp, String staticSection) {
        Util.print(">> Removing following patterns");
        String[] lines = staticSection.substring(0, staticSection.indexOf(Util.STATIC_END)).split("\n");
        boolean foundRemoveKeyword = false;

        for (String line : lines) {
            // Search for STATIC_REMOVE_KW
            if (!foundRemoveKeyword) {
                if (line.trim().equals(Util.STATIC_REMOVE_KW)) {
                    foundRemoveKeyword = true;
                }
                continue;
            }

            // If this is a commented line, apply regex replace all on tmp
            if (line.startsWith("' ")) {
                String regex = line.substring(2);
                Util.print(">>> " + regex);
                tmp = tmp.replaceAll(regex, "");
            }
        }

        return tmp;
    }
}
