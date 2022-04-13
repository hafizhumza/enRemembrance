package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailLinkDto {

    @Email(message = "Invalid email")
    private String receiverEmail;

    private String name;

    @NotNull(message = "Link cannot be null")
    private String link;

    private int expiryDays;

}
