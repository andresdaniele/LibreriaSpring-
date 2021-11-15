package com.libreria.egg.controladores;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.servicios.ClienteServicio;
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
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/listar")
    public String listarClientes(ModelMap modelo) {
        try {
            List<Cliente> clientes = clienteServicio.listarTodosLosClientes();
            modelo.addAttribute("clientes", clientes);

            return "listarCliente";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "listarCliente";
        }
    }

    @GetMapping("/cargar")
    public String guardar(ModelMap modelo) {
        return "cargarCliente";
    }

    @PostMapping("/cargar")
    public String guardarCliente(ModelMap modelo, @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido, @RequestParam(required = false) Long documento, @RequestParam(required = false) String telefono) {

        try {
            clienteServicio.registrarCliente(documento, nombre, apellido, telefono);
            modelo.put("exito", "Cliente registrado correctamente");

            return "cargarCliente";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "cargarClientex";
        }
    }

    @GetMapping("/alta/{id}")
    public String darClienteDeAlta(@PathVariable String id) {
        try {
            clienteServicio.habilitarCliente(id);
            return "redirect:/cliente/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "redirect:/cliente/listar";
        }
    }

    @GetMapping("/baja/{id}")
    public String darClienteDeBaja(@PathVariable String id) {
        try {
            clienteServicio.deshabilitarCliente(id);
            return "redirect:/cliente/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "redirect:/cliente/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(ModelMap modelo, @PathVariable String id) {
        try {
            modelo.put("cliente", clienteServicio.buscarClientePorId(id));

            return "editarCliente";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "editarCliente";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarCliente(ModelMap modelo, @PathVariable String id, @RequestParam(required = false) String nombre, @RequestParam(required = false) Long documento,
            @RequestParam(required = false) String apellido, @RequestParam(required = false) String telefono) {

        try {
            clienteServicio.modificarCliente(id, documento, nombre, apellido, telefono);
            return "redirect:/cliente/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            modelo.put("error", e.getMessage());;
            return "listarCliente";
        }
    }

}
