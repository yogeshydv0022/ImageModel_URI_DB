package com.files.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response {
	private long id;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}