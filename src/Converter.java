import java.util.ArrayList;

abstract class Converter {
    private String lang;

    // Constants to indicate where to replace the output file and scan folder
    public static final String FILE_INSERT = "{FILE}";
    public static final String FOLDER_INSERT = "{FOLDER}";

    public Converter(String lang) {
        this.lang = lang;
    }

    abstract String[] args();

    public String lang() {
        return lang;
    }

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
