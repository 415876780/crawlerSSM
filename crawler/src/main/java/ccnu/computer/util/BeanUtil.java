package ccnu.computer.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class BeanUtil {
    private static ApplicationContext act;
    
    public static void init() {
        if (act == null) {
            act = new ClassPathXmlApplicationContext("beans.xml");
        }
    }
    public static ApplicationContext getAct() {
        return act;
    }

    public static Object getBean(String beanName) {
        init();
        if (beanName == null) {
            return null;
        }
        return act.getBean(beanName);
    }
}