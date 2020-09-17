package com.cskaoyan.test.crowd;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuService menuService;

    @Test
    public void testLog() {
        // 1.获取Logger对象, 传入的class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        // 2.根据不同的日志级别打印
        // 19:10:30.128 [main] DEBUG com.cskaoyan.test.crowd.CrowdTest - Hello I am Debug level!!!
        logger.debug("Hello I am Debug level!!!");

        // 19:10:30.133 [main] INFO com.cskaoyan.test.crowd.CrowdTest - Hello I am Info level!!!
        logger.info("Hello I am Info level!!!");

        // 19:10:30.135 [main] WARN com.cskaoyan.test.crowd.CrowdTest - Hello I am Warn level!!!
        logger.warn("Hello I am Warn level!!!");

        // 19:10:30.136 [main] ERROR com.cskaoyan.test.crowd.CrowdTest - Hello I am Error level!!!
        logger.error("Hello I am Error level!!!");


    }


    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(null, "tom", "123456", "汤姆", "tom@qq.com", null);
        int insert = adminMapper.insert(admin);
        System.out.println(insert);
    }

    @Test
    public void getAdmin() {
        Admin admin = adminService.getAdminById(1);
        // 获取日志记录对象
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        // 按照debug级别打印日志
        logger.debug(admin.toString());
    }

    @Test
    public void testTx() {
        Admin admin = new Admin(null, "jerry", "123456", "杰瑞", "jerry@qq.com", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void insertBatch() {
        for (int i = 0; i < 238; i++) {
            adminMapper.insert(new Admin(null, "loginAcct" + i, "userPwd" + i, "userName" + i, "email" + i,  null));
        }
    }

    @Test
    public void testRole() {
        for (int i = 0; i < 235; i++) {
            roleMapper.insert(new Role(null, "role" + i));
        }

    }

    @Test
    public void testMenu() {
        List<Menu> list = menuService.getAll();
        list.forEach(System.out::println);
    }
}
