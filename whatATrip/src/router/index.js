import Vue from "vue";
import VueRouter from "vue-router";

import About from "../views/About.vue";
import LoginPage from "../views/LoginPage.vue";
import Dashboard from "../views/Dashboard.vue";
import UpdateTrip from "../views/UpdateTrip.vue";
import Welcome from "../views/Welcome.vue";
import ModificaViaggioVecchio from "../views/ModificaViaggioVecchio.vue";
import Error from "../views/ErrorPage.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/login",
    name: "login",
    component: LoginPage,
    meta: { hideHeader: true },
  },
  {
    path: "/",
    name: "Login",
    component: LoginPage,
    meta: { hideHeader: true },
  },
  {
    path: "/welcome",
    name: "Welcome",
    component: Welcome,
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/update-trip",
    name: "UpdateTrip",
    component: UpdateTrip,
  },
  {
    path: "/modifica-viaggio-vecchio",
    name: "ModificaViaggioVecchio",
    component: ModificaViaggioVecchio,
  },
  {
    path: "/about",
    name: "About",
    component: About,
  },
  {
    path: "/*",
    name: "Error",
    component: Error,
  },
  // {
  //   path: "/about",
  //   name: "about",
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  // },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
