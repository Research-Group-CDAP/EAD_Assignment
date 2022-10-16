const bcrypt = require("bcryptjs");
const User = require("../models/User.model");
const jwt = require("jsonwebtoken");
const config = require("config");
const { exec } = require("child_process");
const axios = require("axios");

//get User details
const getUserDetails = async (req, res) => {
  try {
    //get user details
    //-password : dont return the pasword
    const user = await User.findOne({ email: req.user.email }).select(
      "-password"
    );
    res.json(user);
  } catch {
    console.log(err.message);
    res.status(500).send("Server Error");
  }
};

//Authenticate admin and get token
const loginUser = async (req, res) => {
  const { email, password } = req.body;

  try {
    //See if user Exist
    let user = await User.findOne({ email });

    if (!user) {
      return res.status(400).json({ errors: [{ msg: "Invalid Credentials" }] });
    }

    //match the user email and password

    const isMatch = await bcrypt.compare(password, user.password);

    if (!isMatch) {
      return res.status(400).json({ errors: [{ msg: "Invalid Credentials" }] });
    } else {
      return res.json(user);
    }
  } catch (err) {
    //Something wrong with the server
    console.error(err.message);
    return res.status(500).send("Server Error");
  }
};

//Register User
const registerUser = async (req, res) => {
  const { fullName, email, password } = req.body;

  try {
    //See if user Exist
    let user = await User.findOne({ email });

    if (user) {
      return res.status(400).json({ errors: [{ msg: "User already exist" }] });
    }

    //create a Site User instance
    user = new User({
      fullName,
      email,
      password,
    });

    //Encrypt Password

    //10 is enogh..if you want more secured.user a value more than 10
    const salt = await bcrypt.genSalt(10);

    //hashing password
    user.password = await bcrypt.hash(password, salt);

    //Return jsonwebtoken
    const payload = {
      user: {
        email: user.email,
      },
    };

    jwt.sign(
      payload,
      config.get("jwtSecret"),
      { expiresIn: 360000 },
      (err, token) => {
        if (err) throw err;
        //save user to the database
        user.token = token;
        return user
          .save()
          .then((registeredUser) => {
            return res.json(registeredUser);
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

const updateUser = async (request, response) => {
  return await User.findById(request.body.Id)
    .then(async (userDetails) => {
      if (userDetails) {
        if (request.body.fullName) {
          userDetails.fullName = request.body.fullName;
        }
        if (request.body.azureUserName) {
          userDetails.azureUserName = request.body.azureUserName;
        }
        if (request.body.azurePassword) {
          userDetails.azurePassword = request.body.azurePassword;
        }
        if (request.body.resourceGroup) {
          userDetails.resourceGroup = request.body.resourceGroup;
        }
        if (request.body.phoneNumber) {
          userDetails.phoneNumber = request.body.phoneNumber;
        }
        if (request.body.clusterName) {
          userDetails.clusterName = request.body.clusterName;
        }
        if (request.body.azureSubscriptionId) {
          userDetails.azureSubscriptionId = request.body.azureSubscriptionId;
        }

        if (request.body.password) {
          //Encrypt Password

          //10 is enogh..if you want more secured.user a value more than 10
          const salt = await bcrypt.genSalt(10);

          //hashing password
          userDetails.password = await bcrypt.hash(request.body.password, salt);
        }
        return await userDetails
          .save()
          .then((updatedUser) => {
            return response.json(updatedUser);
          })
          .catch((error) => {
            return response.json(error);
          });
      } else {
        return response.json("User Not Found");
      }
    })
    .catch((error) => {
      return response.json(error);
    });
};

const updatePluginList = async (request, response) => {
  return await User.findById(request.body.Id)
    .then(async (userDetails) => {
      if (userDetails) {
        if (request.body.plugin && request.body.type) {
          if (request.body.type === "ADD") {
            userDetails.plugins.push(request.body.plugin);
            userDetails.plugins = userDetails.plugins;
          } else {
            userDetails.plugins = userDetails.plugins.filter(
              (e) => e !== request.body.plugin
            );
          }
        }

        return await userDetails
          .save()
          .then((updatedUser) => {
            return response.json(updatedUser);
          })
          .catch((error) => {
            return response.json(error);
          });
      } else {
        return response.json("User Not Found");
      }
    })
    .catch((error) => {
      return response.json(error);
    });
};

const installIstio = async (request, response) => {
  return await User.findById(request.params.userId)
    .then(async (userDetails) => {
      if (userDetails) {
        axios
          .post("http://localhost:4003/install/istio")
          .then(async (istionResponse) => {
            console.log(istionResponse.data.installed);
            userDetails.isIstioInstalled = istionResponse.data.installed;
            return await userDetails
              .save()
              .then((updatedUser) => {
                return response.json(istionResponse.data);
              })
              .catch((error) => {
                return response.json(error);
              });
          })
          .catch((error) => {
            return response.json(error);
          });
      } else {
        return response.json("User Not Found");
      }
    })
    .catch((error) => {
      return response.json(error);
    });
};

const configurePrometheus = async (request, response) => {
  return await User.findById(request.params.userId)
    .then(async (userDetails) => {
      if (userDetails) {
        axios
          .post("http://localhost:4003/configure/prometheus")
          .then(async (prometheusResponse) => {
            console.log(prometheusResponse.data.configured);
            userDetails.isPrometheusConfigured =
              prometheusResponse.data.configured;
            return await userDetails
              .save()
              .then((updatedUser) => {
                return response.json(prometheusResponse.data);
              })
              .catch((error) => {
                return response.json(error);
              });
          })
          .catch((error) => {
            return response.json(error);
          });
      } else {
        return response.json("User Not Found");
      }
    })
    .catch((error) => {
      return response.json(error);
    });
};

const activePrometheus = async (request, response) => {
  return axios
    .post("http://localhost:4003/active/prometheus")
    .then(async (prometheusResponse) => {
      return response.json(prometheusResponse.data.active);
    })
    .catch((error) => {
      return response.json(error);
    });
};

const deleteUserPermenently = async (request, response) => {
  return await User.findByIdAndDelete(request.params.userId)
    .then((user) => {
      return response.json(user);
    })
    .catch((error) => {
      return response.json(error);
    });
};

const logintoCluster = async (request, response) => {
  await exec(
    `az login -u ${request.body.azureUserName} -p ${request.body.azurePassword}`,
    (error, stdout, stderr) => {
      if (error) {
        response.json({ connected: false });
      } else {
        console.log(`stdout: ${stdout}`);
        console.log(`stderr: ${stderr}`);

        exec(
          `az account set --subscription ${request.body.azureSubscriptionId}`,
          (error, stdout, stderr) => {
            if (error) {
              response.json({ connected: false });
            } else {
              console.log(`stdout: ${stdout}`);
              console.log(`stderr: ${stderr}`);
              exec(
                `az aks get-credentials --resource-group ${request.body.resourceGroup} --name ${request.body.clusterName}`,
                (error, stdout, stderr) => {
                  if (error) {
                    response.json({ connected: false });
                  } else {
                    console.log(`stdout: ${stdout}`);
                    console.log(`stderr: ${stderr}`);
                    exec(`pm2 restart kube-server`, (error, stdout, stderr) => {
                      if (error) {
                        response.json({ connected: false });
                      } else {
                        console.log(`stdout: ${stdout}`);
                        console.log(`stderr: ${stderr}`);
                        exec(
                          `pm2 restart matrics-server`,
                          (error, stdout, stderr) => {
                            if (error) {
                              response.json({ connected: false });
                            } else {
                              console.log(`stdout: ${stdout}`);
                              console.log(`stderr: ${stderr}`);
                              response.json({ connected: true });
                            }
                          }
                        );
                      }
                    });
                  }
                }
              );
            }
          }
        );
      }
    }
  );
};

module.exports = {
  getUserDetails,
  loginUser,
  registerUser,
  updateUser,
  deleteUserPermenently,
  logintoCluster,
  updatePluginList,
  installIstio,
  configurePrometheus,
  activePrometheus
};
