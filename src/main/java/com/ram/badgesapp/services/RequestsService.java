package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.ReqStatus;
import com.ram.badgesapp.entities.ReqType;
import com.ram.badgesapp.entities.Requests;
import com.ram.badgesapp.repos.RequestsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestsService {

    private final RequestsRepo requestsRepo;
    private final NotificationService notificationService;
    
    public RequestsService(RequestsRepo requestsRepo, NotificationService notificationService) {
        this.requestsRepo = requestsRepo;
        this.notificationService = notificationService;
    }

    public List<Requests> getAllRequests() {
        return requestsRepo.findAll();
    }

    public Requests getRequest(Long id) {
        return requestsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id " + id));
    }

    public Requests saveRequest(Requests request) {
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
