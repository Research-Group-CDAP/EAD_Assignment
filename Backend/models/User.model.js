const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const UserSchema = new Schema(
  {
    fullName: { type: String, required: false },
    password: { type: String, required: false },
    email: { type: String, required: false },
    azureUserName: { type: String, required: false },
    azurePassword: { type: String, required: false },
    resourceGroup: { type: String, required: false },
    clusterName: { type: String, required: false },
    azureSubscriptionId :{ type: String, required: false },
    token: { type: String, required: false },
    plugins: {
      type: [String],
      default: ["Fast Builder"],
      required: false
    },
    isIstioInstalled: { type: Boolean, required: false },
    isPrometheusConfigured: { type: Boolean, required: false },
  },
  { timestamps: true }
);

module.exports = User = mongoose.model("User", UserSchema);
