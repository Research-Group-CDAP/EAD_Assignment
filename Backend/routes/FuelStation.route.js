const express = require("express");
const router = express.Router();

const {
    addFuelStation,
    getQueueDetailsFuelStation,
    addVehicleIntoFuelStation,
    exitVehiclefromFuelStation,
} = require("../controller/FuelStation.controller");

router.post("/addFuelStation", addFuelStation);
router.get("/getQueueDetailsFuelStation/:fuelStationName", getQueueDetailsFuelStation);
router.put("/addVehicleIntoFuelStation/:fuelStationName", addVehicleIntoFuelStation);
router.put("/addVehicleIntoFuelStation/:fuelStationName", exitVehiclefromFuelStation);

module.exports = router;
