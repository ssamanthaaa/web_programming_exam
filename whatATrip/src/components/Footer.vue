<template>
  <footer class="page-footer footer">
    <!-- <div class="links"><a href="#">About me</a><a href="#">Contact me</a><a href="#">Projects</a></div> -->
    <div class="row">
      <div class="col-sm">
        <img
          class="units_logo"
          src="@/assets/img/units_logo.jpeg"
          alt="University of Trieste logo"
        />
      </div>
      <div class="col-sm">
        Contact:
        <a href="mailto:samantha.gallone@gmail.com"
          >samantha.gallone@gmail.com</a
        >
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: "Footer-component",
  props: {},
};
</script>
<style>
footer {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.units_logo {
  max-height: 100px;
  max-width: 50%;
}

div.col-sm {
  vertical-align: middle;
  margin-top: 10px;
}
</style>
