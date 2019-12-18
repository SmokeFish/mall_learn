package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Hasee on 2019/12/16.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    public ServerResponse add(Integer userId,Integer productId,Integer count){
        if(userId == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        int resutCount = 0;
        if(cart == null){
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setChecked(Const.Cart.CHECKED);
            resutCount = cartMapper.insert(cart);
        }else {
            cart.setQuantity(cart.getQuantity() + count);
            resutCount = cartMapper.updateByPrimaryKey(cart);
        }
        if(resutCount > 0) {
            return ServerResponse.createBySuccess(getCartVoLimit(userId));
        }else{
            return ServerResponse.createByError("添加失败");
        }
    }

    public ServerResponse update(Integer userId,Integer productId,Integer count){
        if(userId == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        int resutCount = 0;
        if(cart != null){
            cart.setQuantity(count);
            resutCount = cartMapper.updateByPrimaryKey(cart);
        }
        if(resutCount > 0) {
            return ServerResponse.createBySuccess(getCartVoLimit(userId));
        }else{
            return ServerResponse.createByError("更新失败");
        }
    }

    public ServerResponse delete(Integer userId,String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int resutCount  = cartMapper.deleteByUserIdProductIds(userId,productList);
        if(resutCount > 0) {
            return ServerResponse.createBySuccess(getCartVoLimit(userId));
        }else{
            return ServerResponse.createByError("删除失败");
        }
    }

    public ServerResponse list(Integer userId){
        return ServerResponse.createBySuccess(getCartVoLimit(userId));
    }

    public ServerResponse selectOrUnSelectAll(Integer userId,Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,null,checked);
        return this.list(userId);
    }

    public ServerResponse selectProject(Integer userId, Integer productId){
        cartMapper.checkedOrUncheckedProduct(userId,productId,Const.Cart.CHECKED);
        return this.list(userId);
    }

    public ServerResponse unSelectProject(Integer userId, Integer productId){
        cartMapper.checkedOrUncheckedProduct(userId,productId,Const.Cart.UN_CHECKED);
        return this.list(userId);
    }

    public ServerResponse<Integer> getCartProductCount(Integer usrId){
        int count = cartMapper.selectCartProductCount(usrId);
        return ServerResponse.createBySuccess(count);
    }

    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product == null){
                    cartMapper.deleteByUserIdProductId(userId,cartItem.getProductId());
                    continue;
                }else{
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);

                        Cart cartForQuanlity = new Cart();
                        cartForQuanlity.setId(cartItem.getId());
                        cartForQuanlity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuanlity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    cartProductVo.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartProductVo.getProductChecked().equals(Const.Cart.CHECKED)){
                    cartTotalPrice = BigDecimalUtil.add(cartProductVo.getTotalPrice().doubleValue(),cartTotalPrice.doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setAllChekced(getAllCheckedStatus(userId));
            cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        }
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}
