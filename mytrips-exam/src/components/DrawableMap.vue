<template>
  <div>
    <div class="row justify-content-center">
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
        <!-- <l-draw-toolbar position="topleft"></l-draw-toolbar> -->
        <l-marker
          v-for="(value, index) in markers"
          :key="index"
          :lat-lng="value"
        ></l-marker>
      </l-map>
      <div>zoom: {{ zoom }}, center: {{ center }}</div>
    </div>
  </div>
</template>

<script>
import * as L from "leaflet";
// import LDrawToolbar from "vue2-leaflet-draw-toolbar";
// import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.min.js";
import "@geoman-io/leaflet-geoman-free";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";
// import LeafletDraw from 'leaflet-draw'
import { LMap, LTileLayer, LFeatureGroup, GeoJSON } from "vue2-leaflet";
import LDrawToolbar from "vue2-leaflet-draw-toolbar";

export default {
  components: { LMap, LTileLayer, LDrawToolbar, LFeatureGroup, GeoJSON },
  data() {
    return {
      url: "http://{s}.tile.osm.org/{z}/{x}/{y}.png",
      zoom: 10,
      center: [-41.31825, 174.80768], //45.65052568917208, 13.76517096104127
      bounds: null,
      markers: [
        [-41.31825, 174.80768],
        [-41.31606, 174.80774],
        [-41.31581, 174.80777],
        [-41.31115, 174.80827],
        [-41.30928, 174.80835],
        [-41.29127, 174.83841],
        [-41.33571, 174.84846],
        [-41.34268, 174.82877],
      ],
    };
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
  },

  mounted() {
    this.$nextTick(() => {
      let map = this.$refs.map.mapObject;
      map.pm.addControls({
        position: "topleft",
        drawCircle: this.drawCircle,
        drawCircleMarker: false,
        drawRectangle: false,
        drawPolygon: false,
        cutPolygon: false,
        rotateMode: false,
        // oneBlock: true --> all buttons display in one block, don't like it
        tooltips: true,
      });
      map.pm.setPathOptions({
        color: "red",
      });
      L.geoJSON(data, {
        snapIgnore: true,
      });

      // enable global Edit Mode
      // map.pm.enableGlobalEditMode(options);
    });
  },
};
</script>
