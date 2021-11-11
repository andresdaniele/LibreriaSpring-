package com.libreria.egg.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/libro")
    public String libro() {
        return "libro";
    }

    @GetMapping("/editorial")
    public String editorial() {
        return "editorial";
    }

    @GetMapping("/autor")
    public String autor() {
        return "autor";
    }

    @GetMapping("/prestamo")
    public String prestamo() {
        return "prestamo";
    }

    @GetMapping("/cliente")
    public String cliente() {
        return "cliente";
    }

}
