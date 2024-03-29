package com.course.system.controller;

import com.course.server.dto.ResponseDto;
import com.course.server.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    @ExceptionHandler(value= BusinessException.class)
    @ResponseBody
    public ResponseDto businessExceptionHandler(BusinessException e){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(false);
        LOG.error("业务异常: {}",e.getCode().getDesc());
        responseDto.setMessage("请求参数异常");
        return responseDto;

    }
}
