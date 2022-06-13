import axios from "axios";

// const token = localStorage.getItem("token");
const url = "/WhatATrip/api/user";
export default {
  /* Creating a user. */
  async createUser(data) {
    return axios.post(url + "/create", JSON.stringify(data), {
      headers: {
        "Content-type": "application/json",
      },
    });
  },

  /* Sending a post request to the server with the data in the data object. */
  async authenticateUser(data) {
    return axios.post(url + "/authenticate", JSON.stringify(data), {
      headers: {
        "Content-type": "application/json",
      },
    });
  },

  /* This is a function that is used to get all the users from the database. */
  async getAllUsers(token) {
    return axios.get(url + "/getAll", {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  /* Getting the username from the server. */
  async getUsername(token) {
    return axios.get(url + "/get", {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  /* Removing all the local storage items and setting the isLogged to false. */
  async logout(token) {
    localStorage.removeItem("token");
    localStorage.removeItem("id");
    localStorage.removeItem("username");
    localStorage.removeItem("email");
    localStorage.removeItem("role");
    localStorage.setItem("isLogged", false);
    return axios.delete(url + "/logout", {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },
};
