package com.navinfo.data.writer;

import com.navinfo.data.writer.interfaces.IWriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class MifWriter implements IWriter {

    // 最上一级目录，源目录
    private File srcDir;

    // 最上一级目录，目的目录
    private File destDir;

    public File getSrcDir() {
        return srcDir;
    }

    @Override
    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    public File getDestDir() {
        return destDir;
    }

    @Override
    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    @Override
    public void cleanDirectory() throws IOException {
        FileUtils.cleanDirectory(destDir);
    }

    @Override
    public void copyToDirectory(File src) throws IOException {
        //获取父目录，并替换为目的目录
        String dest = src.getParentFile().getCanonicalPath()
                .replace(srcDir.getCanonicalPath(),destDir.getCanonicalPath());

        File dir = new File(dest);
        if(!dir.exists()){
            dir.mkdirs();
        }

        FileUtils.copyToDirectory(src,dir);
    }

    @Override
    public void writer(Object object) throws IOException {

    }
}
