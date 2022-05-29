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
    <!-- {{ maxDistanceError }}: distance is {{ distance }} -->
    <div v-if="maxDistanceError">
      <p class="error-marker">
        The maximum distance allowed between the stage and the route is 100 m.
        Your stage is {{ distance }}m from the route.
      </p>
    </div>
    <div v-if="editRouteAlert">
      <p class="alert-message">
        If you edit your route you will lose all your stages.
      </p>
    </div>
    <div v-if="maxSegmentLengthError">
      <p class="error-marker">
        The maximum distance allowed between the point in the route is 200m.
        Your distance is {{ distanceSegment }}m from the route.
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
      <!-- <pre v-html="geoJsonText"></pre> -->
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
// import "leaflet-geometryutil/src/leaflet.geometryutil.js";

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
      zoom: 8,
      center: [45.859412, 13.601074], //45.65052568917208, 13.76517096104127
      bounds: null,
      tileLayer: null,
      // caller: null,
      geoJsonText: null,
      // geojson: null,
      // myTripjson: null,
      // mainStages: [],
      // pathCoordinates: [],
      maxDistanceError: false,
      distance: null,
      editRouteAlert: false,
      maxSegmentLengthError: false,
      distanceSegment: null,
      showGeoJson: false,
    };
  },
  mounted() {
    this.drawnItems = this.initMap();
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
      // console.log(drawnItems);
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

      function onEachFeature(feature, layer) {
        drawnItems.addLayer(layer);
        layer.bindPopup(
          "<p>" +
            feature.properties.name +
            " (" +
            feature.geometry.coordinates[1] +
            ", " +
            feature.geometry.coordinates[0] +
            ")</p>"
        );
      }
      console.log(this.myTripGeoJSON);
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
          console.log(path);
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

      this.map.pm.setGlobalOptions({
        measurements: {
          measurement: true,
          displayFormat: "metric",
          segmentLength: true,
        },
      });

      let polyline = this.getPath(drawnItems);
      let maxDistance = 100; //maximum distance between point and path

      this.map.on("pm:create", function (e) {
        //({ layer }) =>
        console.log("Pm:create");
        let layer = e.layer;

        let feature = (layer.feature = layer.feature || {});
        feature.type = "Feature";
        feature.properties = feature.properties || {};
        feature.properties["name"] = "";

        if (layer.pm._shape === "Line") {
          feature.properties["name"] = "Path";
          drawnItems.addLayer(layer);
          $this.map.pm.addControls({ drawPolyline: false });
          $this.map.pm.addControls({ drawMarker: true });

          polyline = $this.getPath(drawnItems);
          console.log(polyline);
          $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
        } else {
          // MARKER
          if (polyline != null && polyline.length > 0) {
            console.log("check if marker is on line");

            let point = turf.point([layer._latlng.lng, layer._latlng.lat]);
            let line = turf.lineString(polyline[0].geometry.coordinates);

            let distance = turf.pointToLineDistance(point, line, {
              units: "meters",
            });
            console.log(`The distance is: ${distance}`);
            if (distance <= maxDistance) {
              $this.maxDistanceError = false;
              console.log("distance OK");
              drawnItems.addLayer(layer);
              $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
              $this.distance = distance.toFixed(2);
            } else {
              //messaggio errore entro i 150 m
              $this.distance = distance.toFixed(2);
              $this.maxDistanceError = true;
            }
          } else {
            drawnItems.addLayer(layer);
            console.log(drawnItems.toGeoJSON());
            $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
          }
        }
      });

      drawnItems.on("pm:enable", (e) => {
        console.log("enable");
        console.log(e);
        if (e.source === "Edit") {
          $this.editRouteAlert = true;
        }
      });
      drawnItems.on("pm:disable", () => {
        console.log("enable");
        $this.editRouteAlert = false;
      });

      this.map.on("pm:drawstart", ({ workingLayer }) => {
        console.log(workingLayer);
        let previousPoint;
        workingLayer.on("pm:vertexadded", (e) => {
          // console.log(e);
          let shape = e.shape;
          if (shape === "Line") {
            if (previousPoint) {
              let distance = previousPoint.distanceTo(e.latlng);
              if (distance > 200) {
                $this.distanceSegment = distance.toFixed(2);
                $this.maxSegmentLengthError = true;
                // alert(
                //   "the route between two vertices cannot be long more than 200 m, distance: " +
                //     distance
                // );
                $this.map.pm.Draw.Line._removeLastVertex();
              } else {
                $this.maxSegmentLengthError = false;
                console.log(distance.toFixed(2));
                previousPoint = e.latlng;
              }
            } else {
              previousPoint = e.latlng;
            }
          }
        });
      });

      drawnItems.on("pm:edit", function (e) {
        console.log("edit");
        let layer = e.layer;
        $this.editRouteAlert = false;
        //SE EDIT POLYLINE SHOW MESSAGE THAT THE MARKER WILL BE LOST
        console.log(drawnItems);
        if (layer.pm._shape === "Line") {
          $this.map.eachLayer(function (layer) {
            if (layer instanceof L.Marker) {
              console.log("remove marker layer");
              drawnItems.removeLayer(layer);
            }
          });
        }
        $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
      });

      drawnItems.on("pm:remove", (e) => {
        let layer = e.layer;
        if (layer.pm._shape === "Line") {
          $this.map.pm.addControls({ drawPolyline: true });
          $this.map.pm.addControls({ drawMarker: false });
        }
        drawnItems.removeLayer(e.layer);
        $this.$emit("updateCoordinates", drawnItems.toGeoJSON());
      });

      function showText(e) {
        let layer = e.layer;
        layer.addTo(drawnItems);
        let geojson = JSON.stringify(drawnItems.toGeoJSON(), null, 4);
        $this.geoJsonText = geojson.toString();
        console.log("geoJsonText");
        console.log($this.geoJsonText);
        // return geojson.toString();
      }
      this.map.addEventListener("pm:create", showText);
      drawnItems.addEventListener("pm:edit", showText);
      drawnItems.addEventListener("pm:remove", showText);

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
      this.$emit("updateCoordinates", this.drawnItems.toGeoJSON());
    },

    // TODO forse non mi serve
    getPath(drawnItems) {
      let features = drawnItems.toGeoJSON().features;
      let path = features.filter(
        (value) => value.geometry.type === "LineString"
      );
      return path;
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
