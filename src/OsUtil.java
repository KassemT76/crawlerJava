import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OsUtil {
    private String parent;
    OsUtil(String parentFolder){
        parent = parentFolder;
        File par = new File(parent);
        par.mkdir();
    }
    public boolean deleteFolder(File deleted_file){

        if(deleted_file.exists()){
            String[] entries = deleted_file.list();
            if (entries != null){
                for (String s : entries){
                    File currentfile = new File(deleted_file.getAbsolutePath()+File.separator+s);
                    if(currentfile.isDirectory()){
                        deleteFolder(currentfile);
                    }
                    else {
                        currentfile.delete();
                    }
                }
            }
            deleted_file.delete();
            return true;
        }
        else {
            return false;
        }
    }
    public boolean createFolder(String created_file){
        return new File(parent+File.separator+created_file).mkdir();
    }
    public boolean createFile(String path_name){
        try {
            FileOutputStream out;
            out = new FileOutputStream(parent + File.separator + path_name+".txt");
            out.close();
            return true;
        }catch (IOException e){
            return false;
        }

    }
    public boolean appendFile(String path_name, String x) {
        try {
            FileWriter out = new FileWriter(parent + File.separator + path_name + ".txt",true);
            out.write(x+"\n");
            out.close();
            return true;
        }catch (IOException e){
            return false;
        }

    }
    public boolean createFileWithList(String path_name, ArrayList<String> lst){
        try {
            FileOutputStream out;
            out = new FileOutputStream(parent + File.separator + path_name+".txt");
            for (String s : lst){
                out.write((s+"\n").getBytes());
            }
            out.close();
            return true;
        }catch (IOException e){
            return false;
        }
    }
    public boolean createFileWithHash(String path_name, HashMap<String, Double> hashMap){
        try {
            FileWriter out;
            for (String s : hashMap.keySet()){
                out = new FileWriter(parent + File.separator + path_name + s +".txt");
                out.write(String.valueOf(hashMap.get(s)));
                out.close();
            }

            return true;
        }catch (IOException e){
            return false;
        }
    }
    public ArrayList<String> readFile(String path_name){
        try {
            BufferedReader aFile;
            aFile = new BufferedReader(new FileReader(parent + File.separator + path_name +".txt"));

            ArrayList<String> result = new ArrayList<String>();
            int i = 0;
            while (true){
                String newLine = aFile.readLine();
                if(newLine == null) break;
                result.add(newLine);
                i++;
            }
            aFile.close();
            return result;

        } catch (IOException e){
          return null;
        }

    }

}
