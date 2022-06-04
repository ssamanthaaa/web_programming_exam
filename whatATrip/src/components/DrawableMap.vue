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
              this.map.fitBounds(featureJson.getBounds());
              break;
            }
          }
        }
      }

      let polyline = this.getPath(drawnItems);
      // if (polyline != null && polyline != undefined) {
      //   polyline = polyline[0].geometry.coordinates;
      // }
      // let maxDistance = 200; //maximum distance between stage and path

      this.map.on("pm:create", function (e) {
        //({ layer }) =>
        // console.log("Pm:create");
        let layer = e.layer;

        let feature = (layer.feature = layer.feature || {});
        feature.type = "Feature";
        feature.properties = feature.properties || {};
        feature.properties["name"] = "Stage";
        feature.properties["description"] = "";
        polyline = $this.getPath(drawnItems);
        if (layer.pm._shape === "Line") {
          feature.properties["name"] = "Path";
          drawnItems.addLayer(layer);
          // polyline = $this.getPath(drawnItems);
          $this.map.pm.addControls({ drawPolyline: false });
          $this.map.pm.addControls({ drawMarker: true });

          // console.log(polyline);
          $this.$emit(
            "updateCoordinates",
            drawnItems.toGeoJSON(),
            $this.maxDistanceError
          );
        } else {
          // MARKER
          // polyline = polyline[0].geometry.coordinates;
          // console.log(polyline);
          // console.log(layer._latlng);
          // if (polyline != null && polyline.length > 0) {
          // console.log("check if marker is on line");

          let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
          // console.log(point);
          let line = turf.lineString(polyline);
          // console.log(line);
          // console.log(turf.booleanPointOnLine(point, line));

          let distance = turf.pointToLineDistance(point, line, {
            units: "meters",
          });
          // console.log(`The distanceMeter is: ${distance}`);
          if (distance <= 200) {
            $this.distanceError = false;
            // console.log("distance OK");
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
        // (e)
        // console.log("enable");
        // console.log(e);
        // if (e.source === "Edit") {
        $this.editRouteAlert = true;
        // }
      });
      drawnItems.on("pm:disable", () => {
        // console.log("disable");
        $this.editRouteAlert = false;
      });

      // this.map.on("pm:drawstart", ({ workingLayer }) => {
      //   console.log("drawstart");
      //   console.log(workingLayer);
      //   workingLayer.on("pm:vertexadded", (e) => {
      //     console.log(e);
      //     console.log("added vertex");
      //   });
      // });

      drawnItems.on("pm:edit", function (e) {
        // console.log("edit");
        let layer = e.layer;
        $this.editRouteAlert = false;
        //SE EDIT POLYLINE SHOW MESSAGE THAT THE MARKER WILL BE LOST
        // console.log(drawnItems);
        if (layer.pm._shape === "Line") {
          $this.map.eachLayer(function (subLayer) {
            if (subLayer instanceof L.Marker) {
              // console.log("remove marker layer");
              drawnItems.removeLayer(subLayer);
            }
          });
        } else {
          // console.log(layer._latlng);
          let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
          // console.log(point);
          // polyline = polyline[0].geometry.coordinates;
          let line = turf.lineString(polyline);
          // console.log(line);
          // console.log(turf.booleanPointOnLine(point, line));

          let distance = turf.pointToLineDistance(point, line, {
            units: "meters",
          });
          // console.log(`The distanceMeter is: ${distance}`);
          if (distance <= 200) {
            $this.distanceError = false;
            // console.log("distance OK");
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
        $this.$emit(
          "updateCoordinates",
          drawnItems.toGeoJSON(),
          $this.distanceError
        );
      });
      drawnItems.on("pm:snapdrag", function (e) {
        // console.log("pm:snapdrag");
        // console.log(e);
        let layer = e.layer;
        if (layer.pm._shape === "Marker") {
          // console.log(layer._latlng);
          let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
          // console.log(point);
          // console.log(polyline);
          // polyline = polyline[0].geometry.coordinates;
          // console.log(polyline);
          let line = turf.lineString(polyline);
          // console.log(line);
          // console.log(turf.booleanPointOnLine(point, line));

          let distance = turf.pointToLineDistance(point, line, {
            units: "meters",
          });
          // console.log(`The distanceMeter is: ${distance}`);
          if (distance <= 200) {
            $this.distanceError = false;
            // console.log("distance OK");
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
        // console.log(e);
        // console.log("globalremovalmodetoggled");
        $this.editRouteAlert = true;
      });

      // this.map.pm.globalRemovalEnabled()
      this.map.on("pm:remove", (e) => {
        $this.editRouteAlert = false;
        // console.log("remove");
        let layer = e.layer;
        if (layer.pm._shape === "Line") {
          $this.map.pm.addControls({ drawPolyline: true });
          $this.map.pm.addControls({ drawMarker: false });
          $this.distanceError = false;
          drawnItems.eachLayer(function (subLayer) {
            if (subLayer instanceof L.Marker) {
              // console.log("remove marker layer");
              drawnItems.removeLayer(subLayer);
            }
          });
        }
        drawnItems.removeLayer(e.layer);
        // console.log("layer removed");
        // console.log(drawnItems.toGeoJSON());
        $this.$emit(
          "updateCoordinates",
          drawnItems.toGeoJSON(),
          $this.distanceError
        );
        showText(e);
      });

      function showText() {
        // let layer = e.layer;
        // layer.addTo(drawnItems);
        let geojson = JSON.stringify(drawnItems.toGeoJSON(), null, 4);
        $this.geoJsonText = geojson.toString();
      }
      this.map.addEventListener("pm:create", showText);
      drawnItems.addEventListener("pm:edit", showText);

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

    // emit updated coordinates to parent (UodateTrip.vue)
    updateCoordinates() {
      this.$emit(
        "updateCoordinates",
        this.drawnItems.toGeoJSON(),
        this.distanceError
      );
    },

    getPath(drawnItems) {
      let features = drawnItems.toGeoJSON().features;
      // console.log(features);
      if (features.length > 0) {
        let path = features.filter(
          (value) => value.geometry.type === "LineString"
        );
        //polyline = polyline[0].geometry.coordinates;
        return path[0].geometry.coordinates;
      } else {
        return null;
      }
    },

    //TODO NON USATA
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
};
</script>
<style scoped>
#map {
  height: 500px;
  margin-bottom: 10px;
}
</style>
