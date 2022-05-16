<template>
  <div class="container">
    <Header />
    <main>
      <div class="row">
        <div class="col"><Map /></div>
        <!-- <div class="col"><ModificableMap/></div> -->
      </div>
      <div class="row">
        <div class="col-2">
          <router-link to="modifica-viaggio" class="btn gradient d-block w-100"
            >Aggiungi viaggio</router-link
          >
        </div>
      </div>
      <div class="row">
        <div class="heading">
          <h2>I miei viaggi</h2>
        </div>
        <table class="table">
          <thead>
            <tr>
              <th></th>
              <th>Data</th>
              <th>Luogo</th>
              <th>Mezzo</th>
              <th>Percorso</th>
              <th>Tappe Principali</th>
            </tr>
          </thead>
          <tbody>
            <tr
              class="colored"
              v-for="value in tripsList"
              v-bind:key="value.ID"
            >
              <td>
                <router-link :to="'modifica-viaggio?id=' + value.ID"
                  ><b-icon icon="pencil"></b-icon
                ></router-link>
              </td>
              <!-- <button class="btn"></button></td> -->
              <td>{{ getDate(value.DATA) }}</td>
              <td>{{ value.LUOGO }}</td>
              <td>{{ value.MEZZO }}</td>
              <td>
                <ul style="list-style-type: none">
                  <li>
                    PARTENZA: Latitudine: {{ value.PERCORSO.PARTENZA_LAT }},
                    Longitudine: {{ value.PERCORSO.PARTENZA_LNG }} ARRIVO:
                    Latitudine: {{ value.PERCORSO.ARRIVO_LAT }}, Longitudine:
                    {{ value.PERCORSO.ARRIVO_LNG }}
                  </li>
                </ul>
              </td>
              <td>
                <ul style="list-style-type: none">
                  <li
                    v-for="(val, index) in value.TAPPE_PRINCIPALI"
                    v-bind:key="index"
                  >
                    {{ val.LUOGO_TAPPA }}
                  </li>
                </ul>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
    <!-- <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <Footer />
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import { mapGetters, mapActions } from "vuex";
import moment from "moment";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import Map from "@/components/Map.vue";
// import ModificableMap from '@/components/ModificableMap.vue';

export default {
  name: "Dashboard-component",
  components: {
    // HelloWorld,
    Header,
    Footer,
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
    };
  },
  methods: {
    ...mapActions(["getTrips"]),

    getDate(datetime) {
      return moment(String(datetime)).format("DD/MM/YYYY");
    },
  },

  computed: mapGetters(["tripsList"]),
  created() {
    this.getTrips();
  },

  // mounted: async function() {
  //   await this.loadPage();
  // }
};
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.colored:hover {
  background-color: #c2dff3;
}
</style>
