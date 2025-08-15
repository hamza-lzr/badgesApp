package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.RequestsRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestsService {

    private final RequestsRepo requestsRepo;
    private final NotificationService notificationService;
    private final UserEntityRepo userEntityRepo;
    
    public RequestsService(RequestsRepo requestsRepo, NotificationService notificationService, UserEntityRepo userEntityRepo) {
        this.requestsRepo = requestsRepo;
        this.notificationService = notificationService;
        this.userEntityRepo = userEntityRepo;
    }

    public List<Requests> getAllRequests() {
        return requestsRepo.findAll();
    }

    public Requests getRequest(Long id) {
        return requestsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id " + id));
    }

    public Requests saveRequest(Requests request) {
        notificationService.createNotificationForUser(request.getUser().getId(), "Your request has been sent. Request type: " + request.getReqType() + ", Description: " + request.getDescription());
        List<UserEntity> admins = userEntityRepo.findAllByRole(Role.ADMIN);

        String msg = "Nouvelle demande de type " + request.getReqType() +
                ", (Employé #" + request.getUser().getId() + "), Nom Complet: " + request.getUser().getFirstName() + " " + request.getUser().getLastName() + ")";

        for (UserEntity admin : admins) {
            notificationService.createNotificationForUser(admin.getId(), msg);
        }
        return requestsRepo.save(request);
    }

    public void deleteRequest(Long id) {
        requestsRepo.deleteById(id);
    }

    public Requests updateRequest(Long id, Requests request) {
        Requests req = requestsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id " + id));
        if(request.getDescription() != null) req.setDescription(request.getDescription());
        if(request.getUser() != null) req.setUser(request.getUser());
        if(request.getReqType() != null) req.setReqType(request.getReqType());
        if(request.getReqStatus() != null) req.setReqStatus(request.getReqStatus());
        return requestsRepo.save(req);
    }

    public Requests updateReqType(Long id, ReqType reqType) {
        Requests req = requestsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id " + id));
        req.setReqType(reqType);
        return requestsRepo.save(req);
    }

    public Requests updateReqStatus(Long id, ReqStatus reqStatus) {
        Requests req = requestsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id " + id));
        
        // Store the old status before updating
        ReqStatus oldStatus = req.getReqStatus();
        
        // Update the status
        req.setReqStatus(reqStatus);
        Requests updatedRequest = requestsRepo.save(req);
        
        // Notify the user about the status change
        notificationService.notifyAboutRequestStatus(id, oldStatus, reqStatus);
        
        return updatedRequest;
    }

    public List<Requests> getRequestsByEmployeeId(Long id){
        return requestsRepo.findAllByUser_Id(id);
    }


}
