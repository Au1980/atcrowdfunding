package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {
    @Autowired
    private AdminService adminService;

    /**
     * 登录
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ) {
        // 调用service方法执行登录检查
        // 这个方法如果能够返回admin对象说明登录成功，如果账号密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        // 将登录成功返回的admin对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);

        return "redirect:/admin/to/main/page.html";
    }

    /**
     * 登出
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        // 强制session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 用户维护
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            // 页面上有可能不提供关键词，要进行适配
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            // 浏览器未提供 pageNum 时， 默认前往第一页
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            // 浏览器未提供 pageSize 时， 默认每页显示 5 条记录
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap

    ) {
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    /**
     * 删除用户
     * @param adminId
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword
    ) {
        adminService.remove(adminId);

        return "redirect:/admin/get/page.html?pageNum="+ pageNum + "&keyword=" + keyword;
    }

    /**
     * 新增用户
     * @param admin
     * @return
     */
    @RequestMapping("/admin/save.html")
    public String saveAdmin(Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    /**
     * 显示更新用户页面
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId")Integer adminId,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             ModelMap modelMap) {
        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }

    /**
     * 更新用户
     * @param pageNum
     * @param keyword
     * @param admin
     * @return
     */
    @RequestMapping("/admin/update.html")
    public String update(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "keyword", defaultValue = "") String keyword,
                         Admin admin
    ){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
}
