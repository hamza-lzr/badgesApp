package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.CompanyRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BadgeMapperHelperTest {

    @Mock
    private CompanyRepo companyRepo;

    @Mock
    private UserEntityRepo userEntityRepo;

    @InjectMocks
    private BadgeMapperHelper badgeMapperHelper;

    private Company testCompany;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setName("Test Company");

        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
    }

    @Test
    void companyFromId_validId_returnsCompany() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(testCompany));
        Company result = badgeMapperHelper.companyFromId(1L);
        assertNotNull(result);
        assertEquals(testCompany.getId(), result.getId());
        verify(companyRepo, times(1)).findById(1L);
    }

    @Test
    void companyFromId_nullId_returnsNull() {
        Company result = badgeMapperHelper.companyFromId(null);
        assertNull(result);
        verify(companyRepo, never()).findById(anyLong());
    }

    @Test
    void companyFromId_companyNotFound_throwsRuntimeException() {
        when(companyRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> badgeMapperHelper.companyFromId(99L));
        assertEquals("Company not found with id: 99", exception.getMessage());
        verify(companyRepo, times(1)).findById(99L);
    }

    @Test
    void userFromId_validId_returnsUser() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(testUser));
        UserEntity result = badgeMapperHelper.userFromId(1L);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        verify(userEntityRepo, times(1)).findById(1L);
    }

    @Test
    void userFromId_nullId_returnsNull() {
        UserEntity result = badgeMapperHelper.userFromId(null);
        assertNull(result);
        verify(userEntityRepo, never()).findById(anyLong());
    }

    @Test
    void userFromId_userNotFound_throwsRuntimeException() {
        when(userEntityRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> badgeMapperHelper.userFromId(99L));
        assertEquals("User not found with id: 99", exception.getMessage());
        verify(userEntityRepo, times(1)).findById(99L);
    }
}
