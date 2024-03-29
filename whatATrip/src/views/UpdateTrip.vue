<template>
  <div class="container my-margin" id="app">
    <div class="row justify-content-center">
      <div class="col">
        <h2 v-if="idTrip != null" class="titles">
          Update your trip of {{ date }}
        </h2>
        <h2 v-else class="titles">Add a new trip</h2>
        <p>First draw your route and then add your stages</p>
      </div>
    </div>
    <div class="row justify-content-center" v-if="dataLoaded">
      <DrawableMap
        ref="map"
        :myTripGeoJSON="myTripGeoJSON"
        :maxDistanceError="maxDistanceError"
        @updateCoordinates="updateTripCoordinates"
        :key="stageNameKey"
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
            <option value="Plane">Plane</option>
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

      <div v-if="myTripGeoJSON != null && myTripGeoJSON.features.length > 1">
        <h5>Main stages</h5>
        <div
          v-for="(value, index) in myTripGeoJSON.features"
          v-bind:key="index"
          class="mb-3"
        >
          <div v-if="value.geometry.type == 'Point'" class="row">
            <div class="col-sm">
              <label class="form-label" for="stage_name">Stage name</label>
              <input
                class="form-control"
                type="text"
                v-model="value.properties.name"
                @change="updateStage()"
              />
            </div>
            <div class="col-sm">
              <label class="form-label" for="latitude">Latitude</label>
              <input
                class="form-control"
                type="text"
                v-model="value.geometry.coordinates[1]"
                readonly
              />
            </div>
            <div class="col-sm">
              <label class="form-label" for="longitude">Longitude</label>
              <input
                class="form-control"
                type="text"
                v-model="value.geometry.coordinates[0]"
                readonly
              />
            </div>
            <div class="col-sm">
              <label class="form-label" for="descriptionProp"
                >Description</label
              >
              <input
                class="form-control"
                type="text"
                v-model="value.properties.description"
                @change="updateStage()"
              />
            </div>
            <hr />
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
    <div v-if="maxDistanceError">
      <p class="error-marker">
        You can't save a trip with a marker too far from the route.
      </p>
    </div>
    <div v-if="routeError">
      <p class="error-marker">You can't save a trip without a route.</p>
    </div>

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
        <router-link
          to="/dashboard"
          class="btn orange-color w-100"
          style="color: white"
          >Go back</router-link
        >
      </div>
    </div>

    <!--  MODAL TO CONFIRM DELETE TRIP -->
    <b-modal v-model="showDeleteTrip" size="md">
      <template #modal-header="{ close }">
        <b-button size="sm" class="dark-color" @click="close()">X</b-button>
      </template>
      <template>
        <h3 style="color: #ff6f3c; text-align: center; margin-top: 15px">
          Are you sure you want to delete the trip?
        </h3>
        <div class="row justify-content-center" v-if="showLoader">
          <p style="text-align: center">
            You will be redirected to the home page in a few seconds.
          </p>
          <div class="loader" style="margin-bottom: 10px">
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
        >
        <span style="padding-left: 40px" v-if="showLoader"></span>
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
import DrawableMap from "@/components/DrawableMap.vue";
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
      stageNameKey: 1,

      pathCoordinates: [],
      polylines: [],
      markers: [],

      myTripGeoJSON: null,
      maxDistanceError: false,
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
      routeError: false,
      errorStatus: false,
      showErrorMessage: false,
      showExpiredError: false,

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
    // Checking if the user has filled in all the required fields.
    checkInformations: function () {
      if (!this.date) {
        this.validation.invalid.date = "Please select a date.";
        this.validation.valid.date = "";
        this.errorDate = true;
      } else {
        this.errorDate = false;
        this.validation.valid.date = "Date is fine";
        this.validation.invalid.date = "";
      }

      if (!this.place) {
        this.validation.invalid.place = "Please type a place.";
        this.validation.valid.place = "";
        this.errorPlace = true;
      } else {
        this.errorPlace = false;
        this.validation.valid.place = "Place is fine";
        this.validation.invalid.place = "";
      }

      if (!this.transportation) {
        this.validation.invalid.transportation =
          "Please select a transportation.";
        this.validation.valid.transportation = "";
        this.errorTransportation = true;
      } else {
        this.errorTransportation = false;
        this.validation.valid.transportation = "Transportation is fine";
        this.validation.invalid.transportation = "";
      }

      if (this.description != null && this.description.length > 300) {
        this.validation.invalid.description =
          "You can only inset 200 characters (" +
          this.description.length +
          "/200)";
        this.validation.valid.description = "";
        this.errorDescription = true;
      } else {
        this.errorDescription = false;
        this.validation.valid.description = "Description is fine";
        this.validation.invalid.description = "";
      }

      this.$forceUpdate();
      if (
        this.errorDate ||
        this.errorPlace ||
        this.errorTransportation ||
        this.errorDescription ||
        this.maxDistanceError ||
        this.checkRoute()
      ) {
        return false;
      } else {
        return true;
      }
    },

    // Checking to see if the route has been drawn. If it has, it will return false. If it hasn't, it
    // will return true.
    checkRoute: function () {
      if (this.myTripGeoJSON.features.length > 0) {
        this.routeError = false;
        return false;
      } else {
        this.routeError = true;
        return true;
      }
    },

    // Function that is called when the user clicks the delete button, it sets the
    // showDeleteTrip property to true.
    confirmDeleteTrip: function () {
      this.showDeleteTrip = true;
    },

    // Function for deleting a trip from the database.
    deleteTrip: async function () {
      this.showLoader = true;
      let deleteResponse;
      let errorStatus = 0;
      await TripService.deleteTrip(this.idUser, this.idTrip, this.token)
        .then((response) => {
          deleteResponse = response;
        })
        .catch(function (error) {
          errorStatus = error.response.status;
          console.log(error);
        });
      if (errorStatus != 0) {
        this.showExpiredError = true;
        setTimeout(() => {
          UserService.logout();
          this.redirectLogout();
        }, 2000);
      } else {
        if (deleteResponse.status == 200) {
          setTimeout(() => {
            this.redirectToDashboard();
          }, 2000);
        }
      }
    },

    // Function that is called when the page is loaded. It is used to load the trip
    // data from the database.
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
        this.showExpiredError = true;
        setTimeout(() => {
          UserService.logout();
          this.redirectLogout();
        }, 2000);
      } else {
        if (this.trip.status == 200) {
          this.trip = this.trip.data;
          this.date = this.trip.DATE;
          this.place = this.trip.PLACE;
          this.transportation = this.trip.TRANSPORTATION;
          this.myTripGeoJSON = JSON.parse(this.trip.GEOJSON);
          this.description = this.trip.DESCRIPTION;
          if (this.description != null) {
            this.showAddDescription = true;
          }
          this.getMainStagesAndPathCoordinate();
          this.dataLoaded = true;
        }
      }
    },
    // save new trip/ update trip, function called when the user click the save button
    saveTrip: async function () {
      let trip;
      let okResponse;
      let errorStatus = 0;
      if (this.checkInformations()) {
        if (this.idTrip == null) {
          // console.log(`id null? == ${this.idTrip}`);
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
          if (errorStatus != 0) {
            // this.showErrorNotification();
            this.showExpiredError = true;
            setTimeout(() => {
              UserService.logout();
              this.redirectLogout();
            }, 2000);
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

          if (errorStatus != 0) {
            this.showExpiredError = true;
            setTimeout(() => {
              UserService.logout();
              this.redirectLogout();
            }, 2000);
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
      }
    },

    // Redirecting the user to the dashboard page.
    redirectToDashboard: function () {
      this.$router.push({
        path: "/dashboard",
      });
    },

    // Redirecting the user to the login page.
    redirectLogout: function () {
      this.$router.push({
        path: "/login",
      });
    },

    // Getting the main stages and path coordinates from the GeoJSON file.
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

    updateStage() {
      this.stageNameKey += 1;
    },

    // Updating the trip coordinates.
    updateTripCoordinates(value, maxDistanceError) {
      this.myTripGeoJSON = value;
      this.maxDistanceError = maxDistanceError;
      this.getMainStagesAndPathCoordinate();
    },
  },
};
</script>

<style scoped>
.margin-button {
  margin-top: 20px;
}
h2 {
  color: #155263;
}
div.col-sm {
  margin-bottom: 10px;
}
</style>
