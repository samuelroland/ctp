
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