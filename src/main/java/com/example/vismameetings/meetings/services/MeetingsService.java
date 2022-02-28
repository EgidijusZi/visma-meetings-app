package com.example.vismameetings.meetings.services;

import com.example.vismameetings.common.GenericResponse;
import com.example.vismameetings.meetings.dto.PostMeetingBody;
import com.example.vismameetings.meetings.models.Meeting;
import com.example.vismameetings.meetings.repository.MeetingsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingsService {

    private final ModelMapper modelMapper;
    private final MeetingsRepository meetingsRepository;

    public MeetingsService(ModelMapper modelMapper, MeetingsRepository meetingsRepository) {
        this.modelMapper = modelMapper;
        this.meetingsRepository = meetingsRepository;
    }

    public ResponseEntity<GenericResponse> getMeetingList() {
        List<Meeting> meetingList = meetingsRepository.findAll();
        return new ResponseEntity<>(GenericResponse.success(meetingList), HttpStatus.OK);
    }

    public ResponseEntity<GenericResponse> getFilteredMeetingList(String description, String responsiblePerson,
                                                   String category, String type,
                                                   LocalDateTime startDate, LocalDateTime endDate) {
        List<Meeting> filteredMeetingList = meetingsRepository.findByFilters(description, responsiblePerson, category,
                                                                            type, startDate, endDate);
        return new ResponseEntity<>(GenericResponse.success(filteredMeetingList), HttpStatus.OK);
    }

    public ResponseEntity<GenericResponse> createNewMeeting(PostMeetingBody postMeetingBody) throws IOException {
        Meeting meeting = modelMapper.map(postMeetingBody, Meeting.class);
        meetingsRepository.save(meeting);
        return new ResponseEntity<>(GenericResponse.success(postMeetingBody), HttpStatus.CREATED);
    }

    public ResponseEntity<GenericResponse> deleteMeeting(String name, String responsiblePerson) {
        if (meetingsRepository.findByName(name) == null) {
            return new ResponseEntity<>(GenericResponse.error("Meeting with such name doesn't exist"), HttpStatus.NOT_FOUND);
        }
        for (Meeting m : meetingsRepository.findAll()) {
            if (m.getResponsiblePerson().equals(responsiblePerson) && m.getName().equals(name)) {
                meetingsRepository.delete(m);
                break;
            }
        }
        return new ResponseEntity<>(GenericResponse.success("Meeting successfully deleted"), HttpStatus.OK);
    }

    public ResponseEntity<GenericResponse> addAttendee(String name, String attendeeName) {
        LocalDateTime startDate = meetingsRepository.findByName(name).getStartDate();
        LocalDateTime endDate = meetingsRepository.findByName(name).getEndDate();
        int intersects = 0;
        if (meetingsRepository.findByName(name) == null) {
            return new ResponseEntity<>(GenericResponse.error("Meeting with such name doesn't exist"), HttpStatus.NOT_FOUND);
        }
        if (meetingsRepository.findByName(name).getAttendees().contains(attendeeName)) {
            return new ResponseEntity<>(GenericResponse.error("Attendee already in the list of attendees"), HttpStatus.CONFLICT);
        }
        for (Meeting m : meetingsRepository.findByAttendee(attendeeName)) {
            if ((m.getEndDate().isAfter(startDate) && m.getEndDate().isBefore(endDate))
                    || (m.getStartDate().isAfter(startDate) && m.getStartDate().isBefore(endDate))
                    || (m.getStartDate().isEqual(startDate) && m.getEndDate().isEqual(endDate))) {
                intersects = 1;
            }
        }
        if (intersects == 1) {
            return new ResponseEntity<>(GenericResponse.error("Person is already in a meeting which intersects with the one being added"), HttpStatus.CONFLICT);
        } else {
            meetingsRepository.addAttendee(name, attendeeName);
            return new ResponseEntity<>(GenericResponse.success("Added successfully"), HttpStatus.OK);
        }
    }

    public ResponseEntity<GenericResponse> removeAttendee(String name, String attendeeName) {
        if (meetingsRepository.findByName(name) == null) {
            return new ResponseEntity<>(GenericResponse.error("Meeting with such name doesn't exist"), HttpStatus.NOT_FOUND);
        }
        if (meetingsRepository.findByName(name).getResponsiblePerson().equals(attendeeName)) {
            return new ResponseEntity<>(GenericResponse.error("Attendee is responsible for this meeting"), HttpStatus.FORBIDDEN);
        }
        if (!meetingsRepository.findByName(name).getAttendees().contains(attendeeName)) {
            return new ResponseEntity<>(GenericResponse.error("Attendee is not in the list of attendees"), HttpStatus.NOT_FOUND);
        }
        meetingsRepository.removeAttendee(name, attendeeName);
        return new ResponseEntity<>(GenericResponse.success("Attendee was successfully removed from the list of attendees"), HttpStatus.OK);
    }

}
