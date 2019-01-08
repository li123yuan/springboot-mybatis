package com.vrv.cems.controller;


import com.vrv.cems.pojo.UpgradeInfo;
import com.vrv.cems.utils.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


@RestController
public class makeUpgradePackController {

    @RequestMapping( value = "makeUpgradePack", method = RequestMethod.POST )
    public Map<String, Object> makeUpgradePack(@RequestBody  UpgradeInfo upgradeInfo) {

        //组成升级包
        String upgradePackPath = createUpgradePackFile(upgradeInfo);

        File upgradePackFile = new File(upgradePackPath);
        //将升级包打成zip包
//        FileToZip.fileToZip(upgradePackPath, upgradePackFile.getParent(), upgradePackFile.getName());

        String upgradeZipPath = upgradePackFile.getParent() +"\\" + upgradePackFile.getName()+".zip"; //服务升级包zip文件路径
        try{
            System.out.println("开始生成服务升级包zip文件");
            ZipUtil.zip(upgradePackPath, upgradeZipPath);
            System.out.println("生成服务升级包zip文件完成");
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("打zip包异常");
        }

        //计算crc
        String crc32 = FileCRC32.getCRC32(upgradeZipPath);
        System.out.println("crc值 -- " + crc32);

        System.out.println("开始生成xml");
        //生成升级索引xml文件
        String xmlFilePath = createXml(upgradeInfo,crc32,upgradePackFile.getParent());
        System.out.println("生成xml完成");

        try{
            System.out.println("开始生成完整升级zip包");
            String[] allUpgrradeZipFiles = new String[]{upgradeZipPath, xmlFilePath};
            ZipUtil.zip(allUpgrradeZipFiles, upgradePackFile.getParent() +"\\" + upgradePackFile.getName() + "-" + upgradeInfo.getInputUpgradePackCode() + ".zip");
            System.out.println("生成完整升级zip包完成");
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("打完整升级zip包异常");
        }

        if (upgradeInfo == null) {
            return Json.fail();
        } else {
            return Json.success(upgradeInfo);
        }
    }

    /**
     * 计算服务升级文件zip包的crc值
     */
//    public static void createUpgradePackFile(UpgradeInfo upgradeInfo) {
//
//    }

    /**
     * 根据配置组装服务升级文件，并返回文件全路径
     */
    public static String createUpgradePackFile(UpgradeInfo upgradeInfo) {
        File installFile = new File(upgradeInfo.getInputInstallPackFile());;
        String installPackParentDir = installFile.getParent();
        System.out.println("安装包父级目录 -- " + installPackParentDir);
        String upgradeDir = installPackParentDir + "\\upgrade";  //升级包目录和安装包目录同级
        System.out.println("升级包目录 -- " + upgradeDir);
        try{
            FileControl.createPath(installPackParentDir, "upgrade"); //创建升级根目录
            FileControl.deleteDir(installPackParentDir + "\\upgrade\\" + upgradeInfo.getServiceName()); //先删除原有升级服务文件目录
            FileControl.createPath(installPackParentDir + "\\upgrade", upgradeInfo.getServiceName()); //创建升级服务文件目录
            for(int i=0; i<upgradeInfo.getFileTreeNode().size(); i++) {
                Map<String, Object> upgradeSrcFileMap = upgradeInfo.getFileTreeNode().get(i);
                File upgradeSrcFile = new File(upgradeSrcFileMap.get("path").toString());
//                String upgradeFileName = upgradeFile.getName();

//                upgradeDir = upgradeDir.replace("\\", "/");

                System.out.println("升级包根目录22 -- " + upgradeDir);
                System.out.println("安装包中原文件路径33 -- " + upgradeSrcFile);
                String destUpgradeFile = upgradeSrcFile.getPath().replace(installPackParentDir, upgradeDir);
                System.out.println("升级包中目标文件路径44 -- " + destUpgradeFile);
                if (upgradeSrcFile.exists()) {
                    if (upgradeSrcFile.isDirectory()) {
                        FileControl.createPath(destUpgradeFile.replace(upgradeSrcFile.getName(), ""), upgradeSrcFile.getName());
                    }
                    else if(upgradeSrcFile.isFile()) {
                        FileControl.copyFile(upgradeSrcFile.getPath(), destUpgradeFile);
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("组装升级文件失败");
        }
        return upgradeDir +"\\"+ installFile.getName();
    }

    /**
     * 生成升级索引xml
     */
    public static String createXml(UpgradeInfo upgradeInfo, String crc32, String upgradePackFile) {
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            // 创建跟节点
            Element upgradeIndex = document.createElement("upgradeIndex");
            // 创建子节点package
            Element packageNode = document.createElement("package");
            Element packageNode_name = document.createElement("name");
            packageNode_name.setTextContent(upgradeInfo.getServiceName());
            Element packageNode_version = document.createElement("version");
            packageNode_version.setTextContent(upgradeInfo.getInputUpgradePackCode());
            Element packageNode_crc = document.createElement("crc");
            packageNode_crc.setTextContent(crc32);
            Element packageNode_serviceType = document.createElement("serviceType");
            packageNode_serviceType.setTextContent(upgradeInfo.getRadioServiceType());
            Element packageNode_serviceCode = document.createElement("serviceCode");
            packageNode_serviceCode.setTextContent(upgradeInfo.getServiceCode());
            Element packageNode_description = document.createElement("description");
            packageNode_description.setTextContent(upgradeInfo.getUpgradeDesc());


            packageNode.appendChild(packageNode_name);
            packageNode.appendChild(packageNode_serviceCode);
            packageNode.appendChild(packageNode_version);
            packageNode.appendChild(packageNode_serviceType);
            packageNode.appendChild(packageNode_crc);
            packageNode.appendChild(packageNode_description);

            upgradeIndex.appendChild(packageNode);
            //package节点完成

            //config子节点
            Element config = document.createElement("config");
            for(int i=0; i<upgradeInfo.getConfigFiles().size(); i++)
            {
                Map<String, String> configFileInfo = upgradeInfo.getConfigFiles().get(i);
                Element file = document.createElement("file");
                file.setAttribute("filename", configFileInfo.get("key"));

                String value = configFileInfo.get("value");
                String[] propertyKeyValue = value.split("\\r\\n|\\n");

                for(String pkey : propertyKeyValue) {
                    Element property = document.createElement("property");
                    property.setAttribute("key", pkey.split("=")[0]);
                    property.setAttribute("value", pkey.split("=")[1]);
                    file.appendChild(property);
                }
                config.appendChild(file);
            }
            upgradeIndex.appendChild(config);


            //conf子节点
            Element conf = document.createElement("conf");
            for(int i=0; i<upgradeInfo.getConfFiles().size(); i++)
            {
                Map<String, String> configFileInfo = upgradeInfo.getConfFiles().get(i);
                Element file = document.createElement("file");
                file.setAttribute("filename", configFileInfo.get("key"));

                String value = configFileInfo.get("value");
                String[] propertyKeyValue = value.split("\\r\\n|\\n");

                for(String pkey : propertyKeyValue) {
                    Element property = document.createElement("property");
                    property.setAttribute("key", pkey.split("=")[0]);
                    property.setAttribute("value", pkey.split("=")[1]);
                    file.appendChild(property);
                }
                conf.appendChild(file);
            }
            upgradeIndex.appendChild(conf);

            //lib节点
            Element libs = document.createElement("libs");
            libs.setAttribute("isReplaceDirectory", upgradeInfo.getIsReplaceDirectory());
            if (upgradeInfo.getIsReplaceDirectory().equals("false")) {
                for(int i=0; i<upgradeInfo.getChangeLibFile().size(); i++)
                {
                    Map<String, String> libFileInfo = upgradeInfo.getChangeLibFile().get(i);
                    Element lib = document.createElement("lib");
                    lib.setAttribute("srcPath", libFileInfo.get("oldFile"));
                    lib.setAttribute("destPath", libFileInfo.get("newFile"));
                    libs.appendChild(lib);
                }
            }
            upgradeIndex.appendChild(libs);

            document.appendChild(upgradeIndex);

            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();

            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 创建xml文件并写入内容
            System.out.println(upgradePackFile + "\\upgradeIndex.xml");

            String xmlFilePath = (upgradePackFile + "\\upgradeIndex.xml").replace("\\", "/");

            FileControl.deleteFile(xmlFilePath);
//            File xmlFile = new File("D:\\upgradeIndex.xml");
            File xmlFile = new File(xmlFilePath);
//            tf.transform(new DOMSource(document), new StreamResult(new File( "upgradeIndex.xml")));
            tf.transform(new DOMSource(document), new StreamResult(xmlFile));
            System.out.println("生成upgradeIndex.xml成功");
            return xmlFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成upgradeIndex.xml失败");
        }
        return null;
    }



}
