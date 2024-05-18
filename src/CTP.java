import java.io.*;

public class CTP {

    public static void main(String args[]) {
        // Available convertors for various languages
        Converter[] convertors = new Converter[] { new JavaConvertor(), new CppConvertor() };

        if (args.length == 0) {
            Util.showHelp();
            return;
        }
        if (args.length < 3) {
            Util.showHelp();
            Util.fail("expected arguments number to be 3 !");
        }

        // Checking path is a valid directory
        String path = args[1];
        File f = new File(path);
        if (!f.exists() || !f.isDirectory()) {
            Util.fail("Unknown path or is not a directory: " + path);
        }

        // Checking path is a valid directory
        String outfile = args[2];
        if (!outfile.endsWith(Util.PUML_EXT)) {
            outfile += Util.PUML_EXT; // Append PUML_EXT if not present in file name
        }

        // Checking if lang is supported
        Converter chosenConvertor = null;
        String givenLang = args[0];
        for (var c : convertors) {
            if (c.lang().equals(givenLang)) {
                chosenConvertor = c;
                break;
            }
        }
        if (chosenConvertor == null)
            Util.fail("unsupported language `" + givenLang + "`");

        // All args are valid, let's run external CLIs with params
        var conversionResult = chosenConvertor.convert(path, outfile);

        if (conversionResult == false)
            Util.fail("An error happened during conversion, the program has stopped");

        // Run PostMix
        PostMix.run(outfile, chosenConvertor.defaultPumlStyle());
        Util.print("\nSuccessfully generated " + outfile + " :)");
    }
}
