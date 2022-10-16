const express = require("express");
const router = express.Router();
const auth = require("../middleware/auth");

const {
  getUserDetails,
  loginUser,
  registerUser,
  updateUser,
  deleteUserPermenently,
} = require("../controller/User.controller");

router.get("/", auth, getUserDetails);
router.delete("/remove/:userId", auth, deleteUserPermenently);
router.post("/register", registerUser);
router.put("/edit", auth, updateUser);
router.post("/login", loginUser);

module.exports = router;
