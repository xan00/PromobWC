package za.co.rdata.r_datamobile;

import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FTPUsage {

    static private FTPClient ftpClient = new FTPClient();
    static private String server = "ftp://196.41.122.216/";
    static private String user = "james";
    static private String password = "Crunchie926";

    private static boolean connecttoserver() throws IOException {

        ftpClient.connect(server);
        //ftpClient.login(user, password);
        if (ftpClient.login(user, password)) {
            ftpClient.enterLocalPassiveMode(); // important!
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return true;
        }
        return false;
    }

    public static void getfiles(String[] args) throws IOException {

        connecttoserver();
        FTPFile[] files = ftpClient.listFiles(args[0]);
        for (FTPFile file : files) {
            System.out.println(file.getName());
        }
        close();
    }

    public void storefile(File file, String pathtosave) throws IOException {

        connecttoserver();
        ftpClient.changeWorkingDirectory(pathtosave);
        //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(file));
        ftpClient.storeFile(file.getName(), buffIn);
        buffIn.close();
        close();
    }

    public static void close() throws IOException {
        ftpClient.logout();
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    public File goforIt(String pathtosave, String nameoffile) {

        try {

            if (connecttoserver()) {
                //ftpClient.enterLocalPassiveMode(); // important!
                //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                OutputStream out = new FileOutputStream(new File(pathtosave + nameoffile));
                boolean result = ftpClient.retrieveFile(nameoffile, out);
                out.close();
                if (result) {
                    Log.v("download result", "succeeded");
                    File file = new File(Environment.getExternalStorageDirectory() + pathtosave + nameoffile);
                    FileOutputStream fOut = new FileOutputStream(file);
                    fOut.close();
                    close();
                    return file;

                }
            }
        } catch (Exception e) {
            Log.v("download result", "failed");
            e.printStackTrace();

        }
        return null;
    }

}
