package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;

    /**
     * 展示admin分配role页面
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ) {
        // 1.查询已分配角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 2.查询未分配角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);

        // 3.存入模型（本质上其实是： request.setAttribute("attrName",attrValue);
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList", unAssignedRoleList);

        return "assign-role";
    }

    /**
     * 给admin分配role
     * @param adminId
     * @param pageNum
     * @param keyword
     * @param roleIdList
     * @return
     */
    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam(value = "keyword") String keyword,
            // 我们允许用户在页面上取消所有已分配角色再提交表单， 所以可以不提供roleIdList 请求参数
            // 设置 required=false 表示这个请求参数不是必须的
            @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList
    ) {
        adminService.saveAdminRoleRelationship(adminId, roleIdList);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 获取所有的auth
     * @return
     */
    @ResponseBody
    @RequestMapping("assign/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth() {
        List<Auth> authList = authService.getAllAuth();
        return ResultEntity.successWithData(authList);
    }


    /**
     * 根据roleId查询authId
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
            @RequestParam("roleId") Integer roleId) {
        List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);
    }

    /**
     * 执行给角色分配权限
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity saveRoleAuthRelation(@RequestBody Map<String, List<Integer>> map) {
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }
}
