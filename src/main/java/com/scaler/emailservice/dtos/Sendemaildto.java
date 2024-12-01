package com.scaler.emailservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sendemaildto {
    private String emailid;
    private String subject;
    private String body;
}
