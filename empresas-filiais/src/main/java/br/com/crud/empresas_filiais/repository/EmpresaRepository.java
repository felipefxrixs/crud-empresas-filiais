package br.com.crud.empresas_filiais.repository;

import br.com.crud.empresas_filiais.entity.Empresa;
import br.com.crud.empresas_filiais.entity.TipoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);

    List<Empresa> findByNomeContainingIgnoreCase(String nome);

    List<Empresa> findByEmpresaPaiId(Long empresaPaiId);

    List<Empresa> findByTipoEmpresa(TipoEmpresa tipoEmpresa);

    boolean existsByCnpj(String cnpj);
}