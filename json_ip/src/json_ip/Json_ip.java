package json_ip;
import org.json.*; 
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author Admin
 */
class operations{
    protected void read() throws org.json.simple.parser.ParseException{
        //Getting file path
        Json_ip jp=new Json_ip();
        String  fpath=jp.get_file_path();
        
        System.out.println("Enter the key for the values should be viewed");
        String find;
        Scanner sc=new Scanner(System.in);
        find=sc.next();
        JSONParser jsonParser = new JSONParser();
        
        try(FileReader reader = new FileReader(fpath)){
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            String where=jsonObject.get(find).toString();
            try{

                System.out.println("Value "+where);
            }catch(NullPointerException e){
                //if it was NULL Pointer exception then that specific key is not there
                System.out.println("The Key is not found");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    protected synchronized void create(){
        int length_key=32;
        long length_value=16*125;//kb to bytes
        
        Json_ip js=new Json_ip();
        String fpath=js.get_file_path();
        JSONObject obj=new JSONObject();
        Scanner sc=new Scanner(System.in);
        
        char semi='"';
        Boolean flag=true;
        String key_pair;
        String []key=new String[2];
        
        //to store the words of the file to handle bracket case 
        //Ex:{"key1":"Value1"}{"Key2":"Value2"}=>{"key1":"Value1","Key2":"Value2"}
        String result=new String();
        
        //getting input from the user andd verifying whether it was in correct format
        while(flag){
            System.out.println("Enter thre input in JSON format key:value");
            key_pair=sc.nextLine();
            key=key_pair.split(":");
            if(key.length==2){
                obj.put(key[0],key[1]);
                Boolean already=false;
                
                result=file_to_words(key[0],already);
                
                if(!result.contains(semi+key[0]+semi+":")){
                    flag=false;
                }else{
                    System.out.println("These Key is Already present");
                }
            }else{
                System.out.println("You gave the input in wrong format");
                System.out.println("Please give it in correct format"); 
            }
        }
        result=result.replaceAll("}", " ");
        key[0]=semi+key[0]+semi;
        key[1]=semi+key[1]+semi;
        result=result+","+key[0]+":"+key[1]+"}";
        System.out.println("Contents of File "+result.toString());
      //  result=result.replace('}', ' ');
        try{
            File f=new File(fpath);
            long size=f.length();
            System.out.println("size of the file "+size);
            double size_of_file;
            if(size>10000){
                size_of_file=size/1024;//KB=b/0124
                size_of_file=size_of_file/1024;//mg=kb/1024
                size_of_file=size_of_file/1024;//gb=mb/1024
                if(size_of_file>1){
                    System.out.println("The File size should be less than 1 GB");
                }
            }
            
            PrintWriter pw=new PrintWriter(new File(fpath));
            if(size>2){
                //File f=new File(fpath);
             
                        PrintWriter writer=new PrintWriter(f);
                        writer.append(result.toString());
                        writer.flush();
            }else{
                System.out.println("Elser");
                pw.write("{"+"\n"+key[0]+":"+key[1]+",\n"+"}");
            }
            pw.flush();
        }catch(Exception e){
            System.out.println("Error was occured at creating the key while try to write the replaced content"+e);
        }
    }
    protected String file_to_words(String s,Boolean find){
        Json_ip js=new Json_ip();
        String fpath=js.get_file_path();
        String input=null;

        StringBuilder sb=new StringBuilder();
        try{
            File f=new File(fpath);
            if(f.length()==0){
                return new String("");
            }
            Scanner sc=new Scanner(f);
            while(sc.hasNext()){
                input=sc.next();
                if(input.contentEquals("}") || input.contentEquals("]")){
                    continue;
                }
                if(input.contains(s)){
                    find=true;
                }
                sb.append(input);
            }
        }catch(Exception e){
            System.out.println("Error in converting the file into words");
        }
        find=true;
        return sb.toString();
    }
  
    public synchronized void delete() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {
        Json_ip jp=new Json_ip();
        String fpath=jp.get_file_path();
       
        JSONParser jsonParser = new JSONParser();
        
        try(FileReader reader = new FileReader(fpath)){
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println("before deleting "+jsonObject.toString());
            String del;
            System.out.println("Enter the key which should be delted");
            Scanner sc=new Scanner(System.in);
            del=sc.next();
            jsonObject.remove(del);
            System.out.println("After deleting "+jsonObject.toString());
            PrintWriter writer = new PrintWriter(new File(fpath));
            writer.append(jsonObject.toJSONString());
            writer.flush();
        }catch(Exception e){
            System.out.println("dacd"+e);
        }
    }
}
public class Json_ip {

    /**
     * @param args the command line arguments
     */
    private String fpath=new String();
    Json_ip(){
        //String fpath=new String();
        fpath="f.json";
    }
    public String get_file_path(){
        Json_ip jp=new Json_ip();
        return jp.fpath;
    }
    public static void main(String[] args) throws IOException, FileNotFoundException, org.json.simple.parser.ParseException, ParseException {
        //constructor of the JSONObject class  
        System.out.println("you can specify your location of the file else default location will be continued you can enter no");
        Json_ip jp=new Json_ip();
        jp.fpath="f.json";
        Scanner sc=new Scanner(System.in);
        String path_e=sc.nextLine();
        if(!(path_e.toLowerCase()).equals("no")){
            jp.fpath=path_e;
        }
        operations op=new operations();
        while(true){
            System.out.println("***************************************************************");
            System.out.println("*************************OPTIONS*****************************");
            System.out.println("Click corresponding number for specific operations \n 1.create key \n 2.Delete key\n 3.Read Key \n 4.Exit \n");
            int choice=sc.nextInt();
      
            switch(choice){
                case 1:
                    System.out.println("Create the key ");
                    op.create();
                    break;
                case 2:
                    System.out.println("Delete the Key");
                    op.delete();
                    break;
                case 3:
                    System.out.println("Read Key");
                    op.read();
                    break;
                case 4:
                    System.out.println("Exit");
                    return;
                default:
                    System.out.println("Enter choice 1-4");
            }
        }
    }
}
