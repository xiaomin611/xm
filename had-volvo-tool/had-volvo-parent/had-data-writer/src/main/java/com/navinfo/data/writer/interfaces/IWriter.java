package com.navinfo.data.writer.interfaces;

import java.io.File;
import java.io.IOException;

public interface IWriter {

    /**
     * 设置源目录
     * @param srcDir 最上一级目录，源目录
     */
    void setSrcDir(File srcDir);

    /**
     * 设置目的目录
     * @param destDir 最上一级目录，目的目录
     */
    void setDestDir(File destDir);

    /**
     * 清空目录但不删除目录
     */
    void cleanDirectory() throws IOException;

    /**
     * 将文件或目录复制到另一个目录中，保留文件日期。
     * @param src 文件地址
     */
    void copyToDirectory(File src) throws IOException;

    /**
     * 将对象信息写入文件中
     * @param object 对象信息
     */
    void writer(Object object) throws IOException;

}
