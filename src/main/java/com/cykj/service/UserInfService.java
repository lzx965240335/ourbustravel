package com.cykj.service;

import com.cykj.bean.ChatInf;
import com.cykj.bean.UserInf;
import com.cykj.util.LayuiJson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public interface UserInfService {

    LayuiJson selectUser(HashMap map);
    int login(UserInf userInf);
    UserInf getLoginner();
    String sendMSG(String mobile, HttpSession session);
    String register(String phoneNum , String code , String password , HttpSession session);
    String updatePwd(String phoneNum , String code , String password , HttpSession session);
    ResponseEntity<byte[]> downLoadFile(HttpServletRequest request, int fileId, String fileName1, String fileType);
    String upLoadFile(HttpServletRequest request, MultipartFile uploadFile, HttpSession session);
    String updateUserInf(UserInf userInf);
    String chatLoad(ChatInf chatInf);
}
