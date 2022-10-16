const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const FuelStationSchema = new Schema(
  {
    fuelStationName: { type: String, required: true },
    presentVehicleLogs: [
      {
        vehicleType: { type: String, required: true },
        vehicleNumber: { type: String, required: true },
        inTime: { type: Date, required: true },
      },
    ],
    pastVehicleLogs: [
      {
        vehicleType: { type: String, required: true },
        vehicleNumber: { type: String, required: true },
        outTime: { type: Date, required: true },
      },
    ],
  },
  { timestamps: true }
);

module.exports = FuelStation = mongoose.model("FuelStation", FuelStationSchema);
