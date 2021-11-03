package com.libreria.egg.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @GetMapping("/")
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
