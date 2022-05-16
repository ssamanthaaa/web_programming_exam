const token = localStorage.getItem("token");

export default {
  async getAllTrips(idUser) {
    await fetch("/WPExamProject/api/" + idUser + "/trips/", {
      method: "GET",
      headers: {
        "x-access-token": token,
      },
    });
  },

  async getTrip(idUser, idTrip) {
    await fetch("/WPExamProject/api/" + idUser + "/trips/" + idTrip, {
      method: "GET",
      headers: {
        "x-access-token": token,
      },
    });
  },

  async createTrips(idUser, dati) {
    await fetch("/WPExamProject/api/" + idUser + "/trips/", {
      method: "POST",
      headers: {
        "x-access-token": token,
      },
      body: dati,
    });
  },

  async updateTrips(idUser, dati) {
    await fetch("/WPExamProject/api/" + idUser + "/trips/", {
      method: "PUT",
      headers: {
        "x-access-token": token,
      },
      body: JSON.stringify(dati),
    });
  },
};
