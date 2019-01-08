package com.vrv.cems.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileControl {

    // 文件复制
    public static boolean copyFile(String source, String copy) throws Exception {
        source = source.replace("\\", "/");
        copy = copy.replace("\\", "/");

        File source_file = new File(source);
        File copy_file = new File(copy);

        // BufferedStream缓冲字节流

        if (!source_file.exists()) {
            throw new IOException("文件复制失败：源文件（" + source_file + "） 不存在");
        }
        if (copy_file.isDirectory()) {
            throw new IOException("文件复制失败：复制路径（" + copy_file + "） 错误");
        }
        File parent = copy_file.getParentFile();
        // 创建复制路径
        if (!parent.exists()) {
            System.out.println(parent + "--复制文件时，父级文件夹成功创建");
            parent.mkdirs();
        }
        // 创建复制文件
        if (!copy_file.exists()) {
            System.out.println(copy_file + "--文件成功创建");
            copy_file.createNewFile();
        }

        FileInputStream fis = new FileInputStream(source_file);
        FileOutputStream fos = new FileOutputStream(copy_file);

        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte[] KB = new byte[1024];
        int index;
        while ((index = bis.read(KB)) != -1) {
            bos.write(KB, 0, index);
        }

        bos.close();
        bis.close();
        fos.close();
        fis.close();

        if (!copy_file.exists()) {
            return false;
        } else if (source_file.length() != copy_file.length()) {
            return false;
        } else {
            System.out.println("复制文件成功。原文件：" + source + "；复制文件：" + copy);
            System.out.println();
            return true;
        }

    }

    // 文件重命名
    public static boolean renameFile(String url, String new_name) throws Exception {
        String old_url = url;
        old_url = old_url.replace("\\", "/");
        File old_file = new File(old_url);
        if (!old_file.exists()) {
            throw new IOException("文件重命名失败，文件（"+old_file+"）不存在");
        }
        System.out.println(old_file.exists());

        String old_name = old_file.getName();
        // 获得父路径
        String parent = old_file.getParent();
        // 重命名
        String new_url = parent + "/" + new_name;
        File new_file = new File(new_url);
        old_file.renameTo(new_file);

        System.out.println("原文件：" + old_file.getName());
        System.out.println("新文件：" + new_file.getName());
        new_name = new_file.getName();
        old_name = old_file.getName();
        if (new_name.equals(old_name)) {
            return false;
        } else {
            return true;
        }

    }

    // 文件删除
    public static boolean deleteFile(String url) throws Exception {
        url = url.replace("\\", "/");
        File file = new File(url);

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }else{
                throw new IOException("文件删除失败：（"+file+"）错误");
            }
            return true;
        }
        return true;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(String url) {
        url = url.replace("\\", "/");
        File dir = new File(url);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();  //递归删除目录中的子目录下
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(dir + "\\" + children[i]);
                    if (!success) {
                        return false;
                    }
                }
            }
            // 目录此时为空，可以删除
            return dir.delete();
        }
        return false;
    }

    // 创建文件夹
    public static boolean createPath(String address, String folderName) throws Exception {
        File file = new File(address);
        if (!(file == null)) {
            File newFile = new File(file.getPath() + "\\" + folderName);
            if (newFile.mkdirs()) {
                System.out.println(folderName + " -- 文件夹成功创建");
                return true;
            } else {
                System.err.println(folderName + " -- 创建文件夹失败");
                return false;
            }
        } else {
            System.err.println(address+ " -- 输入的路径不存在 !");
        }
        return false;
    }

}