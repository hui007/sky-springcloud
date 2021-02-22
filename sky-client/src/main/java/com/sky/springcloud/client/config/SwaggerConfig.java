package com.sky.springcloud.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 默认访问地址：http://localhost:8080/swagger-ui.html#
 * springboot2+springfox-swagger2貌似能识别springMvc注解，不需要专门使用@Api、@ApiOperation、@ApiImplicitParam这些注解了
 */
@EnableSwagger2
@Configuration
@Profile(value = {"dev"}) // 最好只在开发环境使用，在线上环境停用
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //需要生成api接口的目录，一般是存放controller的目录
                .apis(RequestHandlerSelectors.basePackage("com.sky.springcloud.client.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 定义ApiInfo生成函数
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("SpringBoot使用Swagger2维护api文档")
                //联系人信息
                .contact(new Contact("joshui", "https://blog.csdn.net/xxx", "xxx@163.com"))
                .version("1.0")
                .description("Sky-client API 描述")
                .build();
    }
}