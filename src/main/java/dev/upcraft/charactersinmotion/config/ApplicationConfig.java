package dev.upcraft.charactersinmotion.config;

import dev.upcraft.charactersinmotion.CharactersInMotion;

import java.util.Properties;

public class ApplicationConfig extends Configuration {

    private static final ApplicationConfig instance = Configuration.load(CharactersInMotion.APP_NAME, Type.XML, ApplicationConfig::readValues);

    private final int commandPort;
    private final String netInterface;

    public ApplicationConfig(String netInterface, int commandPort) {
        this.netInterface = netInterface;
        this.commandPort = commandPort;
    }

    public static ApplicationConfig getInstance() {
        return instance;
    }

    private static ApplicationConfig readValues(Properties props) {
        // set default values here
        String netInterface = Configuration.readString(props, "net.interface", "[::]");
        int commandPort = Configuration.readInt(props, "net.command_port", 4000);

        return new ApplicationConfig(netInterface, commandPort);
    }

    public int getCommandPort() {
        return commandPort;
    }

    public String getNetInterface() {
        return netInterface;
    }
}
