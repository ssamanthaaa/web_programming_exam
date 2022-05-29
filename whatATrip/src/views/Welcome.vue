<template>
  <div id="app" class="container">
    <div class="center">
      <!-- <div > -->
      <div class="row justify-content-center">
        <h1 class="titles">
          Welcome, <span>{{ username }}</span
          >!
        </h1>
        <img
          src="@/assets/img/whatATrip.png"
          class="img-fluid"
          alt="What a trip logo"
          style="width: 35%"
        />
      </div>
      <div class="row">
        <div class="col-6">
          <router-link to="dashboard" class="router-link-style"
            ><button class="btn orange-color btn-lg w-100">Your trips</button>
          </router-link>
        </div>
        <div class="col-6">
          <router-link to="update-trip" class="router-link-style"
            ><button class="btn orange-color btn-lg w-100">Add trip</button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Welcome-component",
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
h1 {
  font-size: 4.5rem;
}

span {
  text-transform: capitalize;
}
</style>
