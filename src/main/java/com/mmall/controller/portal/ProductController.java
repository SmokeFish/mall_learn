package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Hasee on 2019/12/10.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list( @RequestParam( value="keyword", required = false) String keyword ,
                                          @RequestParam( value="categoryId", required = false)Integer categoryId,
                                          @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "12")Integer pageSize,
                                          @RequestParam(value = "orderBy",defaultValue = "")String orderBy){
        return iProductService.getProductListByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}