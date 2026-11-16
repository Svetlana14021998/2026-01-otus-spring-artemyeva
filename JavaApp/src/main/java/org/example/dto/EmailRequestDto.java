package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.enums.EmailTemplate;

import java.util.Map;

@Data
@Builder
public class EmailRequestDto {

    private String to;

    private String subject;

    private EmailTemplate template;

    private Map<String, Object> data;
}
