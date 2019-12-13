package net.zhongbenshuo.attendance.foreground.Condition;


public class ParseUtil {

    public static void parseData(String data, Environment condition) {
        // 判断数据长度是否是4的倍数，协议头和协议尾是否一致
        if (data.length() % 4 == 0 && data.startsWith("425A31533139") && data.endsWith("2A0D")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < data.length(); i += 4) {
                stringBuilder.append(data.substring(i + 2, i + 4));
                stringBuilder.append(data.substring(i, i + 2));
            }
            String parserData = stringBuilder.toString();
           // System.out.println("倒序后的数据：" + parserData);
            // 判断校验和是否一致
            if (checkCode(parserData.substring(32, 88), parserData.substring(88, 92))) {
                // 站号
                String station = parserData.substring(16, 20);
                condition.setStation(MathUtils.HexS4ToInt(station));
                // 状态
                String state = parserData.substring(20, 24);
                condition.setState(MathUtils.HexS4ToInt(state)==0);
                // 温度
                String temp = parserData.substring(32, 36);
                condition.setTemperature(((float)(MathUtils.HexS4ToInt(temp) - 1000)) / 10);
                // 湿度
                String humidity = parserData.substring(40, 44);
                condition.setHumidity(((float)MathUtils.HexS4ToInt(humidity)) / 10);
                // PM2.5
                String pm25 = parserData.substring(48, 52);
                condition.setPm25(((float)MathUtils.HexS4ToInt(pm25)) / 10);
                // PM10
                String pm10 = parserData.substring(56, 60);
                condition.setPm10(((float)MathUtils.HexS4ToInt(pm10)) / 10);
                // CO2
                String co2 = parserData.substring(64, 68);
                condition.setCarbonDioxide(MathUtils.HexS4ToInt(co2));
                // HCHO
                String formaldehyde = parserData.substring(72, 76);
                condition.setFormaldehyde(MathUtils.formatFloat((float)(((float)MathUtils.HexS4ToInt(formaldehyde)/1000)*1.2), 3));
                // 光照度
                String illuminance = parserData.substring(80, 84);
                condition.setIlluminance(MathUtils.HexS4ToInt(illuminance));
            }
        }
    }

    private static boolean checkCode(String data, String checkCode) {
        int sumL = 0;
        for (int i = 0; i < data.length(); i = i + 4) {
            sumL = (sumL + MathUtils.HexS4ToInt(data.substring(i, i + 4))) % 256;
        }
        String r = Integer.toHexString(sumL / 16) + Integer.toHexString(sumL % 16);
        r = r.toUpperCase();
        return checkCode.endsWith(r);
    }

}
