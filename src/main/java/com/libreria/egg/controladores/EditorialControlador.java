/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.egg.controladores;

import com.libreria.egg.entidades.Editorial;
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

        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "cargarEditorial";
        }

    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {

        try {
            List<Editorial> listaEditoriales = editorialServicio.listarTodasLasEditoriales();
            modelo.addAttribute("editoriales", listaEditoriales);
            return "listarEditorial";
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return "listarEditorial";
        }

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, ModelMap modelo) {
        try {
            modelo.put("editorial", editorialServicio.buscarEditoriaPorId(id));
            return "editarEditorial";

        } catch (Exception e) {
            e.getMessage();
            return "editarEditorial";
        }
    }

    @PostMapping("/editar/{id}")
    public String editar(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {

        try {
            editorialServicio.modificarEditorial(id, nombre);
            modelo.put("exito", "Modificacion exitosa");

           return "listarEditorial";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "editarEditorial";
        }
    }
}
