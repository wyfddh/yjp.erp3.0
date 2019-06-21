package com.yjp.erp;

import com.yjp.erp.handle.ScriptHand;
import com.yjp.erp.service.BaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ErpConfigPlatformApplication.class)
public class RemoteFileWrite {
    @Autowired
    private BaseService baseService;
    @Autowired
    private ScriptHand scriptHand;
    @Test
    public void writeTest() throws Exception {
//        JschUtil.remoteWriteFile();
    }

    @Test
    public void upload() throws Exception {
        baseService.uploadData();
    }

    @Test
    public void testScript() throws Exception {
        scriptHand.createScriptByClassIdAndTypeId(12693L);
    }
}
