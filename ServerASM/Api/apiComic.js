var express=require('express');
var router=express.Router();
var app = express();
const Comic = require('../Model/modelComic');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// const imagePath='./uploads';
// app.use('/uploads',express.static(imagePath));

const uri='mongodb+srv://datbntph19949:thanhdat12345@cluster0.8at6t1m.mongodb.net/comic';

router.get('/', async (req, res) => {
    await mongoose.connect(uri);
    try {
        const comic= await Comic.find();
        res.json(comic);
       } catch (error) {
        res.status(500).json({message: error.message});
       }
    
})

router.post('/', async (req, res) => {
    await mongoose.connect(uri);
    try {
        const newComic= new Comic(req.body);
        const comic = await newComic.save();
        res.status(201).json(comic);
      } catch (err) {
        res.status(400).json({ message: err.message });
      }
})

router.put('/:id', async (req, res) => {
    await mongoose.connect(uri);
    const comicId = req.params.id;
    const updatedComic = req.body;
    try {
      const comic = await Comic.findByIdAndUpdate(comicId, updatedComic, { new: true });
      res.json(comic);
    } catch (error) {
      res.status(500).json({ error: 'Lỗi server' });
    }
});
 
router.delete('/:id', async (req, res) => {
    await mongoose.connect(uri);
    const comicId = req.params.id;
    try {
      const deletedComic = await Comic.findByIdAndDelete(comicId);
      if (!deletedComic) {
        res.status(404).json({ error: 'Không tìm thấy comic' });
        return;
      }
      res.json({ message: 'Xóa comic thành công' });
    } catch (error) {
      console.log(error);
      res.status(500).json({ error: 'Lỗi server' });
    }
});


module.exports = router;