package com.libreria.egg.controladores;

import com.libreria.egg.entidades.Libro;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.servicios.AutorServicio;
import com.libreria.egg.servicios.EditorialServicio;
import com.libreria.egg.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/listar")
    public String listarLibro(ModelMap modelo) {
        try {
            modelo.put("libros", libroServicio.listarTodosLosLibros());
            return "listarLibro";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "listarLibro";
        }
    }

    @PostMapping("/cargar")
    public String guardarLibro(ModelMap modelo, @RequestParam(value = "isbn", required = false) Long isbn, @RequestParam String nombre,
            @RequestParam(value = "anio", required = false) Integer anio, @RequestParam(value = "ejemplares", required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial) {
        try {
            libroServicio.ingresarLibro(isbn, nombre, anio, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "Carga Exitosa");
            return "cargarLibro";

        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            modelo.put("isbn", isbn);
            modelo.put("nombre",nombre);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            try {
                modelo.put("autores", autorServicio.listarTodosLosAutoresActivos());
                modelo.put("editoriales", editorialServicio.listarTodasLasEditorialesActivas());
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
                return "cargarLibro";
            }
            return "cargarLibro";
        }
    }

    @GetMapping("/cargar")
    public String guardarLibros(ModelMap modelo) {
        try {
            modelo.put("autores", autorServicio.listarTodosLosAutoresActivos());
            modelo.put("editoriales", editorialServicio.listarTodasLasEditorialesActivas());
            return "cargarLibro";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "cargarLibro";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarLibro(ModelMap modelo, @PathVariable String id) {

        try {
            Libro libro = libroServicio.buscarLibrosPorId(id);

            modelo.put("libro", libro);
            modelo.put("autores", autorServicio.listarTodosLosAutoresActivos());
            modelo.put("editoriales", editorialServicio.listarTodasLasEditorialesActivas());

            return "editarLibro";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "editarLibro";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarLibro(ModelMap modelo, @PathVariable String id, @RequestParam(value = "isbn", required = false) Long isbn, @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "anio", required = false) Integer anio, @RequestParam(value = "ejemplares", required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial) {

        try {
            Libro libro = libroServicio.buscarLibrosPorId(id);

            libroServicio.modificarLibro(id, isbn, nombre, anio, ejemplares, libro.getEjemplaresPrestados(), idAutor, idEditorial);

            modelo.put("exito", "Modificacion Exitosa");

            return "listarLibro";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());

            return "listarLibro";
        }
    }

    @GetMapping("/alta/{id}")
    public String darLibroDeAlta(@PathVariable String id) {
        try {
            libroServicio.habilitarLibro(id);
            return "redirect:/libro/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "redirect:/libro/listar";
        }
    }

    @GetMapping("/baja/{id}")
    public String darLibroDeBaja(@PathVariable String id) {
        try {
            libroServicio.deshabilitarLibro(id);
            return "redirect:/libro/listar";
        } catch (ErrorServicio e) {
            System.out.println(e.getMessage());
            return "redirect:/libro/listar";
        }
    }
}
