package com.aisys.controller.admin;

import com.aisys.dto.JsonResult;
import com.aisys.entity.Page;
import com.aisys.entity.Server;
import com.aisys.entity.User;
import com.aisys.enums.UserRole;
import com.aisys.service.PageService;
import com.aisys.service.ServerService;
import com.aisys.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * @author Jack
 */
@Controller
@RequestMapping("/admin/server")
public class BackServerController {
    @Autowired
    private PageService pageService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private UserService userService;

    private JschUtil returnDefaultServerShell(HttpSession session)  {
        User user = (User) session.getAttribute("user");
        Server server=serverService.getDefaultServerByUserId(user.getUserId());
        JschUtil shell = new JschUtil(server.getIp(),"root", server.getPassword());
        return shell;
    }

    /**
     * 后台页面列表显示
     *
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

    @RequestMapping(value = {"","/xml2pdf"})
    public ModelAndView xml2pdf(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", "xml to pdf");
        modelAndView.setViewName("Admin/Server/xml2pdf");
        return modelAndView;
    }


    @RequestMapping(value = "/shellSubmit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shellSubmit(HttpServletRequest request, HttpSession session) {
        String cmd = request.getParameter("cmd");
        System.out.println("cmd:"+cmd);
        if (cmd == null) {
            return new JsonResult().fail("shell Failed");
        }
        JschUtil shell = returnDefaultServerShell(session);
        if(cmd != null) {
            shell.execute(cmd);
        }
        else shell.execute("uname -s -r -v");
        List<String> shellOutputList = shell.getStandardOutput();
        return new JsonResult().ok(shellOutputList);
    }

    @RequestMapping(value = "/shell")
    public ModelAndView shellPageView(@RequestParam(name="cmd",required = false) String cmd, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        JschUtil shell = returnDefaultServerShell(session);
        if(cmd != null) {
            shell.execute(cmd);
        }
        else shell.execute("uname -s -r -v");
        List<String> shellOutputList = shell.getStandardOutput();
        modelAndView.addObject("outputList", shellOutputList);
        modelAndView.setViewName("Admin/Server/shell");
        return modelAndView;
    }

    @RequestMapping(value = "/gpuinfo")
    public ModelAndView gpuPageView(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        JschUtil shell = returnDefaultServerShell(session);
        shell.execute("nvidia-smi");
        List<String> shellOutputList = shell.getStandardOutput();
        modelAndView.addObject("outputList", shellOutputList);
        modelAndView.addObject("title", "查看显卡");
        modelAndView.setViewName("Admin/Server/gpuinfo");
        return modelAndView;
    }

    @RequestMapping(value = "/memory")
    public ModelAndView memoryView(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        JschUtil shell = returnDefaultServerShell(session);
        shell.execute("free -h");
        List<String> shellOutputList = shell.getStandardOutput();
        modelAndView.addObject("outputList", shellOutputList);
        modelAndView.addObject("title", "内存状态");
        modelAndView.setViewName("Admin/Server/memory");
        return modelAndView;
    }

    @RequestMapping(value = "/harddisk")
    public ModelAndView harddiskView(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        JschUtil shell = returnDefaultServerShell(session);
        shell.execute("df -h");
        List<String> shellOutputList = shell.getStandardOutput();
        modelAndView.addObject("outputList", shellOutputList);
        modelAndView.addObject("title", "查看硬盘");
        modelAndView.setViewName("Admin/Server/harddisk");
        return modelAndView;
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
     * 后台添加用户页面提交
     *
     * @param server
     * @return
     */
    @RequestMapping(value = "/insertSubmit", method = RequestMethod.POST)
    public String insertServerSubmit(Server server,HttpSession session) {
        Server server2 = serverService.getServerByName(server.getName());
        Server server3 = serverService.getServerByIp(server.getIp());
        User user = (User) session.getAttribute("user");
        if (server2 == null && server3 == null) {
            server.setUserId(user.getUserId());
            serverService.addServer(server);
        }
        return "redirect:/admin/server";
    }

    /**
     * 后台添加服务器
     *
     * @return
     */
    @RequestMapping(value = "/insert")
    public ModelAndView insert() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Admin/Server/insert");
        modelAndView.addObject("title", "添加服务器");
        return modelAndView;
    }


    /**
     * 删除页面
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    public String deleteServer(@PathVariable("id") Integer id) {
        //调用service批量删除
        serverService.deleteServer(id);
        return "redirect:/admin/server";
    }

    /**
     * 设为默认服务器
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/setDefault/{id}")
    public String setDefaultServer(@PathVariable("id") Integer id) {
        //调用service查询有无default server
        Server server=serverService.getServerById(id);
        Server default_server=serverService.getDefaultServerByUserId(server.getUserId());

        if(default_server != null) serverService.setDefaultServer(server);
        else serverService.addDefaultServer(server);
        return "redirect:/admin/server";
    }

    /**
     * 编辑页面页面显示
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}")
    public ModelAndView editServer(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();

        Server server = serverService.getServerById(id);
        modelAndView.addObject("server", server);

        modelAndView.setViewName("Admin/Server/edit");
        return modelAndView;
    }


    /**
     * 编辑页面提交
     *
     * @param server
     * @return
     */
    @RequestMapping(value = "/editSubmit", method = RequestMethod.POST)
    public String editServerSubmit(Server server,HttpSession session) {
        User user = (User) session.getAttribute("user");
        server.setUserId(user.getUserId());
        System.out.println("update Server:"+server);
        serverService.updateServer(server);
        return "redirect:/admin/server";
    }


}



