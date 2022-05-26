import axios from "axios";
// const token = localStorage.getItem("token");
// const idUser = localStorage.getItem("id");
const url = "/WPTripExamProject/api/";

export default {
  async getAllTrips(idUser, token) {
    // const response = await fetch(url + idUser + "/trips", {
    //   method: "GET",
    //   headers: {
    //     "x-access-token": token,
    //   },
    // });
    // return response.json();
    return axios.get(url + idUser + "/trips/", {
      headers: {
        "x-access-token": token,
      },
    });
  },

  async getTrip(idUser, idTrip, token) {
    // const response = await fetch(url + idUser + "/trips/" + idTrip, {
    //   method: "GET",
    //   headers: {
    //     "x-access-token": token,
    //   },
    // });
    // return response.json();
    return axios.get(url + idUser + "/trips/" + idTrip, {
      headers: {
        "x-access-token": token,
      },
    });
  },

  async createTrip(idUser, token, dati) {
    // const response = await fetch(url + idUser + "/trips/", {
    //   method: "POST",
    //   headers: {
    //     "Content-type": "application/json",
    //     "x-access-token": token,
    //   },
    //   body: JSON.stringify(dati),
    // });
    // return response.json();
    return axios.post(url + idUser + "/trips/", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  async updateTrip(idUser, idTrip, token, dati) {
    // const response = await fetch(url + idUser + "/trips/" + idTrip, {
    //   method: "PUT",
    //   headers: {
    //     "Content-type": "application/json",
    //     "x-access-token": token,
    //   },
    //   body: JSON.stringify(dati),
    // });
    // return response.json();
    return axios.put(url + idUser + "/trips/" + idTrip, JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },

  async deleteTrip(idUser, idTrip, token) {
    return axios.delete(url + idUser + "/trips/" + idTrip, {
      headers: {
        "x-access-token": token,
      },
    });
  },
};
