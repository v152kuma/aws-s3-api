package com.kaamaaya.awsoperations.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kaamaaya.awsoperations.client.AmazonClient;

@RestController
public class BucketController {

	@Autowired
	private AmazonClient amazonClient;

	@Autowired
	BucketController(AmazonClient amazonClient) {
		this.amazonClient = amazonClient;
	}

	@PostMapping("/uploadFile")
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) {

		return this.amazonClient.uploadFile(file);

	}

	@PostMapping("/uploadMultipleFiles")
	public String uploadFiles(@RequestPart(value = "file") List<MultipartFile> files) {

		try {
			this.amazonClient.uploadMultipleFiles(files);
			
		} catch (Exception e) {
			return "Error uploading files";
		}
		return "Files uploaded";
	   
	   

	}

	@GetMapping("/downloadFile")
	public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename) throws IOException {

		return this.amazonClient.download(filename);

	}

	@DeleteMapping("/deleteFile")
	public String deleteFile(@RequestPart(value = "filename") String filename) {
		return this.amazonClient.deleteFileFromS3Bucket(filename);
	}
}