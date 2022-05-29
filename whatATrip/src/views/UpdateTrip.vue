<template>
  <div class="container myMargin">
    <div class="row">
      <div class="heading col" v-if="idTrip != null">
        <h2 class="titles">Update your trip of {{ date }}</h2>
      </div>
      <div class="heading col" v-else>
        <h2 class="titles">Add a new trip</h2>
      </div>
    </div>
    <div class="row">
      <p>First draw your route and then add your stages</p>
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
        <label class="form-label" for="date">Date</label>
        <input
          class="form-control"
          v-bind:class="{
            'is-valid': validation.valid.date,
            'is-invalid': validation.invalid.date,
          }"
          type="date"
          v-model="date"
          id="date"
        />
        <div class="valid-feedback" v-if="validation.valid.date">
          {{ validation.valid.date }}
        </div>
        <div class="invalid-feedback" v-if="validation.invalid.date">
          {{ validation.invalid.date }}
        </div>
      </div>
      <div class="mb-3">
        <label class="form-label" for="place">Place</label
        ><input
          class="form-control"
          v-bind:class="{
            'is-valid': validation.valid.place,
            'is-invalid': validation.invalid.place,
          }"
          type="text"
          v-model="place"
          id="place"
        />
        <div class="valid-feedback" v-if="validation.valid.place">
          {{ validation.valid.place }}
        </div>
        <div class="invalid-feedback" v-if="validation.invalid.place">
          {{ validation.invalid.place }}
        </div>
      </div>
      <div class="mb-3">
        <label class="form-label" for="transportation">Transportation</label>
        <select
          class="form-select"
          id="transportation"
          v-model="transportation"
          v-bind:class="{
            'is-valid': validation.valid.transportation,
            'is-invalid': validation.invalid.transportation,
          }"
        >
          <optgroup label="Choose transportation">
            <option value="Bike">Bike</option>
            <option value="Bus">Bus</option>
            <option value="Boat">Boat</option>
            <option value="Car">Car</option>
            <option value="Motorcycle">Motorcycle</option>
            <option value="On foot">On foot</option>
            <option value="Plane">Place</option>
            <option value="Train">Train</option>
            <option value="Other">Other</option>
          </optgroup>
        </select>
        <div class="valid-feedback" v-if="validation.valid.transportation">
          {{ validation.valid.transportation }}
        </div>
        <div class="invalid-feedback" v-if="validation.invalid.transportation">
          {{ validation.invalid.transportation }}
        </div>
      </div>
      <!-- <div v-if="mainStages != null && mainStages.length > 0"> -->
      <div v-if="myTripGeoJSON != null && myTripGeoJSON.features.length > 0">
        <h5>Main stages</h5>
        <div
          class="mb-3"
          v-for="(value, index) in myTripGeoJSON.features"
          v-bind:key="index"
        >
          <div v-if="value.geometry.type == 'Point'" class="row">
            <div class="col-4">
              <label class="form-label" for="stage_name">Stage name</label>
              <input
                class="form-control"
                type="text"
                v-model="value.properties.name"
              />
            </div>
            <div class="col-4">
              <label class="form-label" for="latitude">Latitude</label>
              <input
                class="form-control"
                type="text"
                v-model="value.geometry.coordinates[1]"
                readonly
              />
            </div>
            <div class="col-4">
              <label class="form-label" for="longitude">Longitude</label>
              <input
                class="form-control"
                type="text"
                v-model="value.geometry.coordinates[0]"
                readonly
              />
            </div>
          </div>
        </div>
      </div>
      <div class="mb-3">
        <div class="custom-control custom-checkbox checkbox-lg">
          <label class="form-label">
            <input
              type="checkbox"
              v-model="showAddDescription"
              class="custom-control-input"
            />
            Do you want to add a short description of the trip?</label
          >
        </div>
        <textarea
          v-if="showAddDescription"
          v-model="description"
          class="form-control"
          v-bind:class="{
            'is-valid': validation.valid.description,
            'is-invalid': validation.invalid.description,
          }"
          rows="5"
        ></textarea>
        <div class="valid-feedback" v-if="validation.valid.description">
          {{ validation.valid.description }}
        </div>
        <div class="invalid-feedback" v-if="validation.invalid.description">
          {{ validation.invalid.description }}
        </div>
      </div>
    </form>

    <!-- token expired notification -->
    <notifications
      group="unauthorized"
      position="top center"
      width="40%"
      style="top: 40%"
      v-if="showErrorMessage"
    />
    <div class="row margin-button">
      <div class="col" v-if="idTrip != null">
        <button
          class="btn yellow-color w-100"
          type="button"
          v-on:click="confirmDeleteTrip()"
        >
          Delete trip
        </button>
      </div>
      <div class="col">
        <button
          class="btn yellow-color w-100"
          type="button"
          v-on:click="saveTrip()"
        >
          Save
        </button>
      </div>
    </div>
    <div class="row margin-button">
      <div class="col">
        <router-link to="/dashboard" class="btn orange-color w-100"
          >Go back</router-link
        >
      </div>
    </div>

    <!--  MODAL FOR CONFIRM DELETE TRIP -->
    <b-modal v-model="showDeleteTrip" size="md">
      <template #modal-header="{ close }">
        <b-button size="sm" class="dark-color" @click="close()">X</b-button>
      </template>
      <template>
        <h3 style="color: #ff6f3c; text-align: center; margin-top: 15px">
          Are you sure you want to delete the trip?
        </h3>
        <p style="text-align: center">
          You will be redirected to the home page in a few seconds.
        </p>
        <div class="row justify-content-center">
          <div class="loader" v-if="showLoader">
            <div class="ball"></div>
            <div class="ball"></div>
            <div class="ball"></div>
            <span>Deleting trip...</span>
          </div>
        </div>
      </template>
      <template #modal-footer="{ cancel }">
        <span v-if="!showLoader" style="padding-left: 40px"
          ><b-button class="yellow-color" @click="cancel()">NO</b-button></span
        >
        <span v-if="!showLoader" style="padding-right: 40px"
          ><b-button class="orange-color" @click="deleteTrip()"
            >YES</b-button
          ></span
        ><!-- variant="danger"-->
      </template>
    </b-modal>
    <!--  MODAL FOR CONFIRM CREATION/UPDATE OF TRIP -->
    <b-modal v-model="showSuccessTrip" size="md">
      <template #modal-header="{ close }">
        <b-button size="sm" class="dark-color" @click="close()">X</b-button>
      </template>
      <template>
        <h3
          style="color: #ff6f3c; text-align: center; margin-top: 15px"
          v-if="idTrip != null"
        >
          Your trip was successfully updated.
        </h3>
        <h3 style="color: #ff6f3c; text-align: center; margin-top: 15px" v-else>
          Your trip was successfully created.
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
import DrawableMap from "@/components/DrawableMap.vue";
import moment from "moment";
import TripService from "@/services/TripService.js";
import UserService from "@/services/UserService";

export default {
  name: "ModificaViaggio",
  components: {
    DrawableMap,
  },

  data() {
    return {
      // id: 1,
      date: null,
      place: null,
      transportation: null,
      description: null,
      showAddDescription: false,
      path: [],
      latitude: null,
      longitude: null,
      stage: null,
      mainStages: [],

      pathCoordinates: [],
      polylines: [],
      markers: [],

      myTripGeoJSON: null,
      idUser: null,
      username: null,
      token: null,
      idTrip: null,

      dataLoaded: false,
      validation: {
        invalid: {},
        valid: {},
      },
      showSuccessTrip: false,
      errorDate: false,
      errorPlace: false,
      errorTransportation: false,
      errorDescription: false,
      errorStatus: false,
      showErrorMessage: false,

      showDeleteTrip: false,
      showLoader: false,
    };
  },

  beforeMount: async function () {
    this.idTrip = this.$route.query.idTrip;
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
    if (this.$route.query.idTrip != null) {
      await this.loadTrip();
    } else {
      this.dataLoaded = true;
    }
  },

  mounted: function () {},

  methods: {
    /*
    alert(item) {
      alert("Luogo: " + JSON.stringify(item));
    },*/

    checkInformations: function () {
      if (!this.date) {
        this.validation.invalid.date = "Please select a date.";
        this.errorDate = true;
      } else {
        this.errorDate = false;
        this.validation.valid.date = "Date is fine";
      }

      if (!this.place) {
        this.validation.invalid.place = "Please type a place.";
        this.errorPlace = true;
      } else {
        this.errorPlace = false;
        this.validation.valid.place = "Place is fine";
      }

      if (!this.transportation) {
        this.validation.invalid.transportation =
          "Please select a transportation.";
        this.errorTransportation = true;
      } else {
        this.errorTransportation = false;
        this.validation.valid.transportation = "Transportation is fine";
      }

      if (this.description != null && this.description.length > 300) {
        this.validation.invalid.description =
          "You can only inset 200 characters (" + this.description.length + "/200)";
        this.errorDescription = true;
      } else {
        this.errorDescription = false;
        this.validation.valid.description = "Description is fine";
      }

      this.$forceUpdate();
      if (
        this.errorDate ||
        this.errorPlace ||
        this.errorTransportation ||
        this.errorDescription
      ) {
        return false;
      } else {
        return true;
      }
    },

    confirmDeleteTrip: function () {
      this.showDeleteTrip = true;
    },

    deleteTrip: async function () {
      this.showLoader = true;
      let deleteResponse;
      let errorStatus = 0;
      await TripService.deleteTrip(this.idUser, this.idTrip, this.token)
        .then((response) => {
          deleteResponse = response;
        })
        .catch(function (error) {
          console.log(error);
        });
      console.log(deleteResponse);
      if (errorStatus != 0) {
        this.showErrorNotification();
        setTimeout(() => {
          UserService.logout();
        }, 2000);
      } else {
        if (deleteResponse.status == 200) {
          // this.showSuccessCreation();
          setTimeout(() => {
            this.redirectToDashboard();
          }, 2000);
        }
      }
    },

    getDate(datetime) {
      return moment(String(datetime)).format("YYYY-MM-DD");
    },

    getDateDMY(datetime) {
      return moment(String(datetime)).format("DD/MM/YYYY");
    },

    loadTrip: async function () {
      let errorStatus = 0;
      await TripService.getTrip(this.idUser, this.idTrip, this.token)
        .then((response) => {
          this.trip = response;
        })
        .catch(function (error) {
          errorStatus = error;
          console.log(error);
        });
      if (errorStatus != 0) {
        this.showErrorNotification();
        setTimeout(() => {
          UserService.logout();
        }, 2000);
        this.redirectLogout();
      } else {
        if (this.trip.status == 200) {
          this.trip = this.trip.data;
          // console.log(this.trip);
          this.date = this.trip.DATE;
          this.place = this.trip.PLACE;
          this.transportation = this.trip.TRANSPORTATION;
          this.myTripGeoJSON = JSON.parse(this.trip.GEOJSON);
          this.description = this.trip.DESCRIPTION;
          if (this.description != null) {
            this.showAddDescription = true;
          }
          // console.log(this.myTripGeoJSON);
          this.getMainStagesAndPathCoordinate();
          this.dataLoaded = true;
        }
      }
    },
    // save new trip/ update trip
    saveTrip: async function () {
      let trip;
      let okResponse;
      let errorStatus = 0;
      if (this.checkInformations()) {
        console.log(this.checkInformations());
        if (this.idTrip == null) {
          console.log(`id null? == ${this.idTrip}`);
          trip = {
            PLACE: this.place,
            TRANSPORTATION: this.transportation,
            GEOJSON: JSON.stringify(this.myTripGeoJSON),
            DATE: this.date,
            USER: this.idUser,
            DESCRIPTION: this.description,
          };

          await TripService.createTrip(this.idUser, this.token, trip)
            .then((response) => {
              okResponse = response;
            })
            .catch(function (error) {
              errorStatus = error.response.status;
              console.log(error);
            });
          console.log(okResponse);
          if (errorStatus != 0) {
            this.showErrorNotification();
            setTimeout(() => {
              UserService.logout();
            }, 2000);
            this.redirectLogout();
          } else {
            if (okResponse.status == 200) {
              //trip creato
              this.showSuccessTrip = true;
              this.showLoader = true;
              setTimeout(() => {
                this.redirectToDashboard();
              }, 2000);
            }
          }
          // update trip
        } else {
          trip = {
            ID: this.idTrip,
            PLACE: this.place,
            TRANSPORTATION: this.transportation,
            GEOJSON: JSON.stringify(this.myTripGeoJSON),
            DATE: this.date,
            USER: this.idUser,
            DESCRIPTION: this.description,
          };
          await TripService.updateTrip(
            this.idUser,
            this.idTrip,
            this.token,
            trip
          )
            .then((response) => {
              okResponse = response;
            })
            .catch(function (error) {
              errorStatus = error.response.status;
              console.log(error);
            });
          console.log(okResponse);
          if (errorStatus != 0) {
            this.showErrorNotification();
            setTimeout(() => {
              UserService.logout();
            }, 2000);
            this.redirectLogout();
          } else {
            if (okResponse.status == 200) {
              this.showSuccessTrip = true;
              this.showLoader = true;
              setTimeout(() => {
                this.redirectToDashboard();
              }, 2000);
            }
          }
        }
      } else {
        console.log(this.checkInformations());
      }
    },

    showErrorNotification: function () {
      this.showErrorMessage = true;
      console.log(`errorNotification ${this.showErrorMessage}`);
      this.$notify({
        group: "unauthorized",
        title: "Important message",
        text: "Sorry you session is expired, you will be redirected to the login page.",
        type: "error",
      });
    },

    redirectToDashboard: function () {
      this.$router.push({
        path: "/dashboard",
      });
    },

    redirectLogout: function () {
      this.$router.push({
        path: "/login",
      });
    },

    getMainStagesAndPathCoordinate() {
      this.mainStages = [];
      this.pathCoordinates = [];
      let features = this.myTripGeoJSON.features;
      for (let i = 0; i < this.myTripGeoJSON.features.length; ++i) {
        if (features[i].geometry.type === "Point") {
          this.mainStages.push(features[i]);
        } else if (features[i].geometry.type === "LineString") {
          let coordinates = features[i].geometry.coordinates;
          for (let j = 0; j < coordinates.length; ++j) {
            this.pathCoordinates.push(features[i]);
          }
        }
      }
    },

    updateTripCoordinates(value) {
      this.myTripGeoJSON = value;
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
h2 {
  color: #155263;
}

input[type="checkbox"] {
  transform: scale(1.6);
}
</style>
