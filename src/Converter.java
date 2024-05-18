import java.util.ArrayList;

abstract class Converter {
    private String lang;

    // Constants to indicate where to replace the output file and scan folder
    public static final String FILE_INSERT = "{FILE}";
    public static final String FOLDER_INSERT = "{FOLDER}";

    private static final String defaultStyle = """
            hide empty members
            hide circle
            skinparam classAttributeIconSize 0
            """;

    public Converter(String lang) {
        this.lang = lang;
    }

    // This will be defined by concrete classes
    abstract public String[] args();

    // This can be extended by concrete classes for specific default settings
    public String defaultPumlStyle() {
        return defaultStyle;
    }

    public String lang() {
        return lang;
    }

    // Replace FILE_INSERT and FOLDER_INSERT in all args
    public String[] getTransformedArgs(String path, String filename) {
        var args = args();
        ArrayList<String> newArgs = new ArrayList<>(args.length);
        for (var arg : args) {
            String s = arg.replace(FILE_INSERT, filename);
            s = s.replace(FOLDER_INSERT, path);
            newArgs.add(s);
        }

        // https://stackoverflow.com/questions/9572795/convert-list-to-array-in-java
        return newArgs.toArray(new String[0]);
    }

    // Run convertion via the external CLI
    public boolean convert(String path, String outfile) {
        System.out.println("> Running convertion in lang " + lang);

        try {
            var process = new ProcessBuilder(getTransformedArgs(path, outfile + Util.TMP_EXT)).start();
            process.waitFor();
            System.out.println(Util.readEntireStream(process.getErrorStream()));
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }
}
