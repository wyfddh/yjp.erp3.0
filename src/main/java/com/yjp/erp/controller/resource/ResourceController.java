//package com.yjp.erp.controller.resource;
//
//import com.yjp.erp.model.dto.resource.MenuNodeDTO;
//import com.yjp.erp.model.dto.resource.ResourceDTO;
//import com.yjp.erp.model.vo.JsonResult;
//import com.yjp.erp.model.vo.RetResponse;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * description:资源菜单等配置
// * @author liushui
// * @date 2019/3/28
// */
//@Deprecated
//@RestController
//@RequestMapping("/resource/menu")
//public class ResourceController {
//
//    /**
//     * 功能描述: 异步展示菜单树形结构
//     * @author liushui
//     * @date 2019/3/28
//     */
//    @GetMapping
//    @RequestMapping("/structure")
//    public JsonResult menuTree(MenuNodeDTO dto)throws Exception{
//        return RetResponse.makeOKRsp();
//    }
//
//    /**
//     * 功能描述: 添加菜单资源
//     * @author liushui
//     * @date 2019/3/28
//     */
//    @PostMapping
//    @RequestMapping("/add")
//    public JsonResult addMenu(ResourceDTO dto)throws Exception{
//        return RetResponse.makeOKRsp();
//    }
//
//}
