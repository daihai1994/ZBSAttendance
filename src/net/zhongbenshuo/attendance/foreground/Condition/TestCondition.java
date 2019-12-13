package net.zhongbenshuo.attendance.foreground.Condition;

public class TestCondition {

    public static void main(String[] args) {
        String s = "425A3153313901000100000006001C00F7042600670127002C01280040012800C102290018002A00A5002A0062002A0D";
        Environment condition = new Environment();
        ParseUtil.parseData(s.replaceAll(" ",""), condition);
        //System.out.println("站号：" + StringUtil.removeZero(String.valueOf(condition.getStation())));
       // System.out.println("温度：" + StringUtil.removeZero(String.valueOf(condition.getTemperature())) + "℃");
        //System.out.println("湿度：" + StringUtil.removeZero(String.valueOf(condition.getHumidity())) + "%");
        //System.out.println("PM2.5：" + StringUtil.removeZero(String.valueOf(condition.getPm25())) + "μg/m³");
        //System.out.println("PM10：" + StringUtil.removeZero(String.valueOf(condition.getPm10())) + "μg/m³");
        //System.out.println("CO2：" + StringUtil.removeZero(String.valueOf(condition.getCarbonDioxide())) + "ppm");
       // System.out.println("HCHO：" + StringUtil.removeZero(String.valueOf(condition.getFormaldehyde())) + "mg/m³");
       // System.out.println("光照度：" + StringUtil.removeZero(String.valueOf(condition.getIlluminance())) + "lux");
    }

}
