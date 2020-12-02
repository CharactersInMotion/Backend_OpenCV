package dev.upcraft.charactersinmotion.config;

import dev.upcraft.charactersinmotion.CharactersInMotion;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public abstract class Configuration {

    protected static String readString(Properties props, String key, String defaultValue) {
        return readConfigValue(props, key, defaultValue, UnaryOperator.identity());
    }

    protected static int readInt(Properties props, String key, int defaultValue) throws NumberFormatException {
        return readConfigValue(props, key, defaultValue, Integer::parseInt);
    }

    protected static boolean readBoolean(Properties props, String key, boolean defaultValue) {
        return readConfigValue(props, key, defaultValue, Boolean::parseBoolean);
    }

    protected static long readLong(Properties props, String key, long defaultValue) throws NumberFormatException {
        return readConfigValue(props, key, defaultValue, Long::parseLong);
    }

    protected static <T> T readConfigValue(Properties props, String key, T defaultValue, Function<String, T> valueGetter) {
        if (!props.containsKey(key)) {
            props.put(key, String.valueOf(defaultValue));
            return defaultValue;
        }
        return valueGetter.apply(props.getProperty(key));
    }

    protected static <T> T load(String name, Type type, Function<Properties, T> readValues) {
        Path configPath = Paths.get(type.format(name));
        Properties defaultProps = new Properties();
        readValues.apply(defaultProps);

        Properties applicationProps = new Properties(defaultProps);
        if (Files.exists(configPath)) {
            CharactersInMotion.getLogger().trace("reading config file from {}", configPath::toAbsolutePath);
            try (Reader reader = Files.newBufferedReader(configPath)) {
                applicationProps.load(reader);
            } catch (IOException e) {
                CharactersInMotion.getLogger().error("unable to read config file", e);
            }
        } else {
            CharactersInMotion.getLogger().info("No configuration file found, creating new one at {}", configPath::toAbsolutePath);
            try (OutputStream out = Files.newOutputStream(configPath)) {
                type.write(defaultProps, "Characters In Motion Properties", out);
            } catch (IOException e) {
                CharactersInMotion.getLogger().error("Unable to write config file", e);
            }
        }

        return readValues.apply(applicationProps);
    }

    enum Type {
        PROPERTIES("properties", (props, title, stream) -> props.store(stream, title)),
        XML("xml", (props, title, stream) -> props.storeToXML(stream, title, StandardCharsets.UTF_8.name()));

        private final String extension;
        private final Serializer serializer;

        Type(String extension, Serializer serializer) {
            this.extension = extension;
            this.serializer = serializer;
        }

        public String getExtension() {
            return extension;
        }

        public String format(String fileName) {
            return String.format("%s.%s", fileName, getExtension());
        }

        void write(Properties props, String title, OutputStream stream) throws IOException {
            serializer.write(props, title, stream);
        }
    }

    @FunctionalInterface
    interface Serializer {
        void write(Properties props, String title, OutputStream stream) throws IOException;
    }
}
