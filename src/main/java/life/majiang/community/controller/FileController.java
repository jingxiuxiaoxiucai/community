package life.majiang.community.controller;

import life.majiang.community.dto.FileDTO;
import life.majiang.community.provider.UCloudProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Slf4j
public class FileController {
    @Autowired
    private UCloudProvider uCloudProvider;


    @ResponseBody
    @RequestMapping("/file/upload")

    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        System.out.println(file.getName());
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;

        } catch (Exception e) {
            log.error("upload error", e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return fileDTO;
        }


//        try {
//            uCloudProvider.upload(file.getInputStream(),file.getContentType(),file.getOriginalFilename());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FileDTO fileDTO=new FileDTO();
//        fileDTO.setSuccess(1);
//        fileDTO.setUrl("/images/下载.jpg");
//        return  fileDTO;
    }


}
