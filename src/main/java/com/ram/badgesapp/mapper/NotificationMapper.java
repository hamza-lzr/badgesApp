package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.NotificationDTO;
import com.ram.badgesapp.entities.Notification;
import com.ram.badgesapp.mapper.helpers.NotificationMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {NotificationMapperHelper.class})
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user.id")
    NotificationDTO toDTO(Notification notification);

    @Mappings({
            @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId"),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "id", ignore = true)
    })
    Notification toEntity(NotificationDTO notificationDTO);
}

