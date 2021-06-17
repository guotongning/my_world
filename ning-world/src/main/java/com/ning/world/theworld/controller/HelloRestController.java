package com.ning.world.theworld.controller;

import com.ning.world.mvc.annotation.RequestMapping;
import com.ning.world.mvc.annotation.RestController;
import com.ning.world.mvc.annotation.ViewController;
import com.ning.world.mvc.response.Result;

import javax.ws.rs.POST;

/**
 * {@link RestController} test demo
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@RestController
@RequestMapping("hello")
public class HelloRestController {
    @POST
    @RequestMapping("world")
    public Result helloWorld() {
        return Result.ok("hello world");
    }
}
