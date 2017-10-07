package com.heihei.croe;



import com.heihei.po.Datasoruce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dbconnect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static Connection dbConnect(Datasoruce datasoruce) throws ClassNotFoundException, SQLException {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            //根据数据库连接字符，名称，密码给conn赋值
            System.out.println("jdbc:oracle:thin:@//" + datasoruce.getIP() + ":" + datasoruce.getProt() + "/" + datasoruce.getSid());
            return DriverManager.getConnection("jdbc:oracle:thin:@" + datasoruce.getIP() + ":" + datasoruce.getProt() + ":" + datasoruce.getSid()
                    , datasoruce.getUserName(), datasoruce.getPasswd());

    }

    public static Datasoruce getDataSource(int id) {
        Datasoruce datasoruce = null;
        for (Datasoruce s : MyStartupRunner.source) {
            if (s.getId() == id) datasoruce = s;
        }
        return datasoruce;
    }

    public static List query(Connection connection,String sql) throws SQLException {
        System.out.println(sql);
        List listOfRows = new ArrayList();
        ResultSet res=null;
        Statement s=null;
        s= connection.createStatement();
        res= s.executeQuery(sql);
        ResultSetMetaData md = res.getMetaData();
        int num = md.getColumnCount();
        while (res.next()) {
            Map mapOfColValues = new HashMap(num);
            for (int i = 1; i <= num; i++) {
                mapOfColValues.put(md.getColumnName(i), res.getObject(i));
            }
            listOfRows.add(mapOfColValues);
        }
        res.close();
        s.close();
        return listOfRows;
    }
}
