<!-- <template>
  <q-page class="q-pa-lg">
    <div class="text-h5 q-mb-lg">Cadastro de Empresas</div>

    <q-card flat bordered class="q-mb-lg">
      <q-form @submit.prevent="salvarEmpresa">
        <q-card-section class="q-gutter-md">
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-input v-model="form.nome" label="Razão Social" outlined />
            </div>

            <div class="col-12 col-md-6">
              <q-input v-model="form.nomeFantasia" label="Nome Fantasia" outlined />
            </div>

            <div class="col-12 col-md-4">
              <q-input v-model="form.cnpj" label="CNPJ" outlined />
            </div>

            <div class="col-12 col-md-4">
              <q-input v-model="form.telefone" label="Telefone" outlined />
            </div>

            <div class="col-12 col-md-4">
              <q-input v-model="form.email" label="E-mail" outlined />
            </div>

            <div class="col-12 col-md-4">
              <q-input v-model="form.cep" label="CEP" outlined />
            </div>

            <div class="col-12 col-md-2">
              <q-input v-model="form.estado" label="Estado" outlined />
            </div>

            <div class="col-12 col-md-6">
              <q-input v-model="form.cidade" label="Cidade" outlined />
            </div>

            <div class="col-12 col-md-6">
              <q-input v-model="form.logradouro" label="Logradouro" outlined />
            </div>

            <div class="col-12 col-md-2">
              <q-input v-model="form.numero" label="Número" outlined />
            </div>

            <div class="col-12 col-md-4">
              <q-input v-model="form.bairro" label="Bairro" outlined />
            </div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Limpar" color="secondary" @click="limparFormulario" />
          <q-btn label="Salvar" color="primary" type="submit" />
        </q-card-actions>
      </q-form>
    </q-card>

    <q-card flat bordered>
      <q-card-section>
        <div class="text-h6">Empresas cadastradas</div>
      </q-card-section>

      <q-separator />

      <q-card-section>
        <q-table :rows="empresas" :columns="columns" row-key="id" flat>
          <template v-slot:body-cell-acoes="props">
            <q-td align="center">
              <q-btn flat round icon="edit" color="secondary" @click="editarEmpresa(props.row)" />
              <q-btn
                flat
                round
                icon="delete"
                color="negative"
                @click="deletarEmpresa(props.row.id)"
              />
            </q-td>
          </template>
        </q-table>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { Notify } from 'quasar'
import api from 'src/services/api'

const empresas = ref([])

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
})

const form = ref(formInicial())

const columns = [
  { name: 'id', label: 'ID', field: 'id', align: 'left' },
  { name: 'nome', label: 'Razão Social', field: 'nome', align: 'left' },
  { name: 'cnpj', label: 'CNPJ', field: 'cnpj', align: 'left' },
  { name: 'tipoEmpresa', label: 'Tipo', field: 'tipoEmpresa', align: 'left' },
  { name: 'cidade', label: 'Cidade', field: 'cidade', align: 'left' },
  { name: 'estado', label: 'UF', field: 'estado', align: 'left' },
  { name: 'acoes', label: 'Ações', field: 'acoes', align: 'center' },
]

async function listarEmpresas() {
  try {
    const response = await api.get('/empresas')
    empresas.value = Array.isArray(response.data) ? response.data : []
  } catch (error) {
    console.error('Erro ao listar empresas:', error)
    Notify.create({ type: 'negative', message: 'Erro ao listar empresas' })
  }
}

async function salvarEmpresa() {
  try {
    if (!form.value.nome || !form.value.cnpj) {
      Notify.create({
        type: 'warning',
        message: 'Razão Social e CNPJ são obrigatórios',
      })
      return
    }

    const payload = {
      nome: form.value.nome,
      nomeFantasia: form.value.nomeFantasia,
      cnpj: form.value.cnpj,
      telefone: form.value.telefone,
      email: form.value.email,
      logradouro: form.value.logradouro,
      numero: form.value.numero,
      bairro: form.value.bairro,
      cidade: form.value.cidade,
      cep: form.value.cep,
      estado: form.value.estado,
    }

    console.log('Payload empresa:', payload)

    if (form.value.id) {
      await api.put(`/empresas/${form.value.id}`, payload)
      Notify.create({ type: 'positive', message: 'Empresa atualizada com sucesso' })
    } else {
      await api.post('/empresas/sede', payload)
      Notify.create({ type: 'positive', message: 'Empresa cadastrada com sucesso' })
    }

    limparFormulario()
    await listarEmpresas()
  } catch (error) {
    console.error('Erro ao salvar empresa:', error)
    Notify.create({
      type: 'negative',
      message: error?.response?.data?.message || 'Erro ao salvar empresa',
    })
  }
}

function editarEmpresa(empresa) {
  form.value = {
    id: empresa.id,
    nome: empresa.nome || '',
    nomeFantasia: empresa.nomeFantasia || '',
    cnpj: empresa.cnpj || '',
    telefone: empresa.telefone || '',
    email: empresa.email || '',
    logradouro: empresa.logradouro || '',
    numero: empresa.numero || '',
    bairro: empresa.bairro || '',
    cidade: empresa.cidade || '',
    cep: empresa.cep || '',
    estado: empresa.estado || '',
  }
}

async function deletarEmpresa(id) {
  try {
    await api.delete(`/empresas/${id}`)
    await listarEmpresas()
    Notify.create({ type: 'positive', message: 'Empresa excluída com sucesso' })
  } catch (error) {
    console.error('Erro ao excluir empresa:', error)
    Notify.create({ type: 'negative', message: 'Erro ao excluir empresa' })
  }
}

function limparFormulario() {
  form.value = formInicial()
}

onMounted(() => {
  listarEmpresas()
})
</script> -->
