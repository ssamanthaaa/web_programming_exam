<template>
  <div class="wrapper">
    <div class="container marginTop">
      <!-- <div > -->
      <h1 class="titles">My trips</h1>
      <div class="row justify-content-between">
        <div class="col-4">
          <h4>Filter trips by date</h4>
          <date-picker
            v-model="selectedDate"
            value-type="format"
            format="YYYY-MM-DD"
            type="date"
            range
            placeholder="Select date range"
            @change="filterByDates()"
          ></date-picker>
        </div>
        <div class="col-4">
          <router-link
            to="update-trip"
            style="color: white; text-decoration: none"
            ><button class="btn orange-color" style="float: right">
              Add trip
            </button>
          </router-link>
        </div>
        <div class="row"></div>
      </div>
      <!-- <h3>Hi {{ username }}!</h3> -->
      <div class="row">
        <div class="col map">
          <Map v-if="dataLoaded" :pathJson="pathJson" :key="pathJsonKey" />
        </div>
        <!-- <div class="col"><ModificableMap/></div> -->
      </div>

      <div class="row">
        <!-- <div class="heading"> -->
        <!-- </div> -->
        <table class="table tablesorter">
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
              <!-- getDate(value.DATE) -->
              <td>{{ value.PLACE }}</td>
              <td>{{ value.TRANSPORTATION }}</td>
              <td>
                <!-- <span v-for="(val, index) in value.GEOJSON.features"
                      v-bind:key="index">

                  </span> -->
                <ul style="list-style-type: none">
                  <li
                    v-for="(val, index) in value.GEOJSON.features"
                    v-bind:key="index"
                  >
                    <span v-if="val.geometry.type === 'Point'">
                      {{ val.properties.name }} ({{
                        val.geometry.coordinates[1]
                      }}, {{ val.geometry.coordinates[0] }})
                    </span>
                  </li>
                </ul>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <!-- </div> -->
    </div>
    <!-- <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <!-- <Footer /> -->
  </div>
</template>

<script>
// import moment from "moment";
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
      currentSort: "DATE",
      currentSortDir: "desc",
    };
  },

  // mounted: function () {},

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
      // console.log(`selectedDate: ${this.selectedDate}`);
      if (fromDate == null && toDate == null) {
        await this.getTrips();
      } else {
        await this.getTrips();
        // console.log(`fromDate = ${fromDate} - toDate ${toDate}`);
        let dateFromArray = this.tripList.filter(function (el) {
          return el.DATE >= fromDate;
        });
        // console.log(dateFromArray);
        let dateToArray = dateFromArray.filter(function (el) {
          return el.DATE <= toDate;
        });
        // console.log(dateToArray);
        this.dataLoaded = false;
        this.tripList = dateToArray;
        this.dataLoaded = true;
        this.pathJson = [];
        for (let i = 0; i < this.tripList.length; ++i) {
          if (this.tripList[i].GEOJSON != null) {
            // this.tripList[i].GEOJSON = JSON.parse(this.tripList[i].GEOJSON);
            // console.log(this.tripList[i].GEOJSON);
            // console.log("parse json:");
            this.pathJson.push(this.tripList[i].GEOJSON);
          }
        }
        this.$forceUpdate();
        this.pathJsonKey += 1;
        // console.log(`pathJsonKey update: ${this.pathJsonKey}`);
        // console.log("pathJson after being filtered");
        // console.log(this.pathJson);
      }
    },

    // getDate(datetime) {
    //   return moment(String(datetime)).format("DD/MM/YYYY");
    // },

    getTrips: async function () {
      await TripService.getAllTrips(this.idUser, this.token)
        .then((response) => {
          this.tripList = response.data;
        })
        .catch(function (error) {
          console.log(error);
          if (error == 401) {
            console.log(error.message);
            UserService.logout();
            // localStorage.removeItem("token");
            // localStorage.removeItem("id");
            // localStorage.removeItem("username");
            // localStorage.removeItem("email");
            // this.$router.push({
            //   path: "/",
            // });
          }
        });
      console.log(this.tripList);
      if (this.tripList != null) {
        for (let i = 0; i < this.tripList.length; ++i) {
          // console.log(this.tripList[i]);
          if (this.tripList[i].GEOJSON != null) {
            this.tripList[i].GEOJSON = JSON.parse(this.tripList[i].GEOJSON);
            // console.log(this.tripList[i].GEOJSON);
            // console.log("parse json:");
            this.pathJson.push(this.tripList[i].GEOJSON);
          }
        }
      }
      // console.log("stampo geojson.features");
      // for (let i = 0; i < this.tripList[0].GEOJSON.features.length; ++i) {
      //   let features = this.tripList[0].GEOJSON.features[i];
      //   console.log(features.properties.name);
      // }
      this.pathJsonKey += 1;
      // console.log(`pathJsonKey update: ${this.pathJsonKey}`);
      // console.log("stampo pathJson");
      // console.log(this.pathJson);
      this.dataLoaded = true;
    },

    sort: function (s) {
      //if s == current sort, reverse
      if (s === this.currentSort) {
        this.currentSortDir = this.currentSortDir === "asc" ? "desc" : "asc";
      }
      this.currentSort = s;
    },
  },
  computed: {
    sortedTrips: function () {
      if (this.tripList != undefined) {
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
.marginTop {
  margin-top: 100px;
}
h1,
h4 {
  color: #155263;
}
</style>
