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
            ><button class="btn orange-color w-100" style="float: right">
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

      <div>
        <!-- class="table-responsive" class="table"-->
        <table role="table" :class="windowWidth > 760 ? 'table' : ''">
          <thead role="rowgroup">
            <tr role="row">
              <th role="columnheader"></th>
              <th
                @click="sort('DATE')"
                role="columnheader"
                style="cursor: pointer"
              >
                Date<b-icon
                  icon="sort-alpha-up-alt"
                  style="margin-left: 5px"
                ></b-icon>
              </th>
              <th
                @click="sort('PLACE')"
                role="columnheader"
                style="cursor: pointer"
              >
                Place<b-icon
                  icon="sort-alpha-up-alt"
                  style="margin-left: 5px"
                ></b-icon>
              </th>
              <th
                @click="sort('TRANSPORTATION')"
                role="columnheader"
                style="cursor: pointer"
              >
                Transportation<b-icon
                  icon="sort-alpha-up-alt"
                  style="margin-left: 5px"
                ></b-icon>
              </th>
              <th role="columnheader">Main Stages</th>
            </tr>
          </thead>
          <tbody role="rowgroup">
            <tr
              class="colored"
              v-for="value in sortedTrips"
              v-bind:key="value.ID"
              role="row"
            >
              <td role="cell">
                <router-link
                  :to="'update-trip?idTrip=' + value.ID"
                  style="color: #ff6f3c"
                  ><b-icon icon="pencil"></b-icon
                ></router-link>
              </td>
              <td role="cell">{{ value.DATE }}</td>
              <td role="cell">{{ value.PLACE }}</td>
              <td role="cell">{{ value.TRANSPORTATION }}</td>
              <td role="cell">
                <span
                  v-for="(val, index) in value.GEOJSON.features"
                  v-bind:key="index"
                >
                  <span v-if="val.geometry.type === 'Point'">
                    {{ val.properties.name }}
                  </span>
                </span>
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
      windowWidth: window.screen.width,
    };
  },

  mounted: async function () {
    this.idUser = localStorage.getItem("id");
    this.username = localStorage.getItem("username");
    this.token = localStorage.getItem("token");
    await this.getTrips();
    this.$nextTick(() => {
      window.addEventListener("resize", this.onResize);
    });
  },

  methods: {
    // Function that filters the trips by date.
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

    // This function is used to get all the trips of the user.
    getTrips: async function () {
      let errorStatus = 0;
      let status;
      await TripService.getAllTrips(this.idUser, this.token)
        .then((response) => {
          this.tripList = response.data;
          status = response.status;
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
        if (status === 200) {
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

    // A function that is called when the window is resized. It is used to change the table style when the
    // window is resized.
    onResize() {
      this.windowWidth = window.screen.width;
    },

    redirectLogout: function () {
      this.$router.push({
        path: "/login",
      });
    },

    // A function that is used to sort the table.
    sort: function (s) {
      if (s === this.currentSort) {
        this.currentSortDir = this.currentSortDir === "asc" ? "desc" : "asc";
      }
      this.currentSort = s;
    },
  },
  computed: {
    // A computed property that is used to sort the table.
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

/*
	Max width before this PARTICULAR table gets nasty. This query will take effect for any screen smaller than 760px and also iPads specifically.
	*/
@media only screen and (max-width: 760px) {
  /* (min-device-width: 768px) and (max-device-width: 1024px) */
  /* Force table to not be like tables anymore */
  table,
  thead,
  tbody,
  th,
  td,
  tr {
    display: block;
  }
  /* Hide table headers (but not display: none;, for accessibility) */
  thead tr {
    position: absolute;
    top: -9999px;
    left: -9999px;
  }

  table {
    color: #212529;
    vertical-align: top;
    border-color: #dee2e6;
  }

  tr {
    margin: 0 0 1rem 0;
  }

  tr:nth-child(odd) {
    background: #ffc83c4b;
  }

  td {
    /* Behave  like a "row" */
    border: none;
    border-bottom: 1px solid #eee;
    position: relative;
    padding-left: 50%;
  }

  td:before {
    /* Now like a table header */
    position: absolute;
    /* Top/left values mimic padding */
    top: 0;
    left: 6px;
    width: 45%;
    padding-right: 15px;
    white-space: nowrap;
    /* text-align: left;
    margin-left: 5%;
    margin-right: 15%; */
  }

  /*
		Label the data
    You could also use a data-* attribute and content for this. That way "bloats" the HTML, this way means you need to keep HTML and CSS in sync. Lea Verou has a clever way to handle with text-shadow.
		*/
  td:nth-of-type(1):before {
    content: "";
  }
  td:nth-of-type(2):before {
    content: "Date";
  }
  td:nth-of-type(3):before {
    content: "Place";
  }
  td:nth-of-type(4):before {
    content: "Transportation";
  }
  td:nth-of-type(5):before {
    content: "Main Stages";
  }
}
</style>
