package edu.soft2.controller;

import edu.soft2.pojo.User;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;

@Controller
@RequestMapping(value = "user")
public class UserController {
    private MultipartFile image;
    private HttpServletRequest request;

    //删除操作
    @RequestMapping(value = "index")
    public String index() {
        System.out.println("----index()----");
        return "forward:/index.jsp";
    }

    //登录
    @RequestMapping(value = "login")
    public String login(User user, HttpSession session) {
        System.out.println("----login()----");
        int flag = 1;//调用业务层,获取返回值
        if (flag == 1) {
            session.setAttribute("user", user);
            return "welcome";
        }
        return "hello";
    }

    @RequestMapping("/Logout")
    public String logout(HttpServletRequest request) {
        System.out.println("----logout----");
        HttpSession session = request.getSession();
        session.invalidate();
        System.out.println("session失效");
        String msg = "你已退出登录！";
        request.setAttribute("msg", msg);
        return "forward:/index.jsp";
//        return "redirect:/index.jsp";
    }

    @RequestMapping(value = "hello")
    public String hello() {
        System.out.println("----hello()----");
        return "hello";
    }

    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public String upload(MultipartFile image,HttpServletRequest request) throws IOException {
        System.out.println("----upload()----");
        //获取文件输入流对象
        InputStream is = image.getInputStream();
        //获取文件名称
        String filename = image.getOriginalFilename();
        //设置上传路径
        String realPath = request.getServletContext().getRealPath("/uploads");
        System.out.println("上传路径=" + realPath);
        //设置上传server后的新名称
        String newname = doFilename(filename);
        OutputStream os = new FileOutputStream(new File(realPath, newname));
        int size = IOUtils.copy(is, os);//完成文件拷贝的大小(字节)
        System.out.println("上传" + filename + "到(" + realPath + ")完毕，共计" + size + "字节,上传后文件名为'" + newname + "'。");
        //资源释放(原则：先开后关，后开先关)
        os.close();
        is.close();
        return "welcome";//提示框
    }
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public String upload() throws IOException {
        System.out.println("----upload()----");
        //获取文件输入流对象
        InputStream is = image.getInputStream();
        //获取文件名称
        String filename = image.getOriginalFilename();
        //设置上传路径
        String realPath = request.getServletContext().getRealPath("/uploads");
        System.out.println("上传路径=" + realPath);
        //设置上传server后的新名称
        String newname = doFilename(filename);
        OutputStream os = new FileOutputStream(new File(realPath, newname));
        int size = IOUtils.copy(is, os);//完成文件拷贝的大小(字节)
        System.out.println("上传" + filename + "到(" + realPath + ")完毕，共计" + size + "字节,上传后文件名为'" + newname + "'。");{
        }
        os.close();is.close(); //资源释放(原则：先开后关，后开先关)
        return "welcome";//提示框
    }

    private String doFilename(String filename) {//获取文件侯磊名
        String extension = FilenameUtils.getExtension(filename);
        String uuid = UUID.randomUUID().toString();
        return uuid+"."+extension;//上传文件的新名称
    }
    @RequestMapping("/download.do/{filename}")
    public void download(@PathVariable String filename,HttpServletRequest request,HttpServletResponse response) throws IOException {
        String realPath=request.getServletContext().getRealPath("/images");
        System.out.println("下载路径"+realPath);
        InputStream is=new FileInputStream(new File(realPath,filename));
        OutputStream os=response.getOutputStream();
        IOUtils.copy(is,os);//拷贝
        os.close();is.close();//io流
    }

}

