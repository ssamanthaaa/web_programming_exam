<template>
  <div class="container">
    <div class="row justify-content-center" id="map">
      <!-- id="map"
      v-on:change="onMapUpdate()" -->
      <l-map
        ref="map"
        style="height: 450px; width: 100%"
        :zoom="zoom"
        :center="center"
        @update:zoom="zoomUpdated"
        @update:center="centerUpdated"
        @update:bounds="boundsUpdated"
      >
        <l-tile-layer ref="layer" :url="url"></l-tile-layer>
      </l-map>
    </div>
    <!-- {{ maxDistanceError }}: distance is {{ distance }} -->
    <div v-if="distanceError">
      <p class="error-marker">
        The maximum distance allowed between the stage and the route is 200 m.
        Your stage is {{ distance }}m from the route.
      </p>
    </div>
    <div v-if="editRouteAlert">
      <p class="alert-message">
        If you edit or remove your route you will lose all your stages.
      </p>
    </div>

    <div class="mb-3" v-if="geoJsonText != null">
      <div class="custom-control custom-checkbox checkbox-lg">
        <label class="form-label">
          <input
            type="checkbox"
            v-model="showGeoJson"
            class="custom-control-input"
          />
          Do you want to see the geoJson of your trip?</label
        >
      </div>
      <textarea
        v-if="showGeoJson"
        v-model="geoJsonText"
        class="form-control"
        rows="15"
        readonly
      ></textarea>
    </div>
    <!-- <div>zoom: {{ zoom }}, center: {{ center }}</div> -->
  </div>
</template>

<script>
import * as L from "leaflet";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.min.js";
import "@geoman-io/leaflet-geoman-free";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";

import * as turf from "@turf/turf";
import "leaflet-geometryutil/src/leaflet.geometryutil.js";

export default {
  props: {
    myTripGeoJSON: Object,
    maxDistanceError: Boolean,
  },

  components: {},
  data() {
    return {
      url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", //"http://{s}.tile.osm.org/{z}/{x}/{y}.png",
      map: null,
      drawnItems: null,
      zoom: 8,
      center: [45.859412, 13.601074], //45.65052568917208, 13.76517096104127
      bounds: null,
      tileLayer: null,
      // caller: null,
      geoJsonText: null,
      distanceError: false,
      distance: null,
      editRouteAlert: false,
      showGeoJson: false,
    };
  },
  mounted() {
    this.drawnItems = this.initMap();
    // this.$emit(
    //   "updateCoordinates",
    //   this.drawnItems.toGeoJSON(),
    //   this.maxDistanceError
    // );
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
      this.map.addLayer(drawnItems);
      let $this = this;

      this.map.pm.addControls({
        position: "topleft",
        drawMarker: false,
        drawPolyline: {
          snappable: true,
          snapDistance: 20,
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
      // L.control.ruler().addTo(this.map);
      function onEachFeature(feature, layer) {
        drawnItems.addLayer(layer);
        // if (feature.geometry.type === "Point") {
        // console.log(feature.geometry);
        layer.bindPopup(
          "<p>" +
            feature.properties.name +
            " (" +
            feature.geometry.coordinates[1] +
            ", " +
            feature.geometry.coordinates[0] +
            ")</p><p v-if='feature.properties.description'>" +
            feature.properties.description +
            "</p>"
        );
        // }
      }
      // console.log(this.myTripGeoJSON);
      if (this.myTripGeoJSON == null || this.myTripGeoJSON === "undefined") {
        // console.log("myTripGeoJSOn null or undefined");
      } else {
        if (Object.entries(this.myTripGeoJSON).length != 0) {
          let featureJson = L.geoJson(this.myTripGeoJSON, {
            onEachFeature: onEachFeature,
            color: "red",
          }).addTo(this.map);
          let path = this.myTripGeoJSON.features.filter(
            (value) => value.geometry.type === "LineString"
          );
          // console.log(featureJson.getLayers());
          // this.map.on("load", showText);
          showText(featureJson.getLayers());
          // console.log(path);
          if (path != null && path.length > 0) {
            this.map.pm.addControls({ drawPolyline: false });
            this.map.pm.addControls({ drawMarker: true });
          }
          for (let i = 0; i < this.myTripGeoJSON.features.length; ++i) {
            if (this.myTripGeoJSON.features[i].geometry.type === "Point") {
              let point = turf.point([
                this.myTripGeoJSON.features[i].geometry.coordinates[0],
                this.myTripGeoJSON.features[i].geometry.coordinates[1],
              ]);
              // console.log(point);
              // console.log(path[0]);
              let line = turf.lineString(path[0].geometry.coordinates);
              // console.log(line);

              let distance = turf.pointToLineDistance(point, line, {
                units: "meters",
              });
              if (distance > 200) {
                this.distance = distance.toFixed(2);
                this.distanceError = true;
                // console.log(`distanceError = ${this.distanceError}`);
                this.$emit(
                  "updateCoordinates",
                  drawnItems.toGeoJSON(),
                  this.distanceError
                );
              } else {
                this.distanceError = false;
              }
              this.map.fitBounds(featureJson.getBounds());
              break;
            }
          }
        }
      }

      let polyline = this.getPath(drawnItems);

      this.map.on("pm:create", function (e) {
        let layer = e.layer;

        let feature = (layer.feature = layer.feature || {});
        feature.type = "Feature";
        feature.properties = feature.properties || {};
        feature.properties["name"] = "Stage";
        feature.properties["description"] = null;
        polyline = $this.getPath(drawnItems);
        if (layer.pm._shape === "Line") {
          feature.properties["name"] = "Path";
          drawnItems.addLayer(layer);
          $this.map.pm.addControls({ drawPolyline: false });
          $this.map.pm.addControls({ drawMarker: true });

          $this.$emit(
            "updateCoordinates",
            drawnItems.toGeoJSON(),
            $this.maxDistanceError
          );
        } else {
          let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
          let line = turf.lineString(polyline);

          let distance = turf.pointToLineDistance(point, line, {
            units: "meters",
          });
          if (distance <= 200) {
            $this.distanceError = false;
            drawnItems.addLayer(layer);
            $this.$emit(
              "updateCoordinates",
              drawnItems.toGeoJSON(),
              $this.distanceError
            );
            $this.distance = distance.toFixed(2);
          } else {
            $this.distance = distance.toFixed(2);
            $this.distanceError = true;
            $this.map.removeLayer(layer);
          }
        }
      });

      drawnItems.on("pm:enable", () => {
        $this.editRouteAlert = true;
      });
      drawnItems.on("pm:disable", () => {
        $this.editRouteAlert = false;
        this.distanceError = false;
      });

      drawnItems.on("pm:edit", function (e) {
        let layer = e.layer;
        $this.editRouteAlert = false;
        if (layer.pm._shape === "Line") {
          $this.map.eachLayer(function (subLayer) {
            if (subLayer instanceof L.Marker) {
              drawnItems.removeLayer(subLayer);
            }
          });
        } else {
          let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
          let line = turf.lineString(polyline);

          let distance = turf.pointToLineDistance(point, line, {
            units: "meters",
          });
          if (distance <= 200) {
            $this.distanceError = false;
            drawnItems.addLayer(layer);
            $this.$emit(
              "updateCoordinates",
              drawnItems.toGeoJSON(),
              $this.distanceError
            );
            $this.distance = distance.toFixed(2);
          } else {
            $this.distance = distance.toFixed(2);
            $this.distanceError = true;
          }
        }
        $this.$emit(
          "updateCoordinates",
          drawnItems.toGeoJSON(),
          $this.distanceError
        );
      });
      drawnItems.on("pm:snapdrag", function (e) {
        let layer = e.layer;
        if (layer.pm._shape === "Marker") {
          let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
          let line = turf.lineString(polyline);

          let distance = turf.pointToLineDistance(point, line, {
            units: "meters",
          });
          if (distance <= 200) {
            $this.distanceError = false;
            drawnItems.addLayer(layer);
            $this.$emit(
              "updateCoordinates",
              drawnItems.toGeoJSON(),
              $this.distanceError
            );
            $this.distance = distance.toFixed(2);
          } else {
            $this.distance = distance.toFixed(2);
            $this.distanceError = true;
            // $this.map.removeLayer(layer);
          }
        }
      });

      this.map.on("pm:globalremovalmodetoggled", () => {
        $this.editRouteAlert = true;
      });

      this.map.on("pm:remove", (e) => {
        $this.editRouteAlert = false;
        let layer = e.layer;
        if (layer.pm._shape === "Line") {
          $this.map.pm.addControls({ drawPolyline: true });
          $this.map.pm.addControls({ drawMarker: false });
          $this.distanceError = false;
          drawnItems.eachLayer(function (subLayer) {
            if (subLayer instanceof L.Marker) {
              drawnItems.removeLayer(subLayer);
            }
          });
        }
        drawnItems.removeLayer(e.layer);
        $this.$emit(
          "updateCoordinates",
          drawnItems.toGeoJSON(),
          $this.distanceError
        );
        showText(e);
      });

      function showText() {
        let geojson = JSON.stringify(drawnItems.toGeoJSON(), null, 4);
        $this.geoJsonText = geojson.toString();
      }
      this.map.addEventListener("pm:create", showText);
      drawnItems.addEventListener("pm:edit", showText);

      return drawnItems;
    },

    // emit updated coordinates to parent (UpdateTrip.vue)
    updateCoordinates() {
      this.$emit(
        "updateCoordinates",
        this.drawnItems.toGeoJSON(),
        this.distanceError
      );
    },

    getPath(drawnItems) {
      let features = drawnItems.toGeoJSON().features;
      if (features.length > 0) {
        let path = features.filter(
          (value) => value.geometry.type === "LineString"
        );
        return path[0].geometry.coordinates;
      } else {
        return null;
      }
    },
  },
};
</script>
<style scoped>
#map {
  height: 500px;
  margin-bottom: 10px;
}
</style>
