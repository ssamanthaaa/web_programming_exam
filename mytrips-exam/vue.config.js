// const { defineConfig } = require("@vue/cli-service");
// module.exports = defineConfig({
//   transpileDependencies: true,
// });
process.env.NODE_ENV === "production";
module.exports = {
  publicPath: process.env.NODE_ENV === "production" ? "/WPExamProject/" : "/",

  // pages: {
  //     index: {
  //         entry: 'src/main.js',
  //         template: 'public/index.html',
  //         filename: 'index.html',
  //         title: 'Index Page',
  //     }
  // }
};
