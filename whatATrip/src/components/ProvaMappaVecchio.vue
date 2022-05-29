<template>
  <div>
    <div class="row justify-content-center" id="map">
      <!-- id="map"
      v-on:change="onMapUpdate()" -->
      <!-- <l-map
        ref="map"
        style="height: 500px; width: 100%"
        :zoom="zoom"
        :center="center"
        @update:zoom="zoomUpdated"
        @update:center="centerUpdated"
        @update:bounds="boundsUpdated"
      >
        <l-tile-layer ref="layer" :url="url"></l-tile-layer>
        <l-feature-group ref="features">
          <l-popup>
            <span> Yay I was opened by {{ caller }}</span></l-popup
          >
          <l-marker
            v-for="(value, index) in markers"
            :key="index"
            :lat-lng="value"
            @click="openPopUp(value, 'marker')"
          >
          </l-marker>
        </l-feature-group>
        <l-polyline :lat-lngs="markers" color="green" />
      </l-map> -->
    </div>
    <div>zoom: {{ zoom }}, center: {{ center }}</div>
  </div>
</template>

<script>
import * as L from "leaflet";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.min.js";
import "@geoman-io/leaflet-geoman-free";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";
// import LeafletDraw from "leaflet-draw";
// import LDrawToolbar from "vue2-leaflet-draw-toolbar";
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
      zoom: 10,
      center: [40.026534098506396, 32.78335332870484], //45.65052568917208, 13.76517096104127
      bounds: null,
      drawToolbarOptions: {},
      tileLayer: null,
      newMarker: null,

      caller: null,

      markers: [
        { lat: 40.026534098506396, lng: 32.78335332870484 },
        // [40.026534098506396, 32.78335332870484],
        // [40.026402649250066, 32.85012960433961],
        // [40.03954632107637, 32.80137777328492],
        // [-41.31581, 174.80777],
      ],
      pathCoordinates: null,
      mainStages: [],
      geojson: {
        type: "FeatureCollection",
        features: [
          {
            type: "Feature",
            geometry: {
              type: "Line",
              coordinates: [
                [-41.31115, 174.80827],
                [-41.30928, 174.80835],
                [-41.29127, 174.83841],
                [41, 55],
              ],
            },
          },
          {
            type: "line",
            coordinates: [
              [-41, 174],
              [110, 45],
              [41, 55],
            ],
          },
        ],
      },
    };
  },
  mounted() {
    // const map = this.$refs.map.mapObject;
    // let map;
    this.initMap();
    this.onClick();
    // this.onPopupOpen();
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
    openPopUp(latLng, caller) {
      this.caller = caller;
      console.log("open");
    },

    initMap() {
      this.map = L.map("map").setView(
        //, { drawControl: true } con questo spuntano fuori quadrattini vuoti dall'altro lato
        [40.0269319, 32.83604819],
        13
      );
      // this.map = map;
      // this.map = this.$refs.map.mapObject;
      this.tileLayer = L.tileLayer(this.url, {
        continuousWorld: false,
        noWrap: false,
        crs: L.CRS.Simple,
      }).addTo(this.map);

      // let drawControl = new L.Control.Draw({
      //   edit: { featureGroup: drawnItems}
      // });
      // this.map.addControl(drawControl);
      // drawnItems.setStyle({pmIgnore: false});
      // drawnItems.options.pmIgnore = false; // If the layer is a LayerGroup / FeatureGroup / GeoJSON this line is needed too
      // L.PM.reInitLayer(drawnItems);

      this.map.on("pm:drawedited", function (e) {
        console.log(e);
        var layers = e.layers;
        layers.eachLayer(function (layer) {
          console.log(layer);
          //do whatever you want; most likely save back to db
        });
      });

      this.map.pm.addControls({
        position: "topright",
        drawCircle: false,
        drawCircleMarker: false,
        drawRectangle: false,
        drawPolygon: false,
        cutPolygon: false,
        rotateMode: false,
        // editControls: true,
        // optionsControls: false,
        // customControls: false,
        oneBlock: false,
      });
      this.map.on("click", this.onClick, this);
      this.map.pm.setPathOptions({ color: "green" });

      for (let i = 0; i < this.markers.length; i++) {
        L.marker(this.markers[i]).addTo(this.map);
      }
      // var geojsonFeature = {
      //     "type": "Feature",
      //     "properties": {
      //         "name": "Coors Field",
      //         "amenity": "Baseball Stadium",
      //         "popupContent": "This is where the Rockies play!"
      //     },
      //     "geometry": {
      //         "type": "Point",
      //         "coordinates": [40.026534098506396, 32.78335332870484]
      //     }
      // };
      // L.geoJSON(geojsonFeature, {
      //   style: function (feature) {
      //     return {color: feature.properties.color};
      //   }
      // }).bindPopup(function (layer) {
      //     return layer.feature.properties.description;
      // }).addTo(this.map);

      // var myLines = [{
      //     "type": "LineString",
      //     "coordinates": [[40.026534098506396,  32.78335332870484], [40.0269319, 32.83604819]]
      // }, {
      //     "type": "LineString",
      //     "coordinates": [[-105, 40], [-110, 45], [-115, 55]]
      // }];
      // var myStyle = {
      //   "color": "#ff7800",
      //   "weight": 5,
      //   "opacity": 0.65
      // };
      // L.geoJSON(myLines, { style: myStyle}).addTo(this.map);
      // var myLayer = L.geoJSON().addTo(this.map);
      // myLayer.addData(geojsonFeature);

      console.log(this.map.pm.getGlobalOptions());

      // this.map.on("pm:vertexadded", function(e) { //  pm:drawend
      //   console.log(e);
      //   console.log("dentro draw:created");
      //   let shape = e.shape;
      //   console.log(`shape: ${shape}`);
      //   let layer = e.layer;
      //     if (shape === 'Line') {
      //       let geojson = {};

      //       // Finally, show the poly as a geojson object in the console
      //       // console.log(JSON.stringify(geojson));
      //     } else if (shape === 'Marker') {
      //       console.log(`marker? -> ${shape}`);
      //     } else {
      //       console.log("not a polyline");
      //     }
      // });
      return this.map;
    },
    onClick() {
      //e
      let pathCoordinates = [];
      this.map.on("pm:drawstart", ({ workingLayer }) => {
        console.log("workingLsyer");
        console.log(workingLayer);
        workingLayer.on("pm:vertexadded", (e) => {
          //vedo le coordinate nella linea, ma non vedo se modifico
          console.log(e);
          pathCoordinates.push({ lat: e.latlng.lat, lng: e.latlng.lng });
        });
        this.map.on("pm:drawend", function (e) {
          //  pm:drawend
          let shape = e.shape;
          console.log(`shape: ${shape}`);
          // let layer = e.layer;
          // if (shape === "Line") {
          //   console.log("pathCoordinates");
          //   console.log(pathCoordinates);
          // } else if (shape === "Marker") {
          // }
        });
      });

      this.map.on("pm:snap", function (e) {
        console.log("snap");
        console.log(e);
      });
      this.map.on("pm:unsnap", function (e) {
        console.log("unsnap");
        console.log(e);
      });

      // this.map.on('pm:create', function (e) {
      //   console.log("dento pm:create");
      //   console.log(e);
      //   e.layer.on('pm:markerdragend', function (e) {
      //     console.log("dento pm:create-pm:markerdragend");
      //     console.log(e);
      //   });

      //   e.layer.on('pm:update', function (e) {
      //   console.log("dento pm:create-pm:update");
      //     console.log(e);
      //   });

      //   e.layer.on('pm:cut', function (e) {
      //   console.log("dento pm:create-cut");
      //     console.log(e);
      //   });
      // });
      // this.map.on("pm:change", function(e) {
      //   console.log("detro pm:change");
      //   console.log(e);
      // });
      // this.map.on('pm:globaleditmodetoggled', function (e) {
      //   console.log("dentro pm:globaleditmodetoggled")
      //   console.log(e);
      // });

      // console.log(e);
      // this.newMarker = L.marker(e.latlng, { draggable: true })
      //   .addTo(this.map)
      //   .bindPopup(this.popupContent);
      // this.markers.push({lat: this.newMarker._latlng.lat, lng: this.newMarker._latlng.lng});
      // console.log(this.markers);
      // this.map.on("pm:drawstart", ({ workingLayer }) => {
      //   console.log("dentro pm:drawstart");
      //   workingLayer.on("pm:vertexadded", (e) => {
      //     console.log(e);
      //     // this.pathCoordinates.push({})
      //   });
      // });
      //   console.log("secodno e")
      //   this.map.on('draw:editstart', function (e) {
      //     console.log("dentro draw_editstar")
      //     console.log(e);
      //      var layers = e.layers;
      //      console.log(layers);
      //      layers.eachLayer(function (layer) {
      //          //do whatever you want; most likely save back to db
      //          console.log(layer)
      //      });
      //  });
    },

    onMapUpdate() {
      // this.map = this.$refs.map.mapObject;
      this.map.on("pm:drawstart", ({ workingLayer }) => {
        workingLayer.on("pm:vertexadded", (e) => {
          console.log(e);
          // this.pathCoordinates.push({})
        });
        // workingLayer.on("pm:snap", (e) => {
        //   console.log(e);
        // });
      });
      this.map.on("pm:create", function (e) {
        // alert('pm:create event fired. See console for details');
        console.log(e);
        console.log(this.markers);
        // this.mainStages.push(e.layer._latlng);
        // console.log(this.mainStages);
        const layer = e.layer;
        layer.on("pm:cut", function (ev) {
          console.log("cut event on layer");
          console.log(ev);
        });
      });
    },

    //   initMap() {
    //     let map = L.map("map").setView([40.0269319, 32.83604819], 13);
    //     this.map = map;
    //     console.log(this.map);
    //     L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    //       maxZoom: 19,
    //       attribution:
    //         '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    //     }).addTo(map);
    //     for (let i = 0; i < this.markers.length; i++) {
    //       L.marker(this.markers[i]).addTo(map);
    //       console.log(`initMap() markers: ${this.markers[i]}`);
    //     }
    //     map.pm.setPathOptions({ color: "green" });
    //     // listen to vertexes being added to currently drawn layer (called workingLayer)

    //     // Click button -> toggle disabled
    //     // map.pm.Toolbar.createCustomControl({
    //     //   name: "alertBox",
    //     //   block: "custom",
    //     //   className: "leaflet-pm-icon-marker xyz-class",
    //     //   title: "Count layers",
    //     //   onClick: () => {
    //     //     alert(
    //     //       "There are " +
    //     //         L.PM.Utils.findLayers(map).length +
    //     //         " layers on the map"
    //     //     );
    //     //   },
    //     //   toggle: false,
    //     // });

    //     // Copy Geoman Draw Control
    //     // const _actions = [
    //     //   {
    //     //     text: "Custom message, with click event",
    //     //     onClick(e) {
    //     //       alert("click");
    //     //     },
    //     //     name: "actionName",
    //     //   },
    //     // ];
    //     // map.pm.Toolbar.copyDrawControl("Rectangle", {
    //     //   name: "RectangleCopy",
    //     //   block: "custom",
    //     //   title: "Display text on hover button",
    //     //   actions: _actions,
    //     // });

    //     // map.on("pm:actionclick", function (e) {
    //     //   console.log(e);
    //     // });
    //     // map.on("pm:buttonclick", function (e) {
    //     //   console.log(e);
    //     // });
    //   },
    // },
    // mounted () {
    //   this.initMap();
    // },

    // mounted() {
    //   this.$nextTick(() => {
    //     let map = this.$refs.map.mapObject;
    //     map.pm.addControls({
    //       position: "topleft",
    //       drawCircle: this.drawCircle,
    //       drawCircleMarker: false,
    //       drawRectangle: false,
    //       drawPolygon: false,
    //       cutPolygon: false,
    //       rotateMode: false,
    //       // oneBlock: true --> all buttons display in one block, don't like it
    //       tooltips: true,
    //     });
    //     map.pm.setPathOptions({
    //       color: "red",
    //     });
    //   });
  },
};
</script>
<style scoped>
#map {
  height: 500px;
}
</style>
