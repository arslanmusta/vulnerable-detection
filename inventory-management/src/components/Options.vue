<template>
    <v-card class="elevation-12">
        <v-toolbar>
        <v-toolbar-title>Ayarlar</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
        <form ref="form" @submit.prevent="update()">
            <v-text-field
                v-model="emails"
                name="emails"
                label="Mail Adresleri"
                type="text"
                placeholder="Mail Adresleri"
                required
            ></v-text-field>
            <v-btn type="submit" class="mt-4" color="primary" value="update">Güncelle</v-btn>
        </form>
    </v-card-text>
</v-card>
</template>

<script>

import endpoints from '@/endpoints';

export default {
    name: 'OptionsPage',
    data: () => ({
        emails: ''
    }),

    created () {
      this.initialize()
    },

    methods: {
        async initialize() {
            try {
                const response = await fetch(endpoints.EMAIL);
                if(response.status >= 200 & response.status < 300) {
                const result = await response.json();
                this.emails = result.join(',');
                }
            } catch (error) {
                console.log(error);
            }
        },
        async update() {
            try {
                const request = this.emails.split(",");

                if(request[0] !== '') {
                    await fetch(endpoints.EMAIL, {
                        method: 'PUT',
                        body: JSON.stringify(request),
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                }
            } catch (error) {
                console.log(error);
            }
        }
    }
}
</script>
