package ba.unsa.etf.cineapp.service.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonService {
  private AmazonS3 s3client;

  @Value("${aws.s3.endpointUrl}")
  private String endpointUrl;
  @Value("${aws.s3.bucketName}")
  private String bucketName;
  @Value("${aws.s3.accessKey}")
  private String accessKey;
  @Value("${aws.s3.secretKey}")
  private String secretKey;

  @PostConstruct
  private void initializeAmazon() {
    AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

    this.s3client = AmazonS3ClientBuilder
        .standard()
        .withRegion("eu-central-1")
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  private String generateFileName(MultipartFile multiPart) {
    return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
  }

  public void uploadFileTos3bucket(String fileName, File file) {
    try {
      s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
      System.out.println("File uploaded successfully without ACLs.");
    } catch (AmazonS3Exception e) {
      e.printStackTrace();
      System.out.println("Error during upload: " + e.getMessage());
    }
  }



  public String uploadFile(MultipartFile multipartFile) {
    String fileUrl = "";
    try {
      File file = convertMultiPartToFile(multipartFile);
      String fileName = generateFileName(multipartFile);
      fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
      uploadFileTos3bucket(fileName, file);
      file.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return fileUrl;
  }

  public String deleteFileFromS3Bucket(String fileUrl) {
    try {
      String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
      s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
      return "Successfully deleted";
    } catch (Exception e) {
      e.printStackTrace();
      return "Error deleting file";
    }
  }
}
