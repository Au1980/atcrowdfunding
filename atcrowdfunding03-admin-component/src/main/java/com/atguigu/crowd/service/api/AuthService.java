package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

public interface AuthService {
    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    List<Auth> getAllAuth();

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);
}
