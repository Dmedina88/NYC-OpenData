package com.grayherring.nyc_opendata.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by David on 4/1/2015.
 */
public class CollisionModel {

    // i put theses in the order the json gave me altho it makes sense
    @SerializedName("number_of_persons_killed")
    public String personsKilled;
    @SerializedName("vehicle_type_code1")
    public String vehicleCode1;
    @SerializedName("zip_code")
    public String zipCode;
    @SerializedName("vehicle_type_code2")
    public String vehicleCode2;
    //public Location location;
    @SerializedName("number_of_motorist_injured")
    public String moteristInjered;
    public String date;
    @SerializedName("off_street_name")
    public String offStreetName;
    public String time;
    @SerializedName("on_street_name")
    public String onStreetName;
    @SerializedName("number_of_pedestrians_injured")
    public String pedestriansInjered;
    @SerializedName("number_of_cyclist_killed")
    public String cyclistKilled;
    public float longitude;
    @SerializedName("vehicle_type_code3")
    public String vehicleCode3;
    @SerializedName("number_of_cyclist_injured")
    public String cyclistInjured;
    @SerializedName("unique_key")
    public String uniqueKey;
    public String borough;
    @SerializedName("contributing_factor_vehicle_1")
    public String  contributingVehical1;
    @SerializedName("number_of_motorist_killed")
    public String motoristKilled;
    @SerializedName("number_of_persons_injured")
    public String personsInjured;
    @SerializedName("contributing_factor_vehicle_3")
    public String  contributingVehical3;
    @SerializedName("contributing_factor_vehicle_2")
    public String  contributingVehical2;
    public float latitude;


    public String report() {
        return
                "ZipCode: " + zipCode + '\n' +
                "Borough: " + borough + '\n' +
                "Off StreetName: " + offStreetName + '\n' +
                "On StreetName: " + onStreetName + '\n' +
                "Date: " + date + '\n' +
                "Time: " + time + '\n' +
                "Persons Killed: " + personsKilled + '\n' +
                "Motorist Killed: " + motoristKilled + '\n' +
                "Moterist Injered: " + moteristInjered + '\n' +
                "Persons Injured: " + personsInjured + '\n' +
                "Pedestrians Killed: " + pedestriansInjered + '\n' +
                "Pedestrians Injered: " + pedestriansInjered + '\n' +
                "Cyclist Killed: " + cyclistKilled + '\n' +
                "Cyclist Injured: " + cyclistInjured;
    }


//    public class Location {
//       public boolean needs_recoding;
//       public float longitude;
//       public float latitude;
//    }
}
