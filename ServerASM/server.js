var express = require('express');
var app = express();
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const multer= require('multer');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

const uri='mongodb+srv://datbntph19949:thanhdat12345@cluster0.8at6t1m.mongodb.net/comic';
mongoose.connect(uri,{
    useNewUrlParser: true,
    useUnifiedTopology: true,
});

const imagePath='./uploads';
app.use('/uploads',express.static(imagePath));

var apicomic= require('./Api/apiComic');
app.use('/api/comic',apicomic);

var apiUser=require('./Api/apiUser');
app.use('/api/user',apiUser);

app.listen(8000,function() {
    console.log("Server is running on port 8000");
});