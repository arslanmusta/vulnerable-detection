<template>
   <v-app >
      <v-main>
         <v-container fluid fill-height>
            <v-layout align-center justify-center>
               <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                     <v-toolbar dark color="primary">
                        <v-toolbar-title>Giriş</v-toolbar-title>
                     </v-toolbar>
                     <v-card-text>
                     <form ref="form" @submit.prevent="login()">
                            <v-text-field
                              v-model="username"
                              name="username"
                              label="Kullanıcı adı"
                              type="text"
                              placeholder="Kullanıcı adı"
                              required
                           ></v-text-field>
                           
                            <v-text-field
                              v-model="password"
                              name="password"
                              label="Şifre"
                              type="password"
                              placeholder="Şifre"
                              required
                           ></v-text-field>
                           <v-btn type="submit" class="mt-4" color="primary" value="log in">Giriş</v-btn>
                      </form>
                     </v-card-text>
                  </v-card>
                
               </v-flex>
            </v-layout>
         </v-container>
      </v-main>
   </v-app>
</template>

<script>

import endpoints from '@/endpoints';
import Vue from 'vue';

export default {
  name: "LoginPage",
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    async login() {
      const request = {
        username: this.username,
        password: this.password
      }

      try {
        const response = await fetch(`${endpoints.LOGIN}`, {
          method: 'POST',
          body: JSON.stringify(request),
          headers: {
            'Content-Type': 'application/json'
          }
        });
        if (response.status >= 200 & response.status < 300) {
          const result = await response.json();
          localStorage.setItem('token', result.token);
          const parsedToken = parseJwt(result.token);
          console.log(parsedToken);
          Vue.prototype.$user = {
            username: this.username,
            role: parsedToken.role,
          }
          this.$router.push({ path: "/"});
        }
      } catch (error) {
        console.log(error);
      }
    },
  },
};

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}
</script>
