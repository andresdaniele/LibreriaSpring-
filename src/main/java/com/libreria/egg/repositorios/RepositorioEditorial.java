package com.libreria.egg.repositorios;

import com.libreria.egg.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioEditorial extends JpaRepository<Editorial, String> {

    @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public Editorial buscarEditorialPorNombre(@Param("nombre") String nombre);

    @Query("SELECT e FROM Editorial e ORDER BY e.nombre ASC")
    public List<Editorial> listarEditoriales();
}
