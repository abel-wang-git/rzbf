package com.heihei.croe;


import com.alibaba.fastjson.JSON;
import com.heihei.po.Datasoruce;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghuiwen on 17-2-12.
 * 服务启动执行
 */
@Component
//@Order(value=2) 多个CommandLineRunner时 控制顺序
public class MyStartupRunner implements CommandLineRunner {
    public static  final List<Datasoruce> source = new ArrayList<Datasoruce>();

    public  static  final List<String > IP= new ArrayList<>();
    public static  String conf=null;

    public void run(String... strings) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(System.getProperty("user.dir")+System.getProperty("file.separator")+"datasource.conf"),"UTF-8"));
        String s;
        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            String[] dbs=s.split(",");

            Datasoruce d= new Datasoruce();
            d.setName(dbs[0]);
            d.setIP(dbs[1]);
            d.setProt(dbs[2]);
            d.setSid(dbs[3]);
            d.setUserName(dbs[4]);
            d.setPasswd(dbs[5]);
            d.setId((Integer.parseInt(dbs[6])));
            d.setSysUser(dbs[7]);
            d.setSysPwd(dbs[8]);
            source.add(d);
        }
        System.out.print(JSON.toJSON(source));
    }
}
