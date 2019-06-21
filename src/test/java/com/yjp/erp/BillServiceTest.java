package com.yjp.erp;

import com.yjp.erp.service.BillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ErpConfigPlatformApplication.class)
public class BillServiceTest {

    @Resource
    BillService billService;

    @Test
    public void insertBillTest() {
       /* BillDO billDO = new BillDO();
        List<Bill> bills = new ArrayList<>();
        Bill bill = new Bill();
        UUID uuid = UUID.randomUUID();
        bill.setId(uuid.timestamp());*/
    }
}
