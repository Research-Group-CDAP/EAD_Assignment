const express = require("express");
const router = express.Router();

const {
    getAllFuelStations,
    addFuelStation,
    getQueueDetailsFuelStation,
    addVehicleIntoFuelStation,
    exitVehiclefromFuelStation,
    updatedFuelStatus
} = require("../controller/FuelStation.controller");

router.get("/getAllFuelStations", getAllFuelStations);
router.post("/addFuelStation", addFuelStation);
router.get("/getQueueDetailsFuelStation/:fuelStationId", getQueueDetailsFuelStation);
router.put("/addVehicleIntoFuelStation/:fuelStationId", addVehicleIntoFuelStation);
router.put("/exitVehiclefromFuelStation/:fuelStationId", exitVehiclefromFuelStation);
router.put("/updatedFuelStatus/:fuelStationId", updatedFuelStatus);

module.exports = router;
