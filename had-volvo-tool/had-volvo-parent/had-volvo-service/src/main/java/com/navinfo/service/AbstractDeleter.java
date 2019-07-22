package com.navinfo.service;

import com.had.zero.common.bean.InMeshTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AbstractDeleter {
    public  List<File> filelist = new ArrayList<>();
    public  List<File> getFileList(String strPath) throws Exception{
//        filelist.clear();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else if (fileName.endsWith(".mif") || fileName.endsWith(".mid") || fileName.endsWith(".DAT")
                        || fileName.endsWith(".ID") || fileName.endsWith(".MAP") || fileName.endsWith(".TAB")) { // 判断文件名是否以.avi结尾
//                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println("====" + strFileName);
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }

        }
        return filelist;
    }

    protected void divTable(List<File> mList, List<InMeshTable> meshList) {
        for (File f : mList){
            if(f.getName().contains(".mif")){
                InMeshTable t = new InMeshTable();
                t.setMifFile(f);
                meshList.add(t);
            }
        }

        for(File f : mList){

            for (InMeshTable t:meshList){
                if(f.getParent().equals(t.getMifFile().getParent())&&f.getName().contains(".mid")){
                    t.setMidFile(f);
                }
            }

        }

    }
}
