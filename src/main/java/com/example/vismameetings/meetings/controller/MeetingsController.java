package com.example.vismameetings.meetings.controller;

import com.example.vismameetings.common.GenericResponse;
import com.example.vismameetings.meetings.dto.PostMeetingBody;
import com.example.vismameetings.meetings.services.MeetingsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/meetings")
public class MeetingsController {

    private final MeetingsService meetingsService;

    public MeetingsController(MeetingsService meetingsService) {
        this.meetingsService = meetingsService;
    }

    @GetMapping
    public ResponseEntity<GenericResponse> findAll() {
        return meetingsService.getMeetingList();
    }

    @GetMapping("/filter")
    public ResponseEntity<GenericResponse> findByFilters(
                                       @RequestParam(name = "description", required = false) String description,
                                       @RequestParam(name = "responsiblePerson", required = false) String responsiblePerson,
                                       @RequestParam(name = "category", required = false) String category,
                                       @RequestParam(name = "type", required = false) String type,
                                       @RequestParam(name = "startDate", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                       @RequestParam(name = "endDate", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return meetingsService.getFilteredMeetingList(description, responsiblePerson, category, type, startDate, endDate);
    }

    //postMeetingDTO body
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@Valid @RequestBody PostMeetingBody postMeetingBody) throws IOException {
        return meetingsService.createNewMeeting(postMeetingBody);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<GenericResponse> delete(@PathVariable String name, @RequestParam(name = "responsiblePerson") String responsiblePerson) {
        return meetingsService.deleteMeeting(name, responsiblePerson);
    }

    @PutMapping("{name}/add")
    public ResponseEntity<GenericResponse> addAttendee(@PathVariable String name, @RequestParam(name = "attendeeName") String attendeeName) {
        return meetingsService.addAttendee(name, attendeeName);
    }

    @DeleteMapping("{name}/remove")
    public ResponseEntity<GenericResponse> removeAttendee(@PathVariable String name, @RequestParam(name = "attendeeName") String attendeeName) {
        return meetingsService.removeAttendee(name, attendeeName);
    }
}
