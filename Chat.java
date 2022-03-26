import java.net.*;
import java.io.*;
import java.util.*;

public class Chat extends Thread {
	
	DataInputStream din;
	DataOutputStream dout;
	Socket sock;
	ChatServer serv;
	String id;
	
	public String get_id(){
		return id;
	}
	
	public Chat (Socket a_client_socket, ChatServer ss){
		
		try{
			serv=ss;
			sock= a_client_socket;
			din = new DataInputStream(sock.getInputStream());
			dout= new DataOutputStream(sock.getOutputStream() );
			this.start();
		} catch(IOException e){
			System.out.println("Chat: "+e.getMessage());
		}
		
	}
	
	public void run(){
		
		try{
			id=din.readUTF();
			System.out.println(id+" s'est connecte au serveur : " + sock);
			
			envoyer("Bienvenue "+id+" vous etes bien connectes ");			
			envoyer("Liste des utilisateurs connectes: "+serv.listeConnecte());
			serv.envoyertous(id+" s'est connecte.",id);
			serv.ajouter(this);
			
			while(true){
				String message=din.readUTF();

                if(message.equals("Quit")) 
                {      
					envoyer(message);
                    break; 
                } 
				
				serv.envoyertous(id+"> "+message,id); 
			}
			
		}
		catch (EOFException e){
			System.out.println("EOF: "+e.getMessage());
		}
		catch (IOException e){
			System.out.println("readline: "+e.getMessage());
		}
		finally{
			try{
				serv.envoyertous(id+" s'est deconnecte.",id);
				System.out.println(id+" s'est deconnecte du serveur");
				serv.supp(this);
				sock.close(); dout.close(); din.close();
			}
			catch (IOException e){
			System.out.println("close failed");
			}
		}		
	}
	
	public void envoyer(String mes){
		try{
			dout.writeUTF(mes);
			dout.flush();
		}catch (IOException e){
			System.out.println("sending failed"+e.getMessage());
		}
	}
}