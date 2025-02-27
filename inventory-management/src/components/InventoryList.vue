<script>
  import endpoints from '@/endpoints';
  export default {
    data: () => ({
      dialog: false,
      dialogDelete: false,
      headers: [
        { text: 'Id', value: 'id'},
        {
          text: 'Ip',
          align: 'start',
          sortable: false,
          value: 'ip',
        },
        { text: 'Domain', value: 'domain' },
        { text: 'İşlemler', value: 'actions', sortable: false },
      ],
      inventories: [],
      editedIndex: -1,
      editedItem: {
        domain: '',
        ip: ''
      }
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Yeni Envanter' : 'Envanter Düzenle'
      },
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
      dialogDelete (val) {
        val || this.closeDelete()
      },
    },

    created () {
      this.initialize()
    },

    methods: {
      async initialize () {
        console.log(this.$user)
        try {
          const response = await fetch(endpoints.INVENTORY);
          if(response.status >= 200 & response.status < 300) {
            const result = await response.json();
            this.inventories = result['_embedded']['inventoryList'];
          }
        } catch (error) {
          console.log(error);
        }
      },

      editItem (item) {
        this.editedIndex = this.inventories.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.inventories.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      async deleteItemConfirm () {
        try {
          const response = await fetch(`${endpoints.INVENTORY}${this.editedItem.id}`, {
            method: 'DELETE'
          });
          if (response.status >= 200 & response.status < 300) {
            console.log("Deleted " + this.editedItem);
            this.inventories.splice(this.editedIndex, 1)
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
      },

      async save () {
        if (this.editedIndex > -1) {
          try {
            const request = {
              ip: this.editedItem.ip,
              domain: this.editedItem.domain
            };

            const response = await fetch(`${endpoints.INVENTORY}${this.editedItem.id}`, {
              method: 'PUT',
              body: JSON.stringify(request),
              headers: {
                'Content-Type': 'application/json'
              }
            });
            if (response.status >= 200 & response.status < 300) {
              const result = await response.json()
              console.log("Changed " + this.editedItem);
              Object.assign(this.inventories[this.editedIndex], result);
            }
          } catch (error) {
            console.log(error);
          }
        } else {
          try {
            const request = {
              ip: this.editedItem.ip,
              domain: this.editedItem.domain
            };

            const response = await fetch(`${endpoints.INVENTORY}`, {
              method: 'POST',
              body: JSON.stringify(request),
              headers: {
                'Content-Type': 'application/json'
              }
            });
            if (response.status >= 200 & response.status < 300) {
              const result = await response.json();
              console.log("Added " + result);
              this.inventories.push(result);
            }

          } catch (error) {
            console.log(error);
          }
          
        }
        this.close()
      },
    },
  }
</script>

<template>
  <v-data-table
    :headers="headers"
    :items="inventories"
    sort-by="ip"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
        flat
      >
        <v-toolbar-title>Envanter Listesi</v-toolbar-title>
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
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-if="$user.role === 'Admin'"
              color="primary"
              dark
              class="mb-2"
              v-bind="attrs"
              v-on="on"
            >
              Yeni Kayıt
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-text-field
                      v-model="editedItem.ip"
                      label="Ip"
                      required
                    ></v-text-field>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-text-field
                      v-model="editedItem.domain"
                      label="Domain"
                      required
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue darken-1"
                text
                @click="close"
              >
                İptal
              </v-btn>
              <v-btn
                color="blue darken-1"
                text
                @click="save"
                :disabled="!editedItem.domain || !editedItem.ip"
              >
                Kaydet
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Envanter kaydını silme işlemini onaylıyor musunuz?</v-card-title>
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
        class="mr-2"
        @click="editItem(item)"
      >
        mdi-pencil
      </v-icon>
      <v-icon
        v-if="$user.role === 'Admin'"
        small
        class="mr-2"
        @click="deleteItem(item)"
      >
        mdi-delete
      </v-icon>
      <v-icon :to="'/inventory/' + item.id"
        small
        class="mr-2"
        @click="$router.push('/inventory/' + item.id)"
      >
        mdi-alert
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
