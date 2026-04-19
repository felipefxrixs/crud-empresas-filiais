package br.com.crud.empresas_filiais.controller;

import br.com.crud.empresas_filiais.entity.Empresa;
import br.com.crud.empresas_filiais.service.EmpresaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping("/sede")
    public Empresa criarSede(@RequestBody Empresa empresa) {
        return empresaService.inserirSede(empresa);
    }

    @PostMapping("/{idSede}/filiais")
    public Empresa criarFilial(@PathVariable Long idSede, @RequestBody Empresa filial) {
        return empresaService.inserirFilial(idSede, filial);
    }

    @GetMapping
    public List<Empresa> listarTodas() {
        return empresaService.listarTodas();
    }

    @GetMapping("/sedes")
    public List<Empresa> listarSedes() {
        return empresaService.listarSedes();
    }

    @GetMapping("/{id}/filiais")
    public List<Empresa> listarFiliais(@PathVariable Long id) {
        return empresaService.listarFiliaisPorSede(id);
    }

    @GetMapping("/{id}")
    public Empresa buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    @GetMapping("/cnpj/{cnpj}")
    public Empresa buscarPorCnpj(@PathVariable String cnpj) {
        return empresaService.buscarPorCnpj(cnpj);
    }

    @GetMapping("/buscar")
    public List<Empresa> buscarPorNome(@RequestParam String nome) {
        return empresaService.buscarPorNome(nome);
    }

    @PutMapping("/{id}")
    public Empresa atualizar(@PathVariable Long id, @RequestBody Empresa empresa) {
        return empresaService.atualizar(id, empresa);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        empresaService.deletar(id);
    }

    @PutMapping("/{id}/promover-para-sede")
    public Empresa promoverParaSede(@PathVariable Long id) {
        return empresaService.promoverParaSede(id);
    }

    @PutMapping("/{idEmpresa}/vincular-a-sede/{idSede}")
    public Empresa vincularEmpresaExistenteComoFilial(@PathVariable Long idEmpresa, @PathVariable Long idSede) {
        return empresaService.vincularEmpresaExistenteComoFilial(idEmpresa, idSede);
    }
}