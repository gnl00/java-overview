package net.udp;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/25
 */
public class UDPClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(1000);
        ds.connect(InetAddress.getByName("localhost"), 9999);

        // send
        byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(data, data.length);
        ds.send(packet);

        // receive
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        String response = new String(packet.getData(), packet.getOffset(), packet.getLength());
        System.out.println("[response]: " + response);
        ds.disconnect();
    }
}
