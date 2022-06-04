<template>
  <div class="container my-margin" id="app">
    <section>
      <h2 class="titles">Specification of the project and technologies used</h2>
      <!-- <p> -->
      The goal of this project was to realize a web application where a
      registered user can save information about his trips. The users will have
      to login into the platform and then he will be able to see all his trips
      and create more of them. Each trip has a date, a transportation type, a
      path, a small description of the trip that is not mandatory, and a list of
      stages that can also have a name and a small description. On the home
      page, the user can look at all his trips, but also he can filter them
      based on a single date or a range of dates. A trip can be modified or
      deleted at any moment. A new user can create an account and in a few
      seconds he will be able to create his first trip. The technologies used to
      realize this web application are:
      <ul>
        <li>Apache Tomcat</li>
        <li>Eclipse for the development of the bakend part</li>
        <li>Visual Studio Code for the development of the frontend part</li>
        <li>MySQL</li>
        <li>Axios</li>
        <li>Vue.js framework</li>
        <li>
          Leaflet map, in particular
          <a href="https://github.com/geoman-io/leaflet-geoman" target="_blank"
            >leaflet-geoman plugin</a
          >
        </li>
      </ul>
      <!-- To deliver the project docker has been used. -->
      <!-- </p> -->
    </section>
    <section>
      <h2 class="titles">Future updates ideas</h2>
      Some possible future updates ideas are:
      <ul>
        <li>add the possibility to share a trip with your trip buddy</li>
        <li>
          add other types of filters, for example the possibility to filter by
          the type of transpotation or on the trip's buddies that did the trip
          with the user
        </li>
        <li>
          add the possibility to add also a picture for the whole trip or one in
          the main stages
        </li>
        <li>
          add the geolocalization in order to save your trip instantanely as you
          are moving
        </li>
        <li>
          add the possibility to set starting point and arrival point and get
          the shortest path
        </li>
        <li>
          add the possibility to choose for each trip the marker/path color or a
          marker image
        </li>
        <li>create a mobile app</li>
      </ul>
    </section>
  </div>
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
}

.center-content {
  justify-content: center;
  padding: 10% 10%;
}

section {
  text-align: left;
  margin-top: 10px;
}
section h2 {
  color: #ff6f3c;
}
</style>
