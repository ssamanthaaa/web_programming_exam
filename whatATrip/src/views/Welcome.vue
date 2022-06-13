<template>
  <div class="container my-margin" id="app">
    <section>
      <div class="row justify-content-center">
        <h1 class="titles">
          Welcome, <span>{{ username }}</span
          >!
        </h1>
        <div class="container-md container-sm">
          <img src="@/assets/img/whatATrip256.png" alt="What a trip logo" />
        </div>
      </div>
      <div class="row justify-content-center" style="padding-top: 5%">
        <div class="col-sm">
          <router-link to="dashboard" class="router-link-style"
            ><button class="btn orange-color w-100">Your trips</button>
          </router-link>
        </div>
        <div class="col-sm">
          <router-link to="update-trip" class="router-link-style"
            ><button class="btn orange-color w-100">Add trip</button>
          </router-link>
        </div>
      </div>
      <div class="row justify-content-center">
        <div class="col-sm">
          <a
            href="https://youtu.be/kIeKdFZK0GE"
            target="_blank"
            class="router-link-style"
            ><button class="btn orange-color w-100">See Tutorial</button>
          </a>
        </div>
      </div>
    </section>

    <!--  MODAL sesssion expired -->
    <b-modal v-model="showExpiredError" size="md">
      <template #modal-header="{ close }">
        <b-button size="sm" class="dark-color" @click="close()">X</b-button>
      </template>
      <template>
        <h3 style="color: #ff6f3c; text-align: center; margin-top: 15px">
          Your session is expired.
        </h3>
        <p style="text-align: center">
          You will be redirected to the home page in a few seconds.
        </p>
        <div class="row justify-content-center">
          <div class="loader" v-if="showLoader">
            <div class="ball"></div>
            <div class="ball"></div>
            <div class="ball"></div>
            <span>Redirecting...</span>
          </div>
        </div>
      </template>
      <template #modal-footer="{ ok }">
        <span style="padding-right: 40px"
          ><b-button class="orange-color" @click="ok()">OK</b-button></span
        >
      </template>
    </b-modal>
  </div>
</template>

<script>
// import UserService from "@/services/UserService";
export default {
  name: "Welcome-component",
  components: {},
  data() {
    return {
      idUser: null,
      username: null,
      showExpiredError: false,
      showLoader: true,
    };
  },

  methods: {
    //double check if user is authenticate
    // getUsernameUser: async function () {
    //   let okResponse;
    //   let errorStatus = 0;
    //   await UserService.getUsername(this.token)
    //     .then((response) => {
    //       okResponse = response;
    //     })
    //     .catch(function (error) {
    //       errorStatus = error.response.status;
    //       console.log(error);
    //     });
    //   if (errorStatus != 0) {
    //     this.showExpiredError = true;
    //     setTimeout(() => {
    //       UserService.logout();
    //       this.redirectLogout();
    //     }, 2000);
    //   } else {
    //     if (okResponse.status == 200) {
    //       // console.log(okResponse);
    //     }
    //   }
    // },

    redirectLogout: function () {
      this.$router.push({
        path: "/login",
      });
    },
  },

  mounted: async function () {
    // await this.getUsernameUser();
  },

  beforeMount: async function () {
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
  },
};
</script>

<style scoped>
h1 {
  font-size: 3.5rem;
}

span {
  text-transform: capitalize;
}

div.col-sm {
  margin-top: 10px;
}
.my-logo {
  max-width: 50%;
}

.contaier-sm {
  padding-top: 5px;
  padding-bottom: 5px;
}
</style>
