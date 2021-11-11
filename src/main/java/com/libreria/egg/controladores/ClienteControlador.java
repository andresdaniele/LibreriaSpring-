package com.libreria.egg.controladores;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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

}
