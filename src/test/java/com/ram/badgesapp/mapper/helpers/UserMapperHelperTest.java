package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.Status;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.CompanyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMapperHelperTest {

    @Mock
    private BadgeRepo badgeRepo;

    @Mock
    private CompanyRepo companyRepo;

    @InjectMocks
    private UserMapperHelper userMapperHelper;

    private Company testCompany;
    private Badge testBadge1;
    private Badge testBadge2;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setName("Test Company");

        testBadge1 = new Badge();
        testBadge1.setId(1L);
        testBadge1.setCode("Badge 1");

        testBadge2 = new Badge();
        testBadge2.setId(2L);
        testBadge2.setCode("Badge 2");
    }

    @Test
    void companyFromId_validId_returnsCompany() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(testCompany));
        Company result = userMapperHelper.companyFromId(1L);
        assertNotNull(result);
        assertEquals(testCompany.getId(), result.getId());
        verify(companyRepo, times(1)).findById(1L);
    }

    @Test
    void companyFromId_nullId_returnsNull() {
        Company result = userMapperHelper.companyFromId(null);
        assertNull(result);
        verify(companyRepo, never()).findById(anyLong());
    }

    @Test
    void companyFromId_companyNotFound_throwsRuntimeException() {
        when(companyRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userMapperHelper.companyFromId(99L));
        assertEquals("Company not found with id: 99", exception.getMessage());
        verify(companyRepo, times(1)).findById(99L);
    }

    @Test
    void badgeListFromIds_validIds_returnsListOfBadges() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(testBadge1));
        when(badgeRepo.findById(2L)).thenReturn(Optional.of(testBadge2));

        List<Badge> result = userMapperHelper.badgeListFromIds(ids);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testBadge1));
        assertTrue(result.contains(testBadge2));
        verify(badgeRepo, times(1)).findById(1L);
        verify(badgeRepo, times(1)).findById(2L);
    }

    @Test
    void badgeListFromIds_emptyList_returnsEmptyList() {
        List<Badge> result = userMapperHelper.badgeListFromIds(new ArrayList<>());
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(badgeRepo, never()).findById(anyLong());
    }

    @Test
    void badgeListFromIds_nullList_returnsEmptyList() {
        List<Badge> result = userMapperHelper.badgeListFromIds(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(badgeRepo, never()).findById(anyLong());
    }

    @Test
    void badgeListFromIds_badgeNotFound_throwsRuntimeException() {
        List<Long> ids = Arrays.asList(1L, 99L);
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(testBadge1));
        when(badgeRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userMapperHelper.badgeListFromIds(ids));
        assertEquals("Badge not found with id: 99", exception.getMessage());
        verify(badgeRepo, times(1)).findById(1L);
        verify(badgeRepo, times(1)).findById(99L);
    }

    @Test
    void stringToRole_validRole_returnsRoleEnum() {
        assertEquals(Role.ADMIN, userMapperHelper.stringToRole("ADMIN"));
        assertEquals(Role.EMPLOYEE, userMapperHelper.stringToRole("EMPLOYEE"));
    }

    @Test
    void stringToRole_nullString_returnsNull() {
        assertNull(userMapperHelper.stringToRole(null));
    }

    @Test
    void stringToRole_emptyString_returnsNull() {
        assertNull(userMapperHelper.stringToRole(""));
    }

    @Test
    void stringToRole_invalidString_throwsRuntimeException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userMapperHelper.stringToRole("INVALID_ROLE"));
        assertEquals("Invalid role: INVALID_ROLE", exception.getMessage());
    }

    @Test
    void stringToStatus_validStatus_returnsStatusEnum() {
        assertEquals(Status.ACTIVE, userMapperHelper.stringToStatus("ACTIVE"));
        assertEquals(Status.INACTIVE, userMapperHelper.stringToStatus("INACTIVE"));
    }

    @Test
    void stringToStatus_nullString_returnsNull() {
        assertNull(userMapperHelper.stringToStatus(null));
    }

    @Test
    void stringToStatus_emptyString_returnsNull() {
        assertNull(userMapperHelper.stringToStatus(""));
    }

    @Test
    void stringToStatus_invalidString_throwsRuntimeException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userMapperHelper.stringToStatus("INVALID_STATUS"));
        assertEquals("Invalid status: INVALID_STATUS", exception.getMessage());
    }
}
