<template>
  <div id="app">
    <div class="container my-margin">
      <h1 class="titles">My trips</h1>
      <div class="row justify-content-between">
        <div class="col-sm-4">
          <h4>Filter trips by date</h4>
          <date-picker
            v-model="selectedDate"
            value-type="format"
            format="YYYY-MM-DD"
            type="date"
            range
            placeholder="Select date range"
            @change="filterByDates()"
            class="width"
          ></date-picker>
        </div>
        <div class="col-sm-4">
          <router-link
            to="update-trip"
            style="color: white; text-decoration: none"
            ><button class="btn orange-color btn-lg w-100" style="float: right">
              Add trip
            </button>
          </router-link>
        </div>
        <div class="row"></div>
      </div>
      <div class="row">
        <div class="col map">
          <Map v-if="dataLoaded" :tripList="tripList" :key="pathJsonKey" />
        </div>
      </div>

      <div class="table-responsive">
        <table class="table table-responsive-sm table-responsive-md">
          <thead>
            <tr>
              <th></th>
              <th @click="sort('DATE')" style="cursor: pointer">
                Date<b-icon
                  icon="sort-alpha-up-alt"
                  style="margin-left: 5px"
                ></b-icon>
              </th>
              <th @click="sort('PLACE')" style="cursor: pointer">
                Place<b-icon
                  icon="sort-alpha-up-alt"
                  style="margin-left: 5px"
                ></b-icon>
              </th>
              <th @click="sort('TRANSPORTATION')" style="cursor: pointer">
                Transportation<b-icon
                  icon="sort-alpha-up-alt"
                  style="margin-left: 5px"
                ></b-icon>
              </th>
              <th>Main Stages</th>
            </tr>
          </thead>
          <tbody>
            <tr
              class="colored"
              v-for="value in sortedTrips"
              v-bind:key="value.ID"
            >
              <td>
                <router-link
                  :to="'update-trip?idTrip=' + value.ID"
                  style="color: #ff6f3c"
                  ><b-icon icon="pencil"></b-icon
                ></router-link>
              </td>
              <td>{{ value.DATE }}</td>
              <td>{{ value.PLACE }}</td>
              <td>{{ value.TRANSPORTATION }}</td>
              <td>
                <ul style="list-style-type: none">
                  <li
                    v-for="(val, index) in value.GEOJSON.features"
                    v-bind:key="index"
                  >
                    <span v-if="val.geometry.type === 'Point'">
                      {{ val.properties.name }}
                    </span>
                  </li>
                </ul>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
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
import Map from "@/components/Map.vue";
import TripService from "@/services/TripService.js";
import DatePicker from "vue2-datepicker";
import UserService from "@/services/UserService";

export default {
  name: "Dashboard-component",
  components: {
    Map,
    DatePicker,
  },
  data() {
    return {
      selectedDate: [
        new Date(),
        new Date(new Date().getTime() + 9 * 24 * 60 * 60 * 1000),
      ],
      idUser: null,
      username: null,
      tripList: null,
      pathJson: [],
      dataLoaded: false,
      pathJsonKey: 1,
      showExpiredError: false,
      showLoader: true,
      currentSort: "DATE",
      currentSortDir: "desc",
    };
  },

  mounted: async function () {
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
    await this.getTrips();
  },

  methods: {
    filterByDates: async function () {
      let fromDate = this.selectedDate[0];
      let toDate = this.selectedDate[1];
      if (fromDate == null && toDate == null) {
        await this.getTrips();
      } else {
        await this.getTrips();
        let dateFromArray = this.tripList.filter(function (el) {
          return el.DATE >= fromDate;
        });
        let dateToArray = dateFromArray.filter(function (el) {
          return el.DATE <= toDate;
        });
        this.dataLoaded = false;
        this.tripList = dateToArray;
        this.dataLoaded = true;
        this.$forceUpdate();
        this.pathJsonKey += 1;
      }
    },

    getTrips: async function () {
      let errorStatus = 0;
      let status;
      await TripService.getAllTrips(this.idUser, this.token)
        .then((response) => {
          // console.log(response);
          this.tripList = response.data;
          status = response.status;
          // console.log(this.tripList);
        })
        .catch(function (error) {
          errorStatus = error.response.status;
          console.log(error);
        });
      // console.log(`errorStatus: ${errorStatus}`);
      if (errorStatus != 0) {
        this.showExpiredError = true;
        setTimeout(() => {
          UserService.logout();
          this.redirectLogout();
        }, 2000);
      } else {
        if (status === 200) {
          // console.log(this.tripList);
          if (this.tripList != null) {
            for (let i = 0; i < this.tripList.length; ++i) {
              if (this.tripList[i].GEOJSON != null) {
                this.tripList[i].GEOJSON = JSON.parse(this.tripList[i].GEOJSON);
              }
            }
          }
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

    sort: function (s) {
      if (s === this.currentSort) {
        this.currentSortDir = this.currentSortDir === "asc" ? "desc" : "asc";
      }
      this.currentSort = s;
    },
  },
  computed: {
    sortedTrips: function () {
      if (this.tripList != undefined && this.tripList != null) {
        let tripListToSort = this.tripList;
        return tripListToSort.sort((a, b) => {
          let modifier = 1;
          if (this.currentSortDir === "desc") modifier = -1;
          if (a[this.currentSort] < b[this.currentSort]) return -1 * modifier;
          if (a[this.currentSort] > b[this.currentSort]) return 1 * modifier;
          return 0;
        });
      }
      return 0;
    },
  },
};
</script>

<style scoped>
.colored:hover {
  background-color: #ffc83c4b;
}

.map {
  margin-top: 20px;
  margin-bottom: 20px;
}
/* .marginTop {
  margin-top: 120px;
} */
h1,
h4 {
  color: #155263;
}
/* @media only screen and (max-width: 600px) {
  table {
    width: 50%;
  }
} */
div.col-sm-4 {
  margin-top: 10px;
}
.width {
  width: 100%;
}
</style>
