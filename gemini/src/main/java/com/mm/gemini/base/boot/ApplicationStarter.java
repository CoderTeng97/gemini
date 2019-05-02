package com.mm.gemini.base.boot;


import com.mm.gemini.base.enums.Env;

public abstract class ApplicationStarter {
    //环境前缀
    private static final String  ENV_PRX = "--spring.profiles.active=";
    //环境属性名
    private static final String  ENV_PROPERTY_NAME = "user.env";

    public static void start(String[] args){
        String env = Env.LOCAL.name();
        if (args != null && args.length > 0) {
            for (String arg : args) {
                if (null != arg && arg.startsWith(ENV_PRX)) {
                    env = arg.substring(ENV_PRX.length());
                }
            }
        }

        System.out.println(" *********************************");
        System.out.println("*** 				***");
        System.out.println("*** server is starting,env is	***");
        System.out.println("*** 				***");
        System.out.println("***		" + env.toUpperCase() + " !		***");
        System.out.println("*** 				***");
        System.out.println(" *********************************");
        System.setProperty(ENV_PROPERTY_NAME, env.toLowerCase());
    }
}
