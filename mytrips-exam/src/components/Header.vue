<template>
  <nav
    class="navbar navbar-dark navbar-expand-lg fixed-top bg-white portfolio-navbar gradient"
  >
    <!-- gradient -->
    <div class="container">
      <a class="navbar-brand logo" href="#">Map</a>
      <button
        data-bs-toggle="collapse"
        class="navbar-toggler"
        data-bs-target="#navbarNav"
      >
        <span class="visually-hidden">Toggle navigation</span
        ><span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <!-- <a class="nav-link active" href="index.html">Home</a> -->
            <router-link to="dashboard" class="nav-link active"
              >Home</router-link
            >
          </li>
          <li class="nav-item">
            <a class="nav-link active" href="#">Logout</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: "Header-component",
  props: {},
};
</script>
<style scoped>
.navColor {
  background-color: #1e2833;
}
</style>
