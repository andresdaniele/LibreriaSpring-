package com.libreria.egg.repositorios;

import com.libreria.egg.entidades.Autor;
import com.libreria.egg.entidades.Editorial;
import com.libreria.egg.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioLibro extends JpaRepository<Libro, String> {
    
    @Query("SELECT l FROM Libro l WHERE l.nombre = :nombre")
    public List<Libro> buscarLibrosPorNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l WHERE l.autor = :autor")
    public List<Libro> buscarLibrosPorAutor(@Param("autor") Autor autor);

    @Query("SELECT l FROM Libro l WHERE l.alta = true ORDER BY l.nombre ASC")
    public List<Libro> listarLibros();

    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    public Libro buscarLibroPorIsbn(@Param("isbn") Long isbn);

    @Query("SELECT l FROM Libro l WHERE l.editorial = :editorial")
    public List<Libro> buscarLibrosPorEditorial(@Param("editorial") Editorial editorial);

}
