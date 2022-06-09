'use strict'
const path = require('path')
module.exports = {
     publicPath: process.env.NODE_ENV === 'production'
        ? '/WhatATrip/'
        : '/',
    
    dev: {
        assetsSubDirectory: 'static',
        assetsPublicPath: '/WhatATrip/',
        // assetsPublicPath: '/',
        proxyTable: {},

        // Various Dev Server settings
        host: 'localhost',
        port: 8081, 
    },

    build: {
        index: path.resolve(__dirname, '..dist/index.html'),
    
        //Paths
        assetsRoot: path.resolve(__dirname, '..dist/'),
        assetsSubDirectory: 'static',
        assetsPublicPath: '/WhatATrip/',
        productionSourceMap: true,
    },

    
}
