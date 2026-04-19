package br.com.crud.empresas_filiais.repository;

import br.com.crud.empresas_filiais.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByCpf(String cpf);

    List<Pessoa> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);
}