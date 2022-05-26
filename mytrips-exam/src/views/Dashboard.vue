<template>
  <div>
    <card>
      <div class="container">
        <h2>My trips</h2>
        <div class="row">
          <div class="col">
            <button class="btn gradient d-block">
              <router-link
                to="modifica-viaggio"
                style="color: white; text-decoration: none"
                >Add trip</router-link
              >
            </button>
          </div>
        </div>
        <!-- <h3>Hi {{ username }}!</h3> -->
        <div class="row">
          <div class="col map">
            <Map v-if="dataLoaded" :pathJson="pathJson" />
          </div>
          <!-- <div class="col"><ModificableMap/></div> -->
        </div>

        <div class="row">
          <!-- <div class="heading"> -->
          <!-- </div> -->
          <table class="table">
            <thead>
              <tr>
                <th></th>
                <th>Date</th>
                <th>Place</th>
                <th>Transportation</th>
                <th>Main Stages</th>
              </tr>
            </thead>
            <tbody>
              <tr
                class="colored"
                v-for="value in tripList"
                v-bind:key="value.ID"
              >
                <td>
                  <router-link :to="'modifica-viaggio?idTrip=' + value.ID"
                    ><b-icon icon="pencil"></b-icon
                  ></router-link>
                </td>
                <!-- <button class="btn"></button></td> -->
                <td>{{ getDate(value.DATE) }}</td>
                <td>{{ value.PLACE }}</td>
                <td>{{ value.TRANSPORTATION }}</td>
                <td>
                  <ul style="list-style-type: none">
                    <li
                      v-for="(val, index) in value.MAIN_STAGES"
                      v-bind:key="index"
                    >
                      {{ val.NAME_STAGE }}
                    </li>
                  </ul>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </card>
    <!-- <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <!-- <Footer /> -->
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
// import { mapGetters, mapActions } from "vuex";
import moment from "moment";
// import Header from "@/components/Header.vue";
// import Footer from "@/components/Footer.vue";
import Map from "@/components/Map.vue";
import TripService from "@/services/TripService.js";
// import ModificableMap from '@/components/ModificableMap.vue';

export default {
  name: "Dashboard-component",
  components: {
    // HelloWorld,
    // Header,
    // Footer,
    Map,
    // ModificableMap
  },
  data() {
    return {
      // myTrips: [
      //   { ID: 1, LUOGO: '1', DATA: '12/12/2020', MEZZO: 'auto', PERCORSO: [{ ID_PERCORSO: 1, LATITUDINE: '00', LONGITUDINE: '01'},{ ID_PERCORSO: 2, LATITUDINE: '02', LONGITUDINE: '02'}], TAPPE_PRINCIPALI: ''},
      //   { ID: 2, LUOGO: '2', DATA: '14/12/2020', MEZZO: 'moto', PERCORSO: [{ ID_PERCORSO: 1, LATITUDINE: '10', LONGITUDINE: '11'}, { ID_PERCORSO: 2, LATITUDINE: '10', LONGITUDINE: '11'}, { ID_PERCORSO: 3, LATITUDINE: '10', LONGITUDINE: '11'}], TAPPE_PRINCIPALI: ''},
      //   { ID: 3, LUOGO: '3', DATA: '15/12/2020', MEZZO: 'bus', PERCORSO: [{ ID_PERCORSO: 1, LATITUDINE: '20', LONGITUDINE: '21'}], TAPPE_PRINCIPALI: ''},
      // ],
      // trips: [],
      idUser: null,
      username: null,
      tripList: null,
      pathJson: [],
      dataLoaded: false,
    };
  },

  mounted: function () {},

  beforeMount: async function () {
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
    await this.getTrips();
  },

  methods: {
    // ...mapActions(["getTrips"]),

    getDate(datetime) {
      return moment(String(datetime)).format("DD/MM/YYYY");
    },

    getTrips: async function () {
      await TripService.getAllTrips(this.idUser, this.token)
        .then((response) => {
          this.tripList = response.data;
        })
        .catch(function (error) {
          console.log(error);
          if (error == 401) {
            console.log(error.message);
            localStorage.removeItem("token");
            localStorage.removeItem("id");
            localStorage.removeItem("username");
            localStorage.removeItem("email");
            this.$router.push({
              path: "/",
            });
          }
        });
      console.log(this.tripList);
      if (this.tripList != null) {
        for (let i = 0; i < this.tripList.length; ++i) {
          if (this.tripList[i].GEOJSON != null) {
            this.tripList[i].GEOJSON = JSON.parse(this.tripList[i].GEOJSON);
            console.log(this.tripList[i].GEOJSON);
            console.log("parse json:");
            this.pathJson.push(this.tripList[i].GEOJSON);
          }
        }
      }
      console.log("stampo pathJson");
      console.log(this.pathJson);
      this.dataLoaded = true;
    },
  },

  // computed: mapGetters(["tripsList"]),
  // created() {
  //   this.getTrips();
  // },
};
</script>

<style scoped>
.colored:hover {
  background-color: #c2dff3;
}

.map {
  margin-top: 10px;
  margin-bottom: 20px;
}
</style>
