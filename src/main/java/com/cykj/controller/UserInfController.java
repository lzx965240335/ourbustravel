package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.cykj.bean.ChatInf;
import com.cykj.bean.UserInf;
import com.cykj.service.UserInfService;
import com.cykj.util.AdminChatJson;
import com.cykj.util.UserChatJson;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/userMain")
public class UserInfController {

    @Autowired
    private UserInfService userInfService;


    @RequestMapping(value = "/getUserTable")
    @ResponseBody
    public LayuiJson getTableData(HttpServletRequest req){
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String userName = req.getParameter("userName");
        int start = (page-1)*limit;
        HashMap<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("end",limit);
        if (userName != null && !userName.equals("")){
            map.put("userName",userName);
        }
        LayuiJson LayuiJson = userInfService.selectUser(map);
        return LayuiJson;
    }


    //返回页面方式
    @RequestMapping(value = "/getView")
    public String getView(){
        return "login";
    }
    //post请求传递对象
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestBody @Validated UserInf userInf, BindingResult br){
        System.out.println("我要来登录了");
        System.out.println(userInf.getAccount());
        if (br.hasErrors()){
            List<ObjectError> errors = br.getAllErrors();
            StringBuffer sb = new StringBuffer();
            for (ObjectError error : errors){
                sb.append(error.getDefaultMessage()+" ");
                System.out.println(error.getDefaultMessage());
            }
            return JSON.toJSONString(sb);
        }
        int result = userInfService.login(userInf);
        return JSON.toJSONString(result);
    };

    @RequestMapping("/codeLogin")
    @ResponseBody
    public String codeLogin(String phoneNum, String verificationCode, HttpSession session){
        System.out.println("我要来登录了");
        String code = (String) session.getAttribute("verificationCode");
        if (code.equals(verificationCode)){
            UserInf userInf = new UserInf();
            userInf.setPhoneNum(phoneNum);
            int result = userInfService.login(userInf);
            return JSON.toJSONString(result);
        }
        return "";
    }
    @RequestMapping("/backstage")
    public String getBackstage() {
        System.out.println("进来后台了");
        return "backstage";
    }

//    @RequestMapping("/preview")
//    public String getpreview() {
//        System.out.println("preview");
//        return "preview";
//    }

    @RequestMapping("/map")
    public String getqt() {
        System.out.println("进来map.......");
        return "map";
    }
    @RequestMapping("/wallet")
    public String getwallet() {
        System.out.println("进来我的钱包界面");
        return "wallet";
    }
    @RequestMapping("/userInf")
    public String getUserInf(Model model) {
        System.out.println("进来获取UserInf界面");
        model.addAttribute("userInf",userInfService.getLoginner());
        return "UserInf";
    }

    @RequestMapping("/updatePwdView")
    public String getUpdatePwd() {
        System.out.println("进来获取修改密码界面");
        return "updatePwd";
    }
    @RequestMapping("/getLoginner")
    @ResponseBody
    public UserInf getLoginner(){
        System.out.println("进来获取用户");
        return userInfService.getLoginner();
    }
    @RequestMapping("/verification")
    @ResponseBody
    public String getVerificationCode(String phoneNum , HttpSession session){
        System.out.println("进来获取验证码");
        return userInfService.sendMSG(phoneNum , session);
    }
    @RequestMapping("/register")
    @ResponseBody
    public String register(String phoneNum , String code , String password , HttpSession session){
        System.out.println("进来注册账号");
        System.out.println(phoneNum);
        return userInfService.register(phoneNum , code , password , session);
    }
    @RequestMapping("/updatePwd")
    @ResponseBody
    public String updatePwd(String phoneNum , String code , String password , HttpSession session){
        System.out.println("进来修改密码");
        return userInfService.updatePwd(phoneNum , code , password , session);
    }
    @RequestMapping("/upAvatar")
    @ResponseBody
    public String upAvatar(HttpServletRequest request, MultipartFile uploadFile, HttpSession session){
        System.out.println("进来上传头像");
        userInfService.upLoadFile(request,uploadFile,session);
        return "";
    }
    @RequestMapping("/updateUserInf")
    @ResponseBody
    public String updateUserInf(UserInf userInf){
        System.out.println("进来修改个人信息");
        System.out.println(JSON.toJSONString(userInf));
        String result = userInfService.updateUserInf(userInf);
        return result;
    }


    @RequestMapping("/upladFile")
    @ResponseBody
    public synchronized void upladFile(MultipartHttpServletRequest request,HttpSession session){
        MultipartHttpServletRequest multipartRequest =  request;
        /** 构建文件保存的目录* */
        UserInf userInf =(UserInf)session.getAttribute("loginner");
        String logoPathDir = "/"+userInf.getAccount()+"/";
        /** 得到文件保存目录的真实路径* */
        String logoRealPathDir = request.getSession().getServletContext()
                .getRealPath(logoPathDir);
        /** 根据真实路径创建目录* */
        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists())
            logoSaveFile.mkdirs();
        /** 页面控件的文件流* */
        MultipartFile multipartFile = multipartRequest.getFile("file");
        /** 获取文件的后缀* */
        String suffix = multipartFile.getOriginalFilename().substring(
                multipartFile.getOriginalFilename().lastIndexOf("."));
        /** 使用UUID生成文件名称* */
        String logImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
        /** 拼成完整的文件保存路径加文件* */
        String fileName = null;
        try {
            fileName = ResourceUtils.getURL("classpath:").getPath() + "static/imgs/" + logImageName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        userInf.setAvatar("/imgs/"+logImageName);
        userInfService.updateUserInf(userInf);
        System.out.println("upload-》文件保存全路径" + fileName);
        File file = new File(fileName);
        try {
            Map map = new HashMap<String,String>();
            map.put("code","0");
            map.put("msg","");
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/userChat")
    @ResponseBody
    public String userChat(HttpSession session){
        System.out.println("获取聊天界面");
        String result =JSON.toJSONString(new UserChatJson(session));
        return result;
    }
    @RequestMapping("/adminChat")
    @ResponseBody
    public String adminChat(HttpSession session){
        System.out.println("获取聊天界面");
        String result =JSON.toJSONString(new AdminChatJson(session));
        System.out.println(result);
        return result;
    }
    @RequestMapping(value = "/chatLoad")
    @ResponseBody
    public String  chatLoad(int userId){
        System.out.println(JSON.toJSONString(userId));
        return userInfService.chatLoad(userId);
    }
    @RequestMapping(value = "/chatLoadOne")
    @ResponseBody
    public String  chatLoadOne(int userId,int adminId,char role){
        System.out.println(userId+"===="+adminId);
        return userInfService.chatLoadOne(userId,adminId,role);
    }
    @RequestMapping(value = "/userExit")
    @ResponseBody
    public String  userExit(HttpSession session){
        session.removeAttribute("loginner");
        return "已退出";
    }

//    public Boolean newFileInf(HttpServletRequest request, String fileName){
//        String[] fileNames = fileName.split("\\.");
//        System.out.println(fileName);
//        String type = "."+fileNames[fileNames.length-1];
//        if (Arrays.asList(types).contains(type)) {
//            String fileName1 = fileName.split("." + type)[0];
//            User loginner = (User) request.getSession().getAttribute("loginner");
//            FileInf fileInf = new FileInf();
//            fileInf.setAccount(loginner.getAccount());
//            fileInf.setFileType(type);
//            fileInf.setFileName(fileName1);
//            fileInf.setUploadTime(f.format(new Date()));
//            if (fileMapper.uploadFile(fileInf)>0){
//                return true;
//            }
//        }
//        return false;
//    }
}
