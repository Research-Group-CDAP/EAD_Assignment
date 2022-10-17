const User = require("../models/User.model");
const FuelStation = require("../models/FuelStation.model");

//get All Fuel Station
const getAllFuelStations = async (req, res) => {
  try {
    const allFuelStations = await FuelStation.find();
    res.json(allFuelStations);
  } catch {
    console.log(err.message);
    res.status(500).send("Server Error");
  }
};

//get Queue Details Fuel Station
const getQueueDetailsFuelStation = async (req, res) => {
  try {
    const queueDetails = await FuelStation.findById(req.params.fuelStationId);
    res.json(queueDetails);
  } catch {
    console.log(err.message);
    res.status(500).send("Server Error");
  }
};

const addFuelStation = async (req, res) => {
  const { fuelStationName } = req.body;
  let isFuelHave = false;

  fuelStation = new FuelStation({
    fuelStationName,
    isFuelHave
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
  return await FuelStation.findById(request.params.fuelStationId)
    .then(async (FuelStationDetails) => {
      if (FuelStationDetails) {
        let vehicleDetails = {
          vehicleType: request.body.vehicleType,
          vehicleNumber: request.body.vehicleNumber,
          inTime: new Date(),
        };

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
  return await FuelStation.findById(request.params.fuelStationId)
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

const updatedFuelStatus = async (request, response) => {
  return await FuelStation.findById(request.params.fuelStationId)
    .then(async (FuelStationDetails) => {
      if (FuelStationDetails) {

        FuelStationDetails.isFuelHave = request.body.isFuelHave

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
  getAllFuelStations,
  addFuelStation,
  getQueueDetailsFuelStation,
  addVehicleIntoFuelStation,
  exitVehiclefromFuelStation,
  updatedFuelStatus
};
