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
      >
        <l-tile-layer ref="layer" :url="url"></l-tile-layer>
      </l-map>
    </div>
    <!--
      DA VEDERE TODO
       <div class="row">
      <p v-html="geojsonText"></p>
    </div> -->
    <!-- <div>zoom: {{ zoom }}, center: {{ center }}</div> -->
  </div>
</template>

<script>
// import { mapGetters, mapActions } from "vuex";
import * as L from "leaflet";
// import LeafletDraw from "leaflet-draw";
// import "leaflet/dist/leaflet.css";
// import "leaflet-draw/dist/leaflet.draw.css";
// import LDrawToolbar from "vue2-leaflet-draw-toolbar";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.min.js";
import "@geoman-io/leaflet-geoman-free";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";
// import * as turf from "@turf/turf";

import "leaflet-geometryutil/src/leaflet.geometryutil.js";

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
  components: {},
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
      geoJsonText: "",
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
      // drawnItems.toGeoJSON().features[0].properties = {
      //   name: null,
      // };
      this.map.addLayer(drawnItems);
      let $this = this;

      // let guideLayers = new Array();

      // let drawControl = new L.Control.Draw();
      function onEachFeature(feature, layer) {
        drawnItems.addLayer(layer);
      }
      console.log(this.myTripGeoJSON);
      if (this.myTripGeoJSON == null || this.myTripGeoJSON === "undefined") {
        console.log("myTripGeoJSOn null or undefined");
      } else {
        if (Object.entries(this.myTripGeoJSON).length != 0) {
          // let mygeojson =
          L.geoJson(this.myTripGeoJSON, {
            onEachFeature: onEachFeature,
            color: "red",
          }).addTo(this.map);
        }
      }
      console.log("drawnItems.toGeoJSON()");
      console.log(drawnItems.toGeoJSON());
      // this.map.pm.Draw.setPathOptions({ custom: "prop" });

      this.map.pm.addControls({
        position: "topleft",
        custom: "prop",
        drawMarker: true,
        drawPolyline: {
          snapDistance: 5,
        },
        drawRectangle: false,
        drawPolygon: false,
        drawCircleMarker: false,
        drawCircle: false,
        drawText: false,
        rotateMode: false,
        dragMode: false,
        cutPolygon: false,
      });

      // this.map.on('click', function(e) {
      //   alert(e.latlng);
      // });

      let polyline = this.getPath(drawnItems);
      console.log(polyline);

      this.map.on("pm:create", function (e) {
        //({ layer }) =>
        console.log("Pm:create");
        console.log(e);
        let layer = e.layer;
        console.log(layer);

        let feature = (layer.feature = layer.feature || {});
        feature.type = "Feature";
        feature.properties = feature.properties || {};
        feature.properties["name"] = "";
        // feature.properties["color"] = null;

        console.log(layer);
        console.log(layer.pm._shape);

        if (layer.pm._shape === "Line") {
          console.log(drawnItems.toGeoJSON());
          drawnItems.addLayer(layer);
          let coordinates = drawnItems.toGeoJSON().features;
          polyline = coordinates.filter(
            (value) => value.geometry.type === "LineString"
          );
          console.log(polyline);
          if (polyline.length > 1) {
            //TODO SISTEMARE METTERE CHE VENGA SCRITTO BENE
            console.log("ERORRE: NON PUò ESSERCI PIU DI UN PERCORSO!!");
            alert("ERORRE: NON PUò ESSERCI PIU DI UN PERCORSO!!");
            drawnItems.removeLayer(layer);
          } else {
            $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
          }
        } else {
          // MARKER
          console.log(polyline);

          console.log("lat e long marker");
          console.log(layer.toGeoJSON());

          if (polyline != null && polyline.length > 0) {
            // PER CONTROLLARE SE MARKER è SU POLYLINE BISOGNA METTERE NEL PUNTO PRIMA LONGITUDINE!!!
            console.log("check if marker is on line");
            console.log(
              `layer._latlng.lng = ${layer._latlng.lng}, layer._latlng.lat = ${layer._latlng.lat}`
            );
            // let isOnLine = L.GeometryUtil.closest($this.map, polylineCoord, [
            //   layer._latlng.lat,
            //   layer._latlng.lng,
            // ]);
            // console.log(`isOnLine = ${isOnLine}`);
            console.log("MANDO COORDINATE A PARENT COMPONENT");
            drawnItems.addLayer(layer);
            $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
          } else {
            drawnItems.addLayer(layer);
            console.log(drawnItems.toGeoJSON());
            $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
          }
        }
      });

      drawnItems.on("pm:edit", function () {
        // e
        console.log("pm:edit on drawnItems");
        console.log(drawnItems.toGeoJSON());
        $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
      });
      drawnItems.on("pm:remove", (e) => {
        console.log("pm:remove");
        console.log(drawnItems.toGeoJSON());
        drawnItems.removeLayer(e.layer);
        $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
      });

      function showText(e) {
        // layers.clearLayers();
        let layer = e.layer;
        layer.addTo(drawnItems);
        let geojson = JSON.stringify(layer.toGeoJSON(), null, 4);
        this.geoJsonText = geojson.toString();
        console.log("geoJsonText");
        console.log(this.geoJsonText);
      }
      this.map.addEventListener("pm:create", showText);
      /**
       * [45.705951, 13.717831,
          [45.657991, 13.772789, 
          
          
          (2) [13.532437, 45.681495]
          [13.772789, 45.657991]*/

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
      // path = path[0].geometry.coordinates;
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
