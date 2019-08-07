package digital.jameel.twitterelasticsearch;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

public class Handler implements RequestHandler<S3Event, String> {

	private AmazonS3 s3Client;

	public Handler(){
		this.s3Client = AmazonS3ClientBuilder.standard().build();
	}

	@Override
	public String handleRequest(S3Event s3Event, Context context) {
		for (S3EventNotification.S3EventNotificationRecord record: s3Event.getRecords()){
			String bucket = record.getS3().getBucket().getName();
			String key = record.getS3().getObject().getKey();
			System.out.println("File Path is "+record.getS3().getObject().getKey());
			System.out.println("Bucket Name is "+record.getS3().getBucket().getName());
			FileParser fileParser = this.handleFile(bucket, key);
			fileParser.uploadDocuments();
		}
		return null;
	}

	public FileParser handleFile(String bucket, String key){
		S3Object object = this.s3Client.getObject(bucket, key);
		InputStream stream= object.getObjectContent();
		FileParser fileParser = new FileParser(stream);
		try {
			fileParser.parseFile();
			return fileParser;
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		return null;
	}

}
