package com.vrv.cems.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class UpgradeInfo {

    private String inputInstallPackFile; //'D:\\#工作文档\\00-标版\\02服务跨版本升级\\CEMS-SERVICE-ALARM';
    //服务名称
    private String serviceName;
    private String serviceCode;
    private String inputUpgradePackCode;
    private String radioServiceType;
    private String upgradeDesc;
    private ArrayList<Map<String, String>> configFiles;
    private ArrayList<Map<String, String>> confFiles;
    private String isReplaceDirectory;
    private String changeLibFileString;
    private ArrayList<Map<String, String>> changeLibFile;
    private ArrayList<Map<String, Object>> fileTreeNode;

    public String getInputInstallPackFile() {
        return inputInstallPackFile;
    }

    public void setInputInstallPackFile(String inputInstallPackFile) {
        this.inputInstallPackFile = inputInstallPackFile;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getInputUpgradePackCode() {
        return inputUpgradePackCode;
    }

    public void setInputUpgradePackCode(String inputUpgradePackCode) {
        this.inputUpgradePackCode = inputUpgradePackCode;
    }

    public String getRadioServiceType() {
        return radioServiceType;
    }

    public void setRadioServiceType(String radioServiceType) {
        this.radioServiceType = radioServiceType;
    }

    public String getUpgradeDesc() {
        return upgradeDesc;
    }

    public void setUpgradeDesc(String upgradeDesc) {
        this.upgradeDesc = upgradeDesc;
    }

    public ArrayList<Map<String, String>> getConfigFiles() {
        return configFiles;
    }

    public void setConfigFiles(ArrayList<Map<String, String>> configFiles) {
        this.configFiles = configFiles;
    }

    public ArrayList<Map<String, String>> getConfFiles() {
        return confFiles;
    }

    public void setConfFiles(ArrayList<Map<String, String>> confFiles) {
        this.confFiles = confFiles;
    }

    public String getIsReplaceDirectory() {
        return isReplaceDirectory;
    }

    public void setIsReplaceDirectory(String isReplaceDirectory) {
        this.isReplaceDirectory = isReplaceDirectory;
    }

    public String getChangeLibFileString() {
        return changeLibFileString;
    }

    public void setChangeLibFileString(String changeLibFileString) {
        this.changeLibFileString = changeLibFileString;
    }

    public ArrayList<Map<String, String>> getChangeLibFile() {
        return changeLibFile;
    }

    public void setChangeLibFile(ArrayList<Map<String, String>> changeLibFile) {
        this.changeLibFile = changeLibFile;
    }

    public ArrayList<Map<String, Object>> getFileTreeNode() {
        return fileTreeNode;
    }

    public void setFileTreeNode(ArrayList<Map<String, Object>> fileTreeNode) {
        this.fileTreeNode = fileTreeNode;
    }

    public UpgradeInfo(String inputInstallPackFile, String serviceName, String serviceCode, String inputUpgradePackCode, String radioServiceType, String upgradeDesc, ArrayList<Map<String, String>> configFiles, ArrayList<Map<String, String>> confFiles, String isReplaceDirectory, String changeLibFileString, ArrayList<Map<String, String>> changeLibFile, ArrayList<Map<String, Object>> fileTreeNode) {
        this.inputInstallPackFile = inputInstallPackFile;
        this.serviceName = serviceName;
        this.serviceCode = serviceCode;
        this.inputUpgradePackCode = inputUpgradePackCode;
        this.radioServiceType = radioServiceType;
        this.upgradeDesc = upgradeDesc;
        this.configFiles = configFiles;
        this.confFiles = confFiles;
        this.isReplaceDirectory = isReplaceDirectory;
        this.changeLibFileString = changeLibFileString;
        this.changeLibFile = changeLibFile;
        this.fileTreeNode = fileTreeNode;
    }

    public UpgradeInfo() {
    }


}