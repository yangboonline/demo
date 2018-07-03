package com.bert.oauth2.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

/**
 * @author yangbo
 * @date 2018/7/3
 */
@Slf4j
@Service
@Order(Integer.MAX_VALUE - 1)
public class IdGeneratorService implements CommandLineRunner {

    @Value("${server.port}")
    private String port;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        Long currentWork = null;
        try {
            Long workers = null;
            Options opt = new Options();
            Option worker = new Option("w", "worker", true, "total workers");
            worker.setRequired(true);
            Option currentWorker = new Option("cw", "currentWorker", true, "current worker");
            currentWorker.setRequired(true);
            opt.addOption("h", "help", false, "--worker=totalWorkers --currentWorker=currentWorker")
                    .addOption(worker)
                    .addOption(currentWorker);
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(opt, args, true);
            if (!commandLine.hasOption("w") || !commandLine.hasOption("cw")) {
                log.error("must with --worker and --currentWorker args specified");
                System.exit(-1);
            }
            String value = commandLine.getOptionValue("worker");
            if (StringUtils.isNumeric(value) && StringUtils.isNotEmpty(value)) {
                workers = Long.parseLong(value);
            } else {
                log.error("--worker must be a number");
                System.exit(-1);
            }
            value = commandLine.getOptionValue("currentWorker");
            if (StringUtils.isNumeric(value) && StringUtils.isNotEmpty(value)) {
                currentWork = Long.parseLong(value);
            } else {
                log.error("--currentWoker must be a number");
                System.exit(-1);
            }
        } catch (Exception e) {
            log.error("解析项目启动参数异常,args:[{}]", (Object[]) args, e);
            System.exit(-1);
        }

        try {
            String ipAddress = getLocalIPAddress();
            log.info("当前服务的ip地址是:[{}]", ipAddress);
            if (StringUtils.isEmpty(ipAddress)) {
                throw new RuntimeException("ip地址获取失败，程序异常");
            }
            redisTemplate.opsForHash().put("OAUTH:IP", "w_".concat(String.valueOf(currentWork)), ipAddress.concat(":").concat(port));
        } catch (Exception e) {
            log.error("初始化IP地址异常了", e);
            System.exit(-1);
        }
    }

    /**
     * id生成器(类支付宝订单号)
     */
    public String generateId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        //todo redis数字生成器
        return timestamp;
    }

    /**
     * 获取本机ip
     */
    public static String getLocalIPAddress() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    if (!"localhost".equalsIgnoreCase(ip.getHostAddress()) || !"127.0.0.1".equalsIgnoreCase(ip.getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        throw new RuntimeException("未能获取本机IP地址");
    }

}
