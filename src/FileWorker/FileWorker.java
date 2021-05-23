package FileWorker;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FileWorker {
    Path path;
    File directory;
    ArrayList<String> executeFiles;
    private boolean isRecursive;

    public FileWorker(String path){
        directory = new File(path);
        this.path = Paths.get(directory.toURI());
        isRecursive = false;
        executeFiles = new ArrayList<>();
    }


    public boolean getRecursive(){
        return isRecursive;
    }

    public void setRecursive(boolean recursive){
        isRecursive = recursive;
    }

    public ArrayList<String> execute(IExecutable executor) {
        reExecute(executor,directory);
        return executeFiles;
    }

    public void reExecute(IExecutable executor, File directory) {
        var filesInDirectory = directory.listFiles();
        for (File file : filesInDirectory) {
            if (file.isDirectory() && isRecursive)
                reExecute(executor, file);

            String result = executor.process(file);
            if (result != null) {
                Path path = Paths.get(file.toURI());
                executeFiles.add(path.toString());
            }
        }
    }

    public ArrayList execute(IExecutable executable, String param) {
        var result = new ArrayList<>();
        result.add(executable.process(new File(directory + "\\" + param)));
        return result;
    }
}
