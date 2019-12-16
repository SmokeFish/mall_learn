package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Created by Hasee on 2019/12/16.
 */
public interface ICartService {

    ServerResponse add(Integer userId, Integer productId, Integer count);
    ServerResponse update(Integer userId,Integer productId,Integer count);
    ServerResponse delete(Integer userId,String productIds);
    ServerResponse list(Integer userId);
    ServerResponse selectOrUnselectAll(Integer userId,Integer checked);
    ServerResponse selectProject(Integer userId, Integer productId);
    ServerResponse unSelectProject(Integer userId, Integer productId);
    ServerResponse<Integer> getCartProductCount(Integer usrId);
}
