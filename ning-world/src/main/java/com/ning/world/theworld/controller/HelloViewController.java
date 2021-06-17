package com.ning.world.theworld.controller;

import com.ning.world.mvc.annotation.RequestMapping;
import com.ning.world.mvc.annotation.ViewController;

/**
 * {@link ViewController} test demo
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@ViewController
@RequestMapping("hello")
public class HelloViewController {


    @RequestMapping("view")
    public String helloWorld() {
        return "index";
    }
}
