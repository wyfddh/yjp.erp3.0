package com.yjp.erp.controller.view;

import com.yjp.erp.model.dto.view.ViewDTO;
import com.yjp.erp.service.ViewService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 视图查询相关接口
 * @author wyf
 * @date 2019/4/25 下午 3:36
 **/
@RestController
@Slf4j
@RequestMapping(value = "/apps")
public class ViewMoquiRestController {

    @Autowired
    private ViewService viewService;

    /**
     * @author wyf
     * @description      通过视图分页查询
     * @date  2019/5/21 下午 5:34
     * @param viewDTO 查询视图入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("view")
    public JsonResult pageListView(@RequestBody ViewDTO viewDTO) throws Exception{

        Map<String,Object> map = viewService.pageListView(viewDTO);
        return RetResponse.makeOKRsp(map);
    }
}
