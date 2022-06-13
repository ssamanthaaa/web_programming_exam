<template>
  <div>
    <Header v-if="!$route.meta.hideHeader" />
    <div class="content">
      <router-view />
    </div>
    <Footer v-if="!$route.meta.hideHeader" id="footer" />
  </div>
</template>
<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: "Dashboard-component",
  components: {
    Header,
    Footer,
  },
};
</script>

<style lang="scss">
html,
body {
  height: 100%;
}

#app {
  // font-family: "Parisienne";
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #155263;
  display: flex;
  flex-direction: column;
  height: 100%;
}

h1,
h2,
h3 {
  font-family: "Shadows Into Light", Avenir, Helvetica, Arial, sans-serif;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #155263;
    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

.content {
  flex: 1 0 auto;
  padding: 20px;
  width: 100%;
  display: flex;
  flex-direction: column;
  min-height: 90vh;
}

* {
  box-sizing: border-box;
}

#footer {
  bottom: 0;
  width: 100%;
  margin-top: auto;
}
</style>
