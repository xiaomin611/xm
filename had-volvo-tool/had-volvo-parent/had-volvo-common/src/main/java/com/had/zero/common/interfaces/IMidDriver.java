package com.had.zero.common.interfaces;

import java.io.File;
import java.util.concurrent.Callable;

public interface IMidDriver extends Callable<Object> {

    void setFile(File file);

    void setSrcFile(File file1);

    void setDestFile(File file2);

}
