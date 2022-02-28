package com.example.vismameetings.meetings;

import com.example.vismameetings.common.GenericResponse;
import com.example.vismameetings.meetings.models.Meeting;
import com.example.vismameetings.meetings.repository.MeetingsRepository;
import com.example.vismameetings.meetings.services.MeetingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetMeetingsUnitTest {

    @InjectMocks
    private MeetingsService meetingsService;

    @Mock
    private MeetingsRepository meetingsRepository;
    @Test
    public void whenGetMeetingsList_thenStatus200() throws IOException {

        List<Meeting> meetingList = new ArrayList<>();

        Meeting meeting = new Meeting("test1", "test1", "test1",
                "test1", "test1", new ArrayList<>(Arrays.asList("test1", "test2")),LocalDateTime.of(2022, 2, 28, 11, 0),
                LocalDateTime.of(2022, 2, 28, 13, 0));
        Meeting meeting2 = new Meeting("test1", "test1", "test1",
                "test1", "test1", new ArrayList<>(Arrays.asList("test1", "test2")),LocalDateTime.of(2022, 2, 28, 11, 0),
                LocalDateTime.of(2022, 2, 28, 13, 0));

        meetingList.add(meeting);
        meetingList.add(meeting2);

        meetingsRepository.save(meeting);
        meetingsRepository.save(meeting2);
        when(meetingsRepository.findAll()).thenReturn(meetingList);

        ResponseEntity<GenericResponse> response = meetingsService.getMeetingList();
        assertEquals(meetingList, response.getBody().getData());

    }

}
