package org.pioneermc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.pioneermc.networking.StatefulConnectionHandler;
import simplenet.Client;
import simplenet.Server;
import simplenet.utility.exposed.ByteConsumer;

public final class Pioneer {
    
    public static void main(String[] args) {
        final Pioneer pioneer = new Pioneer();
        final JsonObject properties;
        try {
            properties = pioneer.getProperties();
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load properties.json!", ex);
        }
        final String host = properties.get("host").getAsString();
        final int port = properties.get("port").getAsInt();
        pioneer.run(host, port);
    
    }
    
    /**
     * Load the properties object into memory
     * @return The loaded properties object
     * @throws IOException If there was any issue loading / saving the properties object
     */
    private JsonObject getProperties() throws IOException {
        final JsonObject properties;
        final Path propertiesPath = Path.of("properties.json");
        if (!propertiesPath.toFile().exists()) {
            final InputStream stream = Pioneer.class.getResourceAsStream("/properties.json");
            Files.copy(stream, propertiesPath);
            stream.close();
        }
        final Reader fileReader = new FileReader(propertiesPath.toFile());
        final JsonParser parser = new JsonParser();
        properties = (JsonObject) parser.parse(fileReader);
        return properties;
    }
    
    /**
     * Run the Pioneer Minecraft Server on the specified host and port
     * @param host The hostname or IP to bind the server to
     * @param port The port to bind the server to
     */
    private void run(String host, int port){
        final Server server = new Server();
        final StatefulConnectionHandler handler = new StatefulConnectionHandler();
        server.onConnect(client -> {
        
        });
        server.bind(host, port);
    }
}
