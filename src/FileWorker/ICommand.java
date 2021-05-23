package FileWorker;

import java.security.NoSuchAlgorithmException;

public interface ICommand {
    final IResulter resulter = null;
    void start(String ... params) throws NoSuchAlgorithmException;
}
