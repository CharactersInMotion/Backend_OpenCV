package dev.upcraft.charactersinmotion.main;

import dev.upcraft.charactersinmotion.CharactersInMotion;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        // parser.acceptsAll(Arrays.asList("a", "argument"), "some argument").withOptionalArg();
        parser.acceptsAll(Arrays.asList("help", "?"), "show this screen").forHelp();

        OptionSet options = parser.parse(args);
        if (options.has("help")) {
            parser.printHelpOn(System.out);
            return;
        }
        List<?> nonOptions = options.nonOptionArguments();
        if (!nonOptions.isEmpty()) {
            CharactersInMotion.getLogger().info("Ignored command line options: {}", nonOptions.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        }
        // TODO parse CLI arguments here

        CharactersInMotion.getLogger().info("{} {}", CharactersInMotion.APP_NAME, CharactersInMotion.APP_VERSION);
        CharactersInMotion.getLogger().info("Loading...");
        try (CharactersInMotion application = new CharactersInMotion()) {
            CharactersInMotion.getLogger().info("Loaded.");
            application.start();
        } catch (Exception e) {
            CharactersInMotion.getLogger().catching(e);
        }
        CharactersInMotion.getLogger().info("Stopped.");
    }
}
