package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by think9 on 19-12-2.
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //default dataload,while get doesnot get values,if key doesnot have value ,do this method to load
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static  void setKey(String key,String value ){
        localCache.put(key,value);
    }
    public static  String getKey(String key){
        String value = null;
        try{
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
        }catch(Exception e){
            logger.error("localCache get error",e);
        }
        return  value;
    }
}