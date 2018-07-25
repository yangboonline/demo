package com.bert.swagger.config;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author yangbo
 * @date 2018/5/30
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bert.swagger"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Bert的第一个Swagger2应用")
                .description("我的第一个Swagger2应用")
                .termsOfServiceUrl("https://www.baidu.com/")
                .contact(new Contact("Bert", "https://www.baidu.com/", "330719895@qq.com"))
                .version("1.0")
                .build();
    }

    private static final int DEFAULT_MAX_PER_ROUTE = 50;
    private static final int MAX_TOTAL = 500;
    private static final int RETRY_COUNT = 3;
    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int SOCKET_TIMEOUT = 10 * 1000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 200;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        // 创建SSL加密连接上下文,忽略对服务器端证书的校验,始终返回校验成功
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        httpClientBuilder.setSSLContext(sslContext);
        // 创建SSL连接套接字工厂,跳过主机名验证,与服务器存储X.509证书中的主机名不做匹配校验
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);
        // 创建连接工厂注册表,支持http和https两种协议的套接字工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory)
                .build();
        // 创建http连接池管理器
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 默认设置route最大连接数
        connMgr.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        // 连接池最大连接数
        connMgr.setMaxTotal(MAX_TOTAL);
        // 连接池信息
        httpClientBuilder.setConnectionManager(connMgr);
        // 重试次数，默认是3次，没有开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, Boolean.TRUE));
        // 创建HttpClient客户端
        CloseableHttpClient httpClient = httpClientBuilder.build();
        // 创建http请求工厂
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                httpClient);
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(SOCKET_TIMEOUT);
        // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
        clientHttpRequestFactory.setBufferRequestBody(false);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        return restTemplate;
    }

}
