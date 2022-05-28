package com.springcloud.data.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.springcloud.common.model.ResultVO;
import com.springcloud.data.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * The type Data controller.
 */
//@RefreshScope
@RequestMapping("v1/data/")
@Api(value = "数据服务接口")
@RestController
public class DataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    private final RestTemplate restTemplate;

    private final LoadBalancerClient loadBalancerClient;

    private final ProductService productService;

    /**
     * Instantiates a new Data controller.
     * @param restTemplate       the rest template
     * @param loadBalancerClient the load balancer client
     * @param productService     the product service
     */
    public DataController(RestTemplate restTemplate, LoadBalancerClient loadBalancerClient, ProductService productService) {
        this.restTemplate = restTemplate;
        this.loadBalancerClient = loadBalancerClient;
        this.productService = productService;
    }

    // 从对应的配置中心找到文件并把属性注入到value值中
    @Value("${config.name}")
    private String configName;

    /**
     * Gets config name.
     * @return the config name
     */
    @ApiOperation(value = "从配置中心获取配置")
    @GetMapping("/config/get")
    public String getConfigName() {
        return "get config from config-center-server:" + configName;
    }

    /**
     * Gets data info.
     * @return the data info
     */
    @GetMapping(value = "/info")
    public ResultVO getDataInfo() {
        return ResultVO.success("this is data service");
    }

    /**
     * Gets product data info.
     * @return the product data info
     */
    @GetMapping(value = "/product/info")
    public ResultVO getProductDataInfo() {
        return ResultVO.success("this is product data service!!!");
    }


    /**
     * Test feign invoke result vo.
     * @param name the name
     * @return the result vo
     */
    @ApiOperation(value = "测试熔断机制")
    @HystrixCommand(fallbackMethod = "getErrorInfo",
            commandProperties = {
                    // 是否启用服务熔断
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    // 时间窗口内的请求阈值,只有达到这个阈值，才会判断是否打开断路器。比如配置为10次，那么在时间窗口内请求9次，9次都失败了也不会打开断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    // 时间窗口,当断路器打开后，会根据这个时间继续尝试接受请求，如果请求成功则关闭断路器。
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
                    // 错误比率,在时间窗口内请求次数达到请求阈值，并且失败比率达到配置的50%，才会打开断路器
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")})
    @GetMapping(value = "/consume/{name}")
    public ResultVO testFeignInvoke(@PathVariable("name") String name) {
        // return ResultVO.successData(productService.getProductInfo(name));
        return ResultVO.successData(productService.getUserInfo(name));
    }

    /**
     * Gets error info.服务降级提示信息
     *
     * 降级方法和被降级接口方法的入参和返回值必须相同
     * @param name the name
     * @return the error info
     */
    public ResultVO getErrorInfo(String name) {
        return ResultVO.fail("consume服务器内部出现错误".concat(",传入参数:" + name));
    }

    /**
     * Test robbon invoke result vo.
     * @param name the name
     * @return the result vo
     */
    @ApiOperation(value = "测试调用注册中心服务")
    @RequestMapping(value = "/consume", method = RequestMethod.GET)
    public ResultVO testRobbonInvoke(String name) {
        // 通过服务列表调用
        String forObject = this.getByServerOrderList(name);
        // 通过restful-api调用
//        String forObject = this.getByRestfulApi(name);
        //根据服务名 获取服务列表 根据算法选取某个服务 并访问某个服务的网络位置
//        String forObject = this.getByServiceInstance(name);
        LOGGER.info("return result:{}", forObject);
        return ResultVO.success(forObject);
    }

    private String getByServiceInstance(String name) {
        // 不经过注册中心那服务列表，直接访问的servicesupport
        //根据服务名 获取服务列表 根据算法选取某个服务 并访问某个服务的网络位置
        ServiceInstance serviceInstance = loadBalancerClient.choose("EUREKA-COMMON-SERVICE");
        return new RestTemplate().getForObject("http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/v1/data/world?name=" + name, String.class);
    }

    private String getByServerOrderList(String name) {
        // 通过服务列表调用, 被调用者启动多个实例, 实现负载均衡
        return restTemplate.getForObject("http://EUREKA-COMMON-SERVICE/v1/data/world?name=" + name, String.class);
    }

    private String getByRestfulApi(String name) {
        // 通过restful-api调用
        return new RestTemplate().getForObject("http://127.0.0.1:8090/v1/data/world?name=" + name, String.class);
    }
}
