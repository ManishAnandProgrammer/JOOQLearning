package com.manish.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ActorWithExactFieldNames {
    private Long actor_id;
    private String first_name;
    private String last_name;
    private LocalDateTime last_update;
}
