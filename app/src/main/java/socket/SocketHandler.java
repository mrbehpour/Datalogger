package socket;

import ir.saa.android.datalogger.G;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import socket.structs.MyPacket;

import android.util.Log;

public class SocketHandler implements Runnable {

private Socket socket;
private ServerSocket server;
//private PrintWriter out;
//private BufferedReader in;


DataOutputStream dos;
private BufferedInputStream bis; 

private int socketPort;
public SocketHandler(int port) {
    socketPort = port;
}

@Override
public void run() {


    // TODO Auto-generated method stub
    try {
        Log.i("jjj", "Starting socket server ");

        server = new ServerSocket(6000);
        socket = server.accept();

        Log.i("jjj", "Client connected");

//        out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true); 
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bis = new BufferedInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        
        byte[] recvBytes = new byte[G.sizeOfPacket]; 
        
        int countRead = bis.read(recvBytes, 0, recvBytes.length);
        
//        while ((inputText = in.readLine()) != null) {
//            Log.i("jjj", "Received:" + inputText);
//            out.println("you wrote:" + inputText);
//        }
        
        MyPacket packet = MyPacket.toMyPacket(recvBytes);

        Log.i("jjj", String.format("cmd = %s --- len = %s --- countRead = %d", String.valueOf(packet.cmd), String.valueOf(packet.len),countRead));

//        MyPacket packet2 = new MyPacket();
//        packet2.cmd = 4;
//        packet2.len = 54321;
//        
//        dos.write(MyPacket.toBytes(packet2));
        //dos.flush();
        
        
    } catch (SocketException se) {
        Log.i("jjj", se.getMessage());
    } catch (IOException ioe) {
        Log.i("jjj", ioe.getMessage());

    }
}
}