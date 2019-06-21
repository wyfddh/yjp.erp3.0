package com.yjp.erp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErpConfigPlatformApplicationTests {

    /**
     * 模拟mvc测试对象
     */
    private MockMvc mockMvc;

    /**
     * web项目上下文
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 所有测试方法执行之前执行该方法
     */
    @Before
    public void before() {
        //获取mockmvc对象实例
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void contextLoads() throws Exception{

//        MvcResult mvcResult = mockMvc
//                .perform( MockMvcRequestBuilders.get("/bill/insert"))
//                .andReturn();
//
//        int status = mvcResult.getResponse().getStatus();
//        String responseString = mvcResult.getResponse().getContentAsString();
//
//        Assert.assertEquals("请求错误", 200, status);
//        Assert.assertEquals("返回结果不一致", "{\"code\":200,\"msg\":\"success\",\"data\":null}", responseString); // 8
    }

}
