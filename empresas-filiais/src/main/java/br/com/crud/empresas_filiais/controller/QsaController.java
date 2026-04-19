package br.com.crud.empresas_filiais.controller;

import br.com.crud.empresas_filiais.dto.QsaRequestDTO;
import br.com.crud.empresas_filiais.dto.QsaResponseDTO;
import br.com.crud.empresas_filiais.service.QsaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qsa")
@CrossOrigin(origins = "*")
public class QsaController {

    private final QsaService qsaService;

    public QsaController(QsaService qsaService) {
        this.qsaService = qsaService;
    }

    @PostMapping
    public QsaResponseDTO criar(@RequestBody QsaRequestDTO dto) {
        return qsaService.inserir(dto);
    }

    @GetMapping
    public List<QsaResponseDTO> listarTodos() {
        return qsaService.listarTodos();
    }

    @GetMapping("/{id}")
    public QsaResponseDTO buscarPorId(@PathVariable Long id) {
        return qsaService.buscarPorId(id);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<QsaResponseDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return qsaService.listarPorEmpresa(empresaId);
    }

    @GetMapping("/pessoa/{pessoaId}")
    public List<QsaResponseDTO> listarPorPessoa(@PathVariable Long pessoaId) {
        return qsaService.listarPorPessoa(pessoaId);
    }

    @PutMapping("/{id}")
    public QsaResponseDTO atualizar(@PathVariable Long id, @RequestBody QsaRequestDTO dto) {
        return qsaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        qsaService.deletar(id);
    }
}