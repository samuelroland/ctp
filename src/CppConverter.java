class CppConvertor extends Convertor {
    private static final String[] args = new String[] {
            "hpp2plantuml",
            "-i", Convertor.FOLDER_INSERT + "/*.h",
            "-i", Convertor.FOLDER_INSERT + "/**/*.h",
            "-i", Convertor.FOLDER_INSERT + "/*.hpp",
            "-i", Convertor.FOLDER_INSERT + "/**/*.hpp",
            "-i", Convertor.FOLDER_INSERT + "/*.cpp",
            "-i", Convertor.FOLDER_INSERT + "/**/*.cpp",
            "-o", Convertor.FILE_INSERT };

    CppConvertor() {
        super("cpp");
    }

    @Override
    public String[] args() {
        return args;
    }
}
