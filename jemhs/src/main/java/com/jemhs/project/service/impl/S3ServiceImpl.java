package com.jemhs.project.service.impl;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.jemhs.project.model.Message;
import com.jemhs.project.model.Status;
import com.jemhs.project.service.S3Service;

@Service
public class S3ServiceImpl implements S3Service {

	private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);

	@Value("${s3bucketname}")
    private String bucketName;
	
	@Autowired
	private AmazonS3Client amazonS3Client;
	
	@Override
	public Message uploadMultipleFileHandler(@RequestParam("file") MultipartFile[] files,String user) throws IOException {

		Message msg = new Message();
		ArrayList<Integer> arr = new ArrayList<>();

		logger.info("Number of file to upload " + files.length);
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			System.out.println(file.getName()+"  "+file.getOriginalFilename()+"  "+file.getContentType() );
			if (!file.isEmpty()) {
				try {
					
					ObjectMetadata objectMetadata = new ObjectMetadata();
					objectMetadata.setContentType(file.getContentType());
					objectMetadata.setContentLength(IOUtils.toByteArray(file.getInputStream()).length);
                    logger.info("Attempting to upload file to S3");
                    amazonS3Client.putObject(bucketName, "users"+"/"+user+"/"+file.getOriginalFilename(), file.getInputStream(), objectMetadata);
					logger.info("File uploaded to AWS S3 sucessfully");
					if(arr.size() > 0) {
			            msg.setStatus(Status.ERROR);
			            msg.setError("Files upload fail");
			            msg.setErrorKys(arr);
			        } else {
			            msg.setStatus(Status.SUCCESS);
			            msg.setStatusMsg("Files upload success");
			        }
				} 
				catch(AmazonClientException acx){
					logger.error("Error is "+acx.getLocalizedMessage());
					acx.printStackTrace();
				}
				catch(Exception ex){
					logger.error("Error is "+ex.getLocalizedMessage());
					ex.printStackTrace();
				}
			} else {
				arr.add(i);
			}
		}
		return msg;
	}
}
