<template>
  <div>
    <nav
      class="navbar navbar-dark navbar-expand-lg fixed-top bg-white portfolio-navbar myNavbar"
    >
      <!-- gradient -->
      <div class="container">
        <router-link to="/welcome" class="navbar-brand logo"
          >What a trip</router-link
        >
        <!-- <a class="navbar-brand logo" href="#">What a trip</a> -->
        <button
          data-bs-toggle="collapse"
          class="navbar-toggler"
          data-bs-target="#navbarNav"
        >
          <span class="visually-hidden">Toggle navigation</span
          ><span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <!-- <a class="nav-link active" href="index.html">Home</a> -->
              <router-link to="dashboard" class="nav-link active"
                >Home</router-link
              >
            </li>
            <li class="nav-item">
              <router-link to="/about" class="nav-link active"
                >About</router-link
              >
            </li>
            <li class="nav-item">
              <span
                class="nav-link active"
                @click="logout()"
                style="cursor: pointer"
                >Logout</span
              >
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <b-modal v-model="showLoaderLogout" size="md">
      <template #modal-header="{ close }">
        <b-button size="sm" class="dark-color" @click="close()">X</b-button>
      </template>
      <template>
        <div class="row justify-content-center">
          <div class="loader" v-if="showLoaderLogout">
            <div class="ball"></div>
            <div class="ball"></div>
            <div class="ball"></div>
            <span>Thanks, bye!</span>
          </div>
        </div>
      </template>
      <template #modal-footer="{ ok }">
        <span style="padding-right: 40px"
          ><b-button class="orange-color" v-if="!showLoaderLogout" @click="ok()"
            >OK</b-button
          ></span
        >
      </template>
    </b-modal>
    <!-- <div class="row justify-content-center margin" v-if="showLoaderLogout">
      <div class="loader">
        <div class="ball"></div>
        <div class="ball"></div>
        <div class="ball"></div>
        <h3>Thanks, bye!</h3>
      </div>
    </div> -->
  </div>
</template>

<script>
import UserService from "@/services/UserService.js";

export default {
  name: "Header-component",
  props: {},
  data() {
    return {
      showLoaderLogout: false,
    };
  },
  methods: {
    logout: function () {
      this.showLoaderLogout = true;
      setTimeout(() => {
        UserService.logout();
        this.$router.push({
          path: "/login",
        });
      }, 2000);
    },
  },
};
</script>
<style scoped>
.margin {
  margin-top: 20%;
}
</style>
