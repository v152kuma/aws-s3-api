package com.kaamaaya.awsoperations.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class AmazonClient {

	@Autowired
	private AmazonS3Client s3client;

	@Value("${endpointUrl}")
	private String endpointUrl;

	@Value("${bucketName}")
	private String bucketName;

	public String uploadFile(MultipartFile multipartFile) {
		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return fileUrl;
	}

	public void uploadMultipleFiles(List<MultipartFile> files)

	{
		if (files != null) {
			files.forEach(multiPartFile -> {
				try {
					File file = convertMultiPartToFile(multiPartFile);
					String fileName = generateFileName(multiPartFile);
					uploadFileTos3bucket(fileName, file);
					file.delete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
		}
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
				+ file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return multiPart.getOriginalFilename().replace(" ", "_");

	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String deleteFileFromS3Bucket(String fileName) {

		System.out.println(fileName);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return "Successfully deleted " + fileName + " from bucket " + bucketName;
	}

	public ResponseEntity<byte[]> download(String filename) throws IOException {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, filename);

		S3Object s3Object = s3client.getObject(getObjectRequest);

		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

		byte[] bytes = IOUtils.toByteArray(objectInputStream);

		String fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

}