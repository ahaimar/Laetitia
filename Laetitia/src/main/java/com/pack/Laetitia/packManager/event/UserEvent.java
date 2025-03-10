package com.pack.Laetitia.packManager.event;


import com.pack.Laetitia.model.entity.UserEntity;
import com.pack.Laetitia.packManager.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {

    private UserEntity user;
    private EventType type;
    private Map<?, ?> data;

}









