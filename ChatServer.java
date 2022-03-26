import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
	
    Vector V;
	
	public static void main (String[] args) throws Exception {
		//int port=Integer.parseInt(args[0]);
		int port=80;
		new ChatServer(port);
	}
	
	public ChatServer(int port){
		V=new Vector();
		try{
			ServerSocket listenSocket = new ServerSocket(port);
			while(true){
				Socket clientSocket = listenSocket.accept();
				Chat c = new Chat(clientSocket,this);
			}
		} catch(IOException e){
			System.out.println("Listen socket: "+e.getMessage());
		}
	}
	
	
	synchronized public void envoyertous (String s,String id){
		for (int i=0; i<V.size(); i++){
			Chat c=(Chat)V.elementAt(i);
			if(!id.equals(c.get_id()))
				c.envoyer(s);
		}
	}
	
	synchronized public void ajouter (Chat c){
		V.add(c);
	}
	
	synchronized public void supp (Chat c){
		V.remove(c);
	}
	
	synchronized public String listeConnecte (){
		String res="";
		for (int i=0; i<V.size(); i++){
			Chat c=(Chat)V.elementAt(i);
			res+=c.get_id()+", ";
		}
		return res;
	}
}