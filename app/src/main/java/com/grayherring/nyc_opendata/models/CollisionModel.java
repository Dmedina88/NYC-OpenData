package com.grayherring.nyc_opendata.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by David on 4/1/2015.
 */
public class CollisionModel implements Parcelable {

    public static final String USELESS_DATE_STRING = "T00:00:00";
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CollisionModel> CREATOR = new Parcelable.Creator<CollisionModel>() {
        @Override
        public CollisionModel createFromParcel(Parcel in) {
            return new CollisionModel(in);
        }

        @Override
        public CollisionModel[] newArray(int size) {
            return new CollisionModel[size];
        }
    };
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
    public String contributingVehical1;
    @SerializedName("number_of_motorist_killed")
    public String motoristKilled;
    @SerializedName("number_of_persons_injured")
    public String personsInjured;
    @SerializedName("contributing_factor_vehicle_3")
    public String contributingVehical3;
    @SerializedName("contributing_factor_vehicle_2")
    public String contributingVehical2;
    public float latitude;

    protected CollisionModel(Parcel in) {
        personsKilled = in.readString();
        vehicleCode1 = in.readString();
        zipCode = in.readString();
        vehicleCode2 = in.readString();
        moteristInjered = in.readString();
        date = in.readString();
        offStreetName = in.readString();
        time = in.readString();
        onStreetName = in.readString();
        pedestriansInjered = in.readString();
        cyclistKilled = in.readString();
        longitude = in.readFloat();
        vehicleCode3 = in.readString();
        cyclistInjured = in.readString();
        uniqueKey = in.readString();
        borough = in.readString();
        contributingVehical1 = in.readString();
        motoristKilled = in.readString();
        personsInjured = in.readString();
        contributingVehical3 = in.readString();
        contributingVehical2 = in.readString();
        latitude = in.readFloat();
    }

//parcel stuff

    public String getPrettyDate() {
        return date.replace(USELESS_DATE_STRING, "");
    }

    public String report() {
        return
                "ZipCode: " + zipCode + '\n' +
                        "Borough: " + borough + '\n' +
                        "Off StreetName: " + offStreetName + '\n' +
                        "On StreetName: " + onStreetName + '\n' +
                        "Date: " + getPrettyDate() + '\n' +
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(personsKilled);
        dest.writeString(vehicleCode1);
        dest.writeString(zipCode);
        dest.writeString(vehicleCode2);
        dest.writeString(moteristInjered);
        dest.writeString(date);
        dest.writeString(offStreetName);
        dest.writeString(time);
        dest.writeString(onStreetName);
        dest.writeString(pedestriansInjered);
        dest.writeString(cyclistKilled);
        dest.writeFloat(longitude);
        dest.writeString(vehicleCode3);
        dest.writeString(cyclistInjured);
        dest.writeString(uniqueKey);
        dest.writeString(borough);
        dest.writeString(contributingVehical1);
        dest.writeString(motoristKilled);
        dest.writeString(personsInjured);
        dest.writeString(contributingVehical3);
        dest.writeString(contributingVehical2);
        dest.writeFloat(latitude);
    }
}
