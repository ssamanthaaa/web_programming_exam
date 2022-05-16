const token = localStorage.getItem("token");

export default {
  async createUser(dati) {
    await fetch("api/trips/user/create", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dati),
    });
  },

  async authenticateUser(dati) {
    await fetch("http://localhost:8080/api/trips/user/authenticate", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dati),
    });
  },

  async updateUser(dati) {
    await fetch("api/trips/user/update", {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        "x-access-token": token,
      },
      body: JSON.stringify(dati),
    });
  },
};
