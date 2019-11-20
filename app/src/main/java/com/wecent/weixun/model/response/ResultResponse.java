package com.wecent.weixun.model.response;

/**
 * desc:
 * author: wecent
 * date: 2018/9/3
 */
public class ResultResponse<T> {

    public String has_more;
    public String message;
    public String success; 
    public T data;

    public ResultResponse(String more, String _message, T result) {
        has_more = more;
        message = _message;
        data = result;
    }
}
