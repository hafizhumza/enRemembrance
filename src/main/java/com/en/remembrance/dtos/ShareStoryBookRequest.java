package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareStoryBookRequest {

    @NotNull(message = "StoryBook ID cannot be null")
    @Min(value = 1, message = "Invalid StoryBook ID")
    private Long id;

    private String receiverEmail;

}
