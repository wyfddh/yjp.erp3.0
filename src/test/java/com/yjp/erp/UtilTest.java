package com.yjp.erp;

import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.service.parsexml.handle.ParseXmlServiceHandle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import com.yjp.erp.service.AnalysisServiceData;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ErpConfigPlatformApplication.class)
public class UtilTest {
	@Autowired
	private ParseXmlServiceHandle parseXmlServiceHandle;


	@Test
	public void testGetEeecaScript() throws BaseException {
		Module simpleModule = new Module() {{
			setClassId("bill");
			setTypeId("PurchaseInboundOrder");
		}};
        parseXmlServiceHandle.exeParse(simpleModule);
	}
	@Test
	public void parseViewEntity() throws BaseException {
//		parseXmlServiceHandle.exeParse("view","Profit");
	}
}

