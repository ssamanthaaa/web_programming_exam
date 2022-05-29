<template>
  <div class="container">
    <div class="row justify-content-center" id="map">
      <!-- id="map"
      v-on:change="onMapUpdate()" -->
      <l-map
        ref="map"
        style="height: 500px; width: 100%"
        :zoom="zoom"
        :center="center"
        @update:zoom="zoomUpdated"
        @update:center="centerUpdated"
        @update:bounds="boundsUpdated"
        ><!-- v-on:click="emitToParent" -->
        <l-tile-layer ref="layer" :url="url"></l-tile-layer>
        <!-- <l-feature-group ref="features"> -->
        <!--  <l-popup>
            <span> Yay I was opened by {{ caller }}</span></l-popup
          >
          <l-marker
            v-for="(value, index) in mainStages"
            :key="index"
            :lat-lng="value"
          >
          </l-marker>
          <l-polyline :lat-lngs="pathCoordinates" color="green" />-->
        <!-- </l-feature-group>   -->
      </l-map>
    </div>
    <!-- <div v-if="drawnItems != null">
      {{drawnItems.toGeoJSON()}}
    </div>
    <button @click="salva()">Salva</button> -->
    <div>zoom: {{ zoom }}, center: {{ center }}</div>
  </div>
</template>

<script>
// import { mapGetters, mapActions } from "vuex";
import * as L from "leaflet";
// import LeafletDraw from "leaflet-draw";
import "leaflet/dist/leaflet.css";
import "leaflet-draw/dist/leaflet.draw.css";
// import LDrawToolbar from "vue2-leaflet-draw-toolbar";
// import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.min.js";
// import "@geoman-io/leaflet-geoman-free";
// import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";

import "leaflet-geometryutil/src/leaflet.geometryutil.js";
// import "@/assets/js/leaflet.snap.js";
// import {
//   LMap,
//   LTileLayer,
//   LMarker,
// LPopup,
// LFeatureGroup,
// LPolyline,
//   // LControlLayers,
// LGeoJson,
// } from "vue2-leaflet";

export default {
  props: {
    myTripGeoJSON: Object,
  },
  components: {
    // LMap,
    // LTileLayer,
    // L,
    // LMarker,
    // LPopup,
    // LFeatureGroup,
    // LPolyline,
    // LeafletDraw,
    // LDrawToolbar,
    // LControlLayers,
    // LGeoJson,
    // "v-geo-json": LGeoJson,
  },
  data() {
    return {
      url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", //"http://{s}.tile.osm.org/{z}/{x}/{y}.png",
      map: null,
      drawnItems: null,
      zoom: 10,
      center: [45.859412, 13.601074], //45.65052568917208, 13.76517096104127
      bounds: null,
      tileLayer: null,
      // newMarker: null,
      caller: null,
      // geojson: null,
      // myTripjson: null,
      // mainStages: [],
      // pathCoordinates: [],
    };
  },
  mounted() {
    this.drawnItems = this.initMap();
    console.log("SONO IN MOUNTED");
    this.$emit("update_myTripGeoJSON", this.drawnItems.toGeoJSON());
  },

  methods: {
    zoomUpdated(zoom) {
      this.zoom = zoom;
    },
    centerUpdated(center) {
      this.center = center;
    },
    boundsUpdated(bounds) {
      this.bounds = bounds;
    },
    // openPopUp(latLng, caller) {
    //   this.caller = caller;
    //   console.log("open");
    // },

    initMap() {
      this.map = this.$refs.map.mapObject;
      // FeatureGroup is to store editable layers
      let drawnItems = new L.FeatureGroup().addTo(this.map);
      console.log(drawnItems);
      this.map.addLayer(drawnItems);

      let $this = this;

      // let guideLayers = new Array();

      let drawControl = new L.Control.Draw();
      function onEachFeature(feature, layer) {
        drawnItems.addLayer(layer);
      }
      console.log(this.myTripGeoJSON);
      if (this.myTripGeoJSON == null || this.myTripGeoJSON === "undefined") {
        console.log("myTripGeoJSOn null or undefined");
      } else {
        if (Object.entries(this.myTripGeoJSON).length != 0) {
          //   let mygeojson =
          L.geoJson(this.myTripGeoJSON, {
            onEachFeature: onEachFeature,
            color: "red",
          }).addTo(this.map);
          // let guideLayers = [mygeojson];
        }
      }
      //   let guideLayers = new Array();
      let guides = [this.myTripGeoJSON]; //this.getPath(drawnItems);
      // let mainStages = this.getMainStages(drawnItems);

      drawControl.initialize({
        position: "topleft",
        draw: {
          polyline: {
            allowIntersection: true,
            shapeOptions: { color: "#f00707" },
            guideLayers: guides,
          },
          polygon: false,
          rectangle: false,
          circle: false,
          circlemarker: false,
          marker: { guideLayers: guides },
          // {
          // unsnapping: false,
          //   snappable: true
          // },
        },
        edit: {
          featureGroup: drawnItems,
          edit: true,
        },
      });
      this.map.addControl(drawControl);
      console.log(drawControl);

      // for (let i=0; i < mainStages; ++i) {
      //   mainStages[i].snapediting = new L.Handler.MarkerSnap(this.map,  mainStages[i]);
      //    mainStages[i].snapediting.addGuideLayer(guides);
      //    mainStages[i].snapediting.enable();
      // }
      // var guideLayers = guides;
      // drawControl.setDrawingOptions({
      //     polyline: { guideLayers: guideLayers },
      //     marker: { guideLayers: guideLayers, snapVertices: false },
      //     rectangle: false,
      //     circle: false
      // });

      // this.map.on('click', function(e) {
      //   alert(e.latlng);
      // });

      this.map.on("draw:created", function (e) {
        console.log("draw:created");
        let type = e.layerType,
          layer = e.layer;
        if (type === "marker") {
          layer.bindPopup("Call Point!");
          console.log("CREATED MARKER");
        } else if (type === "polyline") {
          console.log("CREATED POLYLINE");
          layer.editing.enable();
          // Activate snapping - does not work ... :(
          layer.snapediting = new L.Handler.PolylineSnap($this.map, layer);
          layer.snapediting.addGuideLayer(guides);
          layer.snapediting.enable();
        }
        drawnItems.addLayer(layer);
        // let shapes = getShapes(drawnItems);
        console.log("drawnitems in draw created");
        console.log(drawnItems.toGeoJSON());
        console.log(drawnItems);
        // updateGeoJSON(drawnItems);
        $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
      });

      // after the user finish editing this function is called
      //TODO ALMENO CHE NON DEVO FARE QUALCOSA DI PRECISO NON SERVE PERHCE drawnItems si aggiorna già da solo!
      this.map.on("draw:edited", function () {
        // e
        console.log("draw:edited");
        // let layers = e.layers;
        // console.log(layers);
        // layers.eachLayer(function (layer) {
        //   console.log(layer._latlngs);
        // });
        console.log(drawnItems.toGeoJSON());
        $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
      });

      // draw:drawstart viene chiamato come si prende un oggetto (inutile?)
      /*this.map.on("draw:drawstart", function (e) {
        console.log("draw:drawstart");
        console.log(e);
      });*/

      //TODO NON PENSO MI SERVA
      /*
      let getShapes = function (drawnItems) {
        let shapes = [];
        shapes["polyline"] = [];
        shapes["marker"] = [];
        drawnItems.eachLayer(function (layer) {
          if (layer instanceof L.Polyline) {
            shapes["polyline"].push(layer.getLatLngs());
          }
          if (layer instanceof L.Marker) {
            shapes["marker"].push([layer.getLatLng()]);
          }
        });
        return shapes;
      };
      */
      return drawnItems;
    },

    updateCoordinates(event) {
      console.log("called emitToParent");
      console.log(event);
      this.$emit("updateCoordinates", this.drawnItems.toGeoJSON());
    },

    getPath(drawnItems) {
      let features = drawnItems.toGeoJSON().features;
      let path = features.filter(
        (value) => value.geometry.type === "LineString"
      );
      path = path[0].geometry.coordinates;
      console.log("path");
      console.log(path);
      return path;
    },
    getMainStages(drawnItems) {
      let features = drawnItems.toGeoJSON().features;
      let points = features.filter((value) => value.geometry.type === "Point");
      console.log("points");
      console.log(points);
      let mainStages = [];
      for (let i = 0; i < points.length; ++i) {
        mainStages.push([
          points[i].geometry.coordinates[1],
          points[i].geometry.coordinates[0],
        ]);
      }
      console.log(mainStages);
      // path = path[0].geometry.coordinates;
      // console.log("path");
      // console.log(path);
      // return path;
    },
  },
  // watch: {
  //   drawnItems: function() {
  //     console.log("drawn items changed");
  //     this.$emit('updateCoordinates', this.drawnItems.toGeoJSON());
  //   }
  // }
  // computed: {
  //   updateCoordinates() {
  //     if (this.drawnItems != null)
  //       this.myTripGeoJSON = this.drawnItems.toGeoJSON();
  //     // return this.$emit("myTripGeoJSON", this.drawnItems.toGeoJSON());
  //   }
  // },
  // watch: {
  //   updateCoordinates(newValue) {
  //     console.log(`yes, computed property changed: ${newValue}`); // <--- INFINITES MESSAGES
  //     this.$emit('update:myTripGeoJson',  this.myTripGeoJSON);
  //   }
  // }
  // computed: {
  //   ...mapGetters(["markersList", "polylineList", "singleTrip"]),
  // },
  // options() {
  //   return {
  //     onEachFeature: this.onEachFeatureFunction,
  //   };
  // },
};
</script>
<style scoped>
#map {
  height: 500px;
}
</style>
