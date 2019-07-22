package com.navinfo.service;

import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;

public class TableDeleter extends AbstractDeleter implements Callable<Object> {
    //out
    private String dir;
    //如 ROAD_LINK_INTERSECTION
    private String tableName;

    public TableDeleter(String dir,String tableName) {
        this.dir = dir;
        this.tableName=tableName;
    }

    @Override
    public Object call() throws Exception {
        File f = new File(dir);
        if(f.isDirectory()){

            //处理多级？
            try {

                    List<File> list = getFileList(dir);
                    for (File sf:list){
                        if((sf.getName().substring(0,sf.getName().indexOf("."))).equalsIgnoreCase(tableName)){
                            sf.delete();
                        }
                    }


            }catch (Exception e){

            }finally {

            }

        }
        return tableName;
    }
}
