import Vue from "vue";
import VueRouter from "vue-router";
// import HomeView from "../views/HomeView.vue";
import LoginPage from "../views/LoginPage.vue";
import Dashboard from "../views/Dashboard.vue";
import ModificaViaggio from "../views/ModificaViaggio.vue";
import ModificaViaggioVecchio from "../views/ModificaViaggioVecchio.vue";
import Errore from "../views/Errore.vue";
import ProvaMappa from "../components/ProvaMappaVecchio.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/prova-mappa",
    name: "provaMappa",
    component: ProvaMappa,
  },
  {
    path: "/login",
    name: "Login",
    component: LoginPage,
  },
  {
    path: "/",
    name: "Login",
    component: LoginPage,
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/modifica-viaggio",
    name: "ModificaViaggio",
    component: ModificaViaggio,
  },
  {
    path: "/modifica-viaggio-vecchio",
    name: "ModificaViaggioVecchio",
    component: ModificaViaggioVecchio,
  },
  {
    path: "/*",
    name: "Errore",
    component: Errore,
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
