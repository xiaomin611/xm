package com.navinfo.service;

import com.had.zero.common.interfaces.IMidDriver;
import com.navinfo.data.writer.MifWriter;
import com.navinfo.data.writer.interfaces.IWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class App {

    public static void main(String[] args) {
//        String a = "java -jar had-zero-service-1.0.190717.1.jar -in D:\\data\\volvo\\china -out D:\\data\\volvo\\outt";
//        String[] s= {"java","-jar","had-zero-service-1.0.190717.1.jar","-in","D:\\data\\volvo\\china","-out","D:\\data\\volvo\\outt"};
        DeleteManager manager = new DeleteManager();
        manager.startDelete(args);
    }

}
