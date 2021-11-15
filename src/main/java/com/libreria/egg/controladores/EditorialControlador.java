/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.egg.controladores;

import com.libreria.egg.entidades.Editorial;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/cargar")
    public String cargar() {
        return "cargarEditorial";
    }

    @PostMapping("/cargar")
    public String cargarEditorial(ModelMap modelo, @RequestParam String nombre) {

        try {
            editorialServicio.ingresarEditorial(nombre);
            modelo.put("exito", "Carga Exitosa");
            return "cargarEditorial";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            return "cargarEditorial";
        }

    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {

        try {
            List<Editorial> listaEditoriales = editorialServicio.listarTodasLasEditoriales();
            modelo.addAttribute("editoriales", listaEditoriales);
            return "listarEditorial";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "listarEditorial";
        }

    }

    @GetMapping("/editar/{id}")
    public String editar(ModelMap modelo, @PathVariable String id) {
        try {
            Editorial editorial = editorialServicio.buscarEditoriaPorId(id);
            modelo.put("editorial", editorial);

            return "editarEditorial";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "editarEditorial";
        }
    }

    @PostMapping("/editar/{id}")
    public String editar(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {

        try {
            editorialServicio.modificarEditorial(id, nombre);
            modelo.put("exito", "Modificacion exitosa");

            return "listarEditorial";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            modelo.put("error", "Falto algun dato");;
            return "editarEditorial";
        }
    }

    @GetMapping("/baja/{id}")
    public String darDeBajaEditorial(@PathVariable String id) {

        try {
            editorialServicio.deshabilitarEditorial(id);
            return "redirect:/editorial/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "redirect:/editorial/listar";
        }

    }

    @GetMapping("/alta/{id}")
    public String darDeAltaEditorial(@PathVariable String id, ModelMap modelo) {

        try {
            editorialServicio.habilitarEditorial(id);
            return "redirect:/editorial/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            return "listarEitorial";
        }

    }
}
