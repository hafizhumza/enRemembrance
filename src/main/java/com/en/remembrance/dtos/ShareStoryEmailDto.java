package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShareStoryEmailDto {

    private String receiverEmail;

    private String senderName;

    private String url;

    private Long expiryDays; // TODO: Not used yet

    public ShareStoryEmailDto(String receiverEmail, String senderName, String url) {
        this.receiverEmail = receiverEmail;
        this.senderName = senderName;
        this.url = url;
    }

}
