package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadDocumentModel {

    private ByteArrayResource resource;

    private HttpHeaders headers;

    public void setHeadersMap(Map<String, Collection<String>> map) {
        headers = new HttpHeaders();
        map.forEach((key, value) -> headers.put(key, new ArrayList<>(value)));
    }
}
