package com.moefilm.web.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;*/

public class UploadUtil {

	/*private static final String accessKeyID = "UoWSYNNNLNaCv1qp";
	private static final String accessKeySecret = "cTUhzFOj1jQVShx1FocJ9WNhJf6YjT";
	private static final String endpoint = "http://oss.aliyuncs.com";
	private static final String bucket = "osfimgs";
	private OSSClient client = new OSSClient(endpoint, accessKeyID, accessKeySecret);*/

	/*private static final String AK = "W0wLQrB0TcbcMINALCuOWnn7t50D1C5Di3xqarse";
	private static final String SK = "YpNOANdydyZ0hVCJh8hRzsPZN4VNQTFP_uHOzgRF";
	private static final String bucket = "osfimg";
	private Auth auth = Auth.create(AK, SK);
	private BucketManager bucketManager = new BucketManager(auth);*/

    public static String uploadFile(byte[] bytes, String key) {
        try {
            String filePath= System.getProperty("user.dir") + "/src/main/webapp/files/";
            OutputStream os = new FileOutputStream(filePath + key);
            os.write(bytes, 0, bytes.length);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;

		/*UploadManager uploadManager = new UploadManager();
		try {
			uploadManager.put(img, key, getUpToken());
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;*/

		/*ObjectMetadata meta = new ObjectMetadata();
		try {
			meta.setContentLength(img.available());

			//上传到图片服务器
			PutObjectResult result = client.putObject(bucket, key, img, meta);
			return result.getETag();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;*/
    }

	/*private String getUpToken() {
	    return auth.uploadToken(bucket, null, 3600, new StringMap().
	    		putNotEmpty("returnBody", "{\"key\": $(key), \"hash\": $(etag), \"width\": $(imageInfo.width), \"height\": $(imageInfo.height)}"));
	}*/

    public static void delFileInBucket(String key) {
        try {
            String filePath= System.getProperty("user.dir") + "/src/main/webapp/files/";
            File file = new File(filePath + key);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

		/*try {
			client.deleteObject(bucket, key);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		/*try {
			bucketManager.delete(bucket, key);
		} catch (QiniuException e) {
			e.printStackTrace();
		}*/
    }
}
