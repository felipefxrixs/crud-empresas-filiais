package br.com.crud.empresas_filiais.service;

import br.com.crud.empresas_filiais.entity.Empresa;
import br.com.crud.empresas_filiais.entity.TipoEmpresa;
import br.com.crud.empresas_filiais.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public Empresa inserirSede(Empresa empresa) {
        validarCnpjDuplicado(empresa.getCnpj());

        empresa.setId(null);
        empresa.setTipoEmpresa(TipoEmpresa.SEDE);
        empresa.setEmpresaPai(null);

        return empresaRepository.save(empresa);
    }

    public Empresa inserirFilial(Long idSede, Empresa filial) {
        validarCnpjDuplicado(filial.getCnpj());

        Empresa sede = buscarPorId(idSede);

        if (sede.getTipoEmpresa() != TipoEmpresa.SEDE) {
            throw new RuntimeException("A empresa informada não é uma sede.");
        }

        filial.setId(null);
        filial.setTipoEmpresa(TipoEmpresa.FILIAL);
        filial.setEmpresaPai(sede);

        return empresaRepository.save(filial);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public List<Empresa> listarSedes() {
        return empresaRepository.findByTipoEmpresa(TipoEmpresa.SEDE);
    }

    public List<Empresa> listarFiliaisPorSede(Long idSede) {
        return empresaRepository.findByEmpresaPaiId(idSede);
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada."));
    }

    public Empresa buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada para o CNPJ informado."));
    }

    public List<Empresa> buscarPorNome(String nome) {
        return empresaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Empresa atualizar(Long id, Empresa dados) {
        Empresa empresa = buscarPorId(id);

        if (!empresa.getCnpj().equals(dados.getCnpj())
                && empresaRepository.existsByCnpj(dados.getCnpj())) {
            throw new RuntimeException("Já existe uma empresa com esse CNPJ.");
        }

        empresa.setNome(dados.getNome());
        empresa.setNomeFantasia(dados.getNomeFantasia());
        empresa.setCnpj(dados.getCnpj());
        empresa.setTelefone(dados.getTelefone());
        empresa.setEmail(dados.getEmail());
        empresa.setLogradouro(dados.getLogradouro());
        empresa.setNumero(dados.getNumero());
        empresa.setBairro(dados.getBairro());
        empresa.setCidade(dados.getCidade());
        empresa.setCep(dados.getCep());
        empresa.setEstado(dados.getEstado());

        return empresaRepository.save(empresa);
    }

    public void deletar(Long id) {
        Empresa empresa = buscarPorId(id);
        empresaRepository.delete(empresa);
    }

    private void validarCnpjDuplicado(String cnpj) {
        if (empresaRepository.existsByCnpj(cnpj)) {
            throw new RuntimeException("Já existe uma empresa com esse CNPJ.");
        }
    }

    @Transactional
    public Empresa promoverParaSede(Long id) {
        Empresa novaSede = buscarPorId(id);

        if (novaSede.getTipoEmpresa() != TipoEmpresa.FILIAL) {
            throw new RuntimeException("A empresa informada não é uma filial.");
        }

        Empresa antigaSede = novaSede.getEmpresaPai();

        if (antigaSede == null) {
            throw new RuntimeException("A filial não possui sede vinculada.");
        }

        List<Empresa> antigasFiliais = empresaRepository.findByEmpresaPaiId(antigaSede.getId());

        // a filial escolhida vira sede
        novaSede.setTipoEmpresa(TipoEmpresa.SEDE);
        novaSede.setEmpresaPai(null);
        empresaRepository.save(novaSede);

        // a antiga sede vira filial da nova sede
        antigaSede.setTipoEmpresa(TipoEmpresa.FILIAL);
        antigaSede.setEmpresaPai(novaSede);
        empresaRepository.save(antigaSede);

        // todas as outras filiais passam para a nova sede
        for (Empresa filial : antigasFiliais) {
            if (!filial.getId().equals(novaSede.getId())) {
                filial.setEmpresaPai(novaSede);
                filial.setTipoEmpresa(TipoEmpresa.FILIAL);
                empresaRepository.save(filial);
            }
        }

        return novaSede;
    }

    @Transactional
    public Empresa vincularEmpresaExistenteComoFilial(Long idEmpresa, Long idSede) {
        Empresa empresa = buscarPorId(idEmpresa);
        Empresa sede = buscarPorId(idSede);

        if (empresa.getId().equals(sede.getId())) {
            throw new RuntimeException("Uma empresa não pode ser filial dela mesma.");
        }

        if (sede.getTipoEmpresa() != TipoEmpresa.SEDE) {
            throw new RuntimeException("A empresa selecionada como sede não é uma sede.");
        }

        empresa.setTipoEmpresa(TipoEmpresa.FILIAL);
        empresa.setEmpresaPai(sede);

        return empresaRepository.save(empresa);
    }
}