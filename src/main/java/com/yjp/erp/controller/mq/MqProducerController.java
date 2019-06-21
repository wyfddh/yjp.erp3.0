//package com.yjp.erp.controller.mq;
//
//import com.yjp.erp.model.dto.mq.MqMessageDTO;
//import com.yjp.erp.service.mq.MqService;
//import com.yjp.erp.model.vo.JsonResult;
//import com.yjp.erp.model.vo.RetResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * description:
// * @author liushui
// * @date 2019/4/9
// */
//@RestController
//@RequestMapping("/mq")
//public class MqProducerController {
//
//    @Autowired
//    private MqService mqService;
//
//    @PostMapping("/sendMessage")
//    public JsonResult sendMessage(@RequestBody MqMessageDTO dto){
//
//        mqService.sendMessage(dto);
//        return RetResponse.makeOKRsp();
//    }
//}
