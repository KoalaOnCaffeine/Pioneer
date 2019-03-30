package org.pioneermc.networking;

import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import simplenet.Client;
import simplenet.packet.Packet;
import simplenet.utility.exposed.ByteConsumer;

public class StatefulConnectionHandler {
    /**
     * Reads a variable-length 'int' from the Client
     * @param client The client to read from
     * @param callback The callback for the received VarInt
     */
    private void readVarInt(Client client, IntConsumer callback) {
        client.readByte(new ByteConsumer(){
            int count, value;
            
            @Override
            public void accept(byte data) {
                value |= ((int) data & 0x7F) << (7 * count);
                if (++count > 5) throw new RuntimeException("VarInt is too big");
                if ((data & 0x80) == 0)  callback.accept(value);
                else client.readByte(this);
            }
        });
    }
    
    /**
     * Reads a variable-length 'long' from the Client
     * @param client The client to read from
     * @param callback The callback for the received VarLong
     */
    private void readVarLong(Client client, LongConsumer callback){
        client.readByte(new ByteConsumer(){
            int count, value;
            
            @Override
            public void accept(byte data) {
                value |= ((int) data & 0x7F) << (7 * count);
                if (++count > 10) throw new RuntimeException("VarLong is too big");
                if ((data & 0x80) == 0)  callback.accept(value);
                else client.readByte(this);
            }
        });
    }
    
    /**
     * Writes a vairable-length number to the Client
     * @param client The client to write to
     * @param value The value to write
     */
    private void writeVarNum(Client client, long value){
        final Packet packet = Packet.builder();
        do {
            byte temp = (byte)(value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            packet.putByte(temp);
        } while (value != 0);
        packet.write(client);
    }
    
    /**
     * An enum representing the numerous available connection states
     */
    enum ConnectionState {
        HANDSHAKE,
        STATUS,
        LOGIN,
        PLAY
    }
}
