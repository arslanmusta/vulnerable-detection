<script>
  import endpoints from '@/endpoints';
  export default {

    data: () => ({
      dialog: false,
      dialogDelete: false,
      headers: [
        {
          text: 'CVE ID',
          align: 'start',
          sortable: false,
          value: 'cveId',
        },
        { text: 'Envanter', value: 'inventoryId' },
        { text: 'Envanter Elemanı', value: 'inventoryItemId' },
        { text: 'İşlemler', value: 'actions', sortable: false },
      ],
      vulnerableList: [],
      editedIndex: -1,
      editedItem: {
        vendor: '',
        product: '',
        version: '',
      },
    }),


    watch: {
      dialog (val) {
        val || this.close()
      },
      dialogDelete (val) {
        val || this.closeDelete()
      },
    },

    created () {
      this.initialize();
    },

    methods: {
      async initialize () {
        try {
          const response = await fetch(`${endpoints.VULNERABLE}`);
          if (response.status >= 200 & response.status < 300) {
            const result = await response.json();
            this.vulnerableList = result['_embedded']['vulnerableList'].filter(v => v.passive === false);
          }
        } catch (error) {
          console.log(error);
        }
      },

      deleteItem (item) {
        this.editedIndex = this.vulnerableList.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      async deleteItemConfirm () {
        try {
          const response = await fetch(`${endpoints.VULNERABLE}${this.editedItem.id}/passive`, {
            method: 'DELETE',
          });
          if (response.status >= 200 & response.status < 300) {
            console.log("Deleted " + this.editedItem);
            this.vulnerableList.splice(this.editedIndex, 1)
          }
        } catch (error) {
          console.log(error);
        }
        this.closeDelete()
      },

      close () {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      closeDelete () {
        this.dialogDelete = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      }
    },
  }
</script>

<template>
  <v-data-table
    :headers="headers"
    :items="vulnerableList"
    sort-by="cveId"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
        flat
      >
        <v-toolbar-title>Zafiyet Raporları</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog
          v-model="dialog"
          max-width="500px"
        >
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Zafiyat kaydını pasif duruma getirme işlemini onaylıyor musunuz?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Hayır</v-btn>
              <v-btn color="red darken-1" text @click="deleteItemConfirm">Evet</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:[`item.actions`]="{ item }">
      <v-icon
        v-if="$user.role === 'Admin'"
        small
        @click="deleteItem(item)"
      >
        mdi-delete
      </v-icon>
    </template>
    <template v-slot:no-data>
      <v-btn
        color="primary"
        @click="initialize"
      >
        Sıfırla
      </v-btn>
    </template>
  </v-data-table>
</template>
