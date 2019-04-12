package com.ljf.po.handler;

import org.omg.CORBA.ServerRequest;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理类
 * SpringBoot 提供了一个默认的异常处理   重写此类
 * 返回统一的SON格式
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 自定义异常信息
     * @param request
     * @param ex
     * @return
     */
    private String buildMessage(ServerRequest request,Throwable ex){
        StringBuffer sb=new StringBuffer("Falied to handle request[");
        sb.append(request.methodName());
        sb.append("  ");
        sb.append(request.uri());
        sb.append("]");
        if(ex !=null){
            sb.append(": ");
            sb.append(ex.getMessage());
        }
        return sb.toString();
    }

    /**
     * 构建返回的SON数据格式
     *
     * @param status
     * @param errorMessage
     * @return
     */
    public static Map<String,Object> response(int status,String errorMessage){
        Map<String,Object> map=new HashMap<>();
        map.put("code",status);
        map.put("message",errorMessage);
        map.put("data",null);
        return map;
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errAttributes
     * @return
     */
    @Override
    protected HttpStatus getHttpStatus(Map<String,Object> errAttributes){
        int  statusCode= (int) errAttributes.get("code");
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes
     * @return
     */
    protected RouterFunction<ServerResponse> getRoutingFuction(ErrorAttributes errorAttributes){
            return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse);
    }


    protected Map<String,Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace){

        int code = 500;
        Throwable error = super.getError(request);
        if(error instanceof NotFoundException){
            code = 404;
        }
        return response(code,this.buildMessage(request, error));

    }


}
