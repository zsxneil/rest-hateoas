package com.my.hateoas;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2017/6/21.
 */
@RestController
public class GreetingController {
    private static  final  String TEMPLATE = "hello %s";
    private final AtomicLong connter = new AtomicLong();

    @RequestMapping(value = "/greeting",method = RequestMethod.GET)
    public ResponseEntity<Greeting> greeting(@RequestParam(value = "name",defaultValue = "world") String name){
        Greeting greeting = new Greeting(String.format(TEMPLATE,name));
        greeting.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GreetingController.class).greeting(name)).withSelfRel());
        return new ResponseEntity<Greeting>(greeting,HttpStatus.OK);
    }

    //"accept", "application/hal+json"
    //这里访问时必须设置request header 中 accept为"application/hal+json"，直接用浏览器访问会出错
    //测试一个
    @RequestMapping(value = "/greetings",method = RequestMethod.GET)
    public Resources<Greeting> greetings(@RequestParam(value = "name",defaultValue = "world") String name){
        Greeting greeting1 = new Greeting(String.format(TEMPLATE,name + 1));
        Greeting greeting2 = new Greeting(String.format(TEMPLATE,name + 2));
        greeting1.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GreetingController.class).greeting(name)).withSelfRel());
        greeting2.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GreetingController.class).greeting(name)).withSelfRel());
        List<Greeting> greetingList = new ArrayList<>();
        greetingList.add(greeting1);
        greetingList.add(greeting2);
        Resources<Greeting> resources = new Resources<>(greetingList);
        resources.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GreetingController.class).greetings(name)).withSelfRel());
        return resources;

    }


}
