<template>
  <v-app>
    <v-app-bar app color="grey-lighten-2"></v-app-bar>
    <v-navigation-drawer app color="grey-darken-2" permanent>
      <v-list>
        <!-- <v-list-item prepend-icon="mdi-email" title="Home" value="home"></v-list-item> -->
        <v-list-item :to="'/inventory'">
            <v-list-item-icon>
                <v-icon>mdi-laptop</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
                <v-list-item-title>
                    Envanter Yönetimi
                </v-list-item-title>
            </v-list-item-content>
        </v-list-item>
        <v-list-item :to="'/vulnerable'">
            <v-list-item-icon>
                <v-icon>mdi-alert</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
                <v-list-item-title>
                    Zafiyet Raporları
                </v-list-item-title>
            </v-list-item-content>
        </v-list-item>
        <v-list-item v-if="$user.role === 'Admin'" :to="'/options'">
            <v-list-item-icon>
                <v-icon>mdi-cog</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
                <v-list-item-title>
                    Ayarlar
                </v-list-item-title>
            </v-list-item-content>
        </v-list-item>
        <v-list-item :to="'#'" @click="logout()">
            <v-list-item-icon>
                <v-icon>mdi-logout</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
                <v-list-item-title>
                    Çıkış Yap
                </v-list-item-title>
            </v-list-item-content>
        </v-list-item>
      </v-list>
      
    </v-navigation-drawer>
    <v-main>
      <v-card height="200px">
        <router-view></router-view>
      </v-card>
    </v-main>
  </v-app>
</template>

<script>
import Vue from 'vue';
export default {
  name: 'App',

  components: {
    // InventoryList
},

  data: () => ({
    //
  }),
  created() {
    var user = localStorage['token'];
    
    if (user === undefined) {
      this.$router.push({ path: '/login'});
    } else {
      Vue.prototype.$user = parseJwt(user);
    }
    
  },
  methods: {
    logout() {
      this.$user = {};
      localStorage.removeItem('token');
      this.$router.push({ path: '/login'});
    }
  }
  
}

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}
</script>

<style >
</style>
