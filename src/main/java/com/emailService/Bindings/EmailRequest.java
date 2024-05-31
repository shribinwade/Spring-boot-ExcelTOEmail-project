package com.emailService.Bindings;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmailRequest {

    String to;
    String subject;
    String message;

}
