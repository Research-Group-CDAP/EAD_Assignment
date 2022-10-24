const express = require("express");
const cors = require("cors");
const connectDB = require("./config/db");

const app = express();

//Connect Database
connectDB();

//Using Cors
app.use(cors());

//Init Middleware( include  bodyparser through express)
app.use(express.json({ extended: false }));

app.get("/", (req, res) => res.send("EAD Backend Api Running"));

//Define Routes
app.use("/user", require("./routes/User.route"));
app.use("/fuelstation", require("./routes/FuelStation.route"));

const PORT = process.env.PORT || 5600;

app.listen(PORT, () => console.log(`Server started on port ${PORT}`));