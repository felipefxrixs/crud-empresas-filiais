package br.com.crud.empresas_filiais.repository;

import br.com.crud.empresas_filiais.entity.Qsa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QsaRepository extends JpaRepository<Qsa, Long> {

    List<Qsa> findByEmpresaId(Long empresaId);

    List<Qsa> findByPessoaId(Long pessoaId);
}