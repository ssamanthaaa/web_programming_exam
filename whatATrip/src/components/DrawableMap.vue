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
      url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
      map: null,
      drawnItems: null,
      zoom: 8,
      center: [45.859412, 13.601074],
      bounds: null,
      tileLayer: null,
      geoJsonText: null,
      distanceError: false,
      distance: null,
      editRouteAlert: false,
      showGeoJson: false,
    };
  },
  mounted() {
    this.drawnItems = this.initMap();
    this.$emit(
      "updateCoordinates",
      this.drawnItems.toGeoJSON(),
      this.maxDistanceError
    );
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

    // Initializing the map.
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

      // A function that is called on each feature in the GeoJSON file.
      function onEachFeature(feature, layer) {
        drawnItems.addLayer(layer);
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
      }

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
          showText(featureJson.getLayers());
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
              let line = turf.lineString(path[0].geometry.coordinates);

              let distance = turf.pointToLineDistance(point, line, {
                units: "meters",
              });
              if (distance > 200) {
                this.distance = distance.toFixed(2);
                this.distanceError = true;
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

      // The code below is listening for the pm:create event. This event is fired when a new layer is created.
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

      // Listening for the pm:enable event. This event is fired when the user clicks on the edit button.
      drawnItems.on("pm:enable", () => {
        this.editRouteAlert = true;
      });
      // Listening for the pm:disable event. This event is fired when the user clicks on the edit button.
      drawnItems.on("pm:disable", () => {
        this.editRouteAlert = false;
        this.distanceError = false;
      });

      /*
        The code is listening for the pm:edit event. This event is triggered when a user clicks
        on a marker or line. If the user clicks on a marker, the code will check if the marker is
        within 200 meters from the line. If it is, the marker will be added to the map. If it is not,
        the marker will not be added to the map and an error message will be displayed. If the user
        clicks on a line, the code will remove all markers from the map.
      */
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
            $this.map.pm.addControls({ drawMarker: true });
          } else {
            $this.distance = distance.toFixed(2);
            $this.distanceError = true;
            $this.map.pm.addControls({ drawMarker: false });
          }
        }
        $this.$emit(
          "updateCoordinates",
          drawnItems.toGeoJSON(),
          $this.distanceError
        );
      });

      /*
       The code is listening for the pm:snapdrag event fired when a user moves a marker in the map, 
       this code checks if the marker is within 200 meters of the polyline. If it is, then the marker 
       is added to the map, otherwise the marker is not added to the map.
      */
      // drawnItems.on("pm:snapdrag", function (e) {
      //   let layer = e.layer;
      //   if (layer.pm._shape === "Marker") {
      //     let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
      //     let line = turf.lineString(polyline);

      //     let distance = turf.pointToLineDistance(point, line, {
      //       units: "meters",
      //     });
      //     if (distance <= 200) {
      //       $this.distanceError = false;
      //       drawnItems.addLayer(layer);
      //       $this.$emit(
      //         "updateCoordinates",
      //         drawnItems.toGeoJSON(),
      //         $this.distanceError
      //       );
      //       $this.distance = distance.toFixed(2);
      //     } else {
      //       $this.distance = distance.toFixed(2);
      //       $this.distanceError = true;
      //     }
      //   }
      // });

      // Listening for the pm:globalremovalmodetoggled event and then setting the editRouteAlert to
      // true.
      this.map.on("pm:globalremovalmodetoggled", () => {
        $this.editRouteAlert = true;
      });

      /* 
        The code here is listening for the pm:remove event fired when the user removes a marker or
        a route from the map.
      */
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

      // Converting the drawnItems to a GeoJSON object and then converting it to a string.
      function showText() {
        let geojson = JSON.stringify(drawnItems.toGeoJSON(), null, 4);
        $this.geoJsonText = geojson.toString();
      }
      /*
        Adding an event listener to the map. The event listener is listening for the
        pm:create event. When the pm:create event is triggered, the showText function is called.
      */
      this.map.addEventListener("pm:create", showText);

      /*
        Adding an event listener to the drawnItems layer. The event listener is listening for the
        pm:edit event. When the pm:edit event is triggered, the showText function is called.
      */
      drawnItems.addEventListener("pm:edit", showText);

      return drawnItems;
    },

    // Emitting updated coordinates to parent (UpdateTrip.vue)
    updateCoordinates() {
      this.$emit(
        "updateCoordinates",
        this.drawnItems.toGeoJSON(),
        this.distanceError
      );
    },

    // Filtering the drawnItems object to get the coordinates of the path.
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
