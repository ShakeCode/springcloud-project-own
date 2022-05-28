package com.springcloud.data.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The type Repeat url request interceptor.
 */
@Component
public class RepeatUrlRequestInterceptor extends RepeatRequestInterceptor {
    private final Logger LOGGER = LoggerFactory.getLogger(RepeatUrlRequestInterceptor.class);

    /**
     * 重复提交判断时间为8s
     */
    private final Cache<String, Map<String, Object>> cache = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build();

    /**
     * The Repeat params.
     */
    public final String REPEAT_PARAMS = "repeatParams";

    /**
     * The Repeat time.
     */
    public final String REPEAT_TIME = "repeatTime";

    /**
     * 间隔时间，单位:秒 默认10秒
     *
     * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
     */
    private final int intervalTime = 10;

    /**
     * Is repeat submit boolean.
     * @param request the request
     * @return the boolean
     */
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request) {
        String nowParams = JSONObject.toJSONString(request.getParameterMap());
        LOGGER.info("request param:{}", nowParams);
        Map<String, Object> nowDataMap = new HashMap<>(2);
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());
        String url = request.getRequestURI();
        // 唯一值（没有消息头可以使用请求地址）
        //String requestKey = request.getHeader(header);
        // 作为存放cache的key值
        String requestKey = url;
        // 唯一标识（指定key + 消息头）
        String cache_repeat_key = "repeat_submit:" + requestKey;
        Map<String, Object> sessionMap = cache.getIfPresent(cache_repeat_key);
        if (!CollectionUtils.isEmpty(sessionMap)) {
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap)) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<>(2);
        cacheMap.put(url, nowDataMap);
        cache.put(cache_repeat_key, cacheMap);
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        if ((time1 - time2) < (this.intervalTime * 1000)) {
            return true;
        }
        return false;
    }
}
