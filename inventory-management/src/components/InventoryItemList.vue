<script>
  import endpoints from '@/endpoints';
  export default {
    props: ['id'],

    data: () => ({
      dialog: false,
      dialogDelete: false,
      headers: [
        { text: 'Id', value: 'id'},
        {
          text: 'Üretici',
          align: 'start',
          sortable: false,
          value: 'vendor',
        },
        { text: 'Ürün', value: 'product' },
        { text: 'Versiyon', value: 'version' },
        { text: 'İşlemler', value: 'actions', sortable: false },
      ],
      inventory: {},
      vendors: [],
      rawProducts: [],
      products: [],
      versions: [],
      editedIndex: -1,
      editedItem: {
        vendor: '',
        product: '',
        version: '',
      },
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Yeni Envanter Elemanı' : 'Envanter Elemanı Düzenle'
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
      console.log(this);
      this.initialize();
    },

    methods: {
      async initialize () {
        try {
          const response = await fetch(`${endpoints.INVENTORY}${this.id}`);
          if (response.status >= 200 & response.status < 300) {
            const result = await response.json();
            this.inventory = result;
          }

          const vendorResponse = await fetch(`${endpoints.VENDOR}`);
          if (vendorResponse.status >= 200 & vendorResponse.status < 300) {
            const result = await vendorResponse.json();
            this.vendors = result;
          }
        } catch (error) {
          console.log(error);
        }
      },

      async editItem (item) {
        this.editedIndex = this.inventory.inventoryItems.indexOf(item)
        this.editedItem = Object.assign({}, item)

        const response = await fetch(`${endpoints.VENDOR}/${this.editedItem.vendor}`);
        if(response.status >= 200 && response.status < 300) {
          const result = await response.json();
          this.products = result['_embedded']['cpeItemList'];
        }

        const product = this.products.find(p => p.id.product === this.editedItem.product);
        this.versions = product.versions;
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.inventory.inventoryItems.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      async deleteItemConfirm () {
        try {
          const response = await fetch(`${endpoints.INVENTORY}${this.id}/item/${this.editedItem.id}`, {
            method: 'DELETE'
          });
          if (response.status >= 200 & response.status < 300) {
            console.log("Deleted " + this.editedItem);
            this.inventory.inventoryItems.splice(this.editedIndex, 1)
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
              vendor: this.editedItem.vendor,
              product: this.editedItem.product,
              version: this.editedItem.version
            };

            const response = await fetch(`${endpoints.INVENTORY}${this.id}/item/${this.editedItem.id}`, {
              method: 'PUT',
              body: JSON.stringify(request),
              headers: {
                'Content-Type': 'application/json'
              }
            });
            if (response.status >= 200 & response.status < 300) {
              const result = await response.json()
              console.log("Changed " + this.editedItem);
              Object.assign(this.inventory.inventoryItems[this.editedIndex], result);
            }
          } catch (error) {
            console.log(error);
          }
        } else {
          try {
            const request = {
              vendor: this.editedItem.vendor,
              product: this.editedItem.product,
              version: this.editedItem.version
            };

            const response = await fetch(`${endpoints.INVENTORY}${this.id}/item`, {
              method: 'POST',
              body: JSON.stringify(request),
              headers: {
                'Content-Type': 'application/json'
              }
            });
            if (response.status >= 200 & response.status < 300) {
              const result = await response.json();
              console.log("Added " + result);
              this.inventory.inventoryItems.push(result);
            }

          } catch (error) {
            console.log(error);
          }
          
        }
        this.close()
      },

      async getProducts() {
        console.log(this.editedItem.vendor);
        this.editedItem.product = '';
        this.editedItem.version = '';
        this.products = [];

        const response = await fetch(`${endpoints.VENDOR}/${this.editedItem.vendor}`);
        if(response.status >= 200 && response.status < 300) {
          const result = await response.json();
          this.products = result['_embedded']['cpeItemList'];
        }
      },

      setProduct() {
        const product = this.products.find(p => p.id.product === this.editedItem.product);
        this.editedItem.version = '';
        this.versions = product.versions;
      }
    },
  }
</script>

<template>
  <v-data-table
    :headers="headers"
    :items="inventory.inventoryItems"
    sort-by="ip"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
        flat
      >
        <v-toolbar-title>Envanter Elemanları Listesi</v-toolbar-title>
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
                    <v-autocomplete
                      label="Üretici"
                      v-model="editedItem.vendor"
                      :items="vendors"
                      @change="getProducts"
                    ></v-autocomplete>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-autocomplete
                      label="Ürün"
                      v-model="editedItem.product"
                      :items="products"
                      item-text="id.product"
                      item-value="id.product"
                      @change="setProduct"
                    ></v-autocomplete>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-autocomplete
                      label="Versiyon"
                      v-model="editedItem.version"
                      :items="versions"
                    ></v-autocomplete>
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
                :disabled="!editedItem.vendor || !editedItem.product || !editedItem.version"
              >
                Kaydet
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Envanter eleman kaydını silme işlemini onaylıyor musunuz?</v-card-title>
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
