/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.egg.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/libro.html")
public class LibroControlador {

    @GetMapping("/index.html")
    public String index() {
        return "index.html";
    }

    @GetMapping("/libro.html")
    public String libro() {
        return "libro.html";
    }

    @GetMapping("/autor.html")
    public String autor() {
        return "autor.html";
    }

    @GetMapping("/editorial.html")
    public String editorial() {
        return "editorial.html";
    }
}
