<template>
  <div class="container">
    <Header />
    <main>
      <section>
        <div class="container myContainer">
          <div class="heading" v-if="id != null">
            <h2>Modifica il tuo viaggio del {{ getDateDMY(data) }}</h2>
          </div>
          <div class="heading" v-else>
            <h2>Aggiungi viaggio</h2>
          </div>
          <!-- <div>
                    <modificable-map></modificable-map>
                </div> -->
          <div class="container">
            <div class="row justify-content-center">
              <l-map
                style="height: 400px; width: 100%"
                :zoom="zoom"
                :center="center"
              >
                <!-- :bounds="bounds"  -->
                <l-tile-layer
                  :url="url"
                  :attribution="attribution"
                ></l-tile-layer
                ><!-- (->scritta in basso) questo carica la mappa -->
                <!-- <l-control-zoom position="bottomright"  ></l-control-zoom> -->
                <!-- <l-marker :lat-lng="markerLatLng"></l-marker> -->
                <l-marker
                  v-for="(marker, index) in markers"
                  :key="index"
                  :draggable="marker.draggable"
                  :lat-lng.sync="marker.POSIZIONE"
                  :icon="marker.icon"
                  @click="alert(marker.tooltip)"
                  ><!-- :visible="marker.visible"-->
                  <!-- <l-popup :content="marker.tooltip" />
                            <l-tooltip :content="marker.tooltip" /> -->
                </l-marker>
                <l-polyline :lat-lngs="polylines"></l-polyline
                ><!--  v-if="showPolyline" -->
              </l-map>
            </div>
            <hr />
            <input
              type="button"
              class="btn gradient w-50"
              value="AGGIORNA PERCORSO"
              @click="createPath()"
            />
          </div>
          <form @submit.prevent>
            <div class="mb-3">
              <label class="form-label" for="data">Data</label
              ><input
                class="form-control"
                type="date"
                v-model="data"
                id="data"
              />
            </div>
            <div class="mb-3">
              <label class="form-label" for="luogo">Luogo</label
              ><input
                class="form-control"
                type="text"
                v-model="luogo"
                id="luogo"
              />
            </div>
            <div class="mb-3">
              <label class="form-label" for="mezzo">Mezzo</label>
              <select class="form-select" id="mezzo" v-model="mezzo">
                <optgroup label="Scegli un mezzo di trasporto">
                  <option value="Aereo">Aereo</option>
                  <option value="Auto">Auto</option>
                  <option value="Autobus">Autobus</option>
                  <option value="A piedi">A piedi</option>
                  <option value="Bici">In bici</option>
                  <option value="Moto">Moto</option>
                  <option value="Treno">Treno</option>
                </optgroup>
              </select>
            </div>
            <div class="mb-3">
              <!-- <div class="row">
                            <h5>Percorso</h5>
                            <table class="table" v-if="percorso.length > 0">
                                <thead>
                                    <tr>
                                        <th>Latitudine</th>
                                        <th>Longitudine</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr class="colored" v-for="(value, index) in percorso" v-bind:key="index">
                                    <td>{{value.LATITUDINE}}</td>
                                    <td>{{value.LONGITUDINE}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div> -->
              <div class="row">
                <h5>Punto di partenza</h5>
                <div class="col">
                  <label class="form-label" for="lat">Latitudine</label
                  ><input
                    class="form-control"
                    id="lat"
                    type="text"
                    v-model="partenza_latitudine"
                  />
                </div>
                <div class="col">
                  <label class="form-label" for="lat">Longitudine</label
                  ><input
                    class="form-control"
                    id="lat"
                    type="text"
                    v-model="partenza_longitudine"
                  />
                </div>
              </div>
            </div>
            <div class="mb-3">
              <div class="row">
                <h5>Punto di arrivo</h5>
                <div class="col">
                  <label class="form-label" for="lat">Latitudine</label
                  ><input
                    class="form-control"
                    id="lat"
                    type="text"
                    v-model="arrivo_latitudine"
                  />
                </div>
                <div class="col">
                  <label class="form-label" for="lat">Longitudine</label
                  ><input
                    class="form-control"
                    id="lat"
                    type="text"
                    v-model="arrivo_longitudine"
                  />
                </div>
              </div>
            </div>
          </form>
          <!-- <div> -->
          <!-- <div class="mb-3"> -->
          <h5>Tappe Principali</h5>
          <div v-if="tappe.length > 0">
            <p style="font-size: 13px">
              (Trascinare le righe della tabella per ordinare il percorso)
            </p>
            <table class="table">
              <thead>
                <tr>
                  <th></th>
                  <th>Luogo tappa</th>
                  <th>Latitudine</th>
                  <th>Longitudine</th>
                  <th>Modifica</th>
                  <th>Rimuovi</th>
                </tr>
              </thead>
              <!-- <tbody> -->
              <draggable
                tag="tbody"
                style="cursor: move"
                v-model="tappe"
                @change="updateValues()"
                ><!--   -->
                <tr
                  class="colored"
                  v-for="(value, index) in tappe"
                  v-bind:key="index"
                >
                  <td><b-icon icon="arrows-move"></b-icon></td>
                  <td>{{ value.LUOGO_TAPPA }}</td>
                  <td>{{ value.POSIZIONE.lat }}</td>
                  <td>{{ value.POSIZIONE.lng }}</td>
                  <td>
                    <!-- <button class="btn gradient" @click="updateMarker(index)"><b-icon icon="pencil"></b-icon></button> -->
                    <button class="btn gradient" v-on:click="getTappa(index)">
                      <b-icon icon="pencil"></b-icon>
                    </button>
                  </td>
                  <td>
                    <button class="btn gradient" @click="removeMarker(index)">
                      X
                    </button>
                  </td>
                </tr>
              </draggable>
              <!-- </tbody> -->
            </table>
            <!-- <button class="btn gradient d-block w-45" type="button" style="float:right" v-on:click="addMarker()">Aggiungi tappa</button> -->
          </div>
          <!-- </div> -->
          <div class="mb-3">
            <div class="row">
              <div class="col">
                <button
                  class="btn gradient d-block w-45"
                  type="button"
                  style="float: right"
                  v-on:click="addMarker()"
                >
                  Aggiungi tappa
                </button>
              </div>
            </div>
          </div>
          <div class="mb-3">
            <div class="row">
              <div class="col">
                <router-link to="/dashboard" class="btn gradient d-block w-100"
                  >Indietro</router-link
                >
              </div>
              <div class="col">
                <button
                  class="btn gradient d-block w-100"
                  type="button"
                  v-on:click="saveTrip()"
                >
                  Salva
                </button>
              </div>
            </div>
          </div>

          <b-modal v-model="showModal" size="lg" style="opacity: 0.5">
            <template #modal-header="{ close }">
              <b-button size="sm" style="background: #00467f" @click="close()"
                >X</b-button
              >
              <h3>Modifica Tappa</h3>
            </template>
            <template>
              <form>
                <div class="mb-3">
                  <label class="form-label" for="luogo">Luogo tappa</label
                  ><input
                    class="form-control"
                    type="text"
                    v-model="luogo_modal"
                    id="luogo"
                  />
                </div>
                <div class="mb-3">
                  <label class="form-label" for="latitudine">Latitude</label
                  ><input
                    class="form-control"
                    type="text"
                    v-model="latitudine_modal"
                    id="latitudine"
                  />
                </div>
                <div class="mb-3">
                  <label class="form-label" for="longitudine">Longitude</label
                  ><input
                    class="form-control"
                    type="text"
                    v-model="longitudine_modal"
                    id="longitudine"
                  />
                </div>
              </form>
            </template>
            <template #modal-footer="{ cancel }">
              <span style="padding-left: 40px"
                ><b-button size="sm" variant="danger" @click="cancel()"
                  >CHIUDI</b-button
                ></span
              >
              <span style="padding-right: 40px"
                ><b-button
                  size="sm"
                  style="background: #00467f"
                  v-on:click="updateTappa()"
                  >SALVA MODIFICHE</b-button
                ></span
              >
            </template>
          </b-modal>
        </div>
      </section>
    </main>
    <Footer />
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import { mapGetters, mapActions } from "vuex";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
// import ModificableMap from '@/components/ModificableMap.vue';
import draggable from "vuedraggable";
import moment from "moment";
import { latLngBounds } from "leaflet";
import {
  LMap,
  LTileLayer,
  LMarker,
  LPolyline /*  LLayerGroup, LControlZoom */,
} from "vue2-leaflet";

export default {
  name: "ModificaViaggio",
  components: {
    // HelloWorld,
    Header,
    Footer,
    // ModificableMap,
    draggable,
    LMap,
    LTileLayer,
    LMarker,
    LPolyline,
  },
  //   props: { markers: Array },
  data() {
    return {
      id: null,
      data: null,
      luogo: null,
      mezzo: null,
      percorso: [],
      latitudine: null,
      longitudine: null,
      tappa: null,
      partenza_latitudine: null,
      partenza_longitudine: null,
      arrivo_latitudine: null,
      arrivo_longitudine: null,
      tappe: [],
      polylines: [],
      markers: [],
      // showPolyline: true,
      showModal: false,
      luogo_modal: null,
      latitudine_modal: null,
      longitudine_modal: null,
      i: null,
      showUpdateTappe: false,
      trip: [],

      // map
      url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
      attribution:
        '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors',
      zoom: 8,
      center: [45.65052568917208, 13.76517096104127], //[51.505, -0.09]
      markerLatLng: [51.505, -0.09],
      // markers: [],
      // polylines: [],
      // stuff: [],
      // id: null,
      // showPolyline: true,
      bounds: latLngBounds(
        { lat: 51.476483373501964, lng: -0.14668464136775586 },
        { lat: 51.52948330894063, lng: -0.019140238291583955 }
      ),
    };
  },
  methods: {
    ...mapActions(["addTrip", "addNewTrip", "getTrip", "updateTrip"]),
    // onUserSubmit() {
    //    if (this.id == null) {
    //         this.addNewTrip(this.trip);
    //    } else {
    //         this.addTrip(this.trip);
    //    }
    // },
    alert(item) {
      alert("Luogo: " + JSON.stringify(item));
    },

    addMarker: function () {
      // this.showPolyline = false;
      // this.$store.state.showPolyline = false;
      // console.log(`addMarker(): showPOyline: ${this.$store.state.showPolyline}`);
      // this.polylines = [];
      // console.log(this.tappe.length);
      // if ( this.tappe.length < 1) {
      console.log(this.tappe);
      if (this.tappe.length < 1) {
        const newMarker = {
          //center: [45.65052568917208, ]
          POSIZIONE: { lat: 45.65052568917208, lng: 13.76517096104127 },
          LUOGO_TAPPA: "nuova tappa",
          draggable: true,
          visible: true,
        };
        this.tappe.push(newMarker);
        this.polylines.push(newMarker.POSIZIONE);
      } else {
        const lastMarker = this.tappe[this.tappe.length - 1];
        console.log(lastMarker);
        const newMarker = {
          POSIZIONE: {
            lat: lastMarker.POSIZIONE.lat,
            lng: lastMarker.POSIZIONE.lng,
          },
          LUOGO_TAPPA: "nuova tappa",
          draggable: true,
          visible: true,
        };
        this.tappe.push(newMarker);
        this.polylines.push(newMarker.POSIZIONE);
      }
      this.$store.state.markers = this.tappe;
      this.$store.state.polylines = this.polylines;
    },

    createPath: function () {
      this.markers = this.$store.state.markers;
      console.log(this.$store.state.markers);
      const bounds = latLngBounds(this.markers.map((o) => o.POSIZIONE));
      this.polylines = [];
      this.bounds = bounds;
      for (let i = 0; i < this.markers.length; ++i) {
        this.polylines[i] = this.markers[i].POSIZIONE;
      }
      this.$store.state.polylines = this.polylines;
      console.log("crea path markers");
      console.log(this.markers);
    },

    getTappa: function (i) {
      this.i = i;
      this.showModal = true;
      console.log(this.tappe[i]);
      this.luogo_modal = this.tappe[i].LUOGO_TAPPA;
      this.latitudine_modal = this.tappe[i].POSIZIONE.lat;
      this.longitudine_modal = this.tappe[i].POSIZIONE.lng;
      console.log(
        `luogo_modal: ${this.luogo_modal}, lat_modal: ${this.latitudine_modal}, lgn_modal: ${this.longitudine_modal}`
      );
    },

    getDate(datetime) {
      return moment(String(datetime)).format("YYYY-MM-DD");
    },

    getDateDMY(datetime) {
      return moment(String(datetime)).format("DD/MM/YYYY");
    },

    loadTrip: async function () {
      // let valori = await fetch(`api/trips/${this.id}`);
      // this.trip = await valori.json();
      let trip = await this.getTrip(this.id);
      console.log("creatd: after get trip");
      console.log(`trips:`);
      console.log(trip);
      this.data = this.getDate(trip.DATA); // .$date
      this.luogo = trip.LUOGO;
      this.mezzo = trip.MEZZO;
      this.percorso = trip.PERCORSO;
      this.partenza_latitudine = this.percorso.PARTENZA_LAT;
      this.partenza_longitudine = this.percorso.PARTENZA_LNG;
      this.arrivo_latitudine = this.percorso.ARRIVO_LAT;
      this.arrivo_longitudine = this.percorso.ARRIVO_LNG;
      console.log(
        `p_lat: ${this.partenza_latitudine}, p_lng: ${this.partenza_longitudine}, a_lat: ${this.arrivo_latitudine}, a_lng: ${this.arrivo_longitudine}`
      );
      this.tappe = trip.TAPPE_PRINCIPALI;
      console.log("load, tappe:");
      console.log(this.tappe);
      for (let i = 0; i < this.tappe.length; ++i) {
        this.polylines[i] = this.tappe[i].POSIZIONE;
      }
      console.log("load, polylines:");
      console.log(this.polylines);
      this.updateValues();
      this.createPath();
    },

    removeMarker: function (index) {
      this.tappe.splice(index, 1);
      this.polylines.splice(index, 1);
    },

    saveTrip: async function () {
      // let data;
      this.percorso = {
        PARTENZA_LAT: this.partenza_latitudine,
        PARTENZA_LNG: this.partenza_longitudine,
        ARRIVO_LAT: this.arrivo_latitudine,
        ARRIVO_LNG: this.arrivo_longitudine,
      };
      // save new trip
      let trip;
      if (this.id == null) {
        console.log(`id null? == ${this.id}`);
        trip = {
          LUOGO: this.luogo,
          MEZZO: this.mezzo,
          PERCORSO: this.percorso,
          TAPPE_PRINCIPALI: this.tappe,
          DATA: this.data,
        };
        // console.log(this.trip);
        console.log("stampo json trip");
        console.log(JSON.stringify(trip));
        // data = await fetch("api/trips/", {
        //     'method': 'POST',
        //     'headers': {
        //         'Accept': 'application/json',
        //         'Content-Type': 'application/json'
        //     },
        //     'body': JSON.stringify(this.trip)
        // });

        this.addNewTrip(JSON.stringify(trip));

        // update trip
      } else {
        console.log(`id == ${this.id}`);
        trip = {
          ID: this.id,
          LUOGO: this.luogo,
          MEZZO: this.mezzo,
          PERCORSO: this.percorso,
          TAPPE_PRINCIPALI: this.tappe,
          DATA: this.data,
        };
        // console.log(this.trip);
        console.log("stampo json trip");
        console.log(JSON.stringify(trip));
        // data = await fetch("api/trips/", {
        //     'method': 'PUT',
        //     'headers': {
        //         'Accept': 'application/json',
        //         'Content-Type': 'application/json'
        //     },
        //     'body': JSON.stringify(this.trip)
        // });
        this.updateTrip(JSON.stringify(trip));
      }
      // trip = await data.json();
      // console.log("trip dopo json");
      // console.log(trip);
    },

    updateTappa: function () {
      console.log(this.tappe[this.i]);
      this.tappe[this.i].LUOGO_TAPPA = this.luogo_modal;
      this.tappe[this.i].POSIZIONE.lat = this.latitudine_modal;
      this.tappe[this.i].POSIZIONE.lng = this.longitudine_modal;
      this.showModal = false;
      this.createPath();
    },

    updateValues: function () {
      console.log("update values modifica viaggio");
      this.$store.state.markers = this.tappe;
      console.log(this.$store.state.markers);
      this.$store.state.polylines = this.polylines;
      this.createPath();
    },
  },
  computed: {
    ...mapGetters(["markersList", "polylineList", "singleTrip"]),
  },

  async mounted() {
    console.log("mounted");
  },

  async created() {
    this.id = this.$route.query.id;
    console.log("creatd: before get trip");
    if (this.id != null) {
      this.loadTrip();
    } else {
      console.log("inserisci viaggio");
    }
  },
};
</script>

<style scoped>
.myContainer {
  max-width: 850px;
  padding: 20px;
  margin: auto;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}
</style>
