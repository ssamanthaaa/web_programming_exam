<template>
  <div id="app">
    <div class="myLoginPage">
      <div class="container">
        <div class="row">
          <div class="col-lg-4 col-md-6 col-sm-8 mx-auto">
            <div
              v-if="!registerActive"
              class="card"
              v-bind:class="{ error: emptyFields }"
            >
              <div class="illustration">
                <h1>What A Trip</h1>
                <img
                  src="@/assets/img/whatATrip.png"
                  class="img-fluid"
                  alt="What a trip logo"
                />
              </div>
              <h2>Sign In</h2>
              <form @submit.prevent="validateLogin">
                <div class="form-group">
                  <input
                    id="usernameLogin"
                    v-model="usernameLogin"
                    type="text"
                    class="form-control"
                    v-bind:class="{
                      'is-valid': validation.valid.usernameLogin,
                      'is-invalid': validation.invalid.usernameLogin,
                    }"
                    placeholder="Username"
                    required
                  />
                  <div
                    class="valid-feedback"
                    v-if="validation.valid.usernameLogin"
                  >
                    {{ validation.valid.usernameLogin }}
                  </div>
                  <div
                    class="invalid-feedback"
                    v-if="validation.invalid.usernameLogin"
                  >
                    {{ validation.invalid.usernameLogin }}
                  </div>
                </div>
                <div class="form-group">
                  <input
                    v-model="passwordLogin"
                    type="password"
                    class="form-control"
                    v-bind:class="{
                      'is-valid': validation.valid.passwordLogin,
                      'is-invalid': validation.invalid.passwordLogin,
                    }"
                    id="passwordLogin"
                    placeholder="Password"
                    required
                  />
                  <div
                    class="valid-feedback"
                    v-if="validation.valid.passwordLogin"
                  >
                    {{ validation.valid.passwordLogin }}
                  </div>
                  <div
                    class="invalid-feedback"
                    v-if="validation.invalid.passwordLogin"
                  >
                    {{ validation.invalid.passwordLogin }}
                  </div>
                </div>
                <div
                  class="form-group invalid-credentials"
                  v-if="showWrongCredentials"
                >
                  <div>
                    {{ wrongCredentialsMessage }}
                  </div>
                </div>
                <button
                  class="btn yellow-color"
                  type="submit"
                  @click="doLogin()"
                >
                  Log in
                </button>
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
              </form>
            </div>
            <div v-else class="card" v-bind:class="{ error: emptyFields }">
              <div class="illustration">
                <h1>What A Trip</h1>
                <img
                  src="@/assets/img/whatATrip.png"
                  class="img-fluid"
                  alt="What a trip logo"
                />
              </div>
              <h2>Sign Up</h2>
              <form @submit.prevent>
                <div class="form-group">
                  <input
                    v-model="email"
                    type="text"
                    class="form-control"
                    v-bind:class="{
                      'is-valid': validation.valid.email,
                      'is-invalid': validation.invalid.email,
                    }"
                    id="email"
                    placeholder="Email"
                    required
                  />
                  <div class="valid-feedback" v-if="validation.valid.email">
                    {{ validation.valid.email }}
                  </div>
                  <div class="invalid-feedback" v-if="validation.invalid.email">
                    {{ validation.invalid.email }}
                  </div>
                </div>
                <div class="form-group">
                  <input
                    v-model="usernameRegistration"
                    type="text"
                    class="form-control"
                    v-bind:class="{
                      'is-valid': validation.valid.usernameRegistration,
                      'is-invalid': validation.invalid.usernameRegistration,
                    }"
                    id="usernameRegistration"
                    placeholder="Username"
                    required
                  />
                  <div
                    class="valid-feedback"
                    v-if="validation.valid.usernameRegistration"
                  >
                    {{ validation.valid.usernameRegistration }}
                  </div>
                  <div
                    class="invalid-feedback"
                    v-if="validation.invalid.usernameRegistration"
                  >
                    {{ validation.invalid.usernameRegistration }}
                  </div>
                </div>
                <div class="form-group">
                  <input
                    v-model="passwordRegistration"
                    type="password"
                    class="form-control"
                    v-bind:class="{
                      'is-valid': validation.valid.passwordRegistration,
                      'is-invalid': validation.invalid.passwordRegistration,
                    }"
                    id="passwordRegistration"
                    placeholder="Password"
                    required
                  />
                  <div
                    class="valid-feedback"
                    v-if="validation.valid.passwordRegistration"
                  >
                    {{ validation.valid.passwordRegistration }}
                  </div>
                  <div
                    class="invalid-feedback"
                    v-if="validation.invalid.passwordRegistration"
                  >
                    {{ validation.invalid.passwordRegistration }}
                  </div>
                </div>
                <div class="form-group">
                  <input
                    v-model="confirmPassword"
                    type="password"
                    class="form-control"
                    v-bind:class="{
                      'is-valid': validation.valid.confirmPassword,
                      'is-invalid': validation.invalid.confirmPassword,
                    }"
                    id="confirmPassword"
                    placeholder="Confirm Password"
                    required
                  />
                  <div
                    class="valid-feedback"
                    v-if="validation.valid.confirmPassword"
                  >
                    {{ validation.valid.confirmPassword }}
                  </div>
                  <div
                    class="invalid-feedback"
                    v-if="validation.invalid.confirmPassword"
                  >
                    {{ validation.invalid.confirmPassword }}
                  </div>
                </div>
                <div
                  class="form-group invalid-credentials"
                  v-if="showCredentialsExisting"
                >
                  <div>
                    {{ credentialsExistingMessage }}
                  </div>
                </div>
                <div
                  class="form-group invalid-credentials"
                  v-if="showErrorMessage"
                >
                  <div>
                    {{ errorMessage }}
                  </div>
                </div>
                <button
                  class="btn yellow-color"
                  type="submit"
                  @click="doRegister()"
                >
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
import UserService from "@/services/UserService.js";

export default {
  name: "LoginPage",
  components: {},
  data() {
    return {
      registerActive: false,
      usernameLogin: null,
      passwordLogin: null,
      email: null,
      usernameRegistration: null,
      passwordRegistration: null,
      confirmPassword: null,

      emptyFields: false,
      authenticationResponse: null,
      showWrongCredentials: false,
      wrongCredentialsMessage: null,
      credentialsExistingMessage: null,
      showCredentialsExisting: false,
      showErrorMessage: false,
      errorMessage: null,

      usernameLoginError: false,
      passwordLoginError: false,
      usernameRegistrationError: false,
      emailError: false,
      passwordRegistrationError: false,
      confirmPasswordError: false,
      errors: [],
      errorsLogin: [],
      validation: {
        invalid: {},
        valid: {},
      },
    };
  },
  methods: {
    async doLogin() {
      this.clearValidationRegistration();
      if (!this.validateLogin()) {
        this.emptyFields = true;
      } else {
        this.showWrongCredentials = false;
        let valori = {
          username: this.usernameLogin,
          password: this.passwordLogin,
        };
        let errorStatus;
        await UserService.authenticateUser(valori)
          .then((response) => {
            this.authenticationResponse = response;
          })
          .catch(function (error) {
            console.log(error);
            errorStatus = error.response.status;
          });

        if (errorStatus == 401 || errorStatus == 404) {
          this.wrongCredentials();
        } else {
          if (this.authenticationResponse.status == 200) {
            this.showWrongCredentials = false;
            this.validation.invalid.credentials = "";
            this.validation.valid.credentials =
              "The credentials seems to be ok, you will be redirected to your personal workspace in a few seconds.";
            console.log(this.authenticationResponse);
            // console.log(this.authenticationResponse.data);
            let tokenJwt = this.authenticationResponse.data["x-access-token"];
            localStorage.setItem("token", tokenJwt);

            localStorage.setItem("id", this.authenticationResponse.data.ID);
            localStorage.setItem(
              "username",
              this.authenticationResponse.data.USERNAME
            );
            localStorage.setItem(
              "email",
              this.authenticationResponse.data.EMAIL
            );
            localStorage.setItem("role", this.authenticationResponse.data.ROLE);
            localStorage.setItem("isLogged", true);

            this.$router.push({
              path: "/welcome",
            });
          } else {
            console.log("There is a problem");
          }
        }
      }
    },
    clearValidationLogin() {
      this.validation.valid.usernameLogin = "";
      this.validation.invalid.usernameLogin = "";
      this.validation.valid.passwordLogin = "";
      this.validation.invalid.passwordLogin = "";
    },

    clearValidationRegistration() {
      this.validation.valid.usernameRegistration = "";
      this.validation.invalid.usernameRegistration = "";
      this.validation.valid.passwordRegistration = "";
      this.validation.invalid.passwordRegistration = "";
      this.validation.valid.confirmPassword = "";
      this.validation.invalid.confirmPassword = "";
      this.validation.valid.email = "";
      this.validation.invalid.email = "";
    },

    async doRegister() {
      this.clearValidationLogin();

      let errorStatus;
      if (!this.validateRegistration()) {
        this.emptyFields = true;
      } else {
        let valori = {
          email: this.email,
          username: this.usernameRegistration,
          password: this.passwordRegistration,
        };
        await UserService.createUser(valori)
          .then((response) => {
            this.authenticationResponse = response;
          })
          .catch(function (error) {
            errorStatus = error.response.status;
            console.log(error);
          });
        if (
          errorStatus == 401 ||
          errorStatus == 404 ||
          errorStatus == 500 ||
          errorStatus == 409
        ) {
          this.credentialsExisting(errorStatus);
        } else {
          if (this.authenticationResponse.status == 200) {
            console.log(this.authenticationResponse);
            let tokenJwt = this.authenticationResponse.data["x-access-token"];
            localStorage.setItem("token", tokenJwt);
            localStorage.setItem("id", this.authenticationResponse.data.ID);
            localStorage.setItem(
              "username",
              this.authenticationResponse.data.USERNAME
            );
            localStorage.setItem(
              "email",
              this.authenticationResponse.data.EMAIL
            );
            localStorage.setItem("isLogged", true);
            localStorage.setItem("role", this.authenticationResponse.data.ROLE);
            this.$router.push({
              path: "/welcome",
            });
          } else {
            alert("Sorry! there is an error");
          }
        }
      }
    },
    validateLogin: function () {
      if (!this.usernameLogin) {
        this.validation.invalid.usernameLogin = "Please type your username.";
      } else if (this.usernameLogin.length < 5) {
        this.usernameLoginError = true;
        this.validation.invalid.usernameLogin =
          "Are you sure you typed the correct username? Username should have min. 5 characters.";
        this.validation.valid.usernameLogin = "";
      } else {
        this.usernameLoginError = false;
        this.validation.valid.usernameLogin = "Username is OK.";
        this.validation.invalid.usernameLogin = "";
      }

      if (!this.passwordLogin) {
        this.passwordLoginError = true;
        this.validation.invalid.passwordLogin = "Please type your password.";
      } else if (this.passwordLogin.length < 8) {
        this.passwordLoginError = true;
        this.validation.invalid.passwordLogin =
          "This is not the right password. Password should have min. 8 characters.";
        this.validation.valid.passwordLogin = "";
      } else {
        this.passwordLoginError = false;
        this.validation.valid.passwordLogin = "Password is fine.";
        this.validation.invalid.passwordLogin = "";
      }

      this.$forceUpdate();
      if (this.usernameLoginError || this.passwordLoginError) {
        return false;
      } else {
        return true;
      }
    },

    validateRegistration: function () {
      if (!this.email) {
        this.validation.invalid.email = "Please type your email.";
      } else if (
        this.email.length < 10 ||
        this.email.search("@") == -1 ||
        this.email.search(".") == -1
      ) {
        this.emailError = true;
        this.validation.invalid.email = "Please provide a valid email address.";
        this.validation.valid.email = "";
      } else {
        this.emailError = false;
        this.validation.valid.email = "Email is OK.";
        this.validation.invalid.email = "";
      }
      if (!this.usernameRegistration) {
        this.validation.invalid.usernameRegistration =
          "Please type your username.";
      } else if (this.usernameRegistration.length < 5) {
        this.usernameRegistrationError = true;
        this.validation.invalid.usernameRegistration =
          "Are you sure you types the correct username? Username should have min. 5 characters.";
        this.validation.valid.usernameRegistration = "";
      } else {
        this.usernameRegistrationError = false;
        this.validation.valid.usernameRegistration = "Username is OK.";
        this.validation.invalid.usernameRegistration = "";
      }

      if (!this.passwordRegistration) {
        this.passwordRegistrationError = true;
        this.validation.invalid.passwordRegistration =
          "Please type your password.";
      } else if (this.passwordRegistration.length < 8) {
        this.passwordRegistrationError = true;
        this.validation.invalid.passwordRegistration =
          "This is the right password. Password should have min. 8 characters.";
        this.validation.valid.passwordRegistration = "";
      } else {
        this.passwordRegistrationError = false;
        this.validation.valid.passwordRegistration = "Password is fine.";
        this.validation.invalid.passwordRegistration = "";
      }

      if (!this.confirmPassword) {
        this.confirmPasswordError = true;
        this.validation.invalid.confirmPassword = "Please type your password.";
      } else if (
        this.confirmPassword != this.passwordRegistration ||
        this.confirmPassword.length < 8
      ) {
        this.confirmPasswordError = true;
        this.validation.invalid.confirmPassword =
          "The passwords are different. Please  type again";
        this.validation.valid.confirmPassword = "";
      } else {
        this.confirmPasswordError = false;
        this.validation.valid.confirmPassword = "Password is fine.";
        this.validation.invalid.confirmPassword = "";
      }

      this.$forceUpdate();
      if (
        this.emailError ||
        this.usernameRegistrationError ||
        this.passwordRegistrationError ||
        this.confirmPasswordError
      ) {
        return false;
      } else {
        return true;
      }
    },

    wrongCredentials: function () {
      this.showWrongCredentials = true;
      this.wrongCredentialsMessage =
        "The username and/or password you entered are incorrect.";
      this.validation.valid.usernameLogin = "";
      this.validation.valid.passwordLogin = "";
      this.validation.invalid.usernameLogin =
        "Check if you typed the correct username";
      this.validation.invalid.passwordLogin =
        "Check if you typed the correct password";
    },
    credentialsExisting: function (status) {
      if (status == 404 || status == 401) {
        this.showCredentialsExisting = false;
        this.showErrorMessage = true;
        this.validation.valid.usernameRegistration = "";
        this.validation.invalid.usernameRegistration = " ";
        this.validation.valid.passwordRegistration = "";
        this.validation.invalid.passwordRegistration = " ";
        this.validation.valid.email = "";
        this.validation.invalid.email = " ";
        this.validation.valid.confirmPassword = "";
        this.validation.invalid.confirmPassword = " ";
        this.errorMessage = "Sorry! An error occured. Try another email";
      } else {
        this.errorMessage = "";
        this.showCredentialsExisting = true;
        if (status == 409) {
          this.validation.valid.usernameRegistration = "";
          this.validation.invalid.usernameRegistration =
            "This username is already used";
          this.credentialsExistingMessage =
            "The username you entered is already used.";
        } else {
          this.validation.valid.email = "";
          this.validation.invalid.email = "This email is already used";
          this.credentialsExistingMessage =
            "The email you entered is already registered.";
        }
      }
    },
  },
};
</script>
<style scoped>
.illustration {
  margin-top: 10%;
  text-align: center;
  padding: 0 0 15px;
  font-size: 100px;
  color: #ff6f3c;
}

h1 {
  color: #ff6f3c;
  font-family: "Shadows Into Light", cursive;
  font-weight: bold;
}
h2 {
  color: #ff9a3c;
}
a {
  color: #ff6f3c;
}
a:hover {
  color: #ff9a3c;
}
p {
  line-height: 1rem;
  margin-top: 10px;
}

.card {
  padding: 10px;
  box-shadow: rgba(0, 0, 0, 0.56) 0px 22px 70px 4px;
  margin-top: 20%;
}

.form-group {
  margin-bottom: 20px;
}

.myLoginPage {
  align-items: center;
  display: flex;
  height: calc(100% - 100px);
}

.error {
  animation-name: errorShake;
  animation-duration: 0.3s;
}

@keyframes errorShake {
  25% {
    transform: translatex(4px);
  }
  50% {
    transform: translatex(-4px);
  }
  75% {
    transform: translatex(4px);
  }
}

.invalid-credentials {
  width: 100%;
  margin-top: 0.25rem;
  font-size: 0.875em;
  color: #dc3545;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
