const User = require("../models/User.model");
const FuelStation = require("../models/FuelStation.model");

//get Queue Details Fuel Station
const getQueueDetailsFuelStation = async (req, res) => {
  try {
    const queueDetails = await FuelStation.findOne({
      fuelStationName: req.params.fuelStationName,
    });
    res.json(queueDetails);
  } catch {
    console.log(err.message);
    res.status(500).send("Server Error");
  }
};

const addFuelStation = async (req, res) => {
  const { fuelStationName } = req.body;

  fuelStation = new FuelStation({
    fuelStationName,
  });

  return fuelStation
    .save()
    .then((fuelStation) => {
      return res.json(fuelStation);
    })
    .catch((error) => {
      return res.json(error);
    });
};

const addVehicleIntoFuelStation = async (request, response) => {
  return await FuelStation.findById(request.params.fuelStationName)
    .then(async (FuelStationDetails) => {
      if (FuelStationDetails) {
        let vehicleDetails = {
          vehicleType: request.body.vehicleType,
          vehicleNumber: request.body.vehicleNumber,
          inTime: new Date(),
        };

        FuelStationDetails.presentVehicleLogs =
          FuelStationDetails.presentVehicleLogs.push(vehicleDetails);

        return await FuelStationDetails.save()
          .then((updatedFuelStation) => {
            return response.json(updatedFuelStation);
          })
          .catch((error) => {
            return response.json(error);
          });
      } else {
        return response.json("FuelStation Not Found");
      }
    })
    .catch((error) => {
      return response.json(error);
    });
};

const exitVehiclefromFuelStation = async (request, response) => {
  return await FuelStation.findById(request.params.fuelStationName)
    .then(async (FuelStationDetails) => {
      if (FuelStationDetails) {
        FuelStationDetails.presentVehicleLogs =
          FuelStationDetails.presentVehicleLogs.filter(function (vehicle) {
            return vehicle.vehicleNumber !== request.body.vehicleNumber;
          });

        let vehicleDetails = {
          vehicleType: request.body.vehicleType,
          vehicleNumber: request.body.vehicleNumber,
          outTime: new Date(),
        };

        FuelStationDetails.pastVehicleLogs =
          FuelStationDetails.pastVehicleLogs.push(vehicleDetails);

        return await FuelStationDetails.save()
          .then((updatedFuelStation) => {
            return response.json(updatedFuelStation);
          })
          .catch((error) => {
            return response.json(error);
          });
      } else {
        return response.json("FuelStation Not Found");
      }
    })
    .catch((error) => {
      return response.json(error);
    });
};

module.exports = {
  addFuelStation,
  getQueueDetailsFuelStation,
  addVehicleIntoFuelStation,
  exitVehiclefromFuelStation,
};
