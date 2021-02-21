package com.cykj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.bean.ChatInf;
import com.cykj.bean.UserInf;
import com.cykj.mapper.ChatInfMapper;
import com.cykj.mapper.UserInfMapper;
import com.cykj.service.UserInfService;
import com.cykj.util.FromatDate;
import com.cykj.util.LayuiJson;
import com.cykj.util.MsgSend;
import com.cykj.util.wxapp.AesUtil;
import com.cykj.util.wxapp.HttpRequest;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;


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
        if (verificationCode!=""){
            System.out.println(111);
            session.setAttribute("verificationCode",verificationCode);
        }
        return "验证码发送成功";
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
//        if (code.trim().equals("1234")) {
        if (code.trim().equals(verificationCode)) {
            UserInf userInf = new UserInf();
            userInf.setUpdateTime(FromatDate.DateFormat("yyyy-MM-dd hh:mm:ss",new Date()));
            userInf.setPhoneNum(phoneNum);
            userInf.setPassword(password);
            System.out.println(JSON.toJSONString(userInf));
            int i =userInfMapper.updatePwd(userInf);
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
    public String updateUserInf(UserInf userInf) {

        int x = userInfMapper.updateUserInf(userInf);
        if (x>0){
            login(userInf);
            return "修改成功";
        }
        return "修改失败";
    }

    @Override
    public String chatLoad(int userId) {
        List<ChatInf> chatInfs =chatInfMapper.selectByUserId(userId);
        chatInfMapper.updateByUserId(userId);
        return JSON.toJSONString(chatInfs);
    }

    @Override
    public String chatLoadOne(int userId,int adminId,char role) {
        chatInfMapper.updateOneToOne(userId,adminId,role);
        return null;
    }
    //getOpId
    @Override
    public Map<String,Object> getOpenid(String code, String encryptedData, String iv ){
        Map<String,Object> map = new HashMap<>();
        //登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }
        //小程序唯一标识   (在微信小程序管理后台获取)
        String wxspAppid = "wx71a60d295ae764af";
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "c7fd323158665fd5a1a48b757ac47727";
        //授权（必填）
        String grant_type = "refresh_token";
//        1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求https://open.weixin.qq.com/connect/oauth2/authorize
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);
        System.out.println(json.toString()+"json");
        //获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");
        System.out.println("openid:" + openid);
//        2、对encryptedData加密数据进行AES解密
        try {
            String result = AesUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");
                JSONObject userInfoJSON = JSONObject.parseObject(result);
                Map<String,Object> userInfo = new HashMap<>();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return map;
    }
    //微信用户登录
    @Override
    public  HashMap<String,Object> wxLogin(String openId) {
        HashMap<String, Object> map = new HashMap<>();
        UserInf userInf = userInfMapper.selectUserByOpenId(openId);
        if (userInf==null){
            int i = userInfMapper.addWxUser(openId);
            if (i<1){
                map.put("msg","注册新账号失败");
                map.put("status",0);
            }else {
                UserInf inf = userInfMapper.selectUserByOpenId(openId);
                map.put("msg","登录成功");
                map.put("status",1);
                map.put("userInf",inf);
                return map;
            }
        }
        map.put("msg","登录成功");
        map.put("status",1);
        map.put("userInf",userInf);
        return map;
    }
}
