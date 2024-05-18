class JavaConvertor extends Converter {

    // Help: See options help on
    // https://github.com/samuelroland/plantuml-parser/blob/main/plantuml-parser-cli/README.md
    private static final String[] args = new String[] {
            "java", "-jar", "/cli/plantuml-parser-cli.jar",
            "-f", Converter.FOLDER_INSERT,
            "-o", Converter.FILE_INSERT,
            "-fdef", "-fpri", "-fpro", "-fpub", // fields (attributes): package, private, protected, public
            "-mdef", "-mpri", "-mpro", "-mpub", // methods: same
            "-sdctr" }; // TODO: what's this ??

    JavaConvertor() {
        super("java");
    }

    @Override
    public String[] args() {
        return args;
    }
}