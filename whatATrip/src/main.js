import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

Vue.config.productionTip = false;

import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "@/assets/css/style-bootstrap.css";
import "@/assets/css/myStyle.css";

import { BootstrapVue, BootstrapVueIcons } from "bootstrap-vue";
Vue.use(BootstrapVue);
Vue.use(BootstrapVueIcons);

import { LMap, LTileLayer, LMarker } from "vue2-leaflet";
// import LDrawToolbar from "vue2-leaflet-draw-toolbar";

import "leaflet/dist/leaflet.css";
import { Icon } from "leaflet";

Vue.component("l-map", LMap);
Vue.component("l-tile-layer", LTileLayer);
Vue.component("l-marker", LMarker);
// Vue.component("l-draw-toolbar", LDrawToolbar);

delete Icon.Default.prototype._getIconUrl;
Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
});

// import VCalendar from "v-calendar";
// Vue.use(VCalendar);
import "vue2-datepicker/index.css";

import VeeValidate from "vee-validate";

Vue.use(VeeValidate, {
  // This is the default
  inject: true,
  // Important to name this something other than 'fields'
  fieldsBagName: "veeFields",
  // This is not required but avoids possible naming conflicts
  errorBagName: "veeErrors",
});

import Notifications from "vue-notification";
Vue.use(Notifications);

new Vue({
  router,
  store,
  render: (h) => h(App),
  Base: "/ WPTripExam /",
  mode: "history",
}).$mount("#app");
