package life.majiang.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * Created by codedrinker on 2019/6/28.
 */
@Service
@Slf4j
public class UCloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;

    @Value("${ucloud.ufile.private-key}")
    private String privateKey;

    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    @Value("${ucloud.ufile.expires}")
    private Integer expires;

    //同步上传
    public String upload(InputStream fileStream, String mimeType, String fileName) {

        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
            ObjectConfig config = new ObjectConfig(region, suffix);
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();

            if (response != null && response.getRetCode() == 0) {
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expires)
                        .createUrl();
                return url;
            } else {
                log.error("upload error,{}", response);
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (UfileClientException e) {
            log.error("upload error,{}", fileName, e);
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            log.error("upload error,{}", fileName, e);
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}

//    String generatedFileName;
//    String[] filePaths = fileName.split("\\.");
//        if (filePaths.length > 1) {
//        generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
//    } else {
//        return null;
//    }
//        try {
//        // 对象相关API的授权器
//        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
//
//        // 对象操作需要ObjectConfig来配置您的地区和域名后缀
//        ObjectConfig config = new ObjectConfig("cn-bj", "ufileos.com");
//        PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
//                .putObject(fileStream, mimeType)
//                .nameAs(generatedFileName)
//                .toBucket("wangrui")
//                /**
//                 * 是否上传校验MD5, Default = true
//                 */
//                //  .withVerifyMd5(false)
//                /**
//                 * 指定progress callback的间隔, Default = 每秒回调
//                 */
//                //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
//                /**
//                 * 配置进度监听
//                 */
//                .setOnProgressListener((bytesWritten, contentLength) -> {
//
//                })
//                .execute();
//    } catch (UfileClientException e) {
//        e.printStackTrace();
//        return null;
//    } catch (UfileServerException e) {
//        e.printStackTrace();
//        return null;
//    }
//        return generatedFileName;
//}
//}