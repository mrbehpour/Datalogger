package usb;

import java.io.IOException;

import ir.saa.android.datalogger.G;
import socket.structs.DataRef;
import socket.structs.MyPacket;

public class UsbController {
    public static String CallMethod(byte command,String data) throws IOException {
        String strJson = null;
        byte[] byteSerial = data.getBytes();
        MyPacket packet = new MyPacket();
        packet.cmd = command;
        packet.len = byteSerial.length;
        G.dos.write(MyPacket.toBytes(packet));
        G.dos.flush();
        G.dos.write(byteSerial);
        byte[] recvBytesPacket = new byte[G.sizeOfPacket];
        int countRead = G.bis.read(recvBytesPacket, 0, recvBytesPacket.length);
        if(countRead == G.sizeOfPacket){
            MyPacket recvPacket = MyPacket.toMyPacket(recvBytesPacket);
            DataRef d = new DataRef(recvPacket.len);
            int countRead2 = G.ReadAll(d, recvPacket.len);
            if(countRead2 == recvPacket.len){
                strJson = new String(d.data);
            }
        }
        return strJson;
    }
}
