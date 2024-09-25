package org.example.adminService;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Builder
@Data
public class Answers {
    private final UUID id=UUID.randomUUID();
    private String answer;
}
