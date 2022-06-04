<template>
  <div id="app">
    <div class="container my-margin">
      <section>
        <h1 class="titles">Admin dashboard</h1>
        <h3>List of users</h3>
        <div class="table-responsive">
          <table class="table table-responsive-sm table-responsive-md">
            <thead>
              <tr>
                <th>Username</th>
                <th>Email</th>
              </tr>
            </thead>
            <tbody>
              <tr
                class="colored"
                v-for="value in userList"
                v-bind:key="value.ID"
              >
                <td>
                  {{ value.USERNAME }}
                </td>
                <td>{{ value.EMAIL }}</td>
              </tr>
            </tbody>
          </table>
        </div>
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
      </section>
    </div>
  </div>
</template>

<script>
import UserService from "@/services/UserService";

export default {
  name: "Dashboard-component",
  components: {},
  data() {
    return {
      idUser: null,
      username: null,
      userList: null,
      showExpiredError: false,
      showLoader: true,
    };
  },

  // mounted: function () {},

  mounted: async function () {
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
    await this.getUsers();
  },

  methods: {
    getUsers: async function () {
      let errorStatus = 0;
      let status;
      await UserService.getAllUsers(this.token)
        .then((response) => {
          console.log(response);
          this.userList = response.data;
          status = response.status;
          //   console.log(this.userList);
          // console.log(status);
        })
        .catch(function (error) {
          errorStatus = error.response.status;
          console.log(error);
        });
      // console.log(`errorStatus: ${errorStatus}`);
      if (errorStatus != 0) {
        // this.showExpiredError = true;
        // setTimeout(() => {
        //   UserService.logout();
        //   this.redirectLogout();
        // }, 2000);
      } else {
        if (status === 200) {
          console.log(this.userList);
        }
      }
      this.pathJsonKey += 1;
      this.dataLoaded = true;
    },

    redirectLogout: function () {
      this.$router.push({
        path: "/login",
      });
    },
  },
};
</script>

<style scoped>
.colored:hover {
  background-color: #ffc83c4b;
}

h1,
h4 {
  color: #155263;
}
</style>
