package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.entidades.Libro;
import com.libreria.egg.entidades.Prestamo;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioCliente;
import com.libreria.egg.repositorios.RepositorioLibro;
import com.libreria.egg.repositorios.RepositorioPrestamo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private RepositorioPrestamo repositorioPrestamo;
    @Autowired
    private RepositorioLibro repositorioLibro;
    @Autowired
    private RepositorioCliente repositorioCliente;
    @Autowired
    private LibroServicio libroServicio;

    @Transactional
    public void crearPrestamo(Date fechaDevolucion, Libro libro, Cliente cliente) throws ErrorServicio {

        validarDatos(fechaDevolucion, libro, cliente);

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(new Date());
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setAlta(true);

        if (libro.getEjemplaresRestantes() == 0) {
            throw new ErrorServicio("El libro no posee ejemplares disponibles para prestamo");
        } else {
            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
        }
        libroServicio.modificarLibro(libro.getId(), libro.getIsbn(), libro.getNombre(), libro.getAnio(), libro.getEjemplares(), libro.getEjemplaresPrestados(), libro.getAutor().getId(), libro.getEditorial().getId());
        prestamo.setLibro(libro);
        prestamo.setCliente(cliente);

        repositorioPrestamo.save(prestamo);
    }

    @Transactional
    public void modificarPrestamo(String id, Date fechaDevolucion, Libro libro, Cliente cliente) throws ErrorServicio {
        System.out.println(id);
        validarDatos(fechaDevolucion, libro, cliente);

        Optional<Prestamo> respuesta = repositorioPrestamo.findById(id);

        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            System.out.println(prestamo.getId());
            prestamo.setFechaPrestamo(respuesta.get().getFechaPrestamo());
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setAlta(true);
            prestamo.setLibro(libro);
            prestamo.setCliente(cliente);

            repositorioPrestamo.save(prestamo);
        } else {
            throw new ErrorServicio("El Id ingresado no pertenece a un prestamo existente");
        }
    }

    @Transactional
    public void devolucionLibro(String id) throws ErrorServicio {

        Optional<Prestamo> respuesta = repositorioPrestamo.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == true) {
            Prestamo prestamo = respuesta.get();
            prestamo.setAlta(false);

            Libro libro = prestamo.getLibro();
            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);
            libroServicio.modificarLibro(libro.getId(), libro.getIsbn(), libro.getNombre(), libro.getAnio(), libro.getEjemplares(), libro.getEjemplaresPrestados(), libro.getAutor().getId(), libro.getEditorial().getId());
            repositorioPrestamo.save(prestamo);
        } else {
            throw new ErrorServicio("El Id ingresado no pertenece a un prestamo registrado");
        }
    }

    private void validarDatos(Date fechaDevolucion, Libro libro, Cliente cliente) throws ErrorServicio {

        if (fechaDevolucion.before(new Date()) || fechaDevolucion == null) {
            throw new ErrorServicio("La fecha de devolucion no puede ser nula o anterior a la fecha de prestamo ");
        }

        Optional<Libro> respuestaLibro = repositorioLibro.findById(libro.getId());
        if (!respuestaLibro.isPresent()) {
            throw new ErrorServicio("El libro ingresado no se encuentra registrado");
        }

        Optional<Cliente> respuestaCliente = repositorioCliente.findById(cliente.getId());
        if (!respuestaCliente.isPresent()) {
            throw new ErrorServicio("El cliente ingresado no se encuentra registrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamosCliente(Cliente cliente) throws ErrorServicio {

        if (cliente == null) {
            throw new ErrorServicio("El cliente ingresado no puede ser nulo");
        }

        List<Prestamo> prestamosClientes = repositorioPrestamo.listarPrestamosCliente(cliente);

        if (prestamosClientes.isEmpty() || prestamosClientes == null) {
            throw new ErrorServicio("El cliente ingresado no posee prestamos activos");
        } else {
            return prestamosClientes;
        }
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamosLibro(Libro libro) throws ErrorServicio {

        if (libro == null) {
            throw new ErrorServicio("El libro ingresado no puede ser nulo");
        }

        List<Prestamo> prestamosLibro = repositorioPrestamo.listarPrestamoLibros(libro);

        if (prestamosLibro.isEmpty() || prestamosLibro == null) {
            throw new ErrorServicio("El libro ingresado no posee prestamos activos");
        } else {
            return prestamosLibro;
        }
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarTodosLosPrestamosActivos() throws ErrorServicio {

        List<Prestamo> prestamosActivos = repositorioPrestamo.listarTodosLosPrestamosActivos();

        if (prestamosActivos.isEmpty() || prestamosActivos == null) {
            throw new ErrorServicio("Aun no hay prestamos activos");
        } else {
            return prestamosActivos;
        }
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarTodosLosPrestamo() throws ErrorServicio {

        List<Prestamo> prestamos = repositorioPrestamo.listarTodosLosPrestamos();

        if (prestamos.isEmpty() || prestamos == null) {
            throw new ErrorServicio("Aun no hay prestamos activos");
        } else {
            return prestamos;
        }
    }

    @Transactional(readOnly = true)
    public Prestamo buscarPrestamosPorId(String id) throws ErrorServicio {

        if (id == null || id.trim().isEmpty()) {
            throw new ErrorServicio("El Id ingresado no puede ser nulo o vacio");
        }

        Optional<Prestamo> prestamo = repositorioPrestamo.findById(id);

        if (prestamo.isPresent()) {
            return prestamo.get();
        } else {
            throw new ErrorServicio("El Id no corresponde a un prestamo registrado");
        }
    }
}
