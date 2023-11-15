const mongoose = require('mongoose');

const ComicSchema =new mongoose.Schema({
name:{
    type: String,
    require:true
},
title:{
    type: String,
    require: true
},
image:{
    type: String,
    require: true
},
chapter:{
    type: String,
    require: true
}
});
const Comic= mongoose.model('comic',ComicSchema);
module.exports=Comic;