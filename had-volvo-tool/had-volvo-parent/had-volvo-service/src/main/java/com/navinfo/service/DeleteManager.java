package com.navinfo.service;

import com.had.zero.common.interfaces.IMidDriver;
import com.navinfo.service.utils.ConfigUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class DeleteManager {

    String msg  ="请正确输入，如：java -jar had-zero-service-1.0.190710.1.jar -in /data/src -out /data/out ";

    String in =null;
    String out = null;
    private void checkArgs(String[] args) throws Exception{

        if (args.length != 4) {
            System.out.println(msg);
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-in")) {
                in= args[i + 1];
            }
            if (args[i].equals("-out")) {
                out=args[i + 1];
            }
        }
        System.out.println("in:"+in);
        System.out.println("out:"+out);
        //in,out 内部文件初步判断??mif  mid

        File inFile = new File(in);
        File outFile = new File(out);

        if (!inFile.isDirectory()) {

            System.out.println("输入路径不是目录");
            throw  new Exception("输入路径异常！");
        }

    }

    private void copyDir(String src,String des){
        try {

            File s = new File(src);
            File d = new File(des);
            FileUtils.copyDirectory(s,d);
        }catch (IOException e){
            System.out.println("文件夹拷贝错误："+ e.getMessage());
            return;
        }
    }

    public void startDelete(String[] args){

        try {
            checkArgs(args);

            Date startTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss SSS");
            System.out.println("开始时间:" + sdf.format(startTime));

            //拷贝
            copyDir(in,out);

            //删除表
            List<String> de = ConfigUtils.getDeleteTables();
            System.out.println("删除表数："+de.size());
            //删除字段的表
            List<String> mo = ConfigUtils.getModifyTables();
            System.out.println("修改表数："+mo.size());

            List<Callable<Object>> list = new ArrayList<Callable<Object>>();
            for (String s:mo){
                ColumnDeleter deleter = new ColumnDeleter(out,s);
                list.add(deleter);

            }
            for (String s:de){
                TableDeleter deleter = new TableDeleter(out,s);
                list.add(deleter);
            }

            System.out.println("开始执行");
            int threadNum = 2 * Runtime.getRuntime().availableProcessors() + 1;
            System.out.println("开始提交线程池，并发数:"+ threadNum);
            ExecutorService pool = Executors.newFixedThreadPool(threadNum);
            CompletionService<Object> completionService = new ExecutorCompletionService<>(pool);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                completionService.submit(list.get(i));
            }
            pool.shutdown();

            System.out.println("提交线程池完成，处理总数:{}" + size);

            for (int i = 0; i < list.size(); i++) {
                Future<Object> future = completionService.take();
                System.out.println("进度："+ future.get()+"表处理完成，" + (i + 1) + "/" + list.size());
            }

            System.out.println("执行完成");

            Date endTime = new Date();
            System.out.println("结束时间:" + sdf.format(endTime));
            long spendTime = endTime.getTime() - startTime.getTime();
            System.out.println("耗时:" + spendTime + "ms");

        }catch (Exception e){
            e.printStackTrace();
        }  finally {

        }
    }

}
