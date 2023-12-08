import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class osutil {
    private String parent;
    osutil(String parentFolder){
        parent = parentFolder;
        File par = new File(parent);

        par.mkdir();
    }

    public boolean deleteFolder(File deleted_file){

        if(deleted_file.exists()){
            String[] entries = deleted_file.list();
            if (entries != null){
                for (String s : entries){
                    File currentfile = new File(deleted_file.getPath(),s);
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
    public boolean createFile(String path_name) throws IOException {
        FileOutputStream out;
        out = new FileOutputStream(parent + File.separator + path_name+".txt");
        out.close();
        return true;
    }
    public boolean appendFile(String path_name, String x) throws IOException {
        FileWriter fw = new FileWriter(parent + File.separator + path_name + ".txt",true);
        fw.write(x+"\n");
        fw.close();
        return true;
    }
    public boolean createFileWithList(String path_name, ArrayList<String> lst) throws IOException {
        FileOutputStream out;
        out = new FileOutputStream(parent + File.separator + path_name+".txt");
        for (String s : lst){
            out.write((s+"\n").getBytes());
        }

        out.close();
        return true;
    }
    public boolean createFileWithHash(String path_name, HashMap<String, Double> hashMap) throws IOException {
        FileWriter out;
        for (String s : hashMap.keySet()){
            out = new FileWriter(parent + File.separator + path_name + s +".txt");
            out.write(String.valueOf(hashMap.get(s)));
            out.close();
        }

        return true;
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

            return result;

        } catch (IOException e){
          return null;
        }

    }

}
