package com.libreria.egg.controladores;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.entidades.Libro;
import com.libreria.egg.entidades.Prestamo;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.servicios.ClienteServicio;
import com.libreria.egg.servicios.LibroServicio;
import com.libreria.egg.servicios.PrestamoServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/prestamo")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoServicio;

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/listar")
    public String listarPrestamo(ModelMap modelo) {
        try {

            List<Prestamo> prestamos = prestamoServicio.listarTodosLosPrestamo();

            modelo.addAttribute("prestamos", prestamos);

            return "listarPrestamo";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "listarPrestamo";
        }
    }

    @PostMapping("/cargar")
    public String cargar(ModelMap modelo, @RequestParam(value = "fechaDevolucion", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaDevolucion,
            @RequestParam(required = false) String idLibro, @RequestParam(required = false) String idCliente) {
        try {
            Libro libro = libroServicio.buscarLibrosPorId(idLibro);
            Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
            prestamoServicio.crearPrestamo(fechaDevolucion, libro, cliente);

            modelo.put("exito", "Prestamo guardado correctamente");
            return "cargarPrestamo";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            modelo.put("fechaDevolucion", fechaDevolucion);

            try {
                modelo.put("libros", libroServicio.listarTodosLosLibrosActivos());
                modelo.put("clientes", clienteServicio.listarTodosLosClientesActivos());
            } catch (ErrorServicio e2) {
                System.out.println(e2.getMessage());
                return "cargarPrestamo";
            }
            return "cargarPrestamo";
        }

    }

    @GetMapping("/cargar")
    public String cargarPrestamo(ModelMap modelo) {
        try {
            modelo.addAttribute("libros", libroServicio.listarTodosLosLibros());
            modelo.addAttribute("clientes", clienteServicio.listarTodosLosClientesActivos());

            return "cargarPrestamo";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "cargarPrestamo";
        }
    }

    @GetMapping("/devolucion/{id}")
    public String devolverLibro(@PathVariable String id) {
        try {
            prestamoServicio.devolucionLibro(id);
            return "redirect:/prestamo/listar";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "redirect:/prestamo/listar";
        }
    }

    @GetMapping("editar/{id}")
    public String editar(ModelMap modelo, @PathVariable String id) {
        try {
            modelo.addAttribute("prestamo", prestamoServicio.buscarPrestamosPorId(id));
            modelo.addAttribute("libros", libroServicio.listarTodosLosLibrosActivos());
            modelo.addAttribute("clientes", clienteServicio.listarTodosLosClientesActivos());
            return "editarPrestamo";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "editarPrestamo";
        }

    }

    @PostMapping("/editar/{id}")
    public String editarPrestamo(ModelMap modelo, @PathVariable String id, @RequestParam(value = "fechaDevolucion", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaDevolucion, @RequestParam(required = false) String idLibro,
            @RequestParam(required = false) String idCliente) {

        try {
            prestamoServicio.modificarPrestamo(id, fechaDevolucion, libroServicio.buscarLibrosPorId(idLibro), clienteServicio.buscarClientePorId(idCliente));
            return "redirect:/prestamo/listar";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            return "listarPrestamo";
        }

    }

    

}
