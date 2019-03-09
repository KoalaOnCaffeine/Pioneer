package org.pioneermc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import simplenet.Server;

public final class Pioneer {
    public static void main(String[] args){
        final Pioneer pioneer = new Pioneer();
        final JsonObject properties = pioneer.getProperties();
        if (properties == null){
            return;
        }
        final String host = properties.get("host").getAsString();
        final int port = properties.get("port").getAsInt();
        pioneer.run(host, port);
    }
    
    private JsonObject getProperties() {
        final JsonObject properties;
        final Path propertiesPath = Path.of("properties.json");
        if (!propertiesPath.toFile().exists()) {
            try (final InputStream stream = Pioneer.class.getResourceAsStream("/properties.json")) {
                Files.copy(stream, propertiesPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (final Reader fileReader = new FileReader(propertiesPath.toFile())) {
            final JsonParser parser = new JsonParser();
            properties = (JsonObject) parser.parse(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }
    
    private void run(String host, int port){
        final Server server = new Server();
        server.onConnect(client -> client.readByteAlways(packetID -> {
            switch (packetID){
                case 0x00:
                    System.out.println("Server List Ping");
                case 0x01:
                    System.out.println("Ping");
            }
        }));
        server.bind(host, port);
    }
}
