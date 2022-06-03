<template>
  <div class="container my-margin" id="app">
    <div class="grid">
      <div>
        <img src="@/assets/img/vuejs.png" class="img-vue" alt="vue.js logo" />
      </div>
      <div>
        <img
          src="@/assets/img/eclipse.png"
          class="img-eclipse"
          alt="Eclipse logo"
        />
      </div>
      <div>
        <img
          src="@/assets/img/tomcatLogo.png"
          class="img-tomcat"
          alt="tomcat logo"
        />
      </div>
      <div>
        <img
          src="@/assets/img/mysqlLogo.png"
          class="img-mysql"
          alt="mySQL logo"
        />
      </div>
      <div>
        <img src="@/assets/img/js.png" class="img-js" alt="Javascript logo" />
      </div>
      <div>
        <img
          src="@/assets/img/vscodeLogo.png"
          class="img-vscode"
          alt="vs code logo"
        />
      </div>
    </div>
  </div>
  <!-- <div class="center">
    <h1 class="titles">Technologies used</h1>
    <h1 class="titles">Future improvements</h1>
  </div> -->
</template>

<script>
export default {
  name: "About-component",
  components: {},
  data() {
    return {
      idUser: null,
      username: null,
    };
  },

  mounted: function () {},

  beforeMount: async function () {
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
  },

  methods: {},
};
</script>

<style scoped>
.center {
  margin: 0;
  position: absolute;
  top: 20%;
  left: 50%;
  -ms-transform: translate(-20%, -20%);
  transform: translate(-50%, -50%);
  /* background: linear-gradient(to right, #155263 0%, #ff6f3c 70%, #ffc93c 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent; */
}

.center-content {
  justify-content: center;
  padding: 10% 10%;
}
.grid {
  display: grid;
  justify-content: center;
  grid-template-columns: repeat(auto-fill, minmax(8rem, 1fr));
  grid-auto-rows: 1fr;
}

.grid::before {
  content: "";
  width: 0;
  padding-bottom: 100%;
  grid-row: 1 / 1;
  grid-column: 1 / 1;
}

.grid > *:first-child {
  grid-row: 1 / 1;
  grid-column: 1 / 1;
}

/* Just to make the grid visible */

.grid > * {
  /* background: rgba(0, 0, 0, 0.1); */
  border: 2px white solid;
}
img {
  max-width: 100%;
}
</style>
