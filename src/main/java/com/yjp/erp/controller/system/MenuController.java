package com.yjp.erp.controller.system;

import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.model.dto.system.MenuDTO;
import com.yjp.erp.model.dto.system.RoleDTO;
import com.yjp.erp.service.system.MenuService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.system.MenuVO;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单相关接口
 * @author wyf
 * @date 2019/4/3 下午 2:16
 **/
@RestController
@Slf4j
@RequestMapping({"/config/menu","/apps/menu"})
public class MenuController {

    @Autowired
    private UserInfoManager userInfoManager;

    @Autowired
    private MenuService menuService;

    /**
     * @author wyf
     * @description       根据当前登录用户获取菜单
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("getMenu")
    public JsonResult getMenu() throws Exception{
        //当前登录的用户
        Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
        MenuVO menuVO = menuService.getMenuByUserId(userId);
        return RetResponse.makeOKRsp(menuVO);
    }

    /**
     * @author wyf
     * @description       获取所有菜单
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("getAllMenu")
    public JsonResult getAllMenu() throws Exception{

        MenuVO menuVO = menuService.getAllMenu();
        return RetResponse.makeOKRsp(menuVO);
    }

    /**
     * @author wyf
     * @description   创建菜单
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("createMenu")
    public JsonResult createMenu(@RequestBody MenuDTO menuDTO) throws Exception{

        menuService.insertMenu(menuDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description   修改菜单
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("updateMenu")
    public JsonResult updateMenu(@RequestBody MenuDTO menuDTO) throws Exception{

        int type = 1;
        menuService.updateMenu(menuDTO,type);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description      删除菜单
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("deleteMenu")
    public JsonResult deleteMenu(@RequestBody MenuDTO menuDTO) throws Exception{

        int type = -1;
        menuService.updateMenu(menuDTO,type);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       根据角色获取菜单
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("getMenuByRole")
    public JsonResult getMenuByRole(@RequestBody RoleDTO roleDTO) throws Exception{

        Map<String, Object> map = menuService.listMenuByRole(roleDTO);
        return RetResponse.makeOKRsp(map);
    }

    /**
     * @author wyf
     * @description       根据名字模糊定位菜单
     * @param name 菜单名
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("menuTree")
    public JsonResult likeMenuTree(String name) throws Exception{

        List<String> longs = menuService.likeMenuTree(name);
        return RetResponse.makeOKRsp(longs);
    }

}
