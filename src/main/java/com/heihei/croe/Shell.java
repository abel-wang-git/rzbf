package com.heihei.croe;

import com.heihei.po.Datasoruce;
import com.heihei.po.SysUserInfo;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

public class Shell {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String ipAddress;

    private String username;

    private String password;

    public static final int DEFAULT_SSH_PORT = 22;

    private Vector<String> stdout;

    public Shell(final String ipAddress, final String username, final String password) {
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = password;
        stdout = new Vector<String>();
    }
    public Shell(Datasoruce datasoruce){
        this.ipAddress=datasoruce.getIP();
        this.username=datasoruce.getSysUser();
        this.password=datasoruce.getSysPwd();
        stdout=new Vector<String>();
    }

    public String execute(final String command) {
        int returnCode = 0;
        JSch jsch = new JSch();
        SysUserInfo userInfo = new SysUserInfo();
        StringBuffer str = new StringBuffer();

        try {
            // Create and connect session.
            Session session = jsch.getSession(username, ipAddress, DEFAULT_SSH_PORT);
            session.setPassword(password);
            session.setUserInfo(userInfo);
            session.setConfig("userauth.gssapi-with-mic", "no");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Create and connect channel.
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(channel
                    .getInputStream()));

            channel.connect();
            logger.info(command);

            // Get the output of remote command.
            String line;
            while ((line = input.readLine()) != null) {
                str.append(line+'\n');
            }

            input.close();

            // Get the return code only after the channel is closed.
            if (channel.isClosed()) {
                returnCode = channel.getExitStatus();
            }

            // Disconnect the channel and session.
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            logger.error(Util.getMessage(e));
        }
        return str.toString();
    }

    public Vector<String> getStandardOutput() {
        return stdout;
    }

    public static void main(final String [] args) {
        Shell sshExecutor = new Shell("192.168.88.252", "oracle", "oracle");
        String s=sshExecutor.execute("source /home/oracle/.bash_profile&&/u01/ogg/ggsci <<EOF \n" +
                "info all \n" +
                "EOF");
       String[] strings= s.split("\\n");
       for(int i=12;i<strings.length-3 ;i++){
           System.out.println(strings[i].split(" "));
       }
    }
}
