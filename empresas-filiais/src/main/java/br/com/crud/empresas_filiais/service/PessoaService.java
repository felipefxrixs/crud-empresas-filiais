package br.com.crud.empresas_filiais.service;

import br.com.crud.empresas_filiais.entity.Pessoa;
import br.com.crud.empresas_filiais.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa inserir(Pessoa pessoa) {
        validarCpfDuplicado(pessoa.getCpf());
        pessoa.setId(null);
        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));
    }

    public Pessoa buscarPorCpf(String cpf) {
        return pessoaRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada para o CPF informado."));
    }

    public List<Pessoa> buscarPorNome(String nome) {
        return pessoaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Pessoa atualizar(Long id, Pessoa dados) {
        Pessoa pessoa = buscarPorId(id);

        if (!pessoa.getCpf().equals(dados.getCpf())
                && pessoaRepository.existsByCpf(dados.getCpf())) {
            throw new RuntimeException("Já existe uma pessoa com esse CPF.");
        }

        pessoa.setNome(dados.getNome());
        pessoa.setCpf(dados.getCpf());
        pessoa.setEmail(dados.getEmail());
        pessoa.setContato(dados.getContato());

        return pessoaRepository.save(pessoa);
    }

    public void deletar(Long id) {
        Pessoa pessoa = buscarPorId(id);
        pessoaRepository.delete(pessoa);
    }

    private void validarCpfDuplicado(String cpf) {
        if (pessoaRepository.existsByCpf(cpf)) {
            throw new RuntimeException("Já existe uma pessoa com esse CPF.");
        }
    }
}