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
