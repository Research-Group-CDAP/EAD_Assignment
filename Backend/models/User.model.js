const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const UserSchema = new Schema(
  {
    username: { type: String, required: true },
    password: { type: String, required: true },
    email: { type: String, required: true },
    vehicleNumber: { type: String, required: true },
    vehicleType: { type: String, required: true },
  },
  { timestamps: true }
);

module.exports = User = mongoose.model("User", UserSchema);
