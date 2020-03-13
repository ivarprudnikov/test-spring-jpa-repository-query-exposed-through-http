package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationRunner {
    @Autowired
    FooRepository fooRepository;

    @Autowired
    BarRepository barRepository;

    @Override
    public void run(ApplicationArguments args) {
        FooEntity foo = new FooEntity();
        foo.setName("bootstrappedFoo");
        foo = fooRepository.saveAndFlush(foo);

        BarEntity bar = new BarEntity();
        bar.setName("bootstrappedBar");
        bar.setFoo(foo);
        barRepository.saveAndFlush(bar);
    }
}
