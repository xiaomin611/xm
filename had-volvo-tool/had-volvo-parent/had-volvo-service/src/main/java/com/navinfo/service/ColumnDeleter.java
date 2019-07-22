package com.navinfo.service;

import com.had.zero.common.bean.InMeshTable;
import com.navinfo.service.utils.ConfigUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ColumnDeleter extends AbstractDeleter implements Callable<Object> {

    private String out;
    private String tableName;

    public ColumnDeleter(String out ,String tableName) {
        this.out = out;
        this.tableName = tableName;
    }

    public static void main(String[] args) throws Exception{
        Process p = Runtime.getRuntime().exec("cmd.exe /k ipconfig /all" );
        InputStream in = p.getInputStream();

        while (in.read() != -1) {

            System.out.println(in.read());

        }

        in.close();

        p.waitFor();


    }

    @Override
    public Object call() throws Exception {
        File f = new File(out);

        File mifFile=null ,midFile = null;
        if(f.isDirectory()){

            List<File> list = getFileList(out);
            List<InMeshTable> meshList = new ArrayList<>();
            List<File> mList = new ArrayList<>();
            for (File sf:list){
                if((sf.getName().substring(0,sf.getName().indexOf("."))).equalsIgnoreCase(tableName)){
                    mList.add(sf);
                }
            }
            divTable(mList,meshList);
//            System.out.println("总共多少组："+meshList.size());
            for (InMeshTable in : meshList){
//                System.out.println("------"+in.getMifFile().getAbsolutePath()+":"+in.getMidFile().getAbsolutePath());
                doDelete(in.getMifFile(),in.getMidFile());
            }


        }
        return tableName;
    }



    protected BufferedReader getFilesBufferedReader(File path) throws IOException {
        BufferedReader reader = null;
        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        reader = new BufferedReader(isr);
        return reader;
    }

    protected BufferedWriter getFilesBufferedWriter(File path) throws IOException {
        BufferedWriter writer = null;
        FileOutputStream fis = new FileOutputStream(path);
        OutputStreamWriter isr = new OutputStreamWriter(fis, "UTF-8");
        writer = new BufferedWriter(isr);
        return writer;
    }


    private void doDelete(File mifFile, File midFile) {

        List<Integer> indexList = null;
        try {

            indexList= deleteMifCol(mifFile);
            deleteMidCol(midFile,indexList);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
    }

    private void deleteMidCol(File mid,List<Integer> list) {
        BufferedReader midReader = null;
        BufferedWriter midWriter = null;
        File tf=null;
        String tempString = null;
        try {
            String s = mid.getParentFile().getCanonicalPath();
            if(!mid.getName().contains(".mid")){

                System.out.println(mid.getName()+"后缀不为.mid");

                return ;
            }
            String tfs = s+File.separator+mid.getName().replace(".mid","_tmp.mid");
            tf = new File(tfs);
            if(!tf.exists()){
                tf.createNewFile();
            }else {
                tf.delete();
                tf.createNewFile();
                tf.setWritable(true);
            }

            midReader =getFilesBufferedReader(mid);
            midWriter=getFilesBufferedWriter(tf);
            while ((tempString = midReader.readLine()) != null) {
                   String[] sa =  tempString.split(",");
                   int[] arr = list.stream().mapToInt(Integer::valueOf).toArray();
                   String[] ns =  ArrayUtils.removeAll(sa, arr);
                   midWriter.write(StringUtils.join(ns,",")+"\n");
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                midWriter.flush();
                if (midReader != null)
                    midReader.close();
                if (midWriter != null)
                    midWriter.close();
                String th = mid.getCanonicalPath();
                mid.delete();
                tf.renameTo(new File(th));
            } catch (IOException e1) {

            }
        }
    }

    private List<Integer> deleteMifCol(File mif) {
        List<Integer> ret = new ArrayList<>();
        BufferedReader mifReader = null;
        BufferedWriter mifWriter = null;
        File tf=null;
        try {
            String s = mif.getParentFile().getCanonicalPath();
            if(!mif.getName().contains(".mif")){

                System.out.println(mif.getName()+"后缀不为.mif");

                return null;
            }
            String tfs = s+File.separator+mif.getName().replace(".mif","_tmp.mif");
            tf = new File(tfs);
            if(!tf.exists()){
                tf.createNewFile();
            }else {
                tf.delete();
                tf.createNewFile();
                tf.setWritable(true);
            }

            mifReader =getFilesBufferedReader(mif);
            mifWriter=getFilesBufferedWriter(tf);

            String tempString = null;
            //从0开始index
            int st = -1;
            boolean start = false;
            while ((tempString = mifReader.readLine()) != null) {
                if (tempString.contains("Columns")) {
                    start=true;

                    String[] sa = tempString.split(" ");
                    String nn = String.valueOf(Integer.parseInt(sa[1])-ConfigUtils.getDeleteColByTable(tableName).size());
                    String ns = sa[0]+ " " + nn;
                    mifWriter.write(ns+ "\n");

                }else if(tempString.equalsIgnoreCase("data")){
                    start=false;
                    mifWriter.write(tempString+ "\n");

                }else{
                    if (start){
                        st =st+1;
                    }
                    List<String> lc =  ConfigUtils.getDeleteColByTable(tableName);
                    //删除的不写
                    String[] sa = tempString.split(" ");
                    boolean f = true;
                    for (String colItem:lc){
                        if(sa[0].equalsIgnoreCase(colItem)){
                            ret.add(st);
                            f=false;
                        }

                    }
                    if(f) {
                        mifWriter.write(tempString+ "\n");
                    }
                }
            }

            mifWriter.flush();
            return  ret;
        }catch (Exception e){
            System.out.println("删除异常:"+mif.getName());
            e.printStackTrace();
        }finally {
            try {
                if (mifReader != null)
                    mifReader.close();
                if (mifWriter != null)
                    mifWriter.close();
                String th = mif.getCanonicalPath();
                mif.delete();
                tf.renameTo(new File(th));


            } catch (IOException e1) {
                    e1.printStackTrace();
            }

        }

        return ret;

    }
}
