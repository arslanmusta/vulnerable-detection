<template>
  <AppLayout v-if="!$route.path.includes('login')"></AppLayout>
  <Login v-else></Login>
</template>

<script>
import AppLayout from './components/AppLayout.vue';
import Login from './components/Login.vue';

export default {
  name: 'App',

  components: {
    AppLayout,
    Login
  },

  data: () => ({
    //
  })
}
</script>

<style >
</style>
