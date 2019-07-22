package com.navinfo.service;

import com.had.zero.common.bean.MifMidObject;
import com.had.zero.common.interfaces.IMidDriver;
import com.navinfo.data.writer.MifWriter;
import com.navinfo.data.writer.interfaces.IWriter;

import java.io.*;

public class MidDriver implements IMidDriver {
    protected File file;
    protected File srcFile;
    protected File destFile;

    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    public File getSrcFile() {
        return srcFile;
    }

    @Override
    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    public File getDestFile() {
        return destFile;
    }

    @Override
    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }

    @Override
    public Object call() throws Exception {
        IWriter writer = new MifWriter();
        writer.setSrcDir(srcFile);
        writer.setDestDir(destFile);

        MifMidObject object = new MifMidObject(file.getCanonicalPath().replace(".mid", ".mif"));
        if (!object.isExistFieldName("Z")) {
            writer.copyToDirectory(file);
        } else {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getCanonicalPath()
                    .replace(srcFile.getCanonicalPath(), destFile.getCanonicalPath())), "UTF-8"))) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                    String midStr = null;
                    while ((midStr = br.readLine()) != null) {
                        object.setMid(midStr);
                        bw.write(object.getMidByFieldName("Z"));
                        bw.write("\n");
                    }
                }
            }
        }
        return null;
    }
}
