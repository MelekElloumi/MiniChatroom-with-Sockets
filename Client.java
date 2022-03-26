import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	
	public static void main (String[] args) throws IOException {
		try {
		System.out.println("Bienvenue dans ce minichat.");
		System.out.println("Veuillez d'ecrire votre id pour se connecter: ");
		
		Scanner sc = new Scanner(System.in);
		String id=sc.nextLine();
		
		//Socket sck = new Socket(args[0], Integer.parseInt(args[1]));
		Socket sck = new Socket("127.0.0.1", 80);
		
		DataInputStream din = new DataInputStream(sck.getInputStream()) ;
		DataOutputStream dout = new DataOutputStream(sck.getOutputStream());
		
		dout.writeUTF(id);
		
		Clientwrite cw=new Clientwrite(dout); //Thread pour écriture des messages
		Clientread cr=new Clientread(din,sck); //Thread pour lire les messages
		
		} catch (IOException e) {System.out.println("Unread: "+e.getMessage()); } 
			
	}
}



 class Clientread extends Thread{
	
	DataInputStream din;
	Socket sck;
	
	
	public Clientread(DataInputStream i,Socket sk){
		din=i;	
		sck=sk;
		this.start();
	}
	
	public void run(){
		while (true)  
            { 
				try{
				String message=din.readUTF();
                

                if(message.equals("Quit")) 
                { 
                    sck.close(); 
                    break; 
                } 	
				
				System.out.println(message); 				
				
			} catch (IOException e) { System.out.println("Unread: "+e.getMessage()); }
		} 
		try{
			din.close();
		} catch (IOException e) {}
	}
}





 class Clientwrite extends Thread{
	DataOutputStream dout;
	Scanner sc;
	
	public Clientwrite(DataOutputStream o){
		dout=o;
		sc=new Scanner(System.in);
		this.start();
	}
	
	public void run(){
		while (true)  
            { try{
                String message = sc.nextLine();
				dout.writeUTF(message);
				dout.flush();

                if(message.equals("Quit")) 
                { 
                    System.out.println("Vous etes deconnecte"); 
					System.out.println("Au revoir ^_^"); 
                    break; 
                } 			        	
			} catch (IOException e) { }
		}
		try{
			dout.close();
			sc.close();
		} catch (IOException e) {}
	}
}