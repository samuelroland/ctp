class CppConvertor extends Converter {
    private static final String[] args = new String[] {
            "hpp2plantuml",
            "-i", Converter.FOLDER_INSERT + "/*.h",
            "-i", Converter.FOLDER_INSERT + "/**/*.h",
            "-i", Converter.FOLDER_INSERT + "/*.hpp",
            "-i", Converter.FOLDER_INSERT + "/**/*.hpp",
            "-i", Converter.FOLDER_INSERT + "/*.cpp",
            "-i", Converter.FOLDER_INSERT + "/**/*.cpp",
            "-o", Converter.FILE_INSERT };

    CppConvertor() {
        super("cpp");
    }

    @Override
    public String[] args() {
        return args;
    }
}
