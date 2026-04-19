<template>
  <q-page class="page-wrap">
    <div class="cadastro-grid">
      <!-- TOPO -->
      <div class="top-actions">
        <q-btn flat no-caps class="sk-btn empresa-btn" label="Atualizar" @click="atualizar" />
        <q-btn flat no-caps class="sk-btn empresa-btn" label="Excluir" @click="excluirRegistro" />
        <q-btn flat no-caps class="sk-btn empresa-btn" label="Salvar" @click="salvar" />
        <q-btn flat no-caps class="sk-btn empresa-btn" label="Cancelar" @click="cancelar" />
        <q-btn
          flat
          no-caps
          class="sk-btn empresa-btn"
          :label="botaoPromocaoLabel"
          @click="promoverOuRebaixar"
        />
        <q-btn
          flat
          no-caps
          class="sk-btn empresa-btn"
          :label="painelDireitoTitulo"
          @click="visualizarPainelDireito"
        />
      </div>

      <!-- BUSCA ESQUERDA -->
      <div class="left-search border-box">
        <q-btn
          flat
          no-caps
          class="clear-btn"
          label="Limpar Dados"
          @click="limpar"
        />

        <q-input
          v-model="documento"
          dense
          outlined
          class="doc-input cnpj-busca-input"
          placeholder="00.000.000/0000-00"
          maxlength="18"
          @keyup.enter="buscarPorDocumento"
        />

        <q-btn
          flat
          no-caps
          class="search-btn search-btn-highlight search-btn-single-line"
          label="Pesquisar Empresa"
          @click="buscarPorDocumento"
        />
      </div>

      <!-- CABEÇALHO -->
      <div class="center-header border-box">
        <div class="header-row">
          <div class="header-label">Razão Social:</div>
          <div class="header-field">
            <q-input v-model="form.nome" dense borderless :disable="!modoEdicao" />
          </div>

          <div class="header-label right-label">CNPJ:</div>
          <div class="header-field">
            <q-input :model-value="formatarCnpj(form.cnpj)" dense borderless disable />
          </div>
        </div>

        <div class="header-row">
          <div class="header-label">Nome Fantasia:</div>
          <div class="header-field">
            <q-input v-model="form.nomeFantasia" dense borderless :disable="!modoEdicao" />
          </div>

          <div class="header-label right-label">SEDE:</div>
          <div class="header-field sede-cell">
            <q-btn
              flat
              no-caps
              class="mini-inline-btn"
              :label="sedeLabel"
              :disable="!modoEdicao || !!form.id"
              @click="dialogTipoCadastro = true"
            />
          </div>
        </div>
      </div>

      <!-- PAINEL DIREITO -->
      <div class="painel-direito border-box">
        <div class="painel-title">{{ painelDireitoSubtitulo }}</div>

        <div class="painel-list">
          <div
            v-for="item in painelDireitoLista"
            :key="item.id"
            class="painel-item"
            :class="{ selected: painelSelecionado?.id === item.id }"
            @click="selecionarPainel(item)"
          >
            {{ item.nome }}
          </div>
        </div>

        <div class="painel-actions">
          <q-btn
            flat
            no-caps
            class="sk-btn full-btn empresa-btn painel-filial-btn"
            :label="painelDireitoBotaoIncluir"
            @click="acaoIncluirPainelDireito"
          />
        </div>
      </div>

      <!-- TABELA QSA -->
      <div class="qsa-area border-box">
        <div class="qsa-head qsa-head-highlight">
          <div>Representante</div>
          <div>Vínculo</div>
          <div>Documentos</div>
        </div>

        <div class="qsa-body">
          <div
            v-for="item in qsaRows"
            :key="item.id"
            class="qsa-row"
            :class="{ selected: qsaSelecionado?.id === item.id }"
            @click="selecionarQsa(item)"
          >
            <div>{{ item.pessoaNome }}</div>
            <div>{{ item.vinculo }}</div>
            <div>{{ formatarCpf(item.pessoaCpf) }}</div>
          </div>
        </div>

        <div class="qsa-actions">
          <q-btn flat no-caps class="sk-btn qsa-btn qsa-btn-highlight" label="Incluir" @click="abrirDialogQsa" />
          <q-btn flat no-caps class="sk-btn qsa-btn qsa-btn-highlight" label="Excluir" @click="excluirQsaSelecionado" />
          <q-btn flat no-caps class="sk-btn qsa-btn qsa-btn-highlight" label="Alterar" @click="alterarQsaSelecionado" />
        </div>
      </div>

      <!-- BOTÕES INFERIORES ESQUERDOS -->
      <div class="print-qsa side-button-wrap">
  <q-btn
    flat
    no-caps
    class="side-action-btn"
    label="Listar Pessoas"
    @click="listarPessoas"
  />
</div>

<div class="print-dados side-button-wrap">
  <q-btn
    flat
    no-caps
    class="side-action-btn"
    label="Listar Empresas"
    @click="listarEmpresas"
  />
</div>

      <!-- ENDEREÇO -->
      <div class="address-area border-box">
        <div class="address-row address-top">
          <div class="address-cell">
            <strong>CEP:</strong>
            <q-input
              v-model="form.cep"
              dense
              borderless
              maxlength="9"
              :disable="!modoEdicao"
            />
          </div>

          <div class="address-cell">
            <strong>UF:</strong>
            <q-input v-model="form.estado" dense borderless :disable="!modoEdicao" />
          </div>

          <div class="address-cell">
            <strong>MUNICÍPIO:</strong>
            <q-input v-model="form.cidade" dense borderless :disable="!modoEdicao" />
          </div>

          <div class="address-cell">
            <strong>E-MAIL:</strong>
            <q-input v-model="form.email" dense borderless :disable="!modoEdicao" />
          </div>
        </div>

        <div class="address-row middle-row">
          <div class="address-cell wrap-cell">
            <strong>LOGRADOURO:</strong>
            <q-input
              v-model="form.logradouro"
              dense
              borderless
              :disable="!modoEdicao"
            />
          </div>

          <div class="address-cell"><strong>PAÍS:</strong> Brasil</div>
        </div>

        <div class="address-row bottom-row">
          <div class="address-cell">
            <strong>BAIRRO:</strong>
            <q-input v-model="form.bairro" dense borderless :disable="!modoEdicao" />
          </div>

          <div class="address-cell">
            <strong>NÚMERO:</strong>
            <q-input
              v-model="form.numero"
              dense
              borderless
              maxlength="10"
              :disable="!modoEdicao"
            />
          </div>

          <div class="address-cell">
            <strong>CONTATO:</strong>
            <q-input
              v-model="form.telefone"
              dense
              borderless
              maxlength="15"
              :disable="!modoEdicao"
            />
          </div>
        </div>

        <div class="address-row obs-row">
          <div class="address-cell wrap-cell">
            <strong>OBSERVAÇÃO:</strong>
            <q-input
              v-model="observacao"
              dense
              borderless
              type="textarea"
              autogrow
              :disable="!modoEdicao"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- DIALOG: CNPJ NÃO ENCONTRADO -->
    <q-dialog v-model="dialogNovoCadastro">
      <q-card class="confirm-card">
        <q-card-section class="text-center text-subtitle1">
          CNPJ não encontrado, deseja incluir?
        </q-card-section>

        <q-separator />

        <q-card-actions align="around">
          <q-btn flat no-caps class="sk-btn small-choice" label="SIM" @click="confirmarCadastro" />
          <q-btn flat no-caps class="sk-btn small-choice" label="NÃO" @click="cancelarDialog" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- DIALOG: SEDE SIM/NÃO -->
    <q-dialog v-model="dialogTipoCadastro">
      <q-card class="confirm-card">
        <q-card-section class="text-center text-subtitle1">
          Esta empresa será sede?
        </q-card-section>

        <q-separator />

        <q-card-actions align="around">
          <q-btn
            flat
            no-caps
            class="sk-btn small-choice"
            label="SIM"
            @click="definirComoSede(true)"
          />
          <q-btn
            flat
            no-caps
            class="sk-btn small-choice"
            label="NÃO"
            @click="definirComoSede(false)"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- DIALOG: QSA -->
    <q-dialog v-model="dialogQsa">
      <q-card class="qsa-dialog">
        <q-card-section class="dialog-title">
          <div><strong>REPRESENTANTE:</strong></div>
          <q-input v-model="filtroPessoa" dense outlined />
        </q-card-section>

        <q-card-section class="dialog-actions-row">
          <q-btn flat no-caps class="sk-btn small-choice" label="Pessoa Física" />
          <q-btn flat no-caps class="sk-btn small-choice" label="Pessoa Jurídica" disable />
          <q-space />
          <q-btn
            flat
            no-caps
            class="sk-btn small-choice"
            label="Nova Pessoa"
            @click="abrirDialogNovaPessoa"
          />
        </q-card-section>

        <q-separator />

        <q-card-section class="qsa-dialog-table">
          <div class="dialog-table-head">
            <div>Nome</div>
            <div>CPF/CNPJ</div>
            <div>Contato</div>
          </div>

          <div
            v-for="pessoa in pessoasFiltradas"
            :key="pessoa.id"
            class="dialog-table-row"
            :class="{ selected: pessoaSelecionada?.id === pessoa.id }"
            @click="selecionarPessoa(pessoa)"
          >
            <div>{{ pessoa.nome }}</div>
            <div>{{ formatarCpf(pessoa.cpf) }}</div>
            <div>{{ formatarTelefone(pessoa.contato) }}</div>
          </div>
        </q-card-section>

        <q-card-section>
          <q-input v-model="vinculoQsa" label="Vínculo" dense outlined />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat no-caps class="sk-btn small-choice" label="Voltar" @click="voltarQsa" />
          <q-btn flat no-caps class="sk-btn small-choice" label="Incluir" @click="salvarQsa" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- DIALOG: NOVA PESSOA -->
    <q-dialog v-model="dialogNovaPessoa">
      <q-card class="nova-pessoa-dialog">
        <q-card-section class="text-h6">Nova Pessoa</q-card-section>

        <q-card-section class="q-gutter-md">
          <q-input v-model="novaPessoa.nome" label="Nome" outlined />
          <q-input v-model="novaPessoa.cpf" label="CPF" outlined maxlength="14" />
          <q-input v-model="novaPessoa.email" label="E-mail" outlined />
          <q-input v-model="novaPessoa.contato" label="Contato" outlined maxlength="15" />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" v-close-popup />
          <q-btn color="primary" label="Salvar" @click="salvarNovaPessoa" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- DIALOG: SELECIONAR SEDE -->
    <q-dialog v-model="dialogSelecionarSede">
      <q-card class="qsa-dialog">
        <q-card-section class="dialog-title">
          <div><strong>SEDE:</strong></div>
          <q-input v-model="filtroSede" dense outlined />
        </q-card-section>

        <q-separator />

        <q-card-section class="qsa-dialog-table">
          <div class="dialog-table-head">
            <div>Nome</div>
            <div>CNPJ</div>
            <div>Tipo</div>
          </div>

          <div
            v-for="empresa in sedesFiltradas"
            :key="empresa.id"
            class="dialog-table-row"
            :class="{ selected: sedeSelecionada?.id === empresa.id }"
            @click="sedeSelecionada = empresa"
          >
            <div>{{ empresa.nome }}</div>
            <div>{{ formatarCnpj(empresa.cnpj) }}</div>
            <div>{{ empresa.tipoEmpresa }}</div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" v-close-popup />
          <q-btn color="primary" label="Selecionar" @click="confirmarSedeSelecionada" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- DIALOG: INCLUIR FILIAL EXISTENTE -->
    <q-dialog v-model="dialogFilialExistente">
      <q-card class="qsa-dialog">
        <q-card-section class="dialog-title">
          <div><strong>FILIAL:</strong></div>
          <q-input v-model="filtroEmpresa" dense outlined placeholder="Nome ou CNPJ" />
        </q-card-section>

        <q-separator />

        <q-card-section class="qsa-dialog-table">
          <div class="dialog-table-head">
            <div>Nome</div>
            <div>CNPJ</div>
            <div>Tipo</div>
          </div>

          <div
            v-for="empresa in empresasFiltradas"
            :key="empresa.id"
            class="dialog-table-row"
            :class="{ selected: empresaSelecionadaFilial?.id === empresa.id }"
            @click="empresaSelecionadaFilial = empresa"
          >
            <div>{{ empresa.nome }}</div>
            <div>{{ formatarCnpj(empresa.cnpj) }}</div>
            <div>{{ empresa.tipoEmpresa }}</div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" v-close-popup />
          <q-btn color="primary" label="Selecionar" @click="confirmarIncluirFilialExistente" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- DIALOG: LISTAR EMPRESAS -->
    <q-dialog v-model="dialogListarEmpresas">
      <q-card class="qsa-dialog">
        <q-card-section class="dialog-title">
          <div><strong>EMPRESAS:</strong></div>
          <q-input v-model="filtroEmpresaLista" dense outlined placeholder="Nome ou CNPJ" />
        </q-card-section>

        <q-separator />

        <q-card-section class="qsa-dialog-table">
          <div class="dialog-table-head">
            <div>Nome</div>
            <div>CNPJ</div>
            <div>Tipo</div>
          </div>

          <div
            v-for="empresa in empresasListadas"
            :key="empresa.id"
            class="dialog-table-row"
            @click="abrirEmpresaDaLista(empresa)"
          >
            <div>{{ empresa.nome }}</div>
            <div>{{ formatarCnpj(empresa.cnpj) }}</div>
            <div>{{ empresa.tipoEmpresa }}</div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Fechar" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- DIALOG: LISTAR PESSOAS -->
    <q-dialog v-model="dialogListarPessoas">
      <q-card class="qsa-dialog pessoas-dialog">
        <q-card-section class="dialog-title">
          <div><strong>PESSOAS:</strong></div>
          <q-input
           v-model="filtroPessoaLista"
          dense
          outlined
          placeholder="Nome ou CPF"
          :model-value="filtroPessoaLista"
          @update:model-value="
            (valor) => {
              const texto = valor || ''
              const digitos = somenteDigitos(texto)

              filtroPessoaLista =
                texto.replace(/[^\d]/g, '').length > 0 && !/[a-zA-Z]/.test(texto)
                  ? formatarCpf(digitos)
                  : texto
            }
          "
        />
      </q-card-section>

      <q-separator />

      <q-card-section class="qsa-dialog-table pessoas-table-section">
         <div class="dialog-table-head pessoas-table-head">
         <div>Nome</div>
          <div>CPF</div>
          <div>Contato</div>
          <div>E-mail</div>
        </div>

        <div
          v-for="pessoa in pessoasListadas"
          :key="pessoa.id"
          class="dialog-table-row pessoas-table-row"
          :class="{ selected: pessoaSelecionadaLista?.id === pessoa.id }"
          @click="selecionarPessoaLista(pessoa)"
        >
          <div>{{ pessoa.nome }}</div>
          <div>{{ formatarCpf(pessoa.cpf) }}</div>
          <div>{{ formatarTelefone(pessoa.contato) }}</div>
          <div>{{ pessoa.email }}</div>
        </div>
      </q-card-section>

      <q-card-section class="pessoas-actions">
        <q-btn flat no-caps class="sk-btn qsa-btn qsa-btn-highlight" label="Inserir" @click="abrirInserirPessoaLista" />
        <q-btn flat no-caps class="sk-btn qsa-btn qsa-btn-highlight" label="Alterar" @click="abrirAlterarPessoaLista" />
        <q-btn flat no-caps class="sk-btn qsa-btn qsa-btn-highlight" label="Excluir" @click="excluirPessoaLista" />
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Fechar" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>

  <q-dialog v-model="dialogPessoaLista">
    <q-card class="nova-pessoa-dialog">
      <q-card-section class="text-h6">
        {{ editandoPessoaLista ? 'Alterar Pessoa' : 'Inserir Pessoa' }}
      </q-card-section>

      <q-card-section class="q-gutter-md">
        <q-input v-model="pessoaListaForm.nome" label="Nome" outlined />
        <q-input v-model="pessoaListaForm.cpf" label="CPF" outlined maxlength="14" />
        <q-input v-model="pessoaListaForm.email" label="E-mail" outlined />
       <q-input v-model="pessoaListaForm.contato" label="Contato" outlined maxlength="15" />
     </q-card-section>

     <q-card-actions align="right">
        <q-btn flat label="Cancelar" v-close-popup />
       <q-btn color="primary" :label="editandoPessoaLista ? 'Salvar Alteração' : 'Salvar'" @click="salvarPessoaLista" />
     </q-card-actions>
   </q-card>
  </q-dialog>

    <q-dialog v-model="dialogSucesso">
    <q-card class="confirm-card">
      <q-card-section class="text-center text-subtitle1">
        {{ mensagemSucesso }}
      </q-card-section>

      <q-separator />

      <q-card-actions align="center">
        <q-btn
          flat
          no-caps
          class="sk-btn small-choice"
          label="OK"
          @click="confirmarDialogSucesso"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</q-page>

</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { Notify } from 'quasar'
import api from 'src/services/api'

const documento = ref('')
const dialogNovoCadastro = ref(false)
const dialogQsa = ref(false)
const dialogTipoCadastro = ref(false)
const dialogNovaPessoa = ref(false)
const dialogSelecionarSede = ref(false)
const dialogFilialExistente = ref(false)
const dialogListarEmpresas = ref(false)
const dialogListarPessoas = ref(false)
const dialogSucesso = ref(false)
const mensagemSucesso = ref('')
const acaoAposSucesso = ref(null)


const modoEdicao = ref(false)
const empresaEhSede = ref(true)

const formInicial = () => ({
  id: null,
  nome: '',
  nomeFantasia: '',
  cnpj: '',
  telefone: '',
  email: '',
  logradouro: '',
  numero: '',
  bairro: '',
  cidade: '',
  cep: '',
  estado: '',
  tipoEmpresa: 'SEDE',
  empresaPai: null,
})

const form = ref(formInicial())
const observacao = ref('')

const qsaRows = ref([])
const qsaSelecionado = ref(null)

const pessoas = ref([])
const filiais = ref([])
const todasEmpresas = ref([])
const todasSedes = ref([])

const painelSelecionado = ref(null)

const filtroPessoa = ref('')
const pessoaSelecionada = ref(null)
const vinculoQsa = ref('')

const filtroSede = ref('')
const sedeSelecionada = ref(null)

const filtroEmpresa = ref('')
const empresaSelecionadaFilial = ref(null)

const filtroEmpresaLista = ref('')
const filtroPessoaLista = ref('')

const dialogPessoaLista = ref(false)
const editandoPessoaLista = ref(false)
const pessoaSelecionadaLista = ref(null)

const pessoaListaForm = ref({
  id: null,
  nome: '',
  cpf: '',
  email: '',
  contato: '',
})

const novaPessoa = ref({
  nome: '',
  cpf: '',
  email: '',
  contato: '',
})

function somenteDigitos(valor) {
  return (valor || '').replace(/\D/g, '')
}

function formatarCpf(valor) {
  const v = somenteDigitos(valor).slice(0, 11)
  return v
    .replace(/^(\d{3})(\d)/, '$1.$2')
    .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/\.(\d{3})(\d)/, '.$1-$2')
}

function formatarCnpj(valor) {
  const v = somenteDigitos(valor).slice(0, 14)
  return v
    .replace(/^(\d{2})(\d)/, '$1.$2')
    .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/\.(\d{3})(\d)/, '.$1/$2')
    .replace(/(\d{4})(\d)/, '$1-$2')
}

function formatarCep(valor) {
  const v = somenteDigitos(valor).slice(0, 8)
  return v.replace(/^(\d{5})(\d)/, '$1-$2')
}

function formatarTelefone(valor) {
  const v = somenteDigitos(valor).slice(0, 11)

  if (v.length <= 10) {
    return v
      .replace(/^(\d{2})(\d)/, '($1) $2')
      .replace(/(\d{4})(\d)/, '$1-$2')
  }

  return v
    .replace(/^(\d{2})(\d)/, '($1) $2')
    .replace(/(\d{5})(\d)/, '$1-$2')
}

function limitarTexto(valor, max = 120) {
  return (valor || '').toString().slice(0, max)
}

function abrirDialogSucesso(mensagem, callback = null) {
  mensagemSucesso.value = mensagem
  acaoAposSucesso.value = callback
  dialogSucesso.value = true
}

async function confirmarDialogSucesso() {
  dialogSucesso.value = false

  if (typeof acaoAposSucesso.value === 'function') {
    await acaoAposSucesso.value()
  }

  acaoAposSucesso.value = null
}

watch(
  () => pessoaListaForm.value.cpf,
  (novo) => {
    pessoaListaForm.value.cpf = formatarCpf(novo)
  },
)

watch(
  () => pessoaListaForm.value.contato,
  (novo) => {
    pessoaListaForm.value.contato = formatarTelefone(novo)
  },
)

watch(documento, (novo) => {
  documento.value = formatarCnpj(novo)
})

watch(
  () => form.value.cnpj,
  (novo) => {
    form.value.cnpj = formatarCnpj(novo)
  },
)

watch(
  () => form.value.cep,
  (novo) => {
    form.value.cep = formatarCep(novo)
  },
)

watch(
  () => form.value.telefone,
  (novo) => {
    form.value.telefone = formatarTelefone(novo)
  },
)

watch(
  () => form.value.numero,
  (novo) => {
    form.value.numero = limitarTexto(novo, 10)
  },
)

watch(
  () => novaPessoa.value.cpf,
  (novo) => {
    novaPessoa.value.cpf = formatarCpf(novo)
  },
)

watch(
  () => novaPessoa.value.contato,
  (novo) => {
    novaPessoa.value.contato = formatarTelefone(novo)
  },
)

const isSedeAtual = computed(() => {
  if (form.value.id) return form.value.tipoEmpresa === 'SEDE'
  return empresaEhSede.value
})

const sedeLabel = computed(() => (isSedeAtual.value ? 'SIM' : 'NÃO'))
const botaoPromocaoLabel = computed(() =>
  isSedeAtual.value ? 'Promover a Sede' : 'Rebaixar à Filial',
)
const painelDireitoTitulo = computed(() =>
  isSedeAtual.value ? 'Visualizar Filiais' : 'Visualizar Sede',
)
const painelDireitoSubtitulo = computed(() =>
  isSedeAtual.value ? 'Filiais Vinculadas:' : 'Sede Vinculada:',
)
const painelDireitoBotaoIncluir = computed(() =>
  isSedeAtual.value ? 'Incluir Filial' : 'Incluir Sede',
)

const painelDireitoLista = computed(() => {
  if (isSedeAtual.value) return filiais.value
  return form.value.empresaPai ? [form.value.empresaPai] : []
})

const pessoasFiltradas = computed(() => {
  const termo = normalizarTexto(filtroPessoa.value)
  const termoDigitos = somenteDigitos(filtroPessoa.value)

  if (!termo && !termoDigitos) return pessoas.value

  return pessoas.value.filter((p) => {
    const nome = normalizarTexto(p.nome)
    const cpf = somenteDigitos(p.cpf || '')

    const bateNome = termo ? nome.includes(termo) : false
    const bateCpf = termoDigitos ? cpf.includes(termoDigitos) : false

    return bateNome || bateCpf
  })
})

const sedesFiltradas = computed(() => {
  const termo = normalizarTexto(filtroSede.value)
  const termoDigitos = somenteDigitos(filtroSede.value)

  if (!termo && !termoDigitos) return todasSedes.value

  return todasSedes.value.filter((e) => {
    const nome = normalizarTexto(e.nome)
    const cnpj = somenteDigitos(e.cnpj || '')

    const bateNome = termo ? nome.includes(termo) : false
    const bateCnpj = termoDigitos ? cnpj.includes(termoDigitos) : false

    return bateNome || bateCnpj
  })
})

const empresasFiltradas = computed(() => {
  const termo = normalizarTexto(filtroEmpresa.value)
  const termoDigitos = somenteDigitos(filtroEmpresa.value)

  if (!termo && !termoDigitos) {
    return todasEmpresas.value.filter((e) => e.id !== form.value.id)
  }

  return todasEmpresas.value.filter((e) => {
    const nome = normalizarTexto(e.nome)
    const cnpj = somenteDigitos(e.cnpj || '')

    const bateNome = termo ? nome.includes(termo) : false
    const bateCnpj = termoDigitos ? cnpj.includes(termoDigitos) : false

    return e.id !== form.value.id && (bateNome || bateCnpj)
  })
})

const empresasListadas = computed(() => {
  const termo = normalizarTexto(filtroEmpresaLista.value)
  const termoDigitos = somenteDigitos(filtroEmpresaLista.value)

  if (!termo && !termoDigitos) return todasEmpresas.value

  return todasEmpresas.value.filter((e) => {
    const nome = normalizarTexto(e.nome)
    const cnpj = somenteDigitos(e.cnpj || '')

    const bateNome = termo ? nome.includes(termo) : false
    const bateCnpj = termoDigitos ? cnpj.includes(termoDigitos) : false

    return bateNome || bateCnpj
  })
})

const pessoasListadas = computed(() => {
  const termo = normalizarTexto(filtroPessoaLista.value)
  const termoDigitos = somenteDigitos(filtroPessoaLista.value)

  if (!termo && !termoDigitos) return pessoas.value

  return pessoas.value.filter((p) => {
    const nome = normalizarTexto(p.nome)
    const cpf = somenteDigitos(p.cpf || '')

    const bateNome = termo ? nome.includes(termo) : false
    const bateCpf = termoDigitos ? cpf.includes(termoDigitos) : false

    return bateNome || bateCpf
  })
})

async function carregarBase() {
  await Promise.all([carregarPessoas(), carregarEmpresas(), carregarSedes()])
}

async function recarregarTelaAtual() {
  await carregarBase()

  if (form.value?.cnpj) {
    await abrirEmpresaPorCnpj(form.value.cnpj)
  } else {
    qsaRows.value = []
    filiais.value = []
    painelSelecionado.value = null
    qsaSelecionado.value = null
  }
}

async function abrirEmpresaPorCnpj(cnpj) {
  if (!cnpj) return

  const cnpjLimpo = somenteDigitos(cnpj)

  const res = await api.get(`/empresas/cnpj/${cnpjLimpo}`)
  form.value = { ...formInicial(), ...res.data }
  documento.value = formatarCnpj(res.data.cnpj || cnpjLimpo)
  empresaEhSede.value = form.value.tipoEmpresa === 'SEDE'
  modoEdicao.value = false
  painelSelecionado.value = null
  qsaSelecionado.value = null

  await carregarQsa()
  await carregarFiliais()
}

async function buscarPorDocumento() {
  if (!documento.value) {
    Notify.create({ type: 'warning', message: 'Informe um CNPJ' })
    return
  }

  try {
    await abrirEmpresaPorCnpj(somenteDigitos(documento.value))
  } catch {
    dialogNovoCadastro.value = true
  }
}

async function atualizar() {
  if (!form.value.id) {
    Notify.create({ type: 'warning', message: 'Nenhuma empresa carregada' })
    return
  }

  try {
    await abrirEmpresaPorCnpj(somenteDigitos(form.value.cnpj))
    modoEdicao.value = true
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: 'Não foi possível atualizar o cadastro',
    })
  }
}

function confirmarCadastro() {
  dialogNovoCadastro.value = false
  form.value = {
    ...formInicial(),
    cnpj: documento.value,
  }
  observacao.value = ''
  qsaRows.value = []
  filiais.value = []
  qsaSelecionado.value = null
  painelSelecionado.value = null
  empresaEhSede.value = true
  modoEdicao.value = true
}

async function definirComoSede(valor) {
  empresaEhSede.value = valor
  form.value.tipoEmpresa = valor ? 'SEDE' : 'FILIAL'
  dialogTipoCadastro.value = false

  if (!valor) {
    sedeSelecionada.value = null
    painelSelecionado.value = null

    await carregarEmpresas()
    await carregarSedes()

    abrirDialogSelecionarSede()
  } else {
    form.value.empresaPai = null

    await carregarEmpresas()
    await carregarSedes()
  }
}

function cancelarDialog() {
  dialogNovoCadastro.value = false
  documento.value = ''
}

async function salvar() {
  try {
    if (!modoEdicao.value) {
      Notify.create({
        type: 'warning',
        message: 'Clique em Atualizar para editar ou pesquise um CNPJ novo para incluir',
      })
      return
    }

    const payload = {
      nome: form.value.nome,
      nomeFantasia: form.value.nomeFantasia,
      cnpj: somenteDigitos(form.value.cnpj),
      telefone: somenteDigitos(form.value.telefone),
      email: form.value.email,
      logradouro: form.value.logradouro,
      numero: form.value.numero,
      bairro: form.value.bairro,
      cidade: form.value.cidade,
      cep: somenteDigitos(form.value.cep),
      estado: form.value.estado,
    }

    let cnpjParaRecarregar = somenteDigitos(form.value.cnpj)

    if (form.value.id) {
      await api.put(`/empresas/${form.value.id}`, payload)
    } else {
      let res

      if (empresaEhSede.value) {
        res = await api.post('/empresas/sede', payload)
      } else {
        if (!sedeSelecionada.value?.id) {
          Notify.create({
            type: 'warning',
            message: 'Selecione uma sede antes de salvar a filial',
          })
          return
        }

        res = await api.post(`/empresas/${sedeSelecionada.value.id}/filiais`, payload)
      }

      cnpjParaRecarregar = somenteDigitos(res.data.cnpj)
    }

    dialogTipoCadastro.value = false
    modoEdicao.value = false

    abrirDialogSucesso('EMPRESA SALVA COM SUCESSO!', async () => {
      if (cnpjParaRecarregar) {
        await abrirEmpresaPorCnpj(cnpjParaRecarregar)
      } else {
        limpar()
        await carregarBase()
      }

      modoEdicao.value = false
    })
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao salvar empresa',
    })
  }
}

async function cancelar() {
  modoEdicao.value = false

  try {
    if (form.value?.cnpj) {
      await abrirEmpresaPorCnpj(somenteDigitos(form.value.cnpj))
    } else {
      limpar()
      await carregarEmpresas()
      await carregarSedes()
      await carregarPessoas()
    }
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: 'Não foi possível restaurar os dados do cadastro',
    })
  }
}

function limpar() {
  documento.value = ''
  form.value = formInicial()
  observacao.value = ''
  qsaRows.value = []
  filiais.value = []
  qsaSelecionado.value = null
  painelSelecionado.value = null
  empresaEhSede.value = true
  sedeSelecionada.value = null
  modoEdicao.value = false
}

async function excluirRegistro() {
  if (!form.value.id) {
    Notify.create({ type: 'warning', message: 'Nenhuma empresa carregada' })
    return
  }

  try {
    await api.delete(`/empresas/${form.value.id}`)

    abrirDialogSucesso('EMPRESA EXCLUÍDA COM SUCESSO!', async () => {
      limpar()
      await carregarBase()
    })
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao excluir empresa',
    })
  }
}

async function visualizarPainelDireito() {
  if (!form.value.id) {
    Notify.create({
      type: 'warning',
      message: 'Nenhuma empresa carregada',
    })
    return
  }

  if (!painelSelecionado.value?.cnpj) {
    Notify.create({
      type: 'warning',
      message: isSedeAtual.value
        ? 'Selecione uma filial no painel lateral'
        : 'Selecione a sede no painel lateral',
    })
    return
  }

  try {
    await abrirEmpresaPorCnpj(painelSelecionado.value.cnpj)
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: 'Não foi possível abrir esse cadastro',
    })
  }
}

async function promoverOuRebaixar() {
  if (!form.value.id) {
    Notify.create({
      type: 'warning',
      message: 'Nenhuma empresa carregada',
    })
    return
  }

  try {
    let idAlvo
    let cnpjAtualizar

    if (isSedeAtual.value) {
      if (!painelSelecionado.value?.id) {
        Notify.create({
          type: 'warning',
          message: 'Selecione uma filial no painel lateral para rebaixar a sede atual',
        })
        return
      }

      idAlvo = painelSelecionado.value.id
      cnpjAtualizar = painelSelecionado.value.cnpj
    } else {
      idAlvo = form.value.id
      cnpjAtualizar = form.value.cnpj
    }

    await api.put(`/empresas/${idAlvo}/promover-para-sede`)
    await carregarBase()

    if (cnpjAtualizar) {
      await abrirEmpresaPorCnpj(cnpjAtualizar)
    }

    Notify.create({
      type: 'positive',
      message: isSedeAtual.value
        ? 'Sede rebaixada e filial promovida com sucesso'
        : 'Filial promovida com sucesso',
    })
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao promover/rebaixar estrutura',
    })
  }
}

async function carregarQsa() {
  if (!form.value.id) {
    qsaRows.value = []
    return
  }

  try {
    const res = await api.get(`/qsa/empresa/${form.value.id}`)
    qsaRows.value = res.data
  } catch {
    qsaRows.value = []
  }
}

async function carregarFiliais() {
  if (!form.value.id || form.value.tipoEmpresa !== 'SEDE') {
    filiais.value = []
    return
  }

  try {
    const res = await api.get(`/empresas/${form.value.id}/filiais`)
    filiais.value = res.data
  } catch {
    filiais.value = []
  }
}

async function carregarPessoas() {
  try {
    const res = await api.get('/pessoas')
    pessoas.value = res.data
  } catch {
    pessoas.value = []
  }
}

async function carregarEmpresas() {
  try {
    const res = await api.get('/empresas')
    todasEmpresas.value = res.data
  } catch {
    todasEmpresas.value = []
  }
}

async function carregarSedes() {
  try {
    const res = await api.get('/empresas/sedes')
    todasSedes.value = res.data
  } catch {
    todasSedes.value = []
  }
}

function selecionarPainel(item) {
  painelSelecionado.value = item
}

function selecionarQsa(item) {
  qsaSelecionado.value = item
}

function abrirDialogQsa() {
  if (!form.value.id) {
    Notify.create({ type: 'warning', message: 'Busque ou cadastre uma empresa primeiro' })
    return
  }

  qsaSelecionado.value = null
  pessoaSelecionada.value = null
  filtroPessoa.value = ''
  vinculoQsa.value = ''
  dialogQsa.value = true
}

function selecionarPessoa(pessoa) {
  pessoaSelecionada.value = pessoa
}

async function salvarQsa() {
  if (!pessoaSelecionada.value || !vinculoQsa.value?.trim()) {
    Notify.create({
      type: 'warning',
      message: 'Selecione uma pessoa e informe o vínculo',
    })
    return
  }

  const cnpjAtual = form.value.cnpj

  const payload = {
    empresaId: form.value.id,
    pessoaId: pessoaSelecionada.value.id,
    vinculo: vinculoQsa.value.trim(),
  }

  dialogQsa.value = false

  const pessoaBackup = pessoaSelecionada.value
  const vinculoBackup = vinculoQsa.value
  const qsaBackup = qsaSelecionado.value

  qsaSelecionado.value = null
  pessoaSelecionada.value = null
  vinculoQsa.value = ''
  filtroPessoa.value = ''

  try {
    if (qsaBackup?.id) {
      await api.put(`/qsa/${qsaBackup.id}`, payload)

      Notify.create({
        type: 'positive',
        message: 'Representante alterado com sucesso',
      })
    } else {
      await api.post('/qsa', payload)

      Notify.create({
        type: 'positive',
        message: 'Representante vinculado com sucesso',
      })
    }

    if (cnpjAtual) {
      await abrirEmpresaPorCnpj(cnpjAtual)
    } else {
      await carregarQsa()
      await carregarFiliais()
    }

    await carregarEmpresas()
    await carregarSedes()
    await carregarPessoas()
  } catch (error) {
    console.error('Erro ao salvar representante:', error)

    pessoaSelecionada.value = pessoaBackup
    vinculoQsa.value = vinculoBackup
    qsaSelecionado.value = qsaBackup
    dialogQsa.value = true

    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao salvar representante',
    })
  }
}

async function voltarQsa() {
  try {
    dialogQsa.value = false

    if (form.value?.cnpj) {
      await abrirEmpresaPorCnpj(somenteDigitos(form.value.cnpj))
    } else {
      await carregarQsa()
      await carregarFiliais()
    }
  } catch (error) {
    console.error('Erro ao voltar do QSA:', error)
    Notify.create({
      type: 'negative',
      message: 'Erro ao atualizar a tela',
    })
  }
}

async function excluirQsaSelecionado() {
  if (!qsaSelecionado.value?.id) {
    Notify.create({
      type: 'warning',
      message: 'Selecione um representante na tabela',
    })
    return
  }

  try {
    await api.delete(`/qsa/${qsaSelecionado.value.id}`)

    qsaSelecionado.value = null

    await abrirEmpresaPorCnpj(form.value.cnpj)
    await carregarBase()

    Notify.create({
      type: 'positive',
      message: 'Representante excluído com sucesso',
    })
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: 'Erro ao excluir representante',
    })
  }
}

function alterarQsaSelecionado() {
  if (!qsaSelecionado.value?.id) {
    Notify.create({
      type: 'warning',
      message: 'Selecione um representante na tabela',
    })
    return
  }

  vinculoQsa.value = qsaSelecionado.value.vinculo
  pessoaSelecionada.value =
    pessoas.value.find((p) => p.id === qsaSelecionado.value.pessoaId) || null
  dialogQsa.value = true
}

function abrirDialogNovaPessoa() {
  novaPessoa.value = {
    nome: '',
    cpf: '',
    email: '',
    contato: '',
  }
  dialogNovaPessoa.value = true
}

async function salvarNovaPessoa() {
  try {
    const res = await api.post('/pessoas', {
      ...novaPessoa.value,
      cpf: somenteDigitos(novaPessoa.value.cpf),
      contato: somenteDigitos(novaPessoa.value.contato),
    })
    dialogNovaPessoa.value = false
    await carregarPessoas()
    pessoaSelecionada.value = res.data

    Notify.create({
      type: 'positive',
      message: 'Nova pessoa cadastrada com sucesso',
    })
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao cadastrar nova pessoa',
    })
  }
}

function abrirDialogSelecionarSede() {
  sedeSelecionada.value = null
  filtroSede.value = ''
  dialogSelecionarSede.value = true
}

function confirmarSedeSelecionada() {
  if (!sedeSelecionada.value?.id) {
    Notify.create({
      type: 'warning',
      message: 'Selecione uma sede',
    })
    return
  }

  form.value.empresaPai = sedeSelecionada.value
  dialogSelecionarSede.value = false
}

function acaoIncluirPainelDireito() {
  if (isSedeAtual.value) {
    incluirFilial()
  } else {
    abrirDialogSelecionarSede()
  }
}

function incluirFilial() {
  if (!form.value.id || form.value.tipoEmpresa !== 'SEDE') {
    Notify.create({
      type: 'warning',
      message: 'Carregue uma sede primeiro',
    })
    return
  }

  empresaSelecionadaFilial.value = null
  filtroEmpresa.value = ''
  dialogFilialExistente.value = true
}

async function confirmarIncluirFilialExistente() {
  if (!empresaSelecionadaFilial.value?.id) {
    Notify.create({
      type: 'warning',
      message: 'Selecione uma empresa para vincular como filial',
    })
    return
  }

  try {
    await api.put(`/empresas/${empresaSelecionadaFilial.value.id}/vincular-a-sede/${form.value.id}`)
    dialogFilialExistente.value = false

    await recarregarTelaAtual()

    Notify.create({
      type: 'positive',
      message: 'Filial vinculada com sucesso',
    })
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao incluir filial',
    })
  }
}

async function listarPessoas() {
  await carregarPessoas()
  pessoaSelecionadaLista.value = null
  dialogListarPessoas.value = true
}

async function listarEmpresas() {
  await carregarEmpresas()
  dialogListarEmpresas.value = true
}

function normalizarTexto(valor) {
  return (valor || '')
    .toString()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim()
}

async function abrirEmpresaDaLista(empresa) {
  dialogListarEmpresas.value = false

  try {
    await abrirEmpresaPorCnpj(empresa.cnpj)
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: 'Não foi possível abrir esse cadastro',
    })
  }
}

function selecionarPessoaLista(pessoa) {
  pessoaSelecionadaLista.value = pessoa
}

function limparPessoaListaForm() {
  pessoaListaForm.value = {
    id: null,
    nome: '',
    cpf: '',
    email: '',
    contato: '',
  }
}

function abrirInserirPessoaLista() {
  pessoaSelecionadaLista.value = null
  editandoPessoaLista.value = false
  limparPessoaListaForm()
  dialogPessoaLista.value = true
}

function abrirAlterarPessoaLista() {
  if (!pessoaSelecionadaLista.value?.id) {
    Notify.create({
      type: 'warning',
      message: 'Selecione uma pessoa na lista',
    })
    return
  }

  editandoPessoaLista.value = true
  pessoaListaForm.value = {
    id: pessoaSelecionadaLista.value.id,
    nome: pessoaSelecionadaLista.value.nome || '',
    cpf: formatarCpf(pessoaSelecionadaLista.value.cpf || ''),
    email: pessoaSelecionadaLista.value.email || '',
    contato: formatarTelefone(pessoaSelecionadaLista.value.contato || ''),
  }

  dialogPessoaLista.value = true
}

async function salvarPessoaLista() {
  try {
    const payload = {
      nome: pessoaListaForm.value.nome,
      cpf: somenteDigitos(pessoaListaForm.value.cpf),
      email: pessoaListaForm.value.email,
      contato: somenteDigitos(pessoaListaForm.value.contato),
    }

    if (!payload.nome || !payload.cpf) {
      Notify.create({
        type: 'warning',
        message: 'Informe pelo menos nome e CPF',
      })
      return
    }

    if (editandoPessoaLista.value && pessoaListaForm.value.id) {
      await api.put(`/pessoas/${pessoaListaForm.value.id}`, payload)

      abrirDialogSucesso('PESSOA ATUALIZADA COM SUCESSO!', async () => {
        dialogPessoaLista.value = false
        await carregarPessoas()
      })
    } else {
      await api.post('/pessoas', payload)

      abrirDialogSucesso('PESSOA INSERIDA COM SUCESSO!', async () => {
        dialogPessoaLista.value = false
        await carregarPessoas()
      })
    }
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao salvar pessoa',
    })
  }
}

async function excluirPessoaLista() {
  if (!pessoaSelecionadaLista.value?.id) {
    Notify.create({
      type: 'warning',
      message: 'Selecione uma pessoa na lista',
    })
    return
  }

  try {
    await api.delete(`/pessoas/${pessoaSelecionadaLista.value.id}`)

    pessoaSelecionadaLista.value = null
    await carregarPessoas()

    Notify.create({
      type: 'positive',
      message: 'Pessoa excluída com sucesso',
    })
  } catch (error) {
    console.error(error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao excluir pessoa',
    })
  }
}

onMounted(async () => {
  await carregarBase()
})
</script>

<style scoped>
.page-wrap {
  background: #f5f5f2;
  padding: 16px;
}

.cadastro-grid {
  display: grid;
  grid-template-columns: 180px 1fr 250px;
  grid-template-rows: 80px 130px 220px 60px 170px;
  gap: 0;
  border: 1px solid #222;
  background: #fafafa;
  font-family: Arial, sans-serif;
}

.border-box {
  border: 1px solid #222;
  box-sizing: border-box;
  background: #f8f8f8;
}

.top-actions {
  grid-column: 1 / 4;
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  border-bottom: 1px solid #222;
}

.sk-btn {
  border-right: 1px solid #222;
  min-height: 78px;
  color: #111;
  font-weight: 700;
}

.sk-btn:last-child {
  border-right: none;
}

.clear-btn {
  width: 100%;
  min-height: 44px;
  border-bottom: 1px solid #222;
  border-right: none;
  border-left: none;
  border-top: none;

  background: #a4b9e6;
  color: #0f2240;

  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}

.clear-btn:hover {
  background: #97afdf;
}

.empresa-btn {
  background: #a4b9e6;
  color: #0f2240;
}

.empresa-btn:hover {
  background: #97afdf;
}

.left-search {
  grid-column: 1 / 2;
  grid-row: 2 / 4;
  display: flex;
  flex-direction: column;
  min-width: 180px;
}

.left-search-title {
  padding: 10px 8px;
  border-bottom: 1px solid #222;
  font-size: 14px;
  font-weight: 700;
}

.doc-input {
  padding: 10px;
  width: 100%;
  box-sizing: border-box;
}

.cnpj-busca-input {
  min-width: 0;
  width: 100%;
}

.cnpj-busca-input :deep(.q-field__control) {
  padding: 0 4px;
}

.cnpj-busca-input :deep(.q-field__native),
.cnpj-busca-input :deep(.q-field__input),
.cnpj-busca-input :deep(input) {
  font-size: 12px;
  white-space: nowrap;
  overflow: visible;
  text-overflow: unset;
  letter-spacing: -0.3px;
}

.doc-input :deep(.q-field__control),
.doc-input :deep(.q-field__native) {
  font-size: 13px;
}

.cnpj-busca-input :deep(.q-field__native) {
  font-size: 12px;
}

.pessoas-table-section {
  min-height: 260px;
}

.pessoas-actions {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border-top: 1px solid #222;
}

.search-btn {
  min-height: 48px;
  margin: 10px;
  border: 1px solid #222;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-btn-highlight {
  background: #fff;
  color: #111;
  font-weight: 700;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.16);
}

.search-btn-highlight:hover {
  background: #f1f1f1;
}

.center-header {
  grid-column: 2 / 3;
  grid-row: 2 / 3;
  padding: 12px 18px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.search-btn-single-line {
  white-space: nowrap;
}

.search-btn-single-line :deep(.q-btn__content) {
  white-space: nowrap;
  font-size: 14px;
  line-height: 1;
}

.header-row {
  display: grid;
  grid-template-columns: 140px minmax(280px, 1fr) 70px 220px;
  align-items: center;
  margin-bottom: 14px;
  gap: 10px;
}

.header-label {
  font-weight: 700;
}

.right-label {
  text-align: right;
}

.header-field {
  border-bottom: 1px solid #777;
  min-height: 28px;
  min-width: 0;
}

.header-field :deep(.q-field__native) {
  white-space: nowrap;
  overflow: visible;
  text-overflow: unset;
}

.sede-cell {
  display: flex;
  align-items: center;
}

.mini-inline-btn {
  min-height: 30px;
  width: 100%;
}

.painel-direito {
  grid-column: 3 / 4;
  grid-row: 2 / 4;
  display: grid;
  grid-template-rows: 48px 1fr 56px;
}

.painel-title {
  padding: 10px 8px;
  border-bottom: 1px solid #222;
  font-weight: 700;
  text-align: center;
  background: #efefef;
  color: #111;
}

.painel-list {
  padding: 10px;
  overflow-y: auto;
  background: #f8f8f8;
}

.painel-item {
  padding: 8px 6px;
  border-bottom: 1px solid #ddd;
  text-align: center;
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.painel-item.selected {
  background: #d4d1cd;
  font-weight: 700;
}

.painel-actions {
  border-top: 1px solid #222;
}

.full-btn {
  width: 100%;
  min-height: 55px;
  height: 55px;
}

.painel-filial-btn {
  background: #a4b9e6;
  color: #0f2240;
}

.painel-filial-btn:hover {
  background: #97afdf;
}

.qsa-area {
  grid-column: 2 / 3;
  grid-row: 3 / 4;
  display: flex;
  flex-direction: column;
}

.qsa-head,
.qsa-row {
  display: grid;
  grid-template-columns: 2fr 1.5fr 1.5fr;
}

.qsa-head {
  font-weight: 700;
  border-bottom: 1px solid #222;
}

.qsa-head > div,
.qsa-row > div {
  padding: 8px 10px;
  border-right: 1px solid #222;
  display: flex;
  align-items: center;
  min-height: 46px;
  box-sizing: border-box;
}

.qsa-head > div:last-child,
.qsa-row > div:last-child {
  border-right: none;
}

.qsa-body {
  flex: 1;
  overflow-y: auto;
}

.qsa-row {
  border-bottom: 1px solid #ddd;
  cursor: pointer;
}

.qsa-row.selected {
  background: #e7e4e0;
}

.qsa-actions {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border-top: 1px solid #222;
}

.qsa-btn {
  min-height: 50px;
}

.qsa-btn-highlight {
  background: #9c9894;
  color: #fff;
}

.qsa-btn-highlight:hover {
  background: #8c8884;
}

.print-qsa {
  grid-column: 1 / 2;
  grid-row: 4 / 5;
  border-left: 1px solid #222;
  border-right: 1px solid #222;
  border-top: none;
  border-bottom: 1px solid #222;
}

.print-dados {
  grid-column: 1 / 2;
  grid-row: 5 / 6;
  border-left: 1px solid #222;
  border-right: 1px solid #222;
  border-top: none;
  border-bottom: 1px solid #222;
}

.side-button-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 10px;
  background: #f8f8f8;
}

.side-action-btn {
  width: 100%;
  min-height: 44px;
  border: 1px solid #222;
  border-radius: 6px;
  background: #fff;
  color: #111;
  font-weight: 700;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.16);
  display: flex;
  align-items: center;
  justify-content: center;
}

.side-action-btn:hover {
  background: #f1f1f1;
}

.side-action-btn :deep(.q-btn__content) {
  white-space: nowrap;
  font-size: 14px;
  line-height: 1;
}

.address-area {
  grid-column: 2 / 4;
  grid-row: 4 / 6;
  display: flex;
  flex-direction: column;
}

.address-row {
  display: grid;
  border-bottom: 1px solid #222;
  min-height: 54px;
}

.address-top {
  grid-template-columns: 1fr 0.8fr 1.8fr 1.8fr;
}

.middle-row {
  grid-template-columns: 2fr 1fr;
}

.bottom-row {
  grid-template-columns: 1.5fr 1fr 1.2fr;
}

.address-row > div {
  padding: 6px 10px;
  border-right: 1px solid #222;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  min-height: 54px;
}

.address-row > div:last-child {
  border-right: none;
}

.address-cell {
  display: grid;
  grid-template-columns: auto 1fr;
  align-items: center;
  gap: 6px;
  min-width: 0;
}

.address-cell strong {
  white-space: nowrap;
  flex-shrink: 0;
  font-weight: 700;
}

.address-cell :deep(.q-field) {
  flex: 1;
  min-width: 0;
}

.address-cell :deep(.q-field__control) {
  min-height: 28px;
}

.address-cell :deep(.q-field__native) {
  min-width: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.wrap-cell {
  white-space: normal;
  overflow-wrap: anywhere;
}

.obs-row {
  grid-template-columns: 1fr;
  border-bottom: none;
  min-height: 55px;
}

.confirm-card {
  min-width: 340px;
  border: 1px solid #222;
}

.small-choice {
  border: 1px solid #222;
  min-width: 100px;
  min-height: 42px;
}

.qsa-dialog,
.nova-pessoa-dialog {
  min-width: 760px;
  max-width: 900px;
  border: 1px solid #222;
}

.pessoas-dialog {
  min-width: 980px;
  max-width: 1100px;
}

.dialog-title {
  display: grid;
  grid-template-columns: 150px 1fr;
  align-items: center;
  gap: 8px;
}

.dialog-actions-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.qsa-dialog-table {
  min-height: 220px;
}

.dialog-table-head,
.dialog-table-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
}

.pessoas-table-head,
.pessoas-table-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 2.5fr;
}

.dialog-table-head {
  font-weight: 700;
  border: 1px solid #222;
  background: #efefef;
}

.dialog-table-head > div,
.dialog-table-row > div {
  padding: 6px 8px;
  border-right: 1px solid #222;
}

.dialog-table-head > div:last-child,
.dialog-table-row > div:last-child {
  border-right: none;
}

.dialog-table-row {
  border-left: 1px solid #222;
  border-right: 1px solid #222;
  border-bottom: 1px solid #ddd;
  cursor: pointer;
}

.dialog-table-row.selected {
  background: #d9edf7;
}

.pessoas-table-row > div:last-child,
.pessoas-table-head > div:last-child {
  overflow-wrap: anywhere;
  white-space: normal;
}

:deep(.q-field__native),
:deep(textarea.q-field__native) {
  white-space: normal;
  overflow-wrap: anywhere;
}

@media (max-width: 1200px) {
  .cadastro-grid {
    display: flex;
    flex-direction: column;
  }

  .top-actions {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
  }

  .pessoas-dialog {
    min-width: 95vw;
  }
}
</style>
