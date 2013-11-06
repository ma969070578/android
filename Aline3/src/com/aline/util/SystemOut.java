package com.aline.util;

public class SystemOut {
   public static void out(String outStr){
	   if(EdjTools.isDebug)
	   System.out.println(outStr);
   }
}
