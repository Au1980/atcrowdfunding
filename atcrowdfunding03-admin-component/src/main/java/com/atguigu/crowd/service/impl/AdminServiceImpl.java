package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;



    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1.根据登录账号查询Admin对象
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> list = adminMapper.selectByExample(adminExample);

        // 2.判断返回对象是否为null
        if (list == null || list.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        Admin admin = list.get(0);
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 3.如果Admin对象不为null则将数据库密码从Admin对象中取出
        String userPswdFromDB = admin.getUserPswd();

        // 4.将表单提交的明文密码进行加密
        String userPwdForm = CrowdUtil.md5(userPswd);

        // 5.对密码进行比较
        if (!Objects.equals(userPswdFromDB, userPwdForm)) {
            // 6. 如果不一致抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 7.如果一致返回admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1.调用pageHelper的静态分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装到PageInfo对象中
        return new PageInfo<>(list);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void saveAdmin(Admin admin) {
        // 生成当前系统时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);

        admin.setCreateTime(createTime);

        // 针对登录密码进行加密
        String source = admin.getUserPswd();
        String encoded = CrowdUtil.md5(source);
        admin.setUserPswd(encoded);

        // 执行保存，如果账户被占用会抛出异常
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            // 检测当前捕获的异常对象，如果是DuplicateKeyException类型说明是账号重复导致的
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            // 为了不掩盖问题， 如果当前捕获到的不是 DuplicateKeyException 类型的异常， 则把当前捕获到的异常对象继续向上抛出
            throw e;
        }
    }
    @Override
    public void update(Admin admin) {
        //表示有选择的更新，null不更新
        try {
            adminMapper.updateByPrimaryKey(admin);
        } catch (Exception e) {

            e.printStackTrace();
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            throw e;
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 为了简化操作： 先根据 adminId 删除旧的数据， 再根据 roleIdList 保存全部新的数据
        // 1.根据 adminId 删除旧的关联关系数据
        adminMapper.deleteOLdRelationship(adminId);
        // 2.根据 roleIdList 和 adminId 保存新的关联关系
        if(roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }
}
