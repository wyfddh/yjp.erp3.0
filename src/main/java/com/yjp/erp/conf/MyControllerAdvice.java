package com.yjp.erp.conf;

import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.RetResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpResponseException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述: 全局Controller异常处理
 *
 * @author liushui
 * @date 2019/4/4
 */
@ControllerAdvice
@Slf4j
public class MyControllerAdvice {

    /**
     * 通用系统异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult errorHandler(Exception ex) {
        log.error("error", ex);
        return RetResponse.makeErrRsp("error");
    }

    /**
     * 通用业务异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public JsonResult baseRunTimeErrorHandler(RuntimeException ex) {
        log.error("error", ex);
        return RetResponse.makeErrRsp(ex.getMessage());
    }

    /**
     * 通用业务异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public JsonResult baseErrorHandler(BusinessException ex) {
        log.error("error", ex);
        return RetResponse.makeRsp(ex.getRetCode(), ex.getMsg());
    }

    /**
     * Http请求方式异常
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public JsonResult requestMethodErrorHandler(HttpRequestMethodNotSupportedException ex) {
        log.error("error", ex);
        return RetResponse.makeRsp(RetCode.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = HttpResponseException.class)
    public JsonResult httpResponseExceptionHandler(HttpResponseException ex) {
        log.error("http请求error", ex);
        return RetResponse.makeRsp(RetCode.MOQUI_ERROR, "moqui请求异常");
    }

    /**
     * 通用键值对请求数据校验异常
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public JsonResult validationErrorHandler(BindException ex) {
        List<String> errorInformation = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return RetResponse.makeRsp(RetCode.PARAM_EMPTY, "参数格式有误", errorInformation);
    }

    /**
     * 通用json请求数据校验异常
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult validationJsonErrorHandler(MethodArgumentNotValidException ex) {
        List<String> errorInformation = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return RetResponse.makeRsp(RetCode.PARAM_EMPTY, "参数格式有误", errorInformation);
    }
}
