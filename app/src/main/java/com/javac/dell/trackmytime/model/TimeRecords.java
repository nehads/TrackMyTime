package com.javac.dell.trackmytime.model;

public class TimeRecords {
     private String dates, checkInTime, checkOutTime;

        public TimeRecords(String dates, String checkInTime, String checkOutTime){
        this.dates = dates;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getDates() {
        return dates;
    }
}
