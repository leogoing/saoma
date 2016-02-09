package com.utils;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 生成流水号工具类
 * @author zmc
 *
 */
public class PrimaryGenerater {
 
    private static final String SERIAL_NUMBER = "XXXX"; // 流水号格式
    private static PrimaryGenerater primaryGenerater = null;
 
    private PrimaryGenerater() {
    }
 
    /**
     * 取得PrimaryGenerater的单例实现
     * 
     * @return
     */
    public static PrimaryGenerater getInstance() {
        if (primaryGenerater == null) {
            synchronized (PrimaryGenerater.class) {
                if (primaryGenerater == null) {
                    primaryGenerater = new PrimaryGenerater();
                }
            }
        }
        return primaryGenerater;
    }
 
    /**
     * 生成流水号(有序生成)
     * @param sno
     * sno=null 默认生成第一个
     * sno!=null 在sno基础上增加1
     * @return
     */
    public synchronized String generaterNextNumber(String sno) {
        String id = null;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        if (sno == null) {
            id = formatter.format(date) + "0001";
        } else {
            int count = SERIAL_NUMBER.length();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count; i++) {
                sb.append("0");
            }
            DecimalFormat df = new DecimalFormat("0000");
            id = formatter.format(date)
                    + df.format(1 + Integer.parseInt(sno.substring(8, 12)));
        }
        return id;
    }
    
    /**
     * 生成流水号(无序的)
     * @param length 需要生成的流水号长度
     * @param isNumber 是否为纯数字
     * @return
     */
    public static String getSysJournalNo(int length, boolean isNumber){
        //replaceAll()之后返回的是一个由十六进制形式组成的且长度为32的字符串
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if(uuid.length() > length){
            uuid = uuid.substring(0, length);
        }else if(uuid.length() < length){
            for(int i=0; i<length-uuid.length(); i++){
                uuid = uuid + Math.round(Math.random()*9);
            }
        }
        if(isNumber){
            return uuid.replaceAll("a", "1").replaceAll("b", "2").replaceAll("c", "3").replaceAll("d", "4").replaceAll("e", "5").replaceAll("f", "6");
        }else{
            return uuid;
        }
    }

    
    
    
    public static void main(String[] args){
    	System.out.println(PrimaryGenerater.getInstance().generaterNextNumber(null));
    	System.out.println(PrimaryGenerater.getInstance().generaterNextNumber("201508060001"));
    	System.out.println(getSysJournalNo(12,true));
    	System.out.println(getSysJournalNo(12,false));
    }
}