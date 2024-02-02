package com.aisys.controller.admin;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JschUtil {
    //远程主机的ip地址
    private String ip;
    //远程主机登录用户名
    private String username;
    //远程主机的登录密码
    private String password;
    //等待返回时间
    private int delayTime;
    //设置ssh连接的远程端口
    public static final int DEFAULT_SSH_PORT = 22;
    //保存输出内容的容器
    private ArrayList<String> stdout;

    /**
     * 初始化登录信息
     * @param ip
     * @param username
     * @param password
     */
    public JschUtil(final String ip, final String username, final String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.delayTime=0;
//        stdout = new ArrayList<String>();
    }

    public JschUtil(final String ip, final String username, final String password,int delayTime) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.delayTime=delayTime;
//        stdout = new ArrayList<String>();
    }
    /**
     * 执行shell命令
     * @param command
     * @return
     */
    public int execute(final String command)  {
        int returnCode = 0;
        JSch jsch = new JSch();

        MyUserInfo userInfo = new MyUserInfo();

        try {
            //创建session并且打开连接，因为创建session之后要主动打开连接
            Session session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);

            if( password == null) {
                jsch.setKnownHosts("C:\\Users\\VULAN\\.ssh/known_hosts");
                jsch.addIdentity("C:\\Users\\VULAN\\.ssh2/id_rsa");
            } else {
                session.setPassword(password);
            }

            session.setUserInfo(userInfo);
            session.connect();

            //打开通道，设置通道类型，和执行的命令
            Channel channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec)channel;
            channelExec.setCommand(command);

            channelExec.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader
                    (channelExec.getInputStream()));

            channelExec.connect();
            System.out.println("The remote command is :" + command);

            //接收远程服务器执行命令的结果
            String line;
            stdout = new ArrayList<>();
            TimeUnit.SECONDS.sleep(delayTime);
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();

            // 得到returnCode
            if (channelExec.isClosed()) {
                returnCode = channelExec.getExitStatus();
            }

            // 关闭通道
            channelExec.disconnect();
            //关闭session
            session.disconnect();
            System.out.println("channel closed !");

        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            System.out.println(e.getMessage());
        }
        return returnCode;
    }

    public int execute(final List<String> commands,Session session)  {
        int returnCode = 0;
        JSch jsch = new JSch();

        MyUserInfo userInfo = new MyUserInfo();

        try {
            //创建session并且打开连接，因为创建session之后要主动打开连接
            if(session==null) {
                session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);

                if( password == null) {
                    jsch.setKnownHosts("C:\\Users\\VULAN\\.ssh/known_hosts");
                    jsch.addIdentity("C:\\Users\\VULAN\\.ssh2/id_rsa");
                } else {
                    session.setPassword(password);
                }

                session.setUserInfo(userInfo);
                session.connect();
             }

            //打开通道，设置通道类型，和执行的命令
            Channel channel = session.openChannel("exec");
            stdout = new ArrayList<>();
            for(String command:commands) {
                ChannelExec channelExec = (ChannelExec)channel;
                channelExec.setCommand(command);

                channelExec.setInputStream(null);
                BufferedReader input = new BufferedReader(new InputStreamReader
                        (channelExec.getInputStream()));

                channelExec.connect();
                System.out.println("The remote command is :" + command);

                TimeUnit.SECONDS.sleep(delayTime);
                //接收远程服务器执行命令的结果
                String line;
                while ((line = input.readLine()) != null) {
                    stdout.add(line);
                    System.out.println(line);
                }
                input.close();

                // 得到returnCode
                if (channelExec.isClosed()) {
                    returnCode = channelExec.getExitStatus();
                    System.out.println("exit status:"+returnCode);
                }

                // 关闭通道
                channelExec.disconnect();
                System.out.println("channel closed!");
            }

            //关闭session
            session.disconnect();

        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnCode;
    }

    public Session getSession(){
        try{
            JSch jsch = new JSch();
            MyUserInfo userInfo = new MyUserInfo();
            //创建session并且打开连接，因为创建session之后要主动打开连接
            Session session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);

            if( password == null) {
                jsch.setKnownHosts("C:\\Users\\VULAN\\.ssh/known_hosts");
                jsch.addIdentity("C:\\Users\\VULAN\\.ssh2/id_rsa");
            } else {
                session.setPassword(password);
            }

            session.setUserInfo(userInfo);
            session.connect();
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * get stdout
     * @return
     */
    public ArrayList<String> getStandardOutput() {
        return stdout;
    }

    public void printOutput() {
        System.out.println("outputs:");
        for (String str : this.stdout) {
            System.out.println(str);
        }
    }


    @Override
    public String toString() {
        return "JschUtil{" +
                "ip='" + ip + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", delayTime=" + delayTime +
                ", stdout=" + stdout +
                '}';
    }

}
