package com.libreria.egg.repositorios;

import com.libreria.egg.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositorioCliente extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c WHERE c.nombre = :nombre")
    public List<Cliente> buscarClientePorNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM Cliente c WHERE c.apellido = :apellido")
    public List<Cliente> buscarClientePorApellido(@Param("apellido") String apellido);

    @Query("SELECT c FROM Cliente c WHERE c.documento = :documento")
    public List<Cliente> buscarClientePorDocumento(@Param("documento") Long documento);

    @Query("SELECT c FROM Cliente c WHERE c.telefono = :telefono")
    public List<Cliente> buscarClientePorTelefono(@Param("telefono") String telefono);

    @Query("SELECT c FROM Cliente c WHERE alta = true ORDER BY c.apellido ASC")
    public List<Cliente> listarTodosLosClientesActivos();

}
