const User = require("../models/User.model");
const FuelStation = require("../models/FuelStation.model");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const config = require("config");

//get All Fuel Station
const getAllFuelStations = async (req, res) => {
  try {
    const allFuelStations = await FuelStation.find();
    res.json(allFuelStations);
  } catch (err) {
    console.log(err.message);
    res.status(500).send("Server Error");
  }
};

//get All Fuel Station By Name
const getAllNamesOfFuelStations = async (req, res) => {
  try {
    const allFuelStations = await FuelStation.find();
    tempArray = [];
    await allFuelStations.forEach((fuelStation) => {
      console.log(fuelStation.fuelStationName);
      tempArray.push(fuelStation.fuelStationName);
    });
    res.json(tempArray);
  } catch (err) {
    console.log(err.message);
    res.status(500).send("Server Error");
  }
};

//get Queue Details Fuel Station
const getQueueDetailsFuelStation = async (req, res) => {
  try {
    const queueDetails = await FuelStation.findOne({
      fuelStationName: req.body.fuelStationName,
    });

    queueDetails.queueCount = queueDetails.presentVehicleLogs.length;
    if (queueDetails.isFuelHave) {
      queueDetails.fuelStatus = "Fuel Have";
    } else {
      queueDetails.fuelStatus = "Fuel Over";
    }

    res.json(queueDetails);
  } catch (err) {
    console.log(err.message);
    res.status(500).send("Server Error");
  }
};

const addFuelStation = async (req, res) => {
  const { fuelStationName } = req.body;
  let isFuelHave = false;

  fuelStation = new FuelStation({
    fuelStationName,
    isFuelHave,
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

//Register Fuel Station
const registerFuelStation = async (req, res) => {
  const { email, password, fuelStationName } = req.body;
  let isFuelHave = false;
  try {
    //See if user Exist
    let fuelStation = await FuelStation.findOne({ email });

    if (fuelStation) {
      return res
        .status(400)
        .json({ errors: [{ msg: "FuelStation already exist" }] });
    }

    fuelStation = new FuelStation({
      email,
      password,
      fuelStationName,
      isFuelHave,
    });

    //Encrypt Password

    //10 is enogh..if you want more secured.user a value more than 10
    const salt = await bcrypt.genSalt(10);

    //hashing password
    fuelStation.password = await bcrypt.hash(password, salt);

    //Return jsonwebtoken
    const payload = {
      user: {
        email: fuelStation.email,
      },
    };

    jwt.sign(
      payload,
      config.get("jwtSecret"),
      { expiresIn: 360000 },
      (err, token) => {
        if (err) throw err;
        //save user to the database
        fuelStation.token = token;
        queueDetails.fuelStatus = "Fuel Over";
        return fuelStation
          .save()
          .then((registeredFuelStation) => {
            return res.json(registeredFuelStation);
          })
          .catch((error) => {
            return res.json(error);
          });
      }
    );
  } catch (err) {
    //Something wrong with the server
    console.error(err.message);
    return res.status(500).send("Server Error");
  }
};

//Authenticate admin and get token
const loginFuelStation = async (req, res) => {
  const { email, password } = req.body;

  try {
    //See if user Exist
    let fuelStation = await FuelStation.findOne({ email });

    if (!fuelStation) {
      return res.status(400).json({ errors: [{ msg: "Invalid Credentials" }] });
    }

    //match the user email and password

    const isMatch = await bcrypt.compare(password, fuelStation.password);

    if (!isMatch) {
      return res.status(400).json({ errors: [{ msg: "Invalid Credentials" }] });
    } else {
      if (fuelStation.isFuelHave) {
        fuelStation.fuelStatus = "Fuel Have";
      } else {
        fuelStation.fuelStatus = "Fuel Over";
      }

      return res.json(fuelStation);
    }
  } catch (err) {
    //Something wrong with the server
    console.error(err.message);
    return res.status(500).send("Server Error");
  }
};

const addVehicleIntoFuelStation = async (request, response) => {
  return await FuelStation.findOne({
    fuelStationName: request.body.fuelStationName,
  })
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
  return await FuelStation.findOne({
    fuelStationName: request.body.fuelStationName,
  })
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
  return await FuelStation.findOne({
    fuelStationName: request.body.fuelStationName,
  })
    .then(async (FuelStationDetails) => {
      if (FuelStationDetails) {
        if (request.body.isFuelHave === "true") {
          FuelStationDetails.isFuelHave = true;
        } else {
          FuelStationDetails.isFuelHave = false;
        }

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
  registerFuelStation,
  getQueueDetailsFuelStation,
  addVehicleIntoFuelStation,
  exitVehiclefromFuelStation,
  updatedFuelStatus,
  loginFuelStation,
  getAllNamesOfFuelStations,
};
