package com.yjp.erp.model.vo;

public class RetResponse {

    private final static String SUCCESS = "success";

    public static <T> JsonResult<T> makeOKRsp() {
        return new JsonResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS);
    }

    public static <T> JsonResult<T> makeOKRsp(T data) {
        return new JsonResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS).setData(data);
    }

    public static <T> JsonResult<T> makeErrRsp(String message) {
        return new JsonResult<T>().setCode(RetCode.INTERNAL_SERVER_ERROR).setMsg(message);
    }

    public static <T> JsonResult<T> makeRsp(RetCode retCode) {
        return new JsonResult<T>().setCode(retCode.code);
    }

    public static <T> JsonResult<T> makeRsp(RetCode retCode, String msg) {
        return new JsonResult<T>().setCode(retCode.code).setMsg(msg);
    }

    public static <T> JsonResult<T> makeRsp(RetCode retCode, String msg, T data) {
        return new JsonResult<T>().setCode(retCode.code).setMsg(msg).setData(data);
    }
}
