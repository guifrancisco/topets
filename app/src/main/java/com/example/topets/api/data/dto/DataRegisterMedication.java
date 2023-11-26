package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

public class DataRegisterMedication {
    @SerializedName("dataRegisterCommonDetails")
    private DataRegisterActivity dataRegisterActivity;
    @SerializedName("dataRegisterMedicine")
    private DataRegisterMedicationDetails dataRegisterMedicationDetails;
    private DataRegisterReminder dataRegisterReminder;

    public DataRegisterMedication(
            String name, String deviceId, String petId,
            String description) {
        this.dataRegisterActivity = new DataRegisterActivity(name, deviceId, petId);
        this.dataRegisterMedicationDetails = new DataRegisterMedicationDetails(description);
    }

    public void setReminder(String dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String reminderDescription){
        this.dataRegisterReminder = new DataRegisterReminder(dateTime, activityType, periodic, recurrenceType, reminderDescription);
    }
    public void setReminder(DataRegisterReminder reminder){
        this.dataRegisterReminder = reminder;
    }

    private class DataRegisterMedicationDetails {
        private String description;

        public DataRegisterMedicationDetails(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "DataRegisterMedicationDetails{" +
                    "description='" + description + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DataRegisterMedication{" +
                "dataRegisterActivity=" + dataRegisterActivity +
                ", dataRegisterMedicationDetails=" + dataRegisterMedicationDetails +
                ", dataRegisterReminder=" + dataRegisterReminder +
                '}';
    }
}
