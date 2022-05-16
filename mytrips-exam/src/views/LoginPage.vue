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
              <form class="form-group">
                <input
                  v-model="username"
                  type="text"
                  class="form-control"
                  placeholder="Username"
                  required
                />
                <input
                  v-model="password"
                  type="password"
                  class="form-control"
                  placeholder="Password"
                  required
                />
                <input
                  type="submit"
                  class="btn gradient"
                  @click="doLogin"
                  value="Log In"
                />
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
              <form class="form-group">
                <input
                  v-model="email"
                  type="email"
                  class="form-control"
                  placeholder="Email"
                  required
                />
                <input
                  v-model="password"
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
                <input
                  type="submit"
                  class="btn gradient"
                  @click="doRegister"
                  value="Register"
                />
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
      username: null,
      password: null,
      email: null,
      confirmPassword: null,
      //   emailLogin: "",
      //   passwordLogin: "",
      //   emailReg: "",
      //   passwordReg: "",
      emptyFields: false,
    };
  },
  methods: {
    async doLogin() {
      if (
        this.username === "" ||
        this.password === "" ||
        this.username == null ||
        this.password == null
      ) {
        this.emptyFields = true;
      } else {
        let valori = { USERNAME: this.username, PASSWORD: this.password };
        console.log(`valori: `);
        console.log(valori);
        console.log(`JSON.stringify(valori): `);
        console.log(JSON.stringify(valori));
        let response = await UserService.authenticateUser(valori);
        console.log(response);
        console.log("response.data");
        console.log(response.data);
        if (response.status == 200) {
          this.$router.push({
            path: "/dashboard",
          });
        } else {
          alert("Credendiali sbalgiate");
        }
        // alert("You are now logged in");
      }
    },

    async doRegister() {
      if (
        this.email === "" ||
        this.password === "" ||
        this.confirmPassword === "" ||
        this.email == null ||
        this.password == null ||
        this.confirmPassword == null
      ) {
        this.emptyFields = true;
      } else {
        let valori = { USERNAME: this.username, PASSWORD: this.password };
        console.log(`valori: `);
        console.log(valori);
        console.log(`JSON.stringify(valori): `);
        console.log(JSON.stringify(valori));
        let response = await UserService.createUser(valori);
        console.log(response);
        console.log("response.data");
        console.log(response.data);
        if (response.status == 200) {
          this.$router.push({
            path: "/dashboard",
          });
        } else {
          alert("Credendiali sbalgiate");
        }
        alert("You are now registered");
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
