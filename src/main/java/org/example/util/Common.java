package org.example.util;

import java.net.DatagramPacket;

public class Common {
    public static DatagramPacket copyDatagram(DatagramPacket src){
        int len = src.getLength();
        byte[] dataCopy = new byte[len];
        System.arraycopy(src.getData(), 0, dataCopy, 0, len);

        DatagramPacket newDatagramPacket = new DatagramPacket(dataCopy, len);
        newDatagramPacket.setPort(src.getPort());
        newDatagramPacket.setAddress(src.getAddress());

        return newDatagramPacket;
    }
}
