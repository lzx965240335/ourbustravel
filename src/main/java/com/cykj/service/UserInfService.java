package com.cykj.service;

import com.cykj.bean.ChatInf;
import com.cykj.bean.UserInf;
import com.cykj.util.LayuiJson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public interface UserInfService {
    Map<String,Object> getOpenid(String code, String encryptedData, String iv );
    HashMap<String,Object> wxLogin(String openId);
    LayuiJson selectUser(HashMap map);
    int login(UserInf userInf);
    UserInf getLoginner();
    String sendMSG(String mobile, HttpSession session);
    String register(String phoneNum, String code, String password, HttpSession session);
    String updatePwd(String phoneNum, String code, String password, HttpSession session);
    String upLoadFile(HttpServletRequest request, MultipartFile uploadFile, HttpSession session);
    String updateUserInf(UserInf userInf);
    String chatLoad(int userId);
    String chatLoadOne(int userId, int adminId, char role);
}
