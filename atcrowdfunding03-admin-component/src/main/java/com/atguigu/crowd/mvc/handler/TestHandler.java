package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.param.ParamData;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
public class TestHandler {
    private static final Logger logger = LoggerFactory.getLogger(TestHandler.class);


    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping("/send/array/one.html")
    public String testReceiveArrayOne(@RequestParam("array[]")List<Integer> array, HttpServletRequest request) {
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult = " + judgeResult);

        System.out.println(1/0);

        array.forEach(System.out::println);

        return "target";
    }

    @ResponseBody
    @RequestMapping("/send/array/two.html")
    public String testReceiveArrayTwo(ParamData paramData) {
        paramData.getArray().forEach(System.out::println);
        return "target";
    }

    @ResponseBody
    @RequestMapping("/send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array) {
        for (Integer number : array) {
            logger.info("number = " + number);
        }
        return "target";
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request) {
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult = " + judgeResult);
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList", adminList);

        System.out.println(1/0);
        return "target";
    }
}
