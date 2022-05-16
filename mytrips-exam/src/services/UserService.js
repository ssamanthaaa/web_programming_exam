const token = localStorage.getItem("token");

export default {
  async createUser(dati) {
    await fetch("/WPExamProject/api/user/create", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dati),
    });
  },

  async authenticateUser(dati) {
    await fetch("/WPExamProject/api/user/authenticate", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dati),
    });
  },

  async updateUser(dati) {
    await fetch("/WPExamProject/api/user/update", {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        "x-access-token": token,
      },
      // body: JSON.stringify(dati),
      body: JSON.stringify(dati),
    });
  },
};
