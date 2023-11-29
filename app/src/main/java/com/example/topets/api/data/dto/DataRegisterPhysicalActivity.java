package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class DataRegisterPhysicalActivity {
    @SerializedName("dataRegisterCommonDetails")
    DataRegisterActivity dataRegisterActivity;
    @SerializedName("dataRegisterPhysicalActivity")
    DataRegisterPhysicalActivityDetails dataRegisterPhysicalActivityDetails;
    DataRegisterReminder dataRegisterReminder;

    public DataRegisterPhysicalActivity(
            String name, String deviceId, String petId,
            String local) {
        this.dataRegisterActivity = new DataRegisterActivity(name, deviceId, petId);
        this.dataRegisterPhysicalActivityDetails = new DataRegisterPhysicalActivityDetails(local);
    }

    public void setReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String reminderDescription){
        this.dataRegisterReminder = new DataRegisterReminder(dateTime, activityType, periodic, recurrenceType, reminderDescription);
    }

    private class DataRegisterPhysicalActivityDetails {
        String local;

        public DataRegisterPhysicalActivityDetails(String local) {
            this.local = local;
        }

        @Override
        public String toString() {
            return "DataRegisterPhysicalActivityDetails{" +
                    "local='" + local + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DataRegisterPhysicalActivity{" +
                "dataRegisterActivity=" + dataRegisterActivity +
                ", dataRegisterPhysicalActivityDetails=" + dataRegisterPhysicalActivityDetails +
                ", dataRegisterReminder=" + dataRegisterReminder +
                '}';
    }
}
