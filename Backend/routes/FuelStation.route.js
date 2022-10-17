const express = require("express");
const router = express.Router();

const {
    getAllFuelStations,
    registerFuelStation,
    getQueueDetailsFuelStation,
    addVehicleIntoFuelStation,
    exitVehiclefromFuelStation,
    updatedFuelStatus,
    loginFuelStation
} = require("../controller/FuelStation.controller");

router.post("/loginFuelStation", loginFuelStation);
router.get("/getAllFuelStations", getAllFuelStations);
router.post("/registerFuelStation", registerFuelStation);
router.get("/getQueueDetailsFuelStation/:fuelStationId", getQueueDetailsFuelStation);
router.put("/addVehicleIntoFuelStation/:fuelStationId", addVehicleIntoFuelStation);
router.put("/exitVehiclefromFuelStation/:fuelStationId", exitVehiclefromFuelStation);
router.put("/updatedFuelStatus/:fuelStationId", updatedFuelStatus);

module.exports = router;
