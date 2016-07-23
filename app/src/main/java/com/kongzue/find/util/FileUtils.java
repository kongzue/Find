package com.kongzue.find.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

/**
 * Created by ZhangChao on 2016/6/24.
 */
public class FileUtils {

    private String SDPATH;

    private int FILESIZE = 4 * 1024;

    public String getSDPATH(){
        return SDPATH;
    }

    public FileUtils(){
        //得到当前外部存储设备的目录( /SDCARD )
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * 在SD卡上创建文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException{
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     * @param dirName
     * @return
     */
    public File createSDDir(String dirName){
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName){
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public File write2SDFromInput(String path,String fileName,InputStream input){
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];

            /*真机测试，这段可能有问题，请采用下面网友提供的
                            while((input.read(buffer)) != -1){
                output.write(buffer);
            }
                            */

                           /* 网友提供 begin */
            int length;
            while((length=(input.read(buffer))) >0){
                output.write(buffer,0,length);
            }
                           /* 网友提供 end */

            output.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
