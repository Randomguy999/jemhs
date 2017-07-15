package com.jemhs.project.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.jemhs.project.model.Message;

public interface S3Service {
	
	public Message  uploadMultipleFileHandler(MultipartFile[] files,String user) throws IOException;

}
