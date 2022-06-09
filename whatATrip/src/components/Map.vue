<template>
  <!-- <section> -->
  <div>
    <!-- <div class="heading">
                <h2>Map</h2>
            </div> -->
    <div class="constainer justify-content-center">
      <l-map
        style="height: 470px; width: 100%"
        :zoom="zoom"
        :center="center"
        ref="map"
      >
        <l-tile-layer :url="url" :attribution="attribution"></l-tile-layer>
        <!-- <l-marker
            v-for="(marker, index) in markers"
            :key="index"
            :lat-lng.sync="marker.position"
            :icon="marker.icon"
            @click="alert(marker.tooltip)"
            draggable="false"
          ></l-marker
          > -->
        <!-- :lat-lng="markerLatLng" -->
      </l-map>
      <!-- <l-polyline
          v-for="(value, index) in allPolylines"
          :key="index"
          :lat-lngs="value"
          :icon="marker.icon"
        ></l-polyline> -->
    </div>
  </div>
  <!-- </section> -->
</template>

<script>
import * as L from "leaflet";
import { LMap, LTileLayer /*LMarker*/ } from "vue2-leaflet";
import { latLngBounds } from "leaflet";

export default {
  props: {
    tripList: Object,
  },
  name: "map-component",
  components: {
    LMap,
    LTileLayer,
    // LMarker,
  },
  data() {
    return {
      map: null,
      url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
      attribution:
        '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors',
      zoom: 6,
      markerLatLng: [45.859412, 13.601074],
      center: [45.65052568917208, 13.76517096104127],
      allMarkers: [],
      allPolylines: [],
      bounds: latLngBounds(
        { lat: 51.476483373501964, lng: -0.14668464136775586 },
        { lat: 51.52948330894063, lng: -0.019140238291583955 }
      ),
      j: null,
    };
  },
  methods: {
    alert(item) {
      alert("Luogo: " + JSON.stringify(item));
    },

    loadMap() {
      this.map = this.$refs.map.mapObject;
      let drawnItems = new L.FeatureGroup().addTo(this.map);
      let $this = this;
      function onEachFeature(feature, layer) {
        drawnItems.addLayer(layer);
        if (
          feature.properties.description == undefined ||
          feature.properties.description == null
        ) {
          layer.bindPopup(
            "<p>" +
              feature.properties.name +
              " (" +
              feature.geometry.coordinates[1] +
              ", " +
              feature.geometry.coordinates[0] +
              ")</p><hr/><p style='font-size:14px'>You were here on " +
              $this.tripList[$this.j].DATE +
              ", and the trasportation type is: " +
              $this.tripList[$this.j].TRANSPORTATION +
              "</p>"
          );
        } else {
          layer.bindPopup(
            "<p>" +
              feature.properties.name +
              " (" +
              feature.geometry.coordinates[1] +
              ", " +
              feature.geometry.coordinates[0] +
              ")</p><hr/><p style='font-size:14px'>" +
              feature.properties.description +
              "</p>"
          );
        }
      }
      if (this.tripList.length > 0) {
        // let mygeojson =
        for (let i = 0; i < this.tripList.length; ++i) {
          this.j = i;
          // let featureJson =
          L.geoJson(this.tripList[i].GEOJSON, {
            onEachFeature: onEachFeature,
            color: "red",
          }).addTo(this.map);
        }
        this.map.fitBounds(drawnItems.getBounds());
        this.zoom = 6;
      }
    },
  },
  mounted() {
    this.loadMap();
  },
};
</script>
