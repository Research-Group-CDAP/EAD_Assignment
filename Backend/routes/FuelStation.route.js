const express = require("express");
const router = express.Router();

const {
    getAllFuelStations,
    registerFuelStation,
    getQueueDetailsFuelStation,
    addVehicleIntoFuelStation,
    exitVehiclefromFuelStation,
    updatedFuelStatus,
    loginFuelStation,
    getAllNamesOfFuelStations
} = require("../controller/FuelStation.controller");

router.post("/loginFuelStation", loginFuelStation);
router.get("/getAllFuelStations", getAllFuelStations);
router.post("/registerFuelStation", registerFuelStation);
router.post("/getQueueDetailsFuelStation", getQueueDetailsFuelStation);
router.put("/addVehicleIntoFuelStation", addVehicleIntoFuelStation);
router.put("/exitVehiclefromFuelStation", exitVehiclefromFuelStation);
router.put("/updatedFuelStatus", updatedFuelStatus);
router.get("/getAllNamesOfFuelStations", getAllNamesOfFuelStations);

module.exports = router;
