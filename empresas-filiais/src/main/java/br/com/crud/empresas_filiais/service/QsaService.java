package br.com.crud.empresas_filiais.service;

import br.com.crud.empresas_filiais.dto.QsaRequestDTO;
import br.com.crud.empresas_filiais.dto.QsaResponseDTO;
import br.com.crud.empresas_filiais.entity.Empresa;
import br.com.crud.empresas_filiais.entity.Pessoa;
import br.com.crud.empresas_filiais.entity.Qsa;
import br.com.crud.empresas_filiais.repository.EmpresaRepository;
import br.com.crud.empresas_filiais.repository.PessoaRepository;
import br.com.crud.empresas_filiais.repository.QsaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QsaService {

    private final QsaRepository qsaRepository;
    private final EmpresaRepository empresaRepository;
    private final PessoaRepository pessoaRepository;

    public QsaService(QsaRepository qsaRepository,
                      EmpresaRepository empresaRepository,
                      PessoaRepository pessoaRepository) {
        this.qsaRepository = qsaRepository;
        this.empresaRepository = empresaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public QsaResponseDTO inserir(QsaRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada."));

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));

        Qsa qsa = new Qsa();
        qsa.setEmpresa(empresa);
        qsa.setPessoa(pessoa);
        qsa.setVinculo(dto.getVinculo());

        return converter(qsaRepository.save(qsa));
    }

    public List<QsaResponseDTO> listarTodos() {
        return qsaRepository.findAll()
                .stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    public QsaResponseDTO buscarPorId(Long id) {
        Qsa qsa = qsaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QSA não encontrado."));
        return converter(qsa);
    }

    public List<QsaResponseDTO> listarPorEmpresa(Long empresaId) {
        return qsaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    public List<QsaResponseDTO> listarPorPessoa(Long pessoaId) {
        return qsaRepository.findByPessoaId(pessoaId)
                .stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    public QsaResponseDTO atualizar(Long id, QsaRequestDTO dto) {
        Qsa qsa = qsaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QSA não encontrado."));

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada."));

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));

        qsa.setEmpresa(empresa);
        qsa.setPessoa(pessoa);
        qsa.setVinculo(dto.getVinculo());

        return converter(qsaRepository.save(qsa));
    }

    public void deletar(Long id) {
        Qsa qsa = qsaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QSA não encontrado."));
        qsaRepository.delete(qsa);
    }

    private QsaResponseDTO converter(Qsa qsa) {
        QsaResponseDTO dto = new QsaResponseDTO();

        dto.setId(qsa.getId());

        dto.setEmpresaId(qsa.getEmpresa().getId());
        dto.setEmpresaNome(qsa.getEmpresa().getNome());
        dto.setEmpresaCnpj(qsa.getEmpresa().getCnpj());

        dto.setPessoaId(qsa.getPessoa().getId());
        dto.setPessoaNome(qsa.getPessoa().getNome());
        dto.setPessoaCpf(qsa.getPessoa().getCpf());
        dto.setPessoaEmail(qsa.getPessoa().getEmail());
        dto.setPessoaContato(qsa.getPessoa().getContato());

        dto.setVinculo(qsa.getVinculo());

        return dto;
    }
}