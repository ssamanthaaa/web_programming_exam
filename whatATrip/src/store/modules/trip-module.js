const state = {
  trip: [],
  trips: [],
  mainStages: [],
  // percorso: [],
  path: [],
};

const getters = {
  // percorso: state => state.percorso,
  tripsList: (state) => state.trips,
  singleTrip: (state) => state.trip,
  mainStagesList: (state) => state.mainStages,
  pathList: (state) => state.path,
};

const actions = {
  async getTrips({ commit }) {
    //  const response = await axios.get("http://localhost:3000/users");
    const response = await fetch("api/1/trips/");
    const responseJson = await response.json();
    console.log("trips");
    console.log(responseJson);
    commit("setTrips", responseJson);
  },

  //TODO rivedere perche non va bene
  async getTrip({ commit }, id) {
    const response = await fetch(`api/1/trips/${id}`);
    const responseJson = await response.json();
    console.log("singleTrip");
    console.log(responseJson);
    // commit("setTrip", responseJson);
    // console.log(state.singleTrip);
    // commit("setPercorso", responseJson.PERCORSO);
    commit("setMainStages", responseJson.TAPPE_PRINCIPALI);
    let path = [];
    for (let i = 0; i < state.mainStages.length; ++i) {
      path[i] = state.mainStages[i].position;
    }
    commit("setPath", path);
    return responseJson;
    // await func();
    // console.log("eseguita funzione");
  },

  async addNewTrip({ commit }, trip) {
    //   const response = await axios.post("http://localhost:3000/users", user);
    const response = await fetch("api/1/trips/", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: trip,
    });
    // commit("setMainStages", response.TAPPE_PRINCIPALI);
    // commit("setPath", response.TAPPE_PRINCIPALI);
    commit("addNewTrip", response);
  },

  async updateTrip({ commit }, trip) {
    //   const response = await axios.post("http://localhost:3000/users", user);
    const response = await fetch("api/1/trips/", {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: trip,
    });
    commit("updateTrip", response);
    commit("setMainStages", response.TAPPE_PRINCIPALI);
    commit("setPath", response.TAPPE_PRINCIPALI);
  },

  // async deleteTrip({commit}, id){
  //   await axios.delete(`http://localhost:3000/users/${id}`);
  //   commit("removeTrip", id)
  // }
};

const mutations = {
  setTrip: (state, singleTrip) => (state.singleTrip = singleTrip),

  setTrips: (state, trips) => (state.trips = trips),

  // setPercorso: (state, percorso) => (
  //     state.percorso = percorso
  // ),

  setMainStages: (state, mainStages) => (state.mainStages = mainStages),

  setPath: (state, path) => (state.path = path),

  addNewTrip: (state, trip) => state.trips.push(trip), //.unshift(trip),
  updateTrip: (state, trip, i) => (state.trips[i] = trip),

  updateMainStages: (state, mainStages) => (state.mainStages = mainStages),

  updatePath: (state, path) => (state.path = path),

  // removeTrip: (state, id) => (
  //     state.trips.filter(trip => trip.id !== id),
  //     state.trips.splice(trip => trip.id, 1)
  // )
};

export default {
  state,
  getters,
  actions,
  mutations,
};
