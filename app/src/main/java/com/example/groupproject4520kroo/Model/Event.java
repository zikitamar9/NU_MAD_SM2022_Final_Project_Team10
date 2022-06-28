package com.example.groupproject4520kroo.Model;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Event implements Serializable {

    private String name;
    private Group group;
//    private Boolean attendance;
    private String location;
    private String eventCreator;
    private String time;



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private String notes;

//    public Boolean getAttendance() {
//        return attendance;
//    }
//
//    public void setAttendance(Boolean attendance) {
//        this.attendance = attendance;
//    }
//

    public String getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(String eventCreator) {
        this.eventCreator = eventCreator;
    }

    public Event(){

    }

    public Event(String eventId, Boolean attendance) {
//
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;
//    private

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Event(String name, Group group, String location, String eventCreator, String time, String notes, String eventId) {
        this.name = name;
        this.group = group;
        this.location = location;
        this.eventCreator = eventCreator;
        this.time = time;
        this.notes = notes;
        this.eventId = eventId;
    }


    public Event(String name, Group group, String eventId, String eventCreator) {
        this.name = name;
        this.group = group;
        this.eventId = eventId;
        this.eventCreator = eventCreator;
    }

    public Event(String eventId){
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                '}';
    }

}

