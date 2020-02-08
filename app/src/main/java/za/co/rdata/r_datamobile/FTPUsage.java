package za.co.rdata.r_datamobile;

import android.content.Context;
import android.os.AsyncTask;
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

/**
 * Created by James de Scande on 05/02/2020 at 14:15.
 */

public class FTPUsage {

    static private FTPClient ftpClient = new FTPClient();
    static private String server = "196.41.122.216";
    static private String user = "james";
    static private String password = "Crunchie926";

    public FTPUsage(Context context) {
    }

    private static boolean connecttoserver(String workingdir) {

        try {
            ftpClient.connect(server);
            if (ftpClient.login(user, password)) {
                ftpClient.enterLocalPassiveMode(); // important!
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                boolean success = ftpClient.changeWorkingDirectory(workingdir);
                showServerReply(ftpClient);

                if (success) {
                    System.out.println("Successfully changed working directory.");
                    return true;
                } else {
                    System.out.println("Failed to change working directory. See server's reply.");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    static void getfilestolog(String[] args) throws IOException {

        connecttoserver(args[1]);
        FTPFile[] files = ftpClient.listFiles(args[0]);
        for (FTPFile file : files) {
            System.out.println(file.getName());
        }
        close();
    }

    static FTPFile[] getfiles(String[] args) throws IOException {
        connecttoserver(args[1]);
        FTPFile[] files = ftpClient.listFiles(args[0]);
        close();
        return files;
    }

    public void storefile(File file, String pathtosave) throws IOException {

        connecttoserver(pathtosave);

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

    static void goforIt(String pathtosave, String nameoffile, String pathtofile) {

        try {

            String nameonmobile = "promob.apk";

              if (connecttoserver(pathtofile)) {
            ftpClient.enterLocalPassiveMode(); // important!
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            OutputStream out = new FileOutputStream(new File(pathtosave + nameonmobile));
            boolean result = ftpClient.retrieveFile(nameoffile, out);
            out.close();
            if (result) {
                Log.v("download result", "succeeded");
                File file = new File(Environment.getExternalStorageDirectory() + pathtosave + nameonmobile);
                FileOutputStream fOut = new FileOutputStream(file);
                fOut.close();
                close();
                //return file;

                 }
            }
        } catch (Exception e) {
            Log.v("download result", "failed");
            e.printStackTrace();

        }
        //return null;
    }

}
