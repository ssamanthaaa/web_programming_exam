import axios from "axios";

const token = localStorage.getItem("token");
const url = "/WPTripExamProject/api/user";
export default {
  async createUser(dati) {
    // const response = await fetch(url + "/create", {
    //   method: "POST",
    //   headers: {
    //     "Content-type": "application/json",
    //   },
    //   body: JSON.stringify(dati),
    // });
    // return response.json();
    return axios.post(url + "/create", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
      },
    });
  },

  async authenticateUser(dati) {
    // const response = await fetch(url + "/authenticate", {
    //   method: "POST",
    //   headers: {
    //     "Content-type": "application/json",
    //   },
    //   body: JSON.stringify(dati),
    // });
    // return response.json();
    return axios.post(url + "/authenticate", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
      },
    });
  },

  async updateUser(dati) {
    // const response = await fetch(url + "/update", {
    //   method: "PUT",
    //   headers: {
    //     "Content-type": "application/json",
    //     "x-access-token": token,
    //   },
    //   body: JSON.stringify(dati),
    // });
    // return response.json();
    return axios.put(url + "/update", JSON.stringify(dati), {
      headers: {
        "Content-type": "application/json",
        "x-access-token": token,
      },
    });
  },
};
