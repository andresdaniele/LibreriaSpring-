package com.libreria.egg.controladores;

import com.libreria.egg.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/cargar")
    public String cargar() {
        return "cargarLibro";
    }

    @PostMapping("/cargar")
    public String cargarLibro(ModelMap modelo, @RequestParam Long isbn, @RequestParam String nombre, @RequestParam Integer anio,
            @RequestParam Integer ejemplares, @RequestParam String idAutor, @RequestParam String idEditorial) throws Exception {

        try {
            libroServicio.ingresarLibro(isbn, nombre, anio, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "Carga Exitosa");
            return "cargarLibro";
            
        } catch (Exception e) {           
            modelo.put("error", "Falto algun dato");
            return "cargarLibro";
        }

    }
}
