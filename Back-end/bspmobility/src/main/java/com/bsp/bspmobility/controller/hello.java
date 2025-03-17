package com.bsp.bspmobility.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kaua")
public class hello {

    @GetMapping
    public String kauaefiot(){
        return "Kaua Fiot do Pai";
    }

}
