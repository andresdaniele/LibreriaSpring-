package com.libreria.egg.controladores;

import com.libreria.egg.entidades.Autor;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/cargar")
    public String cargarAutor() {
        return "cargarAutor";
    }

    @PostMapping("/cargar")
    public String cargarNuevoAutor(ModelMap modelo, @RequestParam String nombre) {

        try {
            autorServicio.ingresarAutor(nombre);
            modelo.put("exito", "Autor cargado exitosamente");
            return "cargarAutor";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            return "cargarAutor";
        }
    }

    @GetMapping("/listar")
    public String listarAutor(ModelMap modelo) {
        try {
            List<Autor> autores = autorServicio.listarTodosLosAutores();
            modelo.addAttribute("autores", autores);
            return "listarAutor";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "listarAutor";
        }
    }

    @GetMapping("/alta/{id}")
    public String darDeAlta(@PathVariable String id) {
        try {
            autorServicio.habilitarAutor(id);
            return "redirect:/autor/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "redirect:/autor/listar";
        }
    }

    @GetMapping("/baja/{id}")
    public String darDeBaja(@PathVariable String id) {
        try {
            autorServicio.deshabilitarAutor(id);
            return "redirect:/autor/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "redirect:/autor/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarAutor(ModelMap modelo, @PathVariable String id) {

        try {           
            modelo.put("autor", autorServicio.buscarAutorPorId(id));
            return "editarAutor";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "editarAutor";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarAutor(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
        try {
            
            autorServicio.modificarAutor(id, nombre);
            
            return "redirect:/autor/listar";
            
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "editarAutor";
        }
    }
}
