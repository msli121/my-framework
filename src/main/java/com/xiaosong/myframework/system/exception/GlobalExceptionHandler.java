package com.xiaosong.myframework.system.exception;

import com.xiaosong.myframework.business.dto.ApiResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void uploadExceptionHandler(MaxUploadSizeExceededException e, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("上传文件大小超出限制");
        out.flush();
        out.close();
    }

    @ExceptionHandler(Exception.class)
    public ApiResult globalDefaultExceptionHandler(Exception e) {
        return ApiResult.F("系统异常");
    }
}
