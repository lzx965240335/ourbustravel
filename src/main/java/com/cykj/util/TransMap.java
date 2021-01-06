package com.cykj.util;



//import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class TransMap {

    private final static DecimalFormat FORMAT = new DecimalFormat("0.00000000");
    private final static int SCALE = 8;
    private final static double AVG_X = 0.006401062;
    private final static double AVG_Y = 0.0060424805;
    private final static double EARTH_RADIUS = 6378137.0;
    private final static double MARS_R = 6378245.0;
    private final static double MARS_EE = 0.00669342162296594323;

    /**
     * 预定义转换模式。
     */
    public static enum Schema
    {
        bdmc2g("bmc2g"), bdmc2gm("bmc2gm"), bdmc2bll("bmc2bll"), bll2bdmc("bll2bmc"),
        g2bdmc("g2bmc"), gm2bdmc("gm2bmc"), /*gps2g("gps2g"), g2gps("g2gps"), */
        gps2b("gps2b"), b2gps("b2gps"), b2g("b2g"), g2b("g2b"), d2m("d2m"), m2d("m2d"),
        gps2mars("gps2mars"), mars2gps("mars2gps"), ll2wmc("ll2wmc"), wmc2ll("wmc2ll");

        private final String value;

        Schema(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    /**
     * 变形后转变形前（高德坐标转GPS）
     *
     * @param lng
     * @param lat
     * @return
     */
   /* public static Double[] convertGoogle2GPS(double lng, double lat)
    {
        String url = String.format(SysConf.get().getString("transform.itf.url"),
                "Public2Secret", FORMAT.format(lng), FORMAT.format(lat), "3600");
        return getXYBy(url);
    }*/

    /**
     * 变形前转变形后（GPS转高德）
     *
     * @param lng
     * @param lat
     * @return
     */
  /*  public static Double[] convertGPS2Google(double lng, double lat)
    {
        String url = String.format(SysConf.get().getString("transform.itf.url"),
                "Secret2Public", FORMAT.format(lng), FORMAT.format(lat), "3600");
        return getXYBy(url);
    }
*/
    /**
     * 百度转高德（内网工具）
     *
     * @param lng
     * @param lat
     * @return
     */
    public static Double[] convertBaidu2Google(double lng, double lat)
    {
        String url = "http://10.2.10.51:81/cgi-bin/bd.fcgi/?opt=bd_decrypt&x=" + FORMAT.format(
                lng) + "&y=" + FORMAT.format(lat);
        return getXYBy(url);
    }

    /**
     * 高德转百度（内网工具）
     *
     * @param lng
     * @param lat
     * @return
     */
    public static Double[] convertGoogle2Baidu(double lng, double lat)
    {
        String urlstr = "http://10.2.10.51:81/cgi-bin/bd.fcgi/?opt=bd_encrypt&x=" + FORMAT.format(
                lng) + "&y=" + FORMAT.format(lat);
        return getXYBy(urlstr);
    }

    private static Double[] getXYBy(String urlstr)
    {
        Double[] xy = null;
        String result = readURLData(urlstr);
        if(result != null && result.length() > 5)
        {
            xy = readJSONBy(result);
        }
        return xy;
    }

    /**
     * GPS坐标转换为百度坐标
     *
     * @param lng
     * @param lat
     * @return
     */
    public static Double[] convertGPS2Baidu(double lng, double lat)
    {
        String url = "http://map.baidu.com/ag/coord/convert?from=0&to=4&x=" + FORMAT.format(
                lng) + "&y=" + FORMAT.format(lat);
        return getXY(url);
    }

    /**
     * 百度坐标转换为GPS坐标
     *
     * @param lng
     * @param lat
     * @return
     */
    public static Double[] convertBaidu2GPS(double lng, double lat)
    {
        return convertBaidu2GPS(lng, lat, 3);
    }

    public static Double[] convertBaidu2GPS(double lng, double lat, int c)
    {
        Double[] xy = new Double[2];
        if(c < 1)
        {
            return xy;
        }
        double ax = AVG_X;
        double ay = AVG_Y;
        for(int i = 1; i <= c; i++)
        {
            xy = convertBaidu2GPS(lng, lat, ax, ay);
            ax = lng - xy[0];
            ay = lat - xy[1];
        }
        return xy;
    }

    private static Double[] convertBaidu2GPS(double x, double y, double ax,
                                             double ay)
    {
        Double[] xy = null;
        double x_b = x - ax;
        double y_b = y - ay;
        Double[] xy_b = convertGPS2Baidu(x_b, y_b);
        if(xy_b != null)
        {
            xy = new Double[2];
            xy[0] = BigDecimal.valueOf(x + x_b - xy_b[0]).setScale(SCALE,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            xy[1] = BigDecimal.valueOf(y + y_b - xy_b[1]).setScale(SCALE,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return xy;
    }

    private static Double[] getXY(String urlstr)
    {
        Double[] xy = null;
        String result = readURLData(urlstr);
        if(result != null && result.length() > 5)
        {
            xy = readJSON(result);
        }
        return xy;
    }

    private static String readURLData(String urlstr)
    {
        String result = null;
        InputStream in = null;
        BufferedReader reader = null;
        FileOutputStream out = null;
        try
        {
            URL url = new URL(urlstr);
            HttpURLConnection httpConn = null;
            HttpURLConnection.setFollowRedirects(false);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Host", url.getHost());
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0");
            httpConn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpConn.setRequestProperty("Accept-Language", "zh-cn");
            httpConn.setRequestProperty("Accept-Encoding", "deflate");
            httpConn.setRequestProperty("Connection", "keep-alive");
            httpConn.setConnectTimeout(20000);
            int responseCode = httpConn.getResponseCode();
            if(responseCode == 200)
            {
                in = httpConn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                result = sb.toString();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch(IOException ex)
            {
            }
            try
            {
                if(in != null)
                {
                    in.close();
                }
            }
            catch(IOException ex)
            {
            }
            try
            {
                if(out != null)
                {
                    out.close();
                }
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static Double[] readJSONBy(String str)
    {
        Double[] xy = null;
        try
        {
            String value = str.substring(1, str.length() - 1);
            value = value.replace("\"", "");
            String[] results = value.split(",");
            if(results.length == 2)
            {
                String mapX = results[0].split(":")[1];
                String mapY = results[1].split(":")[1];
                xy = new Double[2];
                xy[0] = Double.parseDouble(mapX);
                xy[1] = Double.parseDouble(mapY);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return xy;
    }

    private static Double[] readJSON(String str)
    {
        Double[] xy = null;
        try
        {
            String value = str.substring(1, str.length() - 1);
            String[] results = value.split("\\,");
            if(results.length == 3)
            {
                if(results[0].split("\\:")[1].equals("0"))
                {
                    String mapX = results[1].split("\\:")[1];
                    String mapY = results[2].split("\\:")[1];
                    mapX = mapX.substring(1, mapX.length() - 1);
                    mapY = mapY.substring(1, mapY.length() - 1);
//                    mapX = new String(Base64.decodeBase64(mapX));
//                    mapY = new String(Base64.decodeBase64(mapY));
                    xy = new Double[2];
                    xy[0] = new BigDecimal(mapX).setScale(SCALE,
                            BigDecimal.ROUND_HALF_UP).doubleValue();
                    xy[1] = new BigDecimal(mapY).setScale(SCALE,
                            BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return xy;
    }

    /**
     * 转换GPS坐标到MARS坐标。
     * @param x x坐标4gps
     * @param y y坐标4gps
     * @return mars坐标xy
     */
    public static Double[] convertGPS2MXY(double x, double y)
    {
        Double[] mxy = new Double[2];
        if(outOfChina(x, y))
        {
            mxy[0] = x;
            mxy[1] = y;
            return mxy;
        }
        double dLat = convertGPS2MY(x - 105.0, y - 35.0);
        double dLon = convertGPS2MX(x - 105.0, y - 35.0);
        double radLat = y / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - MARS_EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((MARS_R * (1 - MARS_EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (MARS_R / sqrtMagic * Math.cos(radLat) * Math.PI);
        mxy[0] = x + dLon;
        mxy[1] = y + dLat;
        return mxy;
    }

    /**
     * 转换MARS坐标到GPS坐标。
     * @param x x坐标4mxy
     * @param y y坐标4mxy
     * @return gps坐标xy
     */
    public static Double[] convertMXY2GPS(double x, double y)
    {
        Double[] mxy = new Double[2];
        if(outOfChina(x, y))
        {
            mxy[0] = x;
            mxy[1] = y;
            return mxy;
        }
        double dLat = convertGPS2MY(x - 105.0, y - 35.0);
        double dLon = convertGPS2MX(x - 105.0, y - 35.0);
        double radLat = y / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - MARS_EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((MARS_R * (1 - MARS_EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (MARS_R / sqrtMagic * Math.cos(radLat) * Math.PI);
        mxy[0] = x * 2 - (x + dLon);
        mxy[1] = y * 2 - (y + dLat);
        return mxy;
    }

    /**
     * 检查china边界。
     * @param x x坐标4gps
     * @param y y坐标4gps
     * @return 检查结果
     */
    private static boolean outOfChina(double x, double y)
    {
        if(x < 72.004 || x > 137.8347)
            return true;
        if(y < 0.8293 || y > 55.8271)
            return true;
        return false;
    }

    /**
     * 转换GPS坐标到MARS坐标4X。
     * @param x x坐标4gps
     * @param y y坐标4gps
     * @return x坐标4mars
     */
    private static double convertGPS2MY(double x, double y)
    {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 转换GPS坐标到MARS坐标4Y。
     * @param x x坐标4gps
     * @param y y坐标4gps
     * @return y坐标4mars
     */
    private static double convertGPS2MX(double x, double y)
    {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 获得半径偏移量
     *
     * 以3000为周期的变化*0.00002
     *
     * @param y 纬度
     * @return 半径偏移量
     */
    private static double get_delta_r(double y)
    {
        return Math.sin(y * 3000 * (Math.PI / 180)) * 0.00002;
    }

    /**
     * 获得角度偏移量 以3000为周期的变化*0.000003
     *
     * @param x 经度
     * @return 角度偏移量
     */
    private static double get_delta_t(double x)
    {
        double d = Math.cos(x * 3000 * (Math.PI / 180)) * 0.000003;
        return d;
    }

    /**
     * 高德转百度（工具）
     *
     * @param lng
     * @param lat
     * @return
     */
    public static Double[] convertG2B(double lng, double lat)
    {
        // 确保以度参与计算
        lng = (Math.abs(lng) > 180d) ? lng / 3600d : lng;
        lat = (Math.abs(lat) > 180d) ? lat / 3600d : lat;
        double x1 = Math.cos(get_delta_t(lng) + Math.atan2(lat, lng)) * (get_delta_r(
                lat) + Math.sqrt(lng * lng + lat * lat)) + 0.0065;
        double y1 = Math.sin(get_delta_t(lng) + Math.atan2(lat, lng)) * (get_delta_r(
                lat) + Math.sqrt(lng * lng + lat * lat)) + 0.006;
        Double[] xys = new Double[2];
        xys[0] = BigDecimal.valueOf(x1).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
        xys[1] = BigDecimal.valueOf(y1).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
        return xys;
    }

    /**
     * 百度转高德（工具）默认迭代2次
     *
     * @param lng
     * @param lat
     * @return
     */
    public static Double[] convertB2G(double lng, double lat)
    {
        return convertB2G(lng, lat, 2);
    }

    /**
     * 百度转高德（工具）
     *
     * @param lng
     * @param lat
     * @param c 迭代次数
     * @return
     */
    public static Double[] convertB2G(double lng, double lat, int c)
    {
        // 确保以度参与计算
        lng = (Math.abs(lng) > 180d) ? lng / 3600d : lng;
        lat = (Math.abs(lat) > 180d) ? lat / 3600d : lat;
        Double[] xy = new Double[2];
        if(c < 1)
        {
            return xy;
        }
        double ax = AVG_X;
        double ay = AVG_Y;
        for(int i = 1; i <= c; i++)
        {
            xy = convertB2G(lng, lat, ax, ay);
            ax = lng - xy[0];
            ay = lat - xy[1];
        }
        return xy;
    }

    private static Double[] convertB2G(double x, double y, double ax, double ay)
    {
        // 确保以度参与计算
        x = (Math.abs(x) > 180d) ? x / 3600d : x;
        y = (Math.abs(y) > 180d) ? y / 3600d : y;
        Double[] xy = null;
        double x_b = x - ax;
        double y_b = y - ay;
        Double[] xy_b = convertG2B(x_b, y_b);
        if(xy_b != null)
        {
            xy = new Double[2];
            xy[0] = BigDecimal.valueOf(x + x_b - xy_b[0]).setScale(SCALE,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            xy[1] = BigDecimal.valueOf(y + y_b - xy_b[1]).setScale(SCALE,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return xy;
    }

    /**
     * 平面坐标转经纬度
     *
     * @param x 平面坐标X
     * @param y 平面坐标Y
     * @return
     */
    public static Double[] convertMC2Lnglat(double x, double y)
    {
        Double[] res = convertMC2LL(x, y);
        return new Double[]
                {
                        new BigDecimal(res[0]).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue(), new BigDecimal(
                        res[1]).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue()
                };
    }

    /**
     * 经纬度转平面坐标
     *
     * @param lng 经度
     * @param lat 纬度
     * @return
     */
    public static Double[] convertLnglat2MC(double lng, double lat)
    {
        Double[] res = convertLL2MC(lng, lat);
        return new Double[]
                {
                        Double.parseDouble(String.format("%.2f", res[0])), Double.parseDouble(
                        String.format("%.2f", res[1]))
                };
    }

    /**
     * 转换bdmc坐标到g经纬度坐标。
     * @param x mcx
     * @param y mcy
     * @return 转换后的g经纬度坐标
     */
    public static Double[] convertBMC2G(double x, double y)
    {
        Double[] xy = convertMC2Lnglat(x, y);
        return convertB2G(xy[0], xy[1]);
    }

    /**
     * 转换g经纬度坐标到bmc坐标。
     * @param x mcx
     * @param y mcy
     * @return 转换后的bmc坐标
     */
    public static Double[] convertG2BMC(double x, double y)
    {
        Double[] xy = convertG2B(x, y);
        return convertLL2MC(xy[0], xy[1]);
    }

    private static Double[] convertLL2MC(double x, double y)
    {
        // 确保以度参与计算
        x = (Math.abs(x) > 180d) ? x / 3600d : x;
        y = (Math.abs(y) > 180d) ? y / 3600d : y;
        double[] cD = null;
        double[] llband =
                {
                        75, 60, 45, 30, 15, 0
                };
        double[][] ll2mc =
                {
                        {
                                -0.0015702102444, 111320.7020616939, 1704480524535203.0, -10338987376042340.0, 26112667856603880.0, -35149669176653700.0, 26595700718403920.0, -10725012454188240.0, 1800819912950474.0, 82.5
                        },
                        {
                                0.0008277824516172526, 111320.7020463578, 647795574.6671607, -4082003173.641316, 10774905663.51142, -15171875531.51559, 12053065338.62167, -5124939663.577472, 913311935.9512032, 67.5
                        },
                        {
                                0.00337398766765, 111320.7020202162, 4481351.045890365, -23393751.19931662, 79682215.47186455, -115964993.2797253, 97236711.15602145, -43661946.33752821, 8477230.501135234, 52.5
                        },
                        {
                                0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013, -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5
                        },
                        {
                                -0.0003441963504368392, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378, 54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5
                        },
                        {
                                -0.0003218135878613132, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093, 2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45
                        }
                };
        x = getLoop(x, -180, 180);
        y = getRange(y, -74, 74);
        for(int cC = 0; cC < llband.length; cC++)
        {
            if(y >= llband[cC])
            {
                cD = ll2mc[cC];
                break;
            }
        }
        if(cD != null && cD.length > 0)
        {
            for(int cC = llband.length - 1; cC >= 0; cC--)
            {
                if(y <= -llband[cC])
                {
                    cD = ll2mc[cC];
                    break;
                }
            }
        }
        return convertor(x, y, cD);
    }

    private static Double[] convertor(double x, double y, double[] cD)
    {
        double t = cD[0] + cD[1] * Math.abs(x);
        double cB = Math.abs(y) / cD[9];
        double cE = cD[2] + cD[3] * cB + cD[4] * cB * cB + cD[5] * cB * cB * cB + cD[6] * cB * cB * cB * cB + cD[7] * cB * cB * cB * cB * cB + cD[8] * cB * cB * cB * cB * cB * cB;
        t *= (x < 0 ? -1 : 1);
        cE *= (y < 0 ? -1 : 1);
        return new Double[]
                {
                        t, cE
                };
    }

    private static Double[] convertMC2LL(double x, double y)
    {
        double[] mcband =
                {
                        12890594.86, 8362377.87, 5591021, 3481989.83, 1678043.12, 0
                };
        double[][] mc2ll =
                {
                        {
                                1.410526172116255e-8, 0.00000898305509648872, -1.9939833816331, 200.9824383106796, -187.2403703815547, 91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 17337981.2
                        },
                        {
                                -7.435856389565537e-9, 0.000008983055097726239, -0.78625201886289, 96.32687599759846, -1.85204757529826, -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 10260144.86
                        },
                        {
                                -3.030883460898826e-8, 0.00000898305509983578, 0.30071316287616, 59.74293618442277, 7.357984074871, -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37
                        },
                        {
                                -1.981981304930552e-8, 0.000008983055099779535, 0.03278182852591, 40.31678527705744, 0.65659298677277, -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06
                        },
                        {
                                3.09191371068437e-9, 0.000008983055096812155, 0.00006995724062, 23.10934304144901, -0.00023663490511, -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4
                        },
                        {
                                2.890871144776878e-9, 0.000008983055095805407, -3.068298e-8, 7.47137025468032, -0.00000353937994, -0.02145144861037, -0.00001234426596, 0.00010322952773, -0.00000323890364, 826088.5
                        }
                };
        double[] cE = new double[2];
        for(int i = 0; i < mcband.length; i++)
        {
            if(Math.abs(y) >= mcband[i])
            {
                cE = mc2ll[i];
                break;
            }
        }
        return convertor(x, y, cE);
    }

    private static double getRange(double cC, double a, double b)
    {
        cC = Math.max(cC, a);
        cC = Math.min(cC, b);
        return cC;
    }

    private static double getLoop(double cC, double a, double b)
    {
        while(cC > b)
        {
            cC -= b - a;
        }
        while(cC < a)
        {
            cC += b - a;
        }
        return cC;
    }

    /**
     * 获取图片左下角右上角经纬度
     *
     * @param x 图片x坐标
     * @param y 图片y坐标
     * @param level 级别
     * @return 116.361753,40.042802|116.362903,40.041918
     */
    public static Double[] getLnglat(int x, int y, int level)
    {
        Double[] xy1 = convertMC2Lnglat((2 << (25 - level)) * x,
                (2 << (25 - level)) * y);
        Double[] xy2 = convertMC2Lnglat((2 << (25 - level)) * (x + 1),
                (2 << (25 - level)) * (y + 1));
        return new Double[]
                {
                        xy1[0], xy1[1], xy2[0], xy2[1]
                };
    }

    public static Integer[] getGridXYByXY(int minX, int maxX, int minY, int maxY,
                                          int levelSmall, int levelBig)
    {
        int c = 1 << (levelBig - levelSmall);
        return new Integer[]
                {
                        minX * c, ((maxX + 1) * c - 1), minY * c, ((maxY + 1) * c - 1)
                };
    }

    public static Integer[] getGridXYByXY(int x, int y, int levelSmall,
                                          int levelBig)
    {
        return getGridXYByXY(x, x, y, y, levelSmall, levelBig);
    }

    /**
     * 通过经纬度获取图片左下角右上角经纬度范围
     *
     * @param lng_min 左下角经度
     * @param lat_min 左下角纬度
     * @param lng_max 右上角经度
     * @param lat_max 右上角纬度
     * @param level 级别
     * @return
     */
    public static Integer[] getGridXYByLnglat(double lng_min, double lat_min,
                                              double lng_max, double lat_max, int level)
    {
        Double[] pXY0 = convertLnglat2MC(Math.min(lng_min, lng_max),
                Math.min(lat_min, lat_max));
        Double[] pXY1 = convertLnglat2MC(Math.max(lng_min, lng_max),
                Math.max(lat_min, lat_max));
        int x0 = (int) Math.floor(pXY0[0] / (2 << (25 - level)));
        int y0 = (int) Math.floor(pXY0[1] / (2 << (25 - level)));
        int x1 = (int) Math.floor(pXY1[0] / (2 << (25 - level)));
        int y1 = (int) Math.floor(pXY1[1] / (2 << (25 - level)));
        return new Integer[]
                {
                        x0, x1, y0, y1
                };
    }

    /**
     * 通过经纬度获取图片左下角右上角经纬度范围
     *
     * @param lng_min 左下角经度
     * @param lat_min 左下角纬度
     * @param lng_max 右上角经度
     * @param lat_max 右上角纬度
     * @param levelSmall 小级别(13)
     * @param levelBig 大级别(19)
     * @return
     */
    public static Integer[] getGridXYByLnglat(double lng_min, double lat_min,
                                              double lng_max, double lat_max, int levelSmall, int levelBig)
    {
        Integer[] g = getGridXYByLnglat(lng_min, lat_min, lng_max, lat_max,
                levelSmall);
        return getGridXYByXY(g[0], g[1], g[2], g[3], levelSmall, levelBig);
    }

    private static double getRad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 算法1:求两点之间的距离,输入单位: 度；返回单位:米。
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return 两点之间的距离
     */
    public static double getGreatCircleDistance(double lng1, double lat1,
                                                double lng2, double lat2)
    {
        // 确保以度参与计算
        lng1 = (Math.abs(lng1) > 180d) ? lng1 / 3600d : lng1;
        lat1 = (Math.abs(lat1) > 180d) ? lat1 / 3600d : lat1;
        lng2 = (Math.abs(lng2) > 180d) ? lng2 / 3600d : lng2;
        lat2 = (Math.abs(lat2) > 180d) ? lat2 / 3600d : lat2;
        double radLat1 = getRad(lat1);
        double radLat2 = getRad(lat2);

        double dy = radLat1 - radLat2;//a
        double dx = getRad(lng1) - getRad(lng2);//b

        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(dy / 2), 2) + Math.cos(radLat1) * Math.cos(
                        radLat2) * Math.pow(Math.sin(dx / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000.0;
        return s;
    }

    /**
     * 3857-->经纬度.
     * @param x
     * @param y
     * @return 经纬度坐标
     */
    public static Double[] inverseMercator(double x, double y)
    {
        double lon = x / 20037508.34d * 180.0d;
        double lat = y / 20037508.34d * 180.0d;
        lat = 180d / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180d)) - Math.PI / 2);
        return new Double[] { lon, lat };
    }

    /**
     * 经纬度-->3857.
     * @param lon
     * @param lat
     * @return 3857坐标
     */
    public static Double[] forwardMercator(double lon, double lat)
    {
        double x = lon * 20037508.34d /180d;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34d / 180d;
        return new Double [] { x, y };
    }

    /**
     * 度转秒。
     * @param x 经度(度)
     * @param y 维度(度)
     * @return 经纬度(秒)
     */
    private static Double[] convertD2M(double x, double y)
    {
        Double[] xy = new Double[2];
        xy[0] = (Math.abs(x) <= 180d) ? x * 3600 : x;
        xy[1] = (Math.abs(y) <= 180d) ? y * 3600 : y;
        return xy;
    }

    /**
     * 秒转度。
     * @param x 经度(秒)
     * @param y 维度(秒)
     * @return 经纬度(度)
     */
    private static Double[] convertM2D(double x, double y)
    {
        Double[] xy = new Double[2];
        xy[0] = (Math.abs(x) > 180d) ? x / 3600d : x;
        xy[1] = (Math.abs(y) > 180d) ? y / 3600d : y;
        return xy;
    }

    /**
     * 批量转换mc为g经纬度。
     * @param xyArray 逗号分隔的mc坐标串
     * @return 转换后的坐标串
     */
    public static String convertMC2GLnglat(String xyArray)
    {
        String xys[] = xyArray.split(",");
        StringBuilder sbd = new StringBuilder();
        for(int i = 0; i < xys.length; i += 2)
        {
            Double xy1[] = convertMC2Lnglat(Double.valueOf(xys[i]), Double.valueOf(xys[i + 1]));
            Double xy2[] = convertB2G(xy1[0].doubleValue(), xy1[1].doubleValue());
            sbd.append(String.format("%f,%f", xy2[0] * 3600D, xy2[1] * 3600D));
            if(i < xys.length - 2)
            {
                sbd.append(",");
            }
        }
        return sbd.toString();
    }

    /**
     * 批量按照目标schema转换坐标。
     * @param s 目标schema
     * @param xyArray 逗号分隔的坐标串
     * @return 转换后的坐标串
     */
    public static String convertSchema(Schema s, String xyArray)
    {
        String xys[] = xyArray.split(",");
        StringBuilder sbd = new StringBuilder();
        for(int i = 0; i < xys.length; i += 2)
        {
            Double[] xy = convertSchema(s, Double.valueOf(xys[i]), Double.valueOf(xys[i + 1]));
            sbd.append(String.format("%f,%f", xy[0], xy[1]));
            if(i < xys.length - 2)
            {
                sbd.append(",");
            }
        }
        return sbd.toString();
    }

    /**
     * 按照目标schema转换坐标。
     * @param s 目标schema
     * @param x 原始x
     * @param y 原始y
     * @return 转换后坐标
     */
    public static Double[] convertSchema(Schema s, double x, double y)
    {
        switch(s)
        {
            case bdmc2bll:
                return convertMC2Lnglat(x, y);
            case bll2bdmc:
                return convertLnglat2MC(x, y);
            case bdmc2g:
                return convertBMC2G(x, y);
            case bdmc2gm:
                Double[] xy = convertBMC2G(x, y);
                xy[0] *= (Math.abs(xy[0]) <= 180d) ? 3600 : 1;
                xy[1] *= (Math.abs(xy[1]) <= 180d) ? 3600 : 1;
                return xy;
            case g2bdmc:
            case gm2bdmc:
                return convertG2BMC(x, y);
            //  case gps2g:
            //      return convertGPS2Google(x, y);
            // case g2gps:
            //   return convertGoogle2GPS(x, y);
            case gps2b:
                return convertGPS2Baidu(x, y);
            case b2gps:
                return convertBaidu2GPS(x, y);
            case b2g:
                return convertB2G(x, y);
            case g2b:
                return convertG2B(x, y);
            case d2m:
                return convertD2M(x, y);
            case m2d:
                return convertM2D(x, y);
            case gps2mars://wgs84坐标转gcj02 加密
                return convertGPS2MXY(x, y);
            case mars2gps://gcj02转wgs84 解密
                return convertMXY2GPS(x, y);
            case ll2wmc:
                return forwardMercator(x, y);
            case wmc2ll:
                return inverseMercator(x, y);
        }
        return null;
    }
}
