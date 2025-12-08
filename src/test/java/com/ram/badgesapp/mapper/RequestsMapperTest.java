package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.RequestsDTO;
import com.ram.badgesapp.entities.ReqStatus;
import com.ram.badgesapp.entities.ReqType;
import com.ram.badgesapp.entities.Requests;
import com.ram.badgesapp.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RequestsMapperTest {

    @Autowired
    private RequestsMapper requestsMapper;

    private Requests requests;
    private RequestsDTO requestsDTO;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        // For toDTO test
        requests = new Requests();
        requests.setId(10L);
        requests.setUser(user);
        requests.setReqType(ReqType.NEW_BADGE);
        requests.setReqStatus(ReqStatus.PENDING);
        requests.setDescription("Requesting new badge");
        requests.setCreatedAt(LocalDateTime.now().minusDays(1));

        // For toEntity test
        requestsDTO = new RequestsDTO();
        requestsDTO.setUserId(1L); // This should be ignored by toEntity
        requestsDTO.setReqType(ReqType.OTHER);
        requestsDTO.setReqStatus(ReqStatus.APPROVED);
        requestsDTO.setDescription("DTO request");
    }

    @Test
    void testToDTO() {
        RequestsDTO resultDTO = requestsMapper.toDTO(requests);

        assertNotNull(resultDTO);
        assertEquals(requests.getId(), resultDTO.getId());
        assertEquals(requests.getUser().getId(), resultDTO.getUserId());
        assertEquals(requests.getReqType(), resultDTO.getReqType());
        assertEquals(requests.getReqStatus(), resultDTO.getReqStatus());
        assertEquals(requests.getDescription(), resultDTO.getDescription());
        assertEquals(requests.getCreatedAt(), resultDTO.getCreatedAt());
    }

    @Test
    void testToEntity() {
        Requests resultEntity = requestsMapper.toEntity(requestsDTO);

        assertNotNull(resultEntity);

        // Check ignored fields
        assertNull(resultEntity.getId());
        assertNull(resultEntity.getUser());
        assertNull(resultEntity.getCreatedAt());

        // Check mapped fields
        assertEquals(requestsDTO.getReqType(), resultEntity.getReqType());
        assertEquals(requestsDTO.getReqStatus(), resultEntity.getReqStatus());
        assertEquals(requestsDTO.getDescription(), resultEntity.getDescription());
    }
}
