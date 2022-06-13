import Vue from "vue";
import VueRouter from "vue-router";

import About from "../views/About.vue";
import LoginPage from "../views/LoginPage.vue";
import Dashboard from "../views/Dashboard.vue";
import AdminDashboard from "../views/AdminDashboard.vue";
import UpdateTrip from "../views/UpdateTrip.vue";
import Welcome from "../views/Welcome.vue";
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
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem("isLogged") === "false") {
        next({ path: "/" });
      } else {
        next();
      }
    },
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem("isLogged") === "false") {
        next({ path: "/" });
      } else {
        next();
      }
    },
  },
  {
    path: "/admin-dashboard",
    name: "AdminDashboard",
    component: AdminDashboard,
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem("isLogged") === "false") {
        next({ path: "/" });
      } else {
        next();
      }
    },
  },
  {
    path: "/update-trip",
    name: "UpdateTrip",
    component: UpdateTrip,
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem("isLogged") === "false") {
        next({ path: "/" });
      } else {
        next();
      }
    },
  },
  {
    path: "/about",
    name: "About",
    component: About,
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem("isLogged") === "false") {
        next({ path: "/" });
      } else {
        next();
      }
    },
  },
  {
    path: "/*",
    name: "Error",
    component: Error,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
