package com.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * The following code is a simple way of putting any file
 * into amazon s3 bucket and reading any file from amazon s3 bucket
 *
 */
public class App{
	
	/**
	 * Intialize the AWS credentials with the access-key and the secret-key of Aws account
	 */
	private static  AWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials("AWS_ACCESS_KEY",
		      "AWS_SECRET_KEY");
	
	/**
	 * The method builds the Amazon S3 client using the initialized Aws_Credentials
	 * and the Amazon S3 endpoint.
	 * The Amazon S3 client is used for all the interaction with Amazon S3 storage system
	 * 
	 * @return AmazonS3 client
	 */
	private static AmazonS3 getS3Client() {
	    AmazonS3 s3Client = new AmazonS3Client(AWS_CREDENTIALS);
	    s3Client.setEndpoint("AMAZON_S3_ENDPOINT");
	    return s3Client;
	  }
	
	/**
	 * The method puts the file from your local file system
	 * into the amazon s3 bucket
	 * 
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws FileNotFoundException
	 */
    public void putObjecttoS3() throws AmazonServiceException, AmazonClientException, FileNotFoundException{
    	File file = new File("/path to your file/...");
		
    	ObjectMetadata meta = new ObjectMetadata();
    	meta.setContentLength(file.length());

    	getS3Client().putObject(
    	    new PutObjectRequest("AWS_BUCKET_NAME", file.getName(), new FileInputStream(file), meta));
    	
    }
    
    /**
     * The method fetches the specified file from the amazon s3 bucket
     * 
     * @throws IOException
     */
    public void getObjectFromS3() throws IOException{
    	/**
		 * Initializing the GetObjectRequest of Amazon S3. It is used to read files stored
		 * in amazon s3 bucket. It is initialized with the Aws_Bucket_Name, in which the file
		 * is stored, and the file name which we want to read
		 */
    	GetObjectRequest getObj = new GetObjectRequest("AWS_BUCKET_NAME", "fileName");
    	
    	/**
    	 * Use the Amazon S3 client and the GetObjectRequest to fetch the files and
    	 * hold it in the S3Object container.
    	 */
    	S3Object s3FileObj = getS3Client().getObject(getObj);
    	/**
    	 * creating a temp file in memory for writing the file content
    	 * 
    	 * The Amazon S3Object does not directly converts to a File, nor
    	 * does it has any built-in function to do so. Hence we need to use
    	 * the IOUtils of common.io for writing the input Stream to a file.
    	 * We can do the same using the conventional manual style but IOUtils 
    	 * provide the built-in function for it, thus lessening our work.
    	 */
    	File tempJsFile = File.createTempFile("temp", ".js");
    	FileOutputStream out = new FileOutputStream(tempJsFile);
        IOUtils.copy(s3FileObj.getObjectContent(), out);
        out.close();
    }
}
