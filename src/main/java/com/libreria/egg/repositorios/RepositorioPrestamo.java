package com.libreria.egg.repositorios;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.entidades.Libro;
import com.libreria.egg.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioPrestamo extends JpaRepository<Prestamo, String> {

    @Query("SELECT p FROM Prestamo p WHERE p.cliente = :cliente")
    public List<Prestamo> listarPrestamosCliente(@Param("cliente") Cliente cliente);

    @Query("SELECT p FROM Prestamo p WHERE p.libro = :libro AND alta = true")
    public List<Prestamo> listarPrestamoLibros(@Param("libro") Libro libro);

    @Query("SELECT p FROM Prestamo p WHERE alta = true ORDER BY p.fechaPrestamo DESC")
    public List<Prestamo> listarTodosLosPrestamosActivos();

}
