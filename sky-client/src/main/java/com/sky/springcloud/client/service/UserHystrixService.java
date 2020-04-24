package com.sky.springcloud.client.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.sky.springcloud.client.domain.CommonResult;
import com.sky.springcloud.client.domain.User;
import com.sky.springcloud.client.filter.HystrixRequestContextFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 使用hystrix做服务降级
 */
@Service
@Slf4j
public class UserHystrixService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-in-eureka.sky-server}")
    private String userServiceUrl;

    /**
     * 服务降级
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser")
    public CommonResult getUser(Long id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * HystrixCommand注解说明：
     * fallbackMethod：指定服务降级处理方法；
     * ignoreExceptions：忽略某些异常，不发生服务降级；
     * commandKey：命令名称，用于区分不同的命令；
     * groupKey：分组名称，Hystrix会根据不同的分组来统计命令的告警及仪表盘信息；
     * threadPoolKey：线程池名称，用于划分线程池。
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser",
            commandKey = "getUserCommand",
            groupKey = "getUserGroup",
            threadPoolKey = "getUserThreadPool")
    public CommonResult getUserCommand(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * 使用ignoreExceptions忽略某些异常降级
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser2", ignoreExceptions = {NullPointerException.class})
    public CommonResult getUserException(Long id) {
        if (id == 1) {
            throw new IndexOutOfBoundsException();
        } else if (id == 2) {
            throw new NullPointerException();
        }
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * Hystrix的请求缓存。缓存请求远端服务的返回结果，感觉这个功能比较鸡肋。
     * 注解@CacheResult：开启缓存，默认所有参数作为缓存的key，cacheKeyMethod可以通过返回String类型的方法指定key；
     * 注解@CacheKey：指定缓存的key，可以指定参数或指定参数中的属性值为缓存key
     *
     * <p><p>
     * {@link HystrixRequestContextFilter}
     * 在缓存使用过程中，我们需要在每次使用缓存的请求前后对HystrixRequestContext进行初始化和关闭，否则会出现如下异常：
     * java.lang.IllegalStateException: Request caching is not available. Maybe you need to initialize the HystrixRequestContext?
     * 可以通过使用过滤器，在每个请求前后初始化和关闭HystrixRequestContext来解决该问题。
     * @param id
     * @return
     */
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser", commandKey = "getUserCache")
    public CommonResult getUserCache(Long id) {
        log.info("getUserCache id:{}", id);
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * 移除缓存，需要指定commandKey
     * @param id
     * @return
     */
    @CacheRemove(commandKey = "getUserCache", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public CommonResult removeCache(Long id) {
        log.info("removeCache id:{}", id);
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, CommonResult.class, id);
    }

    /**
     * 请求合并
     * batchMethod：用于设置请求合并的方法；
     * collapserProperties：请求合并属性，用于控制实例属性，有很多；
     * timerDelayInMilliseconds：collapserProperties中的属性，用于控制每隔多少时间合并一次请求；
     * @param id
     * @return
     */
    @HystrixCollapser(batchMethod = "getUserByIds",collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public Future<User> getUserFuture(Long id) {
        return new AsyncResult<User>(){
            @Override
            public User invoke() {
                CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
                Map data = (Map) commonResult.getData();
                User user = new User();
                try {
                    BeanUtils.populate(user, data);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                log.info("getUserById username:{}", user.getUsername());
                return user;
            }
        };
    }

    @HystrixCommand
    public List<User> getUserByIds(List<Long> ids) {
        log.info("getUserByIds:{}", ids);
        CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/getUserByIds?ids={1}", CommonResult.class, StringUtils.join(ids,","));
        return (List<User>) commonResult.getData();
    }

    public CommonResult getDefaultUser(@PathVariable Long id) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new CommonResult<>(defaultUser);
    }

    public CommonResult getDefaultUser2(@PathVariable Long id, Throwable e) {
        log.error("getDefaultUser2 id:{},throwable class:{}", id, e.getClass());
        User defaultUser = new User(-2L, "defaultUser2", "123456");
        return new CommonResult<>(defaultUser);
    }

    /**
     * 为缓存生成key的方法
     */
    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }

}
