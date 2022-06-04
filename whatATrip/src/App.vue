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
  // font-family: "Dancing Script", cursive;
  // font-family: "Shadows Into Light";
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #155263;
  // margin-top: 130px;
  display: flex;
  flex-direction: column;
  height: 100%;
  // width: 100%;
  // height: 100%;
}

h1,
h2,
h3 {
  font-family: "Shadows Into Light";
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
  /* Prevent Chrome, Opera, and Safari from letting these items shrink to smaller than their content's default minimum size. */
  padding: 20px;
  width: 100%;
  // height: 1000vh;
  // position: relative;
  display: flex;
  flex-direction: column;
  min-height: 90vh;
}

// .footer {
//   flex-shrink: 0;
//   /* Prevent Chrome, Opera, and Safari from letting these items shrink to smaller than their content's default minimum size. */
//   padding: 20px;
// }

* {
  box-sizing: border-box;
}
#footer {
  bottom: 0;
  // position: fixed;
  width: 100%;
  // margin-top: 20px;
  margin-top: auto;
}
</style>
