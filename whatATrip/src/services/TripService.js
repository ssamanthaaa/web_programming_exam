import axios from "axios";
// const token = localStorage.getItem("token");
// const idUser = localStorage.getItem("id");
const url = "/WhatATrip/api/";

export default {
  /* A function to get all trips from the database. */
  async getAllTrips(idUser, token) {
    return axios.get(url + idUser + "/trips/", {
      headers: {
        "x-access-token": token,
      },
    });
  },

  /* A function to get a single trips from the database. */
  async getTrip(idUser, idTrip, token) {
    return axios.get(url + idUser + "/trips/" + idTrip, {
      headers: {
        "x-access-token": token,
      },
    });
  },

  /* Creating a new trip. */
  async createTrip(idUser, token, dati) {
    return axios.post(url + idUser + "/trips/", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  /* Updating the trip. */
  async updateTrip(idUser, idTrip, token, dati) {
    return axios.put(url + idUser + "/trips/" + idTrip, JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  /* Deleting a trip. */
  async deleteTrip(idUser, idTrip, token) {
    return axios.delete(url + idUser + "/trips/" + idTrip, {
      headers: {
        "x-access-token": token,
      },
    });
  },
};
