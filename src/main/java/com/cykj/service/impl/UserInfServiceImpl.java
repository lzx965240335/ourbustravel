package com.cykj.service.impl;

import com.cykj.bean.ChatInf;
import com.cykj.bean.UserInf;
import com.cykj.mapper.ChatInfMapper;
import com.cykj.mapper.UserInfMapper;
import com.cykj.service.UserInfService;
import com.cykj.util.FromatDate;
import com.cykj.util.LayuiJson;
import com.cykj.util.MsgSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


@Service
public class UserInfServiceImpl implements UserInfService {

    @Autowired
    private UserInfMapper userInfMapper;

    @Autowired
    private ChatInfMapper chatInfMapper;

    @Autowired
    HttpServletRequest request;

    @Override
    public LayuiJson selectUser(HashMap map) {
        List<UserInf> userLayuiJson = userInfMapper.selectUser(map);
        int count = userInfMapper.count(map);
        return new LayuiJson(count,userLayuiJson);
    }


    @Override
    public int login(UserInf user) {
        List<UserInf> users = userInfMapper.selectUsers(user);
        //判断是否为空
        if (users.isEmpty()){
            return 0;
        }
        request.getSession().setAttribute("loginner",users.get(0));
        return users.get(0).getUserId();
    }
    @Override
    public UserInf getLoginner() {
        UserInf user = (UserInf) request.getSession().getAttribute("loginner");
        System.out.println(user.getAccount());
        return user;
    }

    @Override
    public String sendMSG(String mobile , HttpSession session) {
        String verificationCode = MsgSend.sendMSG(mobile);
        if (verificationCode.equals("验证码发送成功")){
            System.out.println(111);
            session.setAttribute("verificationCode",verificationCode);
        }
        return verificationCode;
    }

    @Override
    public String register(String phoneNum, String code, String password, HttpSession session) {
        String verificationCode = (String) session.getAttribute("verificationCode");
        if (code.trim().equals(verificationCode)) {
            //随机生成五位数账号
            Random rd = new Random();
            UserInf userInf = new UserInf();
            boolean bl =true;
            int x = 0;
            while (bl){
                x=(rd.nextInt(89999)+10000);
                userInf.setAccount(x+"");
                List<UserInf> users = userInfMapper.selectUsers(userInf);
                //判断是否为空
                if (users.isEmpty()){
                    bl = false;
                }
            }
            userInf.setRegTime(FromatDate.DateFormat("yyyy-MM-dd hh:mm:ss",new Date()));
            userInf.setPhoneNum(phoneNum);
            userInf.setPassword(password);
            int i =userInfMapper.insert(userInf);
            if (i>0)
                return "您的账号为："+x+",请妥善管理";
            else
                return "账号注册失败";
        }
        return "验证码错误";
    }

    @Override
    public String updatePwd(String phoneNum, String code, String password, HttpSession session) {
        String verificationCode = (String) session.getAttribute("verificationCode");
        System.out.println(code);
        if (code.trim().equals("1234")) {
            //随机生成五位数账号
            UserInf loginner = (UserInf) session.getAttribute("loginner");
            UserInf userInf = new UserInf();
            userInf.setAccount(loginner.getAccount());
            userInf.setUpdateTime(FromatDate.DateFormat("yyyy-MM-dd hh:mm:ss",new Date()));
            userInf.setPhoneNum(phoneNum);
            userInf.setPassword(password);
            int i =userInfMapper.updateUserInf(userInf);
            if (i>0)
                return "密码修改成功";
            else
                return "密码修改失败";
        }
        return "验证码错误";
    }
    @Override
    public String upLoadFile(HttpServletRequest request, MultipartFile uploadFile, HttpSession session) {
        System.out.println(null==uploadFile);
        if (uploadFile.getSize()>0){
            System.out.println(1);
            String path = request.getServletContext().getRealPath("/upload");
            File url =new File(path);
            if (!url.exists()) {
                url.mkdirs();
            }
            String fileName = uploadFile.getOriginalFilename();
            File file = new File(path,fileName);
            try {
                uploadFile.transferTo(file);
                System.out.println(path);
                UserInf userInf = (UserInf) session.getAttribute("loginner");
                userInf.setAvatar(path);
                UserInf userInf1 = (UserInf) session.getAttribute("loginner");
//                System.out.println(userInf.getAvatar());
//                System.out.println(userInf1.getAvatar()+"=======");
                return "";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downLoadFile(HttpServletRequest request, int fileId, String fileName1, String fileType) {
        return null;
    }

    @Override
    public String updateUserInf(UserInf userInf) {
        int x = userInfMapper.updateUserInf(userInf);
        if (x>0){
            login(userInf);
            return "修改成功";
        }
        return "修改失败";
    }

    @Override
    public String chatLoad(ChatInf chatInf) {
        int x = chatInfMapper.insert(chatInf);
        return null;
    }

}
