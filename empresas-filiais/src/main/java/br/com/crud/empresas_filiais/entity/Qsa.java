package br.com.crud.empresas_filiais.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "qsa")
public class Qsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonIgnoreProperties({"filiais", "qsa"})
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    @JsonIgnoreProperties("qsa")
    private Pessoa pessoa;

    @Column(nullable = false, length = 100)
    private String vinculo;

    public Qsa() {
    }

    public Long getId() {
        return id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }
}