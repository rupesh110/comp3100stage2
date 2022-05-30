import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class client {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
       
        String username = System.getProperty("user.name");
        try{
        String client = "HELO\n";
        dout.write(client.getBytes());
        dout.flush();
        String serverInfo = in.readLine();

        client = "AUTH " + username + "\n";
        dout.write(client.getBytes());
        dout.flush();
        serverInfo = in.readLine();

        client = "REDY\n";
        dout.write(client.getBytes());
        dout.flush();
        serverInfo = in.readLine();
        System.out.println("This one is job number "+serverInfo);
        String jobDescription = serverInfo;

        client = "GETS All\n";
        dout.write(client.getBytes());
        dout.flush();
        serverInfo = in.readLine();
        String [] numservers = serverInfo.split(" ");
        String numServer = numservers[1];
        int serverNumber = Integer.parseInt(numServer);
        System.out.println(serverInfo);
        System.out.println("Number of Server "+ serverNumber);

        String [] ServersData = new String [serverNumber];

        client = "OK\n";
        dout.write(client.getBytes());
        dout.flush();
       
        for(int i=0; i<serverNumber; i++){
            serverInfo = in.readLine();    
            ServersData[i] = serverInfo;    
        }                                      

        System.out.println("This is server :" +Arrays.toString(ServersData));
        String largestServer = ServersData[serverNumber-1];
        System.out.println(largestServer);
        String largestSerName = largestServer.substring(0,10);
        System.out.println(largestSerName);
      

        client = "OK\n";
        dout.write(client.getBytes());
        dout.flush();

        serverInfo = in.readLine();

        int [] maxCores = new int [ServersData.length];
        for(int i=0; i<ServersData.length; i++){
            String [] serverDataTemp = ServersData[i].split(" ");
            maxCores[i] =Integer.parseInt(serverDataTemp[4]);
        }
        int maxiCo = 0;
        for(int i=0;i<maxCores.length; i++){
            if(maxCores[i]>maxiCo){
                maxiCo = maxCores[i];
            }
        }


        System.out.println("This is a maximum core "+ maxiCo);

        
        String [] serverNumberData = new String [ServersData[0].length()];
        String foundServers = "";
        for(int i=0; i<ServersData.length; i++){
            serverNumberData = ServersData[i].split(" ");
            int serCore = Integer.parseInt(serverNumberData[4]);
            if(maxiCo == serCore){
                foundServers = serverNumberData[0];
                System.out.println("FInnally found servers "+ foundServers);
            }
        }
        
        client = "SCHD 0 "+ foundServers+" 0\n";
        dout.write(client.getBytes());
        dout.flush();

        serverInfo = in.readLine();

        while(!serverInfo.contains("NONE")){

            client = "REDY\n";
            dout.write(client.getBytes());
            dout.flush();

            serverInfo= in.readLine();
            //System.out.println("Reprinted job "+ serverInfo);;
            String [] arr = serverInfo.split(" ");
         
            if(arr[0].equals("JOBN")){
               
                int jobNumb = Integer.parseInt(arr[2]);
                 
                client = "SCHD "+ jobNumb +" "+ foundServers+ " 0\n";
                dout.write(client.getBytes());
                dout.flush();
               
                serverInfo = in.readLine();
            }
     
        }
        client = "QUIT\n";
        dout.write(client.getBytes());
        dout.flush();

        dout.close();
        s.close();
        }catch(UnknownHostException e){
            System.out.println("SOCK:" +e.getMessage());
        }catch(EOFException e){
            System.out.println("EOF:"+e.getMessage());
        }catch(IOException e){
            System.out.println("IO:"+e.getMessage());
        }
    }
}