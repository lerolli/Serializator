package FileWorker;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FileCommand implements ICommand {
    private final IResulter resulter;
    private final IExecutable executable;
    private final boolean isRecursive;

    public FileCommand(IExecutable executable, boolean isRecursive, IResulter resulter) {
        this.executable = executable;
        this.resulter = resulter;
        this.isRecursive = isRecursive;
    }

    @Override
    public void start(String ... params){
        var fileWorker = new FileWorker("C:\\Users\\anton\\IdeaProjects\\WebServer\\folder");
        fileWorker.setRecursive(isRecursive);
        ArrayList result;

        if (params.length != 0)
            result = fileWorker.execute(executable, params[0]);
        else
            result = fileWorker.execute(executable);

        resulter.showResult(result);
    }
}