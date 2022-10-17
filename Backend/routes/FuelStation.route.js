const express = require("express");
const router = express.Router();

const {
    addFuelStation,
    getQueueDetailsFuelStation,
    addVehicleIntoFuelStation,
    exitVehiclefromFuelStation,
} = require("../controller/FuelStation.controller");

router.post("/addFuelStation", addFuelStation);
router.get("/getQueueDetailsFuelStation/:fuelStationId", getQueueDetailsFuelStation);
router.put("/addVehicleIntoFuelStation/:fuelStationId", addVehicleIntoFuelStation);
router.put("/exitVehiclefromFuelStation/:fuelStationId", exitVehiclefromFuelStation);

module.exports = router;
