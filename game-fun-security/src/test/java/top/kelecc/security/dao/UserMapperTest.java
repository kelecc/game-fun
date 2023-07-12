package top.kelecc.security.dao;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserMapperTest {
    @Resource
    private UserMapper userMapper;

//    @Test
//    void selectPermissionByUserIdTest() {
//        userMapper.selectPermissionByUserId(1L).forEach(System.out::println);
//    }

}
