package com.example.vismameetings.meetings;

import com.example.vismameetings.common.GenericResponse;
import com.example.vismameetings.meetings.dto.PostMeetingBody;
import com.example.vismameetings.meetings.models.Meeting;
import com.example.vismameetings.meetings.repository.MeetingsRepository;
import com.example.vismameetings.meetings.services.MeetingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostMeetingUnitTest {

    @InjectMocks
    private MeetingsService meetingsService;

    @Mock
    private final ModelMapper modelMapper = new ModelMapper();

    @Mock
    private MeetingsRepository meetingsRepository;
    @Test
    public void givenPostMeetingBodyOK_thenStatus201() throws IOException {

        PostMeetingBody postMeetingBody = new PostMeetingBody("test1", "test1", "test1",
                "test1", "test1", LocalDateTime.of(2022, 2, 28, 11, 0),
                LocalDateTime.of(2022, 2, 28, 13, 0));

        meetingsRepository.save(modelMapper.map(postMeetingBody, Meeting.class));

        ResponseEntity<GenericResponse> response = meetingsService.createNewMeeting(postMeetingBody);
        assertEquals(postMeetingBody, response.getBody().getData());

    }

}
