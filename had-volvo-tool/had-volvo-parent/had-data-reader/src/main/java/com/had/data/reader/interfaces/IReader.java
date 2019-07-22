package com.had.data.reader.interfaces;

import java.io.File;

public interface IReader {

    /**
     * 从文件中获取对象信息
     * @param src 文件地址
     */
    Object reader(File src);



}
