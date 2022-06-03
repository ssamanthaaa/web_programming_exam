import axios from "axios";

const token = localStorage.getItem("token");
const url = "/WhatATrip/api/user";
export default {
  async createUser(dati) {
    return axios.post(url + "/create", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
      },
    });
  },

  async authenticateUser(dati) {
    return axios.post(url + "/authenticate", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
      },
    });
  },

  async getUsername(token) {
    return axios.get(url + "/get", {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  async updateUser(dati) {
    return axios.put(url + "/update", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("id");
    localStorage.removeItem("username");
    localStorage.removeItem("email");
  },
};
