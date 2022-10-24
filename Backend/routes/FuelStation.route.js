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
router.get("/getQueueDetailsFuelStation", getQueueDetailsFuelStation);
router.put("/addVehicleIntoFuelStation", addVehicleIntoFuelStation);
router.put("/exitVehiclefromFuelStation", exitVehiclefromFuelStation);
router.put("/updatedFuelStatus", updatedFuelStatus);

module.exports = router;
