package com.mm.gemini.helper.utils;

/**
 * 随机数产生工具
 */
public class RandomCodeUtils {
	/**
	 * 产生四位随机字符串
	 * @return
	 */
	public static String getRandomCode(){
		String code="";
		for(int i=1;i<5;i++){
			if(i%2==1){
				code+=String.valueOf((int)(Math.random()*10));
			}else{
				code+=(char)(int)(Math.random()*26+97);
			}
		}
		return code;
	};
}
