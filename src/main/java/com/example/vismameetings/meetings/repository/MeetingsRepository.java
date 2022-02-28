package com.example.vismameetings.meetings.repository;

import com.example.vismameetings.meetings.models.Meeting;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.vismameetings.helpers.JSONHandlers.*;

@Repository
public class MeetingsRepository {

    List<Meeting> meetings = parseMeetings();

    public MeetingsRepository() throws IOException {}

    public List<Meeting> findAll() {
        return meetings;
    }

    public List<Meeting> findByFilters(String description, String responsiblePerson,
                         String category, String type,
                         LocalDateTime startDate, LocalDateTime endDate) {
        return meetings.stream().filter(m -> (Objects.isNull(responsiblePerson) || m.getResponsiblePerson().equals(responsiblePerson))
                && (Objects.isNull(category) || m.getCategory().equals(category))
                && (Objects.isNull(description) || m.getDescription().contains(description))
                && (Objects.isNull(type) || m.getType().equals(type))
                && (Objects.isNull(startDate) || m.getStartDate().isAfter(startDate))
                && (Objects.isNull(endDate) || m.getEndDate().isBefore(endDate)))
                .collect(Collectors.toList());
    }

    public List<Meeting> findByAttendee(String attendeeName) {
        return meetings.stream().filter(m -> m.getAttendees().contains(attendeeName)).collect(Collectors.toList());
    }

    public Meeting findByName(String name) {
        for (Meeting m : meetings) {
            if (m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }

    public void save(Meeting meeting) throws IOException {
        meetings.add(meeting);
        writeMeeting(meeting);
    }

    public void delete(Meeting meeting) {
        meetings.remove(meeting);
        removeMeeting(meeting.getName());
    }

    public void addAttendee(String name, String attendeeName) {
        findByName(name).getAttendees().add(attendeeName);
        addAttendeeToMeeting(name, attendeeName);
    }

    public void removeAttendee(String name, String attendeeName) {
        findByName(name).getAttendees().remove(attendeeName);
        removeAttendeeFromMeeting(name, attendeeName);
    }
}
