package com.vrv.cems.controller;


import com.vrv.cems.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@RestController
//@RequestMapping(value = {"/user"})
public class LoadInstallPackController {

    public static int num = 0;

    @RequestMapping("loadInstallPack")
    public Map<String, Object> loadInstallPack(@RequestParam("paramPath") String filePath) {
        List<Object> allFiles = new ArrayList();
        allFiles = traverseFolder(filePath);

        Map<String, String> info = new HashMap<String, String>();
        info = getConfigInfo(filePath);

        Map<String, Object> returnMapData = new HashMap<String, Object>();
        returnMapData.put("installPackFileList", allFiles);
        returnMapData.put("installConfigInfo", info);


        if (returnMapData == null || returnMapData.size() == 0) {
            return Json.fail();
        } else {
            return Json.success(returnMapData);
        }
    }

    public static List<Object> traverseFolder(String path) {
        List<Object> allFiles = new ArrayList();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return null;
            } else {
                for (File file2 : files) {
                    num++;
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("id",num);
                    resultMap.put("label",file2.getName());
                    resultMap.put("path",file2.getAbsolutePath());
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());

                        List<Object> childrenFiles = new ArrayList();
                        childrenFiles = traverseFolder(file2.getAbsolutePath());
                        if(childrenFiles != null)
                        {
                            resultMap.put("children",childrenFiles);
                        }
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                    allFiles.add(resultMap);
                }
            }
            return allFiles;
        } else {
            System.out.println("文件不存在!");
            return null;
        }
    }

    public static Map<String, String> getConfigInfo(String filePath)
    {
        Map<String, String> info = new HashMap<String, String>();
        try{
            Properties properties = new Properties();
            // 使用InPutStream流读取properties文件
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + "\\config\\config.properties"));
            properties.load(bufferedReader);
            // 获取key对应的value值

            info.put("serviceName", properties.getProperty("service.name"));
            info.put("serviceCode", properties.getProperty("service.code"));
            info.put("serviceVersion", properties.getProperty("service.version"));

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("读取config.properties文件失败");
        }
        return info;
    }

}
