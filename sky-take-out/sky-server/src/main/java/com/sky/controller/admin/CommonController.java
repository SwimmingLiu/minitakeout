package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.QiNiuCloudOSSUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@RestController
@Tag(name="通用接口")
public class CommonController {

    @Autowired
    private QiNiuCloudOSSUtil qiNiuCloudOSSUtil;

    @Operation(summary = "文件上传")
    @PostMapping("/admin/common/upload")
    public Result upload(@RequestBody MultipartFile file) throws Exception {
        log.info("上传文件：{}", file.getOriginalFilename());
        String imageUrl = qiNiuCloudOSSUtil.uploadFiles(file);
        return Result.success(imageUrl);
    }
}
