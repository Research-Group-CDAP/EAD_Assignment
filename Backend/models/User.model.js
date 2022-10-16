const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const UserSchema = new Schema(
  {
    username: { type: String, required: false },
    password: { type: String, required: false },
    email: { type: String, required: false },
    vehicleNumber: { type: String, required: false },
    vehicleType: { type: String, required: false },
  },
  { timestamps: true }
);

module.exports = User = mongoose.model("User", UserSchema);
