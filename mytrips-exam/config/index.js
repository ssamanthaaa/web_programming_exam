'use strict'
const path = require('path')
module.exports = {
    //  publicPath: process.env.NODE_ENV === 'production'
    //     ? '/exam-rest/'
    //     : '/',
    
    dev: {
        assetsSubDirectory: 'static',
        assetsPublicPath: '/WPTripExamProject/',
        // assetsPublicPath: '/',
        proxyTable: {},

        // Various Dev Server settings
        host: 'localhost',
        port: 8080, 
    },

    build: {
        index: path.resolve(__dirname, '..dist/index.html'),
    
        //Paths
        assetsRoot: path.resolve(__dirname, '..dist/'),
        assetsSubDirectory: 'static',
        assetsPublicPath: '/WPTripExamProject/',
        productionSourceMap: true,
    },

    
}
