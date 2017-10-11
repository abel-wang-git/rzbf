package com.heihei.ctrl;
import com.alibaba.fastjson.JSON;
import com.heihei.Constant;
import com.heihei.croe.Dbconnect;
import com.heihei.croe.MyStartupRunner;
import com.heihei.croe.Shell;
import com.heihei.croe.Util;
import com.heihei.po.Datasoruce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class Cat {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/admin")
    public String toadmin(Model model) {
        model.addAttribute("datasource", MyStartupRunner.source);
        for (int i =0 ;i<2;i++) {
            try {
                Connection conn=Dbconnect.dbConnect(MyStartupRunner.source.get(i));
                List dbstat= Dbconnect.query(conn,"select status,INSTANCE_NAME from v$instance");
                model.addAttribute(MyStartupRunner.source.get(i).getName(),dbstat);
                model.addAttribute(MyStartupRunner.source.get(i).getName()+"statlist",status(MyStartupRunner.source.get(i)));
                model.addAttribute(MyStartupRunner.source.get(i).getName()+"lsnrstat",lsnrstat(MyStartupRunner.source.get(i)));
                model.addAttribute(MyStartupRunner.source.get(i).getName()+"mgrstat",mgr(MyStartupRunner.source.get(i)));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "admin";
    }

    @GetMapping(value = "/cat/{id}")
    public String Cat(@PathVariable int id, Model model) {
        Connection connect = null;
        model.addAttribute("conid",id);
        try {
            connect = Dbconnect.dbConnect(Dbconnect.getDataSource(id));
            List tablespace = Dbconnect.query(connect, Constant.tablespace);

            model.addAttribute("tablespace", tablespace);

            List psu = Dbconnect.query(connect, Constant.psu);

            model.addAttribute("psu", psu);

            List resourceLimit = Dbconnect.query(connect, Constant.resourceLimit);

            model.addAttribute("resourceLimit", resourceLimit);


            List envnt = Dbconnect.query(connect,Constant.envnt);

            model.addAttribute("envnt",envnt);

            List isBadBlock = Dbconnect.query(connect, Constant.isBadBlock);

            model.addAttribute("isBadBlock", isBadBlock);
        } catch (Exception e) {
            logger.error(Util.getMessage(e));
            logger.error("数据库链接失败");
            return "error";
        } finally {
            try {
                connect.close();
            } catch (SQLException e) {
                logger.error(Util.getMessage(e));
            }
        }
        return "detail";
    }
    @PostMapping(value = "/cat/cpu")
    @ResponseBody
    public String  cpu(Model model){
        Shell sshExecutor = new Shell(MyStartupRunner.source.get(1).getIP(), "oracle", "oracle");
        String c= sshExecutor.execute("vmstat |awk 'NR==3{print $15}'");
        return c.trim();
    }

    @PostMapping(value = "/cat/mem")
    @ResponseBody
    public String  mem(Model model){
        Shell sshExecutor = new Shell(MyStartupRunner.source.get(1).getIP(), "oracle", "oracle");
        String c= sshExecutor.execute("free -m | grep Mem |awk  '{printf(\"%.2f\",($3-$7)/$2*100)}'");
        return c.trim();
    }

    private List<String[]> status(Datasoruce s){
        List<String[]> list = new ArrayList<>();
        Shell sshExecutor = new Shell(s.getIP(), s.getSysUser(), s.getSysPwd());
        String c= sshExecutor.execute("source /home/oracle/.bash_profile&&/u01/ogg/ggsci <<EOF \n" +
                "info all \n" +
                "EOF");

        String[] strings= c.split("\\n");
        for(int i=14;i<strings.length-3 ;i++){

            String[] str =strings[i].split("    ");
            logger.info(JSON.toJSONString(str));
            list .add(str);
        }
        return list;
    }

    private String[] mgr(Datasoruce s){
        Shell sshExecutor = new Shell(s.getIP(), s.getSysUser(), s.getSysPwd());
        String c= sshExecutor.execute("source /home/oracle/.bash_profile&&/u03/zb/ggsci <<EOF \n" +
                "info all \n" +
                "EOF");
        String[] strings= c.split("\\n");
        String[] str =strings[13].split("    ");
        logger.info(JSON.toJSONString(str));
        return str ;
    }

    private String lsnrstat(Datasoruce s){
        Shell sshExecutor = new Shell(s.getIP(), s.getSysUser(), s.getSysPwd());
        String c= sshExecutor.execute(" source /home/oracle/.bash_profile&&lsnrctl status |grep Start");
        return c.trim();

    }

    @GetMapping(value = "/sys/{id}")
    public String CatSys(@PathVariable int id, Model model) {
        Connection connect = null;
        try {
            connect = Dbconnect.dbConnect(Dbconnect.getDataSource(id));
            List instancestat = Dbconnect.query(connect, Constant.instanceStatus);

            model.addAttribute("instat", instancestat);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Shell shell = new Shell(Dbconnect.getDataSource(id));
        String mem = shell.execute(Constant.catMemory);

        model.addAttribute("sysMem", mem);

        String cpu = shell.execute(Constant.catCpu);

        model.addAttribute("cpu", cpu);

        String disk = shell.execute(Constant.disk);
        List<String> str = Arrays.asList(disk.split("\n"));
        List<String[]> dis = new ArrayList<String[]>();
        for (int i = 0; i < str.size(); i++) {
            dis.add(str.get(i).split(","));
        }
        model.addAttribute("disk", dis);

        String lsnrstat = shell.execute(Constant.lsnrctl);

        model.addAttribute("lsnrstat",(lsnrstat==null||lsnrstat.trim().equals(""))?"stop":"start");
        return "sysDetail";
    }

    @GetMapping(value = "/base/{id}")
    public String catBase(@PathVariable int id, Model model){
        Connection connect = null;
        model.addAttribute("conid",id);
        try {
            connect = Dbconnect.dbConnect(Dbconnect.getDataSource(id));
            List datafile = Dbconnect.query(connect, Constant.datafile);
            model.addAttribute("datafile", datafile);

            List segment = Dbconnect.query(connect, Constant.segment);

            model.addAttribute("segment", segment);

            List sga = Dbconnect.query(connect, Constant.sga);

            model.addAttribute("sga", sga);

            List pga = Dbconnect.query(connect, Constant.pga);

            model.addAttribute("pga", pga);

            List dataName = Dbconnect.query(connect, Constant.databaseName);

            model.addAttribute("dataName", dataName);

            List instance = Dbconnect.query(connect, Constant.instanceName);

            model.addAttribute("instance", instance);

            List version = Dbconnect.query(connect, Constant.version);

            model.addAttribute("version", version);
            List session = Dbconnect.query(connect, Constant.session);

            model.addAttribute("sessiondb", session);

            List avticeSession = Dbconnect.query(connect, Constant.avtiveSession);

            model.addAttribute("avticese", avticeSession);

            List sessionGroupUser = Dbconnect.query(connect, Constant.sessionGroupUser);

            model.addAttribute("sessGrop", sessionGroupUser);

            List memory = Dbconnect.query(connect, Constant.memory);

            model.addAttribute("memory", memory);

            List dbid = Dbconnect.query(connect, Constant.dbid);

            model.addAttribute("dbid", dbid);

            List contronfile = Dbconnect.query(connect, Constant.controlfile);

            model.addAttribute("controlfile", contronfile);

            List chart = Dbconnect.query(connect, Constant.cahrt);

            model.addAttribute("chart", chart);

            List nchart = Dbconnect.query(connect, Constant.ncahrt);

            model.addAttribute("nchart", nchart);

            List logSize = Dbconnect.query(connect, Constant.logSize);

            model.addAttribute("logSize", logSize);

            List logCount = Dbconnect.query(connect, Constant.logCount);

            model.addAttribute("logCount", logCount);

            List logMember = Dbconnect.query(connect, Constant.logMember);

            model.addAttribute("logMember", logMember);

            List logMode = Dbconnect.query(connect, Constant.logMode);

            model.addAttribute("logMode", logMode);

            List archFile = Dbconnect.query(connect, Constant.archFile);

            model.addAttribute("archFile", archFile);

        }catch (Exception e){
            logger.error(Util.getMessage(e));
        }finally {
            try {
                connect.close();
            } catch (SQLException e) {
                logger.error(Util.getMessage(e));
            }
        }
        return "baseInfo";
    }

}
