<template>
  <div class="container">
    <main>
      <div class="row">
        <div class="heading col" v-if="idTrip != null">
          <h2>Update your trip of {{ getDateDMY(date) }}</h2>
        </div>
        <div class="heading col" v-else>
          <h2>Add a new trip</h2>
        </div>
      </div>
      <div class="row justify-content-center" v-if="dataLoaded">
        <DrawableMap
          ref="map"
          :myTripGeoJSON="myTripGeoJSON"
          @updateCoordinates="updateTripCoordinates"
        />
      </div>
      <hr />
      <form @submit.prevent>
        <div class="mb-3">
          <label class="form-label" for="date">Date</label
          ><input class="form-control" type="date" v-model="date" id="date" />
        </div>
        <div class="mb-3">
          <label class="form-label" for="place">Place</label
          ><input class="form-control" type="text" v-model="place" id="place" />
        </div>
        <div class="mb-3">
          <label class="form-label" for="transportation">Transportation</label>
          <select
            class="form-select"
            id="transportation"
            v-model="transportation"
          >
            <optgroup label="Choose transportation">
              <option value="Bike">Bike</option>
              <option value="Bus">Bus</option>
              <option value="Car">Car</option>
              <option value="Motorcycle">Motorcycle</option>
              <option value="On foot">On foot</option>
              <option value="Plane">Place</option>
              <option value="Train">Train</option>
            </optgroup>
          </select>
        </div>
        <!-- <div class="mb-3">
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
        </div> -->
      </form>
      <!-- <div v-if="mainStages != null && mainStages.length > 0"> -->
      <div v-if="myTripGeoJSON != null && myTripGeoJSON.features.length > 0">
        <h5>Main stages</h5>
        <div
          class="mb-3"
          v-for="(value, index) in myTripGeoJSON.features"
          v-bind:key="index"
        >
          <div v-if="value.geometry.type == 'Point'" class="row">
            <div class="col">
              <label class="form-label" for="stage_name">Stage name</label>
              <input
                class="form-control"
                type="text"
                v-model="value.properties.name"
              />
            </div>
            <!-- <div class="col">
            <label class="form-label" for="latitude">Coordinates</label>
            <input
              class="form-control"
              type="text"
              v-model="value.geometry.coordinates"
              readonly
            />
          </div> -->
            <div class="col">
              <label class="form-label" for="latitude">Latitude</label>
              <input
                class="form-control"
                type="text"
                v-model="value.geometry.coordinates[1]"
                readonly
              />
            </div>
            <div class="col">
              <label class="form-label" for="longitude">Longitude</label>
              <input
                class="form-control"
                type="text"
                v-model="value.geometry.coordinates[0]"
                readonly
              />
            </div>
            <!-- <div class="col">
            <label class="form-label" for="markerColor">Color</label>
            <input
              class="form-control"
              type="color"
              v-model="value.properties.color"
            />
          </div> -->
          </div>
        </div>
      </div>
      <div class="row margin-button">
        <div class="col" v-if="idTrip != null">
          <button
            class="btn btn-danger w-100"
            type="button"
            v-on:click="deleteTrip()"
          >
            Delete trip
          </button>
        </div>
        <div class="col">
          <button
            class="btn gradient w-100"
            type="button"
            v-on:click="saveTrip()"
          >
            Save
          </button>
        </div>
      </div>
      <div class="row margin-button">
        <div class="col">
          <router-link to="/dashboard" class="btn gradient w-100"
            >Go back</router-link
          >
        </div>
      </div>
      <!-- </div> -->

      <!-- <b-modal v-model="showModal" size="lg">
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
      </b-modal> -->
      <!-- </div> -->
      <!-- </section> -->
    </main>
  </div>
</template>

<script>
import DrawableMap from "@/components/DrawableMap.vue";
import moment from "moment";
import TripService from "@/services/TripService.js";

export default {
  name: "ModificaViaggio",
  components: {
    DrawableMap,
  },
  //   props: { markers: Array },
  data() {
    return {
      // id: 1,
      date: null,
      place: null,
      transportation: null,
      path: [],
      latitude: null,
      longitude: null,
      stage: null,
      // partenza_latitudine: null,
      // partenza_longitudine: null,
      // arrivo_latitudine: null,
      // arrivo_longitudine: null,
      mainStages: [],

      pathCoordinates: [],
      polylines: [],
      markers: [],
      // showPolyline: true,
      showModal: false,
      luogo_modal: null,
      latitudine_modal: null,
      longitudine_modal: null,
      i: null,
      showUpdatemainStages: false,
      trip: [],
      myTripGeoJSON: null,
      idUser: null,
      username: null,
      token: null,
      idTrip: null,

      dataLoaded: false,
    };
  },

  mounted() {},

  beforeMount: async function () {
    this.idTrip = this.$route.query.idTrip;
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
    if (this.$route.query.idTrip != null) {
      console.log(`idTrip = ${this.$route.query.idTrip}`);
      await this.loadTrip();
    } else {
      this.dataLoaded = true;
      console.log(`idTrip = ${this.$route.query.idTrip}`);
    }
  },

  methods: {
    /*
    alert(item) {
      alert("Luogo: " + JSON.stringify(item));
    },*/

    deleteTrip: async function () {
      let deleteResponse;
      await TripService.deleteTrip(this.idUser, this.idTrip, this.token)
        .then((response) => {
          deleteResponse = response;
        })
        .catch(function (error) {
          console.log(error);
        });
      console.log(deleteResponse);
    },
    // NOO USATA
    getTappa: function (i) {
      this.i = i;
      this.showModal = true;
      console.log(this.mainStages[i]);
      this.luogo_modal = this.mainStages[i].STAGE_NAME;
      this.latitudine_modal = this.mainStages[i].LAT;
      this.longitudine_modal = this.mainStages[i].LNG;
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
      await TripService.getTrip(this.idUser, this.idTrip, this.token)
        .then((response) => {
          this.trip = response;
        })
        .catch(function (error) {
          console.log(error);
        });
      console.log("created: after get trip");
      console.log(`trip:`);
      console.log(this.trip);
      this.trip = this.trip.data;
      console.log(this.trip);
      this.date = this.trip.DATE;
      this.place = this.trip.PLACE;
      this.transportation = this.trip.TRANSPORTATION;
      this.myTripGeoJSON = JSON.parse(this.trip.GEOJSON);
      console.log(this.myTripGeoJSON);
      this.getMainStagesAndPathCoordinate();
      this.dataLoaded = true;
    },

    saveTrip: async function () {
      // save new trip
      let trip;
      let risposta;
      if (this.idTrip == null) {
        console.log(`id null? == ${this.idTrip}`);
        trip = {
          PLACE: this.place,
          TRANSPORTATION: this.transportation,
          GEOJSON: JSON.stringify(this.myTripGeoJSON),
          DATE: this.date,
          USER: this.idUser,
        };
        console.log("stampo json trip");
        console.log(JSON.stringify(trip));

        await TripService.createTrip(this.idUser, this.token, trip)
          .then((response) => {
            risposta = response;
          })
          .catch(function (error) {
            console.log(error);
          });
        console.log(risposta);
        // update trip
      } else {
        console.log(`idTrip = ${this.idTrip}`);
        trip = {
          ID: this.idTrip,
          PLACE: this.place,
          TRANSPORTATION: this.transportation,
          GEOJSON: JSON.stringify(this.myTripGeoJSON),
          DATE: this.date,
          USER: this.idUser,
        };
        // console.log(this.trip);
        console.log("stampo json trip");
        console.log(JSON.stringify(trip));
        await TripService.updateTrip(this.idUser, this.idTrip, this.token, trip)
          .then((response) => {
            risposta = response;
          })
          .catch(function (error) {
            console.log(error);
          });
        console.log(risposta);
      }
    },

    getMainStagesAndPathCoordinate() {
      this.mainStages = [];
      this.pathCoordinates = [];
      let features = this.myTripGeoJSON.features;
      for (let i = 0; i < this.myTripGeoJSON.features.length; ++i) {
        if (features[i].geometry.type === "Point") {
          this.mainStages.push(
            features[i]
            // STAGE_NAME: features[i].properties.name,
            // LAT: features[i].geometry.coordinates[1],
            // LNG: features[i].geometry.coordinates[0],
          );
        } else if (features[i].geometry.type === "LineString") {
          let coordinates = features[i].geometry.coordinates;
          for (let j = 0; j < coordinates.length; ++j) {
            this.pathCoordinates.push(features[i]); //[coordinates[i][1], coordinates[j][0]]
          }
        }
      }
      console.log("mainStages");
      console.log(this.mainStages);
      console.log("pathCoordinates");
      console.log(this.pathCoordinates);
    },

    updateTripCoordinates(value) {
      console.log("update cooridnates of my trip");
      console.log(value);
      this.myTripGeoJSON = value;
      console.log(" updated mytripJSON");
      console.log(this.myTripGeoJSON);
      this.getMainStagesAndPathCoordinate();
    },
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
.margin-button {
  margin-top: 30px;
}
</style>
