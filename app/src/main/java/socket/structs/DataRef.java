package socket.structs;

public class DataRef {
	public byte [] data;
	
	public DataRef(int len) {
		this.data = new byte[len];
	}
}
