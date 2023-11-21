var express=require('express');
var router=express.Router();
var app = express();
const User=require("../Model/modelUser");
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

const uri='mongodb+srv://datbntph19949:thanhdat12345@cluster0.8at6t1m.mongodb.net/comic';

router.post('/', async (req, res) => {
    const { username, password } = req.query;
  
    try {
      const user = await User.findOne({ username, password });
      if (!user) {
        res.status(404).json({ message: "User not found" });
      } else {
        res.json(user);
      }
    } catch (error) {
      res.status(500).json({ message: error.message });
    }
  });


router.post('/addUser', async (req, res) => {
    await mongoose.connect(uri);
    try {
        const newUser= new User(req.body);
        await newUser.save();

        let users = await User.find();
        res.status(201).json(users);
      } catch (err) {
        res.status(400).json({ message: err.message });
      }
})

router.put('/update/:id', async (req, res) => {
    await mongoose.connect(uri);
    const userId = req.path.id;
    const updatedUser = req.body;

    console.log(updatedUser);
    try {
     
      const user = await User.findByIdAndUpdate(userId, updatedUser, { new: true });
      res.json(user);
    } catch (error) {
      res.status(500).json({ error: 'Lỗi server' });
    }
});
 
router.delete('/:id', async (req, res) => {
    await mongoose.connect(uri);
    const userId = req.params.id;
    try {
      const deletedUser = await Comic.findByIdAndDelete(userId);
      if (!deletedUser) {
        res.status(404).json({ error: 'Không tìm thấy user' });
        return;
      }
      res.json({ message: 'Xóa user thành công' });
    } catch (error) {
      console.log(error);
      res.status(500).json({ error: 'Lỗi server' });
    }
});


module.exports = router;