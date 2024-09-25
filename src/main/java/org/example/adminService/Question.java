package org.example.adminService;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
@Builder
public class Question {
    private final UUID id=UUID.randomUUID();
    private String question;
    private final List<Answers>answers= new ArrayList<>();
}
