package com.springcloud.common.test;

import com.springcloud.common.dao.UserMapper;
import com.springcloud.common.model.UserDetail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


/**
 * The type Mybatis test.
 */
@SpringBootTest
public class MybatisTest {

    private final UserMapper userMapper;

    /**
     * Instantiates a new Mybatis test.
     * @param userMapper the user mapper
     */
    public MybatisTest(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Test select.
     */
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<UserDetail> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
