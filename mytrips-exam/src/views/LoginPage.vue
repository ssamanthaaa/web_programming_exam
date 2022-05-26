<template>
  <div id="app">
    <!-- style="background-color: #1e2833"-->
    <div class="login-page">
      <transition name="fade">
        <div v-if="!registerActive" class="wallpaper-login"></div>
      </transition>
      <div class="wallpaper-register"></div>

      <div class="container">
        <div class="row">
          <div class="col-lg-4 col-md-6 col-sm-8 mx-auto">
            <div
              v-if="!registerActive"
              class="card login"
              v-bind:class="{ error: emptyFields }"
            >
              <h1>Sign In</h1>
              <div class="illustration">
                <Icon icon="carbon:locked" style="" />
              </div>
              <form class="form-group" @submit.prevent>
                <input
                  v-model="usernameLogin"
                  type="text"
                  class="form-control"
                  placeholder="Username"
                  required
                />
                <input
                  v-model="passwordLogin"
                  type="password"
                  class="form-control"
                  placeholder="Password"
                  required
                />
                <!-- <input
                  type="submit"
                  class="btn gradient"
                  @click="doLogin"
                  value="Log In"
                /> -->
                <button class="btn gradient" @click="doLogin()">Log In</button>
                <p>
                  Don't have an account?
                  <a
                    href="#"
                    @click="
                      (registerActive = !registerActive), (emptyFields = false)
                    "
                    >Sign up here</a
                  >
                </p>
                <!-- <p><a href="#">Forgot your password?</a></p> -->
              </form>
            </div>

            <div
              v-else
              class="card register"
              v-bind:class="{ error: emptyFields }"
            >
              <h1>Sign Up</h1>
              <div class="illustration">
                <Icon icon="carbon:locked" />
              </div>
              <form class="form-group" @submit.prevent>
                <input
                  v-model="email"
                  type="email"
                  class="form-control"
                  placeholder="Email"
                  required
                />
                <input
                  v-model="usernameRegistration"
                  type="text"
                  class="form-control"
                  placeholder="Username"
                  required
                />
                <input
                  v-model="passwordRegistration"
                  type="password"
                  class="form-control"
                  placeholder="Password"
                  required
                />
                <input
                  v-model="confirmPassword"
                  type="password"
                  class="form-control"
                  placeholder="Confirm Password"
                  required
                />
                <!-- <input
                  type="submit"
                  class="btn gradient"
                  @click="doRegister"
                  value="Register"
                /> -->
                <button class="btn gradient" @click="doRegister()">
                  Register
                </button>
                <p>
                  Already have an account?
                  <a
                    href="#"
                    @click="
                      (registerActive = !registerActive), (emptyFields = false)
                    "
                    >Sign in here</a
                  >
                </p>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
// import AboutView from "./AboutView.vue";
// import Login from "@/components/Login.vue";
// import Registrazione from '@/components/Registrazione.vue';
import UserService from "@/services/UserService.js";
import { Icon } from "@iconify/vue2";
// import axios from "axios";

export default {
  name: "LoginPage",
  components: {
    // AboutView,
    // HelloWorld,
    // Login,
    // Registrazione
    Icon,
  },
  data() {
    return {
      registerActive: false,
      usernameLogin: null,
      passwordLogin: null,
      email: null,
      usernameRegistration: null,
      passwordRegistration: null,
      confirmPassword: null,
      //   emailLogin: "",
      //   passwordLogin: "",
      //   emailReg: "",
      //   passwordReg: "",
      emptyFields: false,
      authenticationResponse: null,
    };
  },
  methods: {
    async doLogin() {
      if (
        this.usernameLogin === "" ||
        this.passwordLogin === "" ||
        this.usernameLogin == null ||
        this.passwordLogin == null
      ) {
        this.emptyFields = true;
      } else {
        let valori = {
          username: this.usernameLogin,
          password: this.passwordLogin,
        };

        await UserService.authenticateUser(valori)
          // axios
          //   .post(
          //     "/WPExamProject/api/user/authenticate",
          //     JSON.stringify(valori),
          //     {
          //       headers: {
          //         "Content-type": "application/json",
          //       },
          //     }
          //   )
          .then((response) => {
            this.authenticationResponse = response;
          })
          .catch(function (error) {
            console.log(error);
          });
        // console.log(this.authenticationResponse);
        // console.log(this.authenticationResponse.data);
        let tokenJwt = this.authenticationResponse.data["x-access-token"];
        localStorage.setItem("token", tokenJwt);

        localStorage.setItem("id", this.authenticationResponse.data.ID);
        localStorage.setItem(
          "username",
          this.authenticationResponse.data.USERNAME
        );
        localStorage.setItem("email", this.authenticationResponse.data.EMAIL);
        if (this.authenticationResponse.status == 200) {
          this.$router.push({
            path: "/dashboard",
          });
        } else {
          alert("The credentials are wrong!");
        }
      }
    },

    async doRegister() {
      if (
        this.usernameRegistration === "" ||
        this.passwordRegistration === "" ||
        this.confirmPassword === "" ||
        this.usernameRegistration == null ||
        this.passwordRegistration == null ||
        this.confirmPassword == null ||
        this.email == "" ||
        this.email == null
      ) {
        this.emptyFields = true;
      } else if (this.password != this.confirmPassword) {
        alert("Password diverse");
      } else {
        let valori = {
          email: this.email,
          username: this.username,
          password: this.password,
        };
        await UserService.createUser(valori)
          .then((response) => {
            this.authenticationResponse = response;
          })
          .catch(function (error) {
            console.log(error);
          });
        console.log("print repsonse");
        console.log(this.authenticationResponse);
        console.log(this.authenticationResponse.data["x-access-token"]);
        let tokenJwt = this.authenticationResponse.data["x-access-token"];
        localStorage.setItem("token", tokenJwt);
        console.log(
          this.authenticationResponse.data.ID,
          this.authenticationResponse.data.USERNAME
        );
        localStorage.setItem("id", this.authenticationResponse.data.ID);
        localStorage.setItem(
          "username",
          this.authenticationResponse.data.USERNAME
        );
        localStorage.setItem("email", this.authenticationResponse.data.EMAIL);
        if (this.authenticationResponse.status == 200) {
          this.$router.push({
            path: "/dashboard",
          });
        } else {
          alert("Sorry! there is an error");
        }
        // alert("You are now registered");
        // RESPONSE IF USER ALREADY EXIST --> {"message": "User already registered: samantha"}
      }
    },
  },
  computed: {
    // currentTabComponent: function () {
    //   // return 'tab-' + this.currentTab.toLowerCase()
    //   return this.currentTab;
    // },
  },
};
</script>
<style scoped>
/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
} */
.tab-button {
  padding: 6px 10px;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  border: 1px solid #10a0fe;
  cursor: pointer;
  color: black;
  /* background: #f0f0f0; */
  background: #ffffff;
  /* margin-bottom: -1px;*/
  margin-right: 2px;
}
.tab-button:hover {
  background: #c2dff3;
}
.tab-button.active {
  /* background: #10a0fe;
  color: white; */
  background: linear-gradient(120deg, #7f70f5, #0ea0ff);
  color: #fff;
}
.tab {
  padding: 50px;
  border: 2px solid #10a0fe;
  /* border: 2px solid transparent; */
  border-radius: 8px;
  /* border-image: linear-gradient(120deg, #7f70f5, #0ea0ff) ;
  border-image-slice: 15; */
}
/* 
.login-dark {
  height: 1000px;
  background-size: cover;
  position: relative;
}

.login-dark form {
  max-width: 320px;
  width: 90%;
  background-color: #1e2833;
  padding: 40px;
  border-radius: 4px;
  transform: translate(-50%, -50%);
  position: absolute;
  top: 50%;
  left: 50%;
  color: #fff;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.2);
}

.login-dark .illustration {
  text-align: center;
  padding: 15px 0 20px;
  font-size: 100px;
  color: #2980ef;
}

.login-dark form .form-control {
  background: none;
  border: none;
  border-bottom: 1px solid #434a52;
  border-radius: 0;
  box-shadow: none;
  outline: none;
  color: inherit;
}

.login-dark form .btn-primary {
  background: #214a80;
  border: none;
  border-radius: 4px;
  padding: 11px;
  box-shadow: none;
  margin-top: 26px;
  text-shadow: none;
  outline: none;
}

.login-dark form .btn-primary:hover,
.login-dark form .btn-primary:active {
  background: #214a80;
  outline: none;
}

.login-dark form .forgot {
  display: block;
  text-align: center;
  font-size: 12px;
  color: #6f7a85;
  opacity: 0.9;
  text-decoration: none;
}

.login-dark form .forgot:hover,
.login-dark form .forgot:active {
  opacity: 1;
  text-decoration: none;
}

.login-dark form .btn-primary:active {
  transform: translateY(1px);
} */

.illustration {
  text-align: center;
  padding: 15px 0 20px;
  font-size: 100px;
  color: #11517c;
}
/* background: linear-gradient(120deg, #7f70f5, #0ea0ff); */

p {
  line-height: 1rem;
}

.card {
  padding: 20px;
}

.form-group input {
  margin-bottom: 20px;
}

.login-page {
  align-items: center;
  display: flex;
  height: 100vh;
}

.error {
  animation-name: errorShake;
  animation-duration: 0.3s;
}

@keyframes errorShake {
  0% {
    transform: translateX(-25px);
  }
  25% {
    transform: translateX(25px);
  }
  50% {
    transform: translateX(-25px);
  }
  75% {
    transform: translateX(25px);
  }
  100% {
    transform: translateX(0);
  }
}
</style>
