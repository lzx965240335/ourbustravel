package com.cykj.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.cykj.util.FromatDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/payController")
public class PayController {
    private final String APP_ID = "2021000116696107";
    private final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCqQi2R7HgzONlbcFTKjTkDeNZo0t4B1U1LOTtJ6cjxbYCI30uUdUOIi1mMAS2x7WPKKMebG1kVrV8EOnRH+zDPRzQ3St43J+NJE9G5SoE1eaiGu0mIYacJudmeeBthKMNJvF8ce5uWBYVwR70cgZqAylLwxZi1sruE8Tk9osmRJf694u0O39Bo66kB7pzSbuToeQyf5oWHT7omybDowTrNR3yaBboXsBtCNDrT1im0C5O7c8ey2wOcEDXk9Kwol/ZjbHQPdd6thmyI9TPfcdkNmXb5JAVyr4Tw84hG093VSsqubJ8Xs0vtg3ll7Rb0fvipAse9hdxuY+7QjS15ZEQfAgMBAAECggEAPmIjKl2XqUUCN0PQfasDIeeLwDrLR6atPIvK0olLrFJwDzaqDcptpUFR3T+tS41sy+znPkjl+lBdCUKVyq4aM0imtD/FbjY6TFNru9W1xC9AdaV4CvWk66ZiO3NMrOvvqjvQKbKMzopVW0+d1i0SvG/ltfzguGq11L9CNvhHnNvTGDtNuC5JznFOkkglJxWsuLPuBP1Y4ML8cNqNFYJl6eebINd0ocWjBhRwYSFK2MGuW02ez/J9iV1unpdnphFYTPDBE/FhfWCtDfu87rKD2CgXEkIiLq8YP26OFyjy0kvCwdcKpR1V10kgnCI+FmWT4gF5WccOLtFBMDlOtg/W4QKBgQDUY1mUpTq2QMukfK9XtlM0IOecxhUaUqV+gvXKekdeUHQen/vDQXU0c5j1VUFjWEygGUXlVxYk13jSTsPG7uPT6UEkg/Rc+dM4Wj/5iZi5om6XwkZwrkwhABGBJ2zz1A4y4Y9Jqb+w417GaDC7IL4NUDKcbWRzAN2Z7h5DDImQUQKBgQDNODGK82enJmAfF4HHVIRMGs/kMauoyCjswwARoPtwnhJJjmvxg75wFyNn+fPWmCqNBcjA9mbr1viYXlaccD6uAQHvg83H3SXV9xIo5m/iHzsKxWDP34GwewSo1IJCbHUuL+KVmIKUTlSsn7KXSWvik9P7Y74ClWRNTKynVRBhbwKBgDfEPmlBWUExkkcGViP5w68UVLxdZn0a5klpqZAYu1SQW20PJNQIA/ZZw/fnAtxK+xOujyPA0heqXBcch1tcroWd4XTtEOS41DUfvVyQPIHBnLhMUqKToXmowu/eokUDkRbbEvyMjcCsct41zVXVKxHj1OYt8wofWBBq7Zc9W4GBAoGBAKPF3ZvDJgw5zunhK5u0siO+HfxBO3nlyusvr4ViEGdObs+uziwedEMl5Aadbd/q7cI/RHfh7D73/soyNzJ/TTY17aKEcBu5O/GmTCLBy7YMMGUnIVyjRO9fLVfCx/VUcquaYVv7KvJKFL7yb2bWnDdku/5aGhAbwsEBmKOtefYXAoGBAIEZbCuNlow6ddpHRp8E3MUYTO+mB8JFf9BghWZLwfZIm6YVPeGbCnfnmIWR7CdeKBXs+WvGk9bhFHBEScXnRAYC5BHx49xua62712OV8aP2sq5HHQy6OADV3MZEaqJg0AWIwy3scpFdJaAFRTLDkA6Ijq9rsv8ti6JLItom0Rz6";
    private final String CHARSET = "UTF-8";
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuCfzAQTB7iPtQrBwRvnvjjRW2eD+NcBkoFlm9tmeleGVJ8VSMPLyTy5LI0i/5cbkB9mS1yf8Pg9PId0LIpHtOCoszRMblHed+Q80jHR+FhGfVGSVlYisW3CmgaJHD8/U5kTju5iODL1u8nGJ+HfZkOL8/muqAeaKoyRlwJbKo3h6zbKcadHrly6GDuKXXti6th44+r1/ySi9vT2G6cH3/kQdFu+swwWD+dpBBfnCXwFIc1hQs13RFi2QHoQfLr1qx5pMpxfykw6YCBAz7b7kj9K9+LDyAVgUhQ/90nUGD7i1bk/V2FPhP1smFvg4dPdz2BAdVJuCXGcEholgEBBfNQIDAQAB";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL ="https://openapi.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://localhost:8080/userMain/map";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://localhost:8080/payController/returnUrl";
    Random rd;
    @RequestMapping("/alipay")
    public void alipay(String money,HttpServletResponse httpResponse) throws IOException {
        //实例化客户端,填入所需参数
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //在公共参数中设置回跳和通知地址
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);
        //根据订单编号,查询订单相关信息
//        Order order = payService.selectById(orderId);
        //商户订单号，商户网站订单系统中唯一订单号，必填
//        String out_trade_no = order.getOrderId().toString();
        String out_trade_no = FromatDate.DateFormat("yyyy-mm-dd hh:mm:ss",new Date());
        //付款金额，必填
//        String total_amount = order.getOrderPrice().toString();
        String total_amount = money;
        //订单名称，必填
//        String subject = order.getOrderName();
        String subject = "测试付款";
        //商品描述，可空
        String body = "";
        request.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @RequestMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request)
            throws IOException, AlipayApiException {
        System.out.println("=================================同步回调=====================================");

        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
            params.put(name, valueStr);
        }

        System.out.println(params);//查看参数都有哪些
        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); // 调用SDK验证签名
        //验证签名通过
        if(signVerified){
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("商户订单号="+out_trade_no);
            System.out.println("支付宝交易号="+trade_no);
            System.out.println("付款金额="+total_amount);


            //支付成功，修复支付状态
//            payService.updateById(Integer.valueOf(out_trade_no));
            return "map";//跳转付款成功页面
        }else{
            return "map";//跳转付款失败页面
        }

    }


}
