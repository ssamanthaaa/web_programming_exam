<template>
  <div class="myMargin" id="app">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col">
          <img
            src="@/assets/img/logo.png"
            alt="vue.js logo"
            style="width: 40%"
          />
        </div>
        <div class="col">
          <img
            src="@/assets/img/eclipse.png"
            alt="Eclipse logo"
            style="width: 53%"
          />
        </div>
        <div class="col">
          <img
            src="@/assets/img/tomcatLogo.png"
            alt="tomcat logo"
            style="width: 40%"
          />
        </div>
        <div class="col">
          <img
            src="@/assets/img/mysqlLogo.png"
            alt="mySQL logo"
            style="width: 60%"
          />
        </div>
        <div class="col">
          <img
            src="@/assets/img/js.png"
            alt="Javascript logo"
            style="width: 40%"
          />
        </div>
        <div class="col">
          <img
            src="@/assets/img/vsCodeLogo.png"
            alt="vs code logo"
            style="width: 40%"
          />
        </div>
      </div>
      <div class="row justify-content-center"></div>
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
</style>
