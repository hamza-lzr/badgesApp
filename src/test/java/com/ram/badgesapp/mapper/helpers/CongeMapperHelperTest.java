package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CongeMapperHelperTest {

    @Mock
    private UserEntityRepo userRepo;

    @InjectMocks
    private CongeMapperHelper congeMapperHelper;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
    }

    @Test
    void userFromId_validId_returnsUser() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        UserEntity result = congeMapperHelper.userFromId(1L);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    void userFromId_nullId_returnsNull() {
        UserEntity result = congeMapperHelper.userFromId(null);
        assertNull(result);
        verify(userRepo, never()).findById(anyLong());
    }

    @Test
    void userFromId_userNotFound_throwsRuntimeException() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> congeMapperHelper.userFromId(99L));
        assertEquals("User not found with id: 99", exception.getMessage());
        verify(userRepo, times(1)).findById(99L);
    }

    @Test
    void formatLocalDate_validDate_returnsFormattedString() {
        LocalDate date = LocalDate.of(2023, 10, 26);
        String expected = "2023-10-26";
        assertEquals(expected, congeMapperHelper.formatLocalDate(date));
    }

    @Test
    void formatLocalDate_nullDate_returnsNull() {
        assertNull(congeMapperHelper.formatLocalDate(null));
    }

    @Test
    void parseLocalDate_validString_returnsLocalDate() {
        String dateStr = "2023-10-26";
        LocalDate expected = LocalDate.of(2023, 10, 26);
        assertEquals(expected, congeMapperHelper.parseLocalDate(dateStr));
    }

    @Test
    void parseLocalDate_nullString_returnsNull() {
        assertNull(congeMapperHelper.parseLocalDate(null));
    }

    @Test
    void parseLocalDate_emptyString_returnsNull() {
        assertNull(congeMapperHelper.parseLocalDate(""));
    }

    @Test
    void formatLocalDateTime_validDateTime_returnsFormattedString() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 26, 10, 30);
        String expected = "2023-10-26T10:30:00";
        assertEquals(expected, congeMapperHelper.formatLocalDateTime(dateTime));
    }

    @Test
    void formatLocalDateTime_nullDateTime_returnsNull() {
        assertNull(congeMapperHelper.formatLocalDateTime(null));
    }

    @Test
    void parseLocalDateTime_validString_returnsLocalDateTime() {
        String dateTimeStr = "2023-10-26T10:30:00";
        LocalDateTime expected = LocalDateTime.of(2023, 10, 26, 10, 30);
        assertEquals(expected, congeMapperHelper.parseLocalDateTime(dateTimeStr));
    }

    @Test
    void parseLocalDateTime_nullString_returnsNull() {
        assertNull(congeMapperHelper.parseLocalDateTime(null));
    }

    @Test
    void parseLocalDateTime_emptyString_returnsNull() {
        assertNull(congeMapperHelper.parseLocalDateTime(""));
    }
}
