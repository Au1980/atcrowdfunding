package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.exception.AccessForbiddenException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CrowdExceptionResolver {
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception
            , HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolveException(viewName, exception, request, response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception
            , HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolveException(viewName, exception, request, response);
    }

    /**
     * 核心异常处理方法
     * @param viewName 指定要前往的视图名称
     * @param exception SpringMVC捕获到的异常对象
     * @param request 为了判断当前请求是"普通请求"还是"ajax请求"
     * @param response 为了能够将json字符串作为当前请求的响应参数返回给浏览器
     * @return ModelAndView
     * @throws IOException
     */
    private ModelAndView commonResolveException(String viewName, Exception exception
            , HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        // 1.判断当前请求类型
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);

        // 2.如果是Ajax请求
        if (judgeRequestType) {
            // 3.创建ResultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());

            // 4.创建Gson对象, 将ResultEntity对象转换为Json字符串
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);

            // 5.将JSON字符串作为响应体返回给浏览器
            response.getWriter().write(json);

            // 6.由于上面已经通过原生response对象返回了响应，所以不提供ModelAndView对象，
            // 这样 SpringMVC 就知道不需要框架解析视图来提供响应，而是程序员自己提供了响应
            return null;
        }

        // 7.如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", exception);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
