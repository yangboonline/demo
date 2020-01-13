package com.yangbo.es.config;

import com.google.common.collect.Lists;
import com.yangbo.es.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Configuration
public class ElasticSearchClientConfig {

    @Value("${es.http.hosts}")
    private String httpHosts;

    @Bean
    public RestClientBuilder restClientBuilder() {
        List<HttpHost> list = Lists.newArrayList();
        for (String httpHost : StringUtils.split(httpHosts, Constant.COMMA)) {
            String hostName = StringUtils.substringBeforeLast(httpHost, Constant.COLON);
            String port = StringUtils.substringAfterLast(httpHost, Constant.COLON);
            Assert.hasText(hostName, "[Assertion failed] missing host name in 'httpHosts'");
            Assert.hasText(port, "[Assertion failed] missing port in 'httpHosts'");
            log.info("parsing http host : {}", httpHost);
            list.add(new HttpHost(hostName, Integer.parseInt(port), Constant.SCHEME));
        }
        HttpHost[] httpHosts = list.toArray(new HttpHost[]{});
        return RestClient.builder(httpHosts);
    }

    @Bean
    public RestClient restClient(RestClientBuilder restClientBuilder) {
        return restClientBuilder
                .setRequestConfigCallback(builder -> builder.setConnectTimeout(5000).setSocketTimeout(60000)).build();
    }

    @Bean
    public Sniffer sniffer(RestClient restClient) {
        return Sniffer.builder(restClient).build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

}
