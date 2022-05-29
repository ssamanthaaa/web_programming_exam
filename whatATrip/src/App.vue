<template>
  <div id="app">
    <Header v-if="!$route.meta.hideHeader" />
    <router-view />
    <!-- <Footer /> -->
  </div>
</template>
<script>
import Header from "@/components/Header.vue";
// import Footer from "@/components/Footer.vue";

export default {
  name: "Dashboard-component",
  components: {
    Header,
    // Footer
  },
};
</script>

<style lang="scss">
#app {
  // font-family: Avenir, Helvetica, Arial, sans-serif;
  font-family: "Parisienne";
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  // margin-top: 130px;
  height: 100%;
  width: 100%;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
// h1,
// h2,
// h3,
// h4 {
//   font-family: "Parisienne", cursive;
//   color: #155263;
//   font-weight: bold;
// }
</style>
