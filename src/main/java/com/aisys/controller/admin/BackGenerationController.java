package com.aisys.controller.admin;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.aisys.dto.JsonResult;
import com.aisys.entity.Server;
import com.aisys.entity.User;
import com.aisys.service.PageService;
import com.aisys.service.ServerService;
import com.aisys.service.UserService;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Jack
 */
@Controller
@RequestMapping("/admin/generation")
public class BackGenerationController {
    @Autowired
    private PageService pageService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private UserService userService;

    private BlockingQueue<Boolean> generationQueue=new LinkedBlockingQueue<>(1);

    private String realRootPath="";
    
    private static final String srcIP="";  // www.example.com
    private static final String srcUserDir="";  // /home/user

    private JschUtil getDefaultServerShell(HttpSession session)  {
        User user = (User) session.getAttribute("user");
        try {
            Server server=serverService.getDefaultServerByUserId(user.getUserId());
            return new JschUtil(server.getIp(),"root", server.getPassword());
        } catch (Exception e) {
            return null;
        }
    }

    private JschUtil getDefaultServerShell(HttpSession session,int delayTime)  {
        User user = (User) session.getAttribute("user");
        try {
            Server server=serverService.getDefaultServerByUserId(user.getUserId());
            return new JschUtil(server.getIp(),"root", server.getPassword(),delayTime);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 后台页面列表显示
     * @param session
     * @return
     */
    @RequestMapping(value = {"","/list"})
    public ModelAndView index(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        List<Server> serverList;
        if (user.getUserName().equals("admin")) {
            serverList = serverService.listServer();
        } else {
            serverList = serverService.listServerByUserId(user.getUserId());
        }
        Server default_server=serverService.getDefaultServerByUserId(user.getUserId());
        modelAndView.addObject("serverList", serverList);
        modelAndView.addObject("defaultserver", default_server);
        modelAndView.setViewName("Admin/Server/index");
        return modelAndView;
    }


    /**
     *stable diffusion
     * @param cmd
     * @return
     */
    @RequestMapping(value = "/sd")
    public ModelAndView sdPageView(@RequestParam(name="cmd",required = false) String cmd) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", "stable diffusion");
        modelAndView.setViewName("Admin/Generation/sd");
        return modelAndView;
    }

    @RequestMapping(value = "/sdSubmit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult sdSubmit(HttpServletRequest request) {
        if(!generationQueue.offer(true)) {
            System.out.println("server is in use");
            return new JsonResult().fail("服务器正在使用,请稍等5分钟后再试");
        }
        User user = (User) request.getSession().getAttribute("user");
        int userCount=user.getUserCount();
        if(userCount<1) {
            generationQueue.poll();
            return new JsonResult().fail("今日次数已用完，请明日再试");
        }
        user.setUserCount(userCount-1);
        userService.updateUser(user);

        String cmd = request.getParameter("cmd");
        System.out.println("cmd:"+cmd);
        if (cmd == null) {
            return new JsonResult().fail("shell Failed");
        }
        JschUtil shell = getDefaultServerShell(request.getSession());
        if(shell == null) {
            Server server=serverService.getDefaultServerByUserId(userService.getUserByName("admin").getUserId());
            shell = new JschUtil(server.getIp(),"root", server.getPassword());
        }
        LinkedList<String> commands=new LinkedList<>();
        String randomDirName=generateRandomFilename();
        commands.add("cd "+srcUserDir+"/shell && bash stable_diffusion_web.sh \"" + cmd + "\" " + randomDirName + generateRandomSeeds(1));
//        commands.add("conda activate textual_inversion");
//        commands.add("python3 scripts/txt2img.py --ddim_eta 0.0 --W 512       --H 512 " +
//                "  --n_samples 10  --seed 45    " +
//                "  --scale 10.0    --ddim_steps 50 " +
//                "  --embedding_path logs/test.pt   " +
//                "  --ckpt_path models/ldm/text2img-large/model.ckpt   " +
//                "  --prompt "+ cmd +
//                "  --outdir outputs/txt2img-samples/"+randomDirName);

        if(cmd != null) {
            shell.execute(commands, shell.getSession());
        }

        System.out.println("output dir: "+randomDirName);
        List<String> shellOutputList = shell.getStandardOutput();
        String srcServer="";
        String command="scp -P port  root@" + srcIP +":";
        String srcDir=srcUserDir+"/project/earthquakes/stable-diffusion/out/txt2img-samples/"+randomDirName;
        String command2=srcDir+"/samples/*.jpg ";
        if(realRootPath=="") realRootPath= request.getServletContext().getRealPath("/");
        String dstDir=  realRootPath+"/resource/assets/uploads/sd/"+randomDirName;
        runLocalShell("mkdir -p " +dstDir);
        command=command+command2+dstDir;
        System.out.println("execute command:"+command);
        runLocalShell(command);
//        shell.execute("rm -rf " + srcDir);
        generationQueue.poll();

        File dstFile = new File(dstDir);
        LinkedList<String> pics=new LinkedList<>();
        for(File picFile:dstFile.listFiles()) {
            pics.add(randomDirName+"/"+picFile.getName());
        }
        return new JsonResult(0,shellOutputList,pics);
    }

    @RequestMapping(value = "/sdPicDelete", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult sdPicDelete(HttpServletRequest request) {
        String delUrl = request.getParameter("del_url");
        if(delUrl==null) return new JsonResult().fail("del_url is null");
        String dstDir = realRootPath+"/resource/assets/uploads/sd/";
        File dstFile = new File(dstDir + delUrl);
        dstFile.delete();
        return new JsonResult(0, "success",0);
    }

    /**
     *stable diffusion 2
     * @param cmd
     * @return
     */
    @RequestMapping(value = "/sd2")
    public ModelAndView sd2PageView(@RequestParam(name="cmd",required = false) String cmd) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", "stable diffusion2");
        modelAndView.setViewName("Admin/Generation/sd2");
        return modelAndView;
    }

    @RequestMapping(value = "/sd2Submit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult sd2Submit(HttpServletRequest request) {
        if(!generationQueue.offer(true)) {
            System.out.println("server is in use");
            return new JsonResult().fail("服务器正在使用,请稍等5分钟后再试");
        }
        User user = (User) request.getSession().getAttribute("user");
        int userCount=user.getUserCount();
        if(userCount<1) {
            generationQueue.poll();
            return new JsonResult().fail("今日次数已用完，请明日再试");
        }
        user.setUserCount(userCount-1);
        userService.updateUser(user);

        String cmd = request.getParameter("cmd");
        System.out.println("cmd:"+cmd);
        if (cmd == null) {
            return new JsonResult().fail("shell Failed");
        }
        JschUtil shell = getDefaultServerShell(request.getSession());
        if(shell == null) {
            Server server=serverService.getDefaultServerByUserId(userService.getUserByName("admin").getUserId());
            shell = new JschUtil(server.getIp(),"root", server.getPassword());
        }
        LinkedList<String> commands=new LinkedList<>();
        String randomDirName=generateRandomFilename();
        commands.add("cd "+srcUserDir+"/shell && bash stable_diffusion2_web.sh \"" + cmd + "\" " + randomDirName);

        if(cmd != null) {
            shell.execute(commands, shell.getSession());
        }

        System.out.println("output dir: "+randomDirName);
        List<String> shellOutputList = shell.getStandardOutput();

        String command="scp -P port  root@"+srcIP+":";
        String srcDir=srcUserDir+"/project/earthquakes/stable-diffusion-v2/out/txt2img-samples/"+randomDirName;
        String command2=srcDir +"/samples/*.jpg ";
        if(realRootPath=="") realRootPath= request.getServletContext().getRealPath("/");

        String dstDir= realRootPath+"/resource/assets/uploads/sd2/"+randomDirName;
        runLocalShell("mkdir -p "+dstDir);
        command=command+command2+dstDir;
        System.out.println("execute command:"+command);
        runLocalShell(command);
        shell.execute("rm -rf "+srcDir);
        generationQueue.poll();

        File dstFile = new File(dstDir);
        LinkedList<String> pics=new LinkedList<>();
        for(File picFile:dstFile.listFiles()) {
            pics.add(randomDirName+"/"+picFile.getName());
        }
        return new JsonResult(0,shellOutputList,pics);
    }

    @RequestMapping(value = "/sd2PicDelete", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult sd2PicDelete(HttpServletRequest request) {
        String delUrl = request.getParameter("del_url");
        if(delUrl==null) return new JsonResult().fail("del_url is null");
        String dstDir = realRootPath+"/resource/assets/uploads/sd2/";
        File dstFile = new File(dstDir + delUrl);
        dstFile.delete();
        return new JsonResult(0, "success",0);
    }


    /**
     *Projected GAN
     * @param cmd
     * @return
     */
    @RequestMapping(value = "/pg")
    public ModelAndView pgPageView(@RequestParam(name="cmd",required = false) String cmd, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", "Projected GAN");
        modelAndView.setViewName("Admin/Generation/projected-gan");
        System.out.println(request.getServletContext().getRealPath("/"));
        System.out.println(request.getServletPath());
        return modelAndView;
    }


    @RequestMapping(value = "/pgSubmit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult pgSubmit(HttpServletRequest request) {
        if(!generationQueue.offer(true)) {
            System.out.println("server is in use");
            return new JsonResult().fail("服务器正在使用,请稍等5分钟后再试");
        }
        User user = (User) request.getSession().getAttribute("user");
        int userCount=user.getUserCount();
        if(userCount<1) {
            generationQueue.poll();
            return new JsonResult().fail("今日次数已用完，请明日再试");
        }
        user.setUserCount(userCount-1);
        userService.updateUser(user);

        String cmd = request.getParameter("cmd");
        System.out.println("pgSubmit: cmd:"+cmd);
        cmd="";

        JschUtil shell = getDefaultServerShell(request.getSession());
        System.out.println("pgSubmit: shell: "+shell);
        if(shell == null) {
            Server server=serverService.getDefaultServerByUserId(userService.getUserByName("admin").getUserId());
            shell = new JschUtil(server.getIp(),"root", server.getPassword());
        }
        LinkedList<String> commands=new LinkedList<>();
        String randomDirName=generateRandomFilename();


        commands.add("cd "+srcUserDir+"/shell && bash projected_gan_web.sh " + randomDirName+generateRandomSeeds());
        if(cmd != null) { shell.execute(commands, shell.getSession()); }
        System.out.println("output dir: "+randomDirName);
        List<String> shellOutputList = shell.getStandardOutput();

        String command="scp -P port  root@"+srcIP+":";
        String srcDir=srcUserDir+"/project/earthquakes" +
                "/projected-gan/out/00006-fastgan_lite-less1024_all_noman-batch4-best/"
                +randomDirName;
        String command2=srcDir +"/*.jpg ";
        if(realRootPath=="") realRootPath= request.getServletContext().getRealPath("/");

        String dstDir= realRootPath+"/resource/assets/uploads/pg/"+randomDirName;
        File dstFile = new File(dstDir);
        dstFile.mkdirs();
        command=command+command2+dstDir;
        System.out.println("execute command:"+command);
        runLocalShell(command);
        shell.execute("rm -rf "+srcDir);
        generationQueue.poll();

        LinkedList<String> pics=new LinkedList<>();
        for(File picFile:dstFile.listFiles()) {
            pics.add("/uploads/pg/"+randomDirName+"/"+picFile.getName());
        }
        return new JsonResult(0,shellOutputList,pics);
    }

    @RequestMapping(value = "/pgPicDelete", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult pgPicDelete(HttpServletRequest request) {
        String delUrl = request.getParameter("del_url");
        if(delUrl==null) return new JsonResult().fail("del_url is null");
        String dstDir = realRootPath+"/resource/assets/uploads/pg/";
        File dstFile = new File(dstDir + delUrl);
        dstFile.delete();
        return new JsonResult(0, "success",0);
    }

    /**
     *Projected GAN
     * @param cmd
     * @return
     */
    @RequestMapping(value = "/fasterPg")
    public ModelAndView fasterPgPageView(@RequestParam(name="cmd",required = false) String cmd) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", "Faster Projected GAN");
        modelAndView.setViewName("Admin/Generation/faster_projected_gan");
        return modelAndView;
    }

    @RequestMapping(value = "/fasterPgSubmit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult fasterPgSubmit(HttpServletRequest request) {
        if(!generationQueue.offer(true)) {
            System.out.println("server is in use");
            return new JsonResult().fail("服务器正在使用,请稍等5分钟后再试");
        }
        User user = (User) request.getSession().getAttribute("user");
        int userCount=user.getUserCount();
        if(userCount<1) {
            generationQueue.poll();
            return new JsonResult().fail("今日次数已用完，请明日再试");
        }
        user.setUserCount(userCount-1);
        userService.updateUser(user);

        String cmd = request.getParameter("cmd");
        cmd = "";
        JschUtil shell = getDefaultServerShell(request.getSession());
        if(shell == null) {
            Server server=serverService.getDefaultServerByUserId(userService.getUserByName("admin").getUserId());
            shell = new JschUtil(server.getIp(),"root", server.getPassword());
        }
        LinkedList<String> commands=new LinkedList<>();
        String randomDirName=generateRandomFilename();
        commands.add("cd "+srcUserDir+"/shell && bash faster_projected_gan_web.sh " + randomDirName+generateRandomSeeds());
        if(cmd != null) { shell.execute(commands, shell.getSession()); }
        System.out.println("output dir: "+randomDirName);
        List<String> shellOutputList = shell.getStandardOutput();

        String command="scp -P port  root@"+srcIP+":";
        String srcDir=srcUserDir+"/project/earthquakes/projected-gan-upblock-dsc/out/00002-fastgan_lite-less1024_all_noman-gpus1-batch4/"
                +randomDirName;
        String command2=srcDir +"/*.jpg ";
        if(realRootPath=="") realRootPath= request.getServletContext().getRealPath("/");

        String dstDir= realRootPath+"/resource/assets/uploads/faster_pg/"+randomDirName;
        File dstFile = new File(dstDir);
        dstFile.mkdirs();
        command=command+command2+dstDir;
        System.out.println("execute command:"+command);
        runLocalShell(command);
        shell.execute("rm -rf "+srcDir);
        generationQueue.poll();

        LinkedList<String> pics=new LinkedList<>();
        for(File picFile:dstFile.listFiles()) {
            pics.add("/uploads/faster_pg/"+randomDirName+"/"+picFile.getName());
        }
        return new JsonResult(0,shellOutputList,pics);
    }

    @RequestMapping(value = "/fasterPgPicDelete", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult fasterPgPicDelete(HttpServletRequest request) {
        String delUrl = request.getParameter("del_url");
        if(delUrl==null) return new JsonResult().fail("del_url is null");
        String dstDir = realRootPath+"/resource/assets/uploads/faster_pg/";
        File dstFile = new File(dstDir + delUrl);
        dstFile.delete();
        return new JsonResult(0, "success",0);
    }

    private void runLocalShell(String command) {
        try {
            Process proc = Runtime.getRuntime().exec(command); //Whatever you want to execute
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            try {
                proc.waitFor();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            while (read.ready()) {
                System.out.println(read.readLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "/checkServerName", method = RequestMethod.POST, produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String checkServerName(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String name = request.getParameter("name");
        Server server = serverService.getServerByName(name);
        //用户名已存在,但不是当前用户(编辑用户的时候，不提示)
        if (server != null) {
            if (server.getName() != name) {
                map.put("code", 1);
                map.put("msg", "用户名已存在！");
            }
        } else {
            map.put("code", 0);
            map.put("msg", "");
        }
        String result = new JSONObject(map).toString();
        return result;
    }

    /**
     * 检查IP是否存在
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/checkServerIp", method = RequestMethod.POST, produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String checkServerIp(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String ip = request.getParameter("ip");
        int port = Integer.parseInt(request.getParameter("port"));
        Server server = serverService.getServerByIp(ip);
        //用户名已存在,但不是当前用户(编辑用户的时候，不提示)
        if (server != null) {
            if (server.getPort() == port) {
                map.put("code", 1);
                map.put("msg", "服务器端口已存在！");
            }
        } else {
            map.put("code", 0);
            map.put("msg", "");
        }
        String result = new JSONObject(map).toString();
        return result;
    }

    /**
     * 随机生成文件夹名
     * @return
     */
    public static String generateRandomFilename(){
        String RandomFilename = "";
        Random rand = new Random();//生成随机数
        int random = rand.nextInt();

        Calendar calCurrent = Calendar.getInstance();
        int intDay = calCurrent.get(Calendar.DATE);
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        int intHour= calCurrent.get(Calendar.HOUR);
        int intMinute=calCurrent.get(Calendar.MINUTE);
        int intSecond= calCurrent.get(Calendar.SECOND);
        int intMillisecond=calCurrent.get(Calendar.MILLISECOND);
        String now = String.valueOf(intYear) + "_" + String.valueOf(intMonth) + "_" +
                String.valueOf(intDay) + "_"+String.valueOf(intHour)+
                String.valueOf(intMinute)+String.valueOf(intSecond)+
                String.valueOf(intMillisecond);

        RandomFilename = now + String.valueOf(random > 0 ? random : ( -1) * random);

        return RandomFilename;
    }

    public static String generateRandomSeeds(int ...counts) {
        int count=21;
        for(int acount:counts) {
            count=acount;
            break;
        }
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        stringBuilder.append(' ');
        int randInt;
        for(int i=0;i<count-1;++i) {
            randInt=random.nextInt();
            stringBuilder.append(randInt>0?randInt:(-1)*randInt);
            stringBuilder.append(',');
        }
        randInt=random.nextInt();
        stringBuilder.append(randInt>0?randInt:(-1)*randInt);
        return stringBuilder.toString();
    }
}



