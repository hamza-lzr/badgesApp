package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.RequestsRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestsServiceTest {

    @Mock
    private RequestsRepo requestsRepo;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserEntityRepo userEntityRepo;

    @InjectMocks
    private RequestsService requestsService;

    private Requests request;
    private UserEntity user;
    private UserEntity admin;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        
        admin = new UserEntity();
        admin.setId(2L);
        admin.setRole(Role.ADMIN);

        request = new Requests();
        request.setId(1L);
        request.setUser(user);
        request.setReqType(ReqType.OTHER);
        request.setReqStatus(ReqStatus.PENDING);
    }

    @Test
    void getAllRequests() {
        when(requestsRepo.findAll()).thenReturn(Collections.singletonList(request));
        List<Requests> requests = requestsService.getAllRequests();
        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
    }

    @Test
    void getRequest() {
        when(requestsRepo.findById(1L)).thenReturn(Optional.of(request));
        Requests foundRequest = requestsService.getRequest(1L);
        assertNotNull(foundRequest);
        assertEquals(1L, foundRequest.getId());
    }
    
    @Test
    void getRequest_notFound() {
        when(requestsRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> requestsService.getRequest(1L));
    }


    @Test
    void saveRequest() {
        when(requestsRepo.save(any(Requests.class))).thenReturn(request);
        when(userEntityRepo.findAllByRole(Role.ADMIN)).thenReturn(Collections.singletonList(admin));
        
        Requests newRequest = new Requests();
        newRequest.setUser(user);
        newRequest.setReqType(ReqType.NEW_BADGE);
        newRequest.setDescription("New Badge");


        Requests savedRequest = requestsService.saveRequest(newRequest);

        assertNotNull(savedRequest);
        verify(notificationService, times(1)).createNotificationForUser(eq(1L), anyString());
        verify(notificationService, times(1)).createNotificationForUser(eq(2L), anyString());
    }

    @Test
    void deleteRequest() {
        doNothing().when(requestsRepo).deleteById(1L);
        requestsService.deleteRequest(1L);
        verify(requestsRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateRequest() {
        when(requestsRepo.findById(1L)).thenReturn(Optional.of(request));
        when(requestsRepo.save(any(Requests.class))).thenReturn(request);

        Requests patch = new Requests();
        patch.setDescription("Updated description");

        Requests updatedRequest = requestsService.updateRequest(1L, patch);

        assertNotNull(updatedRequest);
        assertEquals("Updated description", updatedRequest.getDescription());
    }
    
    @Test
    void updateRequest_notFound() {
        when(requestsRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> requestsService.updateRequest(1L, new Requests()));
    }

    @Test
    void updateReqType() {
        when(requestsRepo.findById(1L)).thenReturn(Optional.of(request));
        when(requestsRepo.save(any(Requests.class))).thenReturn(request);

        Requests updatedRequest = requestsService.updateReqType(1L, ReqType.AIRPORT_ACCESS);

        assertNotNull(updatedRequest);
        assertEquals(ReqType.AIRPORT_ACCESS, updatedRequest.getReqType());
    }

    @Test
    void updateReqStatus() {
        when(requestsRepo.findById(1L)).thenReturn(Optional.of(request));
        when(requestsRepo.save(any(Requests.class))).thenReturn(request);

        Requests updatedRequest = requestsService.updateReqStatus(1L, ReqStatus.APPROVED);

        assertNotNull(updatedRequest);
        assertEquals(ReqStatus.APPROVED, updatedRequest.getReqStatus());
        verify(notificationService, times(1)).notifyAboutRequestStatus(1L, ReqStatus.PENDING, ReqStatus.APPROVED);
    }

    @Test
    void getRequestsByEmployeeId() {
        when(requestsRepo.findAllByUser_Id(1L)).thenReturn(Collections.singletonList(request));
        List<Requests> requests = requestsService.getRequestsByEmployeeId(1L);
        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
    }
}
