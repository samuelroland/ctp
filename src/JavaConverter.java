
class JavaConvertor extends Converter {
    private static final String[] args = new String[] {
            "java", "-jar", "/cli/plantuml-parser-cli.jar",
            "-f", Converter.FILE_INSERT,
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