package com.example.vismameetings.helpers;

import com.example.vismameetings.meetings.models.Meeting;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONHandlers {

    public static List<Meeting> parseMeetings() throws IOException {
        File file = new File("C:\\Users\\Kompiuteris\\Desktop\\meetings.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Meeting> meetingList = null;
        try {
            if (!file.exists()) {
                return new ArrayList<>();
            } else {
                meetingList = objectMapper.readValue(new File(String.valueOf(file)), new TypeReference<List<Meeting>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return meetingList;
    }

    public static void writeMeeting(Meeting meeting) throws IOException {
        File file = new File("C:\\Users\\Kompiuteris\\Desktop\\meetings.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            if (!file.exists()) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(meeting);
                objectMapper.writeValue(new File(String.valueOf(file)), jsonArray);
            } else {
                String jsonFileStr = Files.readString(Paths.get(String.valueOf(file)));
                JSONArray jsonArray = (JSONArray) new JSONParser().parse(jsonFileStr);
                jsonArray.add(meeting);
                objectMapper.writeValue(new File(String.valueOf(file)), jsonArray);
            }
        } catch(IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void removeMeeting(String name) {
        File file = new File("C:\\Users\\Kompiuteris\\Desktop\\meetings.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            String jsonFileStr = Files.readString(Paths.get(String.valueOf(file)));
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(jsonFileStr);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = new JSONObject((Map) jsonArray.get(i));
                String element = (String) jsonObject.get("name");

                if (element.equals(name)) {
                    jsonArray.remove(jsonObject);
                    objectMapper.writeValue(new File(String.valueOf(file)), jsonArray);
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void addAttendeeToMeeting(String name, String attendeeName) {
        File file = new File("C:\\Users\\Kompiuteris\\Desktop\\meetings.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            String jsonFileStr = Files.readString(Paths.get(String.valueOf(file)));
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(jsonFileStr);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = new JSONObject((Map) jsonArray.get(i));
                String element = (String) jsonObject.get("name");

                if (element.equals(name)) {
                    //removing old array item and updating with a new object with included attendee
                    jsonArray.remove(jsonObject);
                    JSONObject copyOfObject = jsonObject;
                    JSONArray attendees = (JSONArray) copyOfObject.get("attendees");

                    attendees.add(attendeeName);
                    copyOfObject.put("attendees", attendees);

                    jsonArray.add(copyOfObject);
                    objectMapper.writeValue(new File(String.valueOf(file)), jsonArray);
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void removeAttendeeFromMeeting(String name, String attendeeName) {
        File file = new File("C:\\Users\\Kompiuteris\\Desktop\\meetings.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            String jsonFileStr = Files.readString(Paths.get(String.valueOf(file)));
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(jsonFileStr);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = new JSONObject((Map) jsonArray.get(i));
                String element = (String) jsonObject.get("name");

                if (element.equals(name)) {
                    //removing old array item and updating with a new object with included attendee
                    jsonArray.remove(jsonObject);
                    JSONObject copyOfObject = jsonObject;
                    JSONArray attendees = (JSONArray) copyOfObject.get("attendees");

                    attendees.remove(attendeeName);
                    copyOfObject.put("attendees", attendees);

                    jsonArray.add(copyOfObject);
                    objectMapper.writeValue(new File(String.valueOf(file)), jsonArray);
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
