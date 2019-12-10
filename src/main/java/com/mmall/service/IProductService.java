package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by Hasee on 2019/12/7.
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductListByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);

    ServerResponse getProductList(Integer pageNum , Integer pageSize);

    ServerResponse searchProduct(String productName,Integer productId, Integer pageNum , Integer pageSize);
}
