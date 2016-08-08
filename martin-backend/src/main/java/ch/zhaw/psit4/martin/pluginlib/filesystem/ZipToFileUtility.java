package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * 
 * @version 0.0.1-SNAPSHOT
 */
public class ZipToFileUtility {

    private ZipInputStream stream;
    private String path;

    public ZipToFileUtility(ZipInputStream stream, String path) {
        this.stream = stream;
        this.path = path;
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by destDirectory
     * (will be created if does not exists)
     * 
     * @throws IOException
     */
    public File unzip() throws IOException {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipEntry entry = stream.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = path + File.separatorChar + entry.getName().replace('/', File.separatorChar);
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(stream, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            stream.closeEntry();
            entry = stream.getNextEntry();
        }
        return destDir;
    }

    /**
     * Extracts a zip entry (file entry)
     * 
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        FileOutputStream fout = new FileOutputStream(filePath);
        for (int c = zipIn.read(); c != -1; c = zipIn.read()) {
            fout.write(c);
        }
        zipIn.closeEntry();
        fout.close();
    }
}
