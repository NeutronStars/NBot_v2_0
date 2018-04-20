package fr.neutronstars.nbot.command;

import java.lang.annotation.*;

/**
 * Created by NeutronStars on 02/08/2017
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command
{
    String name();
    String description() default "hasn't description.";

    int powers() default 0;

    long[] guilds() default {};

    boolean toPrivate() default false;
    boolean privateOnly() default false;

    ExecutorType executor() default ExecutorType.USER;

    enum ExecutorType{
        USER, CONSOLE, ALL
    }
}
