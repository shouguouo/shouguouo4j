package com.shouguouo.spring.aop.full;

import com.shouguouo.common.util.OutputUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

/**
 * @author shouguouo
 * @date 2021-11-28 12:44:34
 */
public class AopFullTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        Do doo = context.getBean(Do.class);
        String result = doo.doDoDo("shouuouo", LocalDate.of(1996, 12, 26));
        System.out.println("result：" + result);
        OutputUtils.cuttingLine("exceptionally");
        doo = context.getBean(Do.class);
        result = doo.doDoDo("", LocalDate.of(1996, 12, 26));
        System.out.println("result：" + result);
    }

}
