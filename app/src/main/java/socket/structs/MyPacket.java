package socket.structs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

public class MyPacket {
	// CMD_GetUsers = 1;
	// CMD_GetItems = 2;
	// CMD_GetSetting = 3;
	// CMD_GetLastValues = 4;
	// CMD_SaveItemValues = 5;
	// CMD_GetApkVersion = 5;
	// CMD_GetApk = 6;
	public byte cmd;
	public int len;

	public static MyPacket toMyPacket(byte[] arr){
        MyPacket packet2 = new MyPacket();
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(arr));
		try {
			int bytesAvailable = data.available();
			if(bytesAvailable>0){
				packet2.cmd = data.readByte();
				Log.i("jjj","data = "+bytesAvailable+" , cmd = "+packet2.cmd);
				bytesAvailable = data.available();
				byte[] byteint = new byte[4];
				data.readFully(byteint);
				packet2.len = ByteBuffer.wrap(byteint).order(ByteOrder.LITTLE_ENDIAN).getInt();
//				packet3.len = data.readInt();-- This is Big-endian
				
				Log.i("jjj","data = "+bytesAvailable+" , len = "+packet2.len);
			}else{
				packet2=null;
			}
		} catch (IOException e) {
			packet2=null;
			Log.i("sockerr",e.getMessage());
		}
		return packet2;
	}

	public static byte[] toBytes(MyPacket packet){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] bytes = new byte[5];
		try {
			os.write(packet.cmd);
			os.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(packet.len).array());
			bytes = os.toByteArray();
		} catch (IOException e) {
			bytes = null;
			Log.i("sockerr",e.getMessage());
		}
		
		return bytes;
	}
}
