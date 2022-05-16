<template>
  <div class="container-login" :class="{ 'sign-up-active': signUp }">
    <div class="overlay-container-login">
      <div class="overlay-login">
        <div class="overlay-left-login">
          <h2>Welcome Back!</h2>
          <p>Please login with your personal info</p>
          <button class="invert-login" id="signIn" @click="signUp = !signUp">
            Sign In
          </button>
        </div>
        <div class="overlay-right-login">
          <h2>Hello, Friend!</h2>
          <p>Please enter your personal details</p>
          <button class="invert-login" id="signUp" @click="signUp = !signUp">
            Sign Up
          </button>
        </div>
      </div>
    </div>
    <form class="sign-up-login" action="#">
      <h2>Create login</h2>
      <div>Use your email for registration</div>
      <input type="text" placeholder="Name" />
      <input type="email" placeholder="Email" />
      <input type="password" placeholder="Password" />
      <button>Sign Up</button>
    </form>
    <form class="sign-in-login" action="#">
      <h2>Sign In</h2>
      <div>Use your account</div>
      <input type="email" placeholder="Email" />
      <input type="password" placeholder="Password" />
      <a href="#">Forgot your password?</a>
      <button>Sign In</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "login-component",
  data() {
    return {
      username: null,
      password: null,
      signUp: false,
    };
  },
  methods: {
    accedi: async function () {
      let valori = { USERNAME: this.username, PASSWORD: this.password };
      let data = await fetch("api/trips/auth", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(valori),
      });
      console.log(data);
      console.log("data.data");
      console.log(data.data);
      console.log(`data.status: ${data.status}`);
      if (data.status == 200) {
        this.$router.push({
          path: "/dashboard",
        });
      } else {
        alert("Credendiali sbalgiate");
      }
    },
  },

  mounted() {},
};
</script>
