import java.io.*;

public class CLI {

    public static void main(String args[]) {
        Util.printHelp();

        // Available convertors for various languages
        Convertor[] convertors = new Convertor[] { new JavaConvertor(), new CppConvertor() };

        if (args.length == 0) {
            return;
        }
        if (args.length < 3) {
            fail("expected arguments number to be 3 !");
        }

        // Checking path is a valid directory
        String path = args[1];
        File f = new File(path);
        if (!f.exists() || !f.isDirectory()) {
            fail("Unknown path or is not a directory: " + path);
        }

        // Checking path is a valid directory
        String output = args[2];
        String tmpOutput = output + ".tmp";

        // Checking if lang is supported
        Convertor chosenConvertor = null;
        String givenLang = args[0];
        for (var c : convertors) {
            if (c.lang().equals(givenLang)) {
                chosenConvertor = c;
                break;
            }
        }
        if (chosenConvertor == null)
            fail("unsupported language `" + givenLang + "`");

        // All args are valid, let's run external CLIs with params
        var conversionResult = chosenConvertor.convert(path, tmpOutput);

        if (conversionResult == false)
            fail("An error happened during conversion, the program has stopped");

        // Run PostMix
        System.out.println("Running PostMix");
    }

    public static void fail(String error) {
        System.out.println("\nError: " + error);
        System.exit(1);
    }
}
