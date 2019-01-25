package com.vrv.cems.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;



public class FileCRC32 {

    public static String getCRC32(String fileUri) {
        fileUri = fileUri.replace("\\", "/");
        CRC32 crc32 = new CRC32();
        FileInputStream fileinputstream = null;
        CheckedInputStream checkedinputstream = null;
        String crc = null;
        try {
            fileinputstream = new FileInputStream(new File(fileUri));
            checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
            while (checkedinputstream.read() != -1) {
            }
            System.out.println("crc32 --- " + crc32.getValue());
            crc = Long.toHexString(crc32.getValue()).toUpperCase();
            //crc计算出来默认少了前面的0
            if (crc.length() != 8) {

            }
            crc = crcStringFormat(crc);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileinputstream != null) {
                try {
                    fileinputstream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (checkedinputstream != null) {
                try {
                    checkedinputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return crc;
    }

    public static String crcStringFormat(String crc){
        if (crc.length() == 8) {
            return crc;
        }else{
            for(int i=8-crc.length();i>0;i--){
                crc = "0" + crc;
            }
        }
        return crc;
    }

}
