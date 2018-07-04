package com.bert.oauth2.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author yangbo
 * @date 2018/7/3
 */
@Slf4j
@Service
@Order(Integer.MAX_VALUE - 1)
public class IdGeneratorService implements CommandLineRunner {

    private static final long COUNT = 11;

    @Value("${server.port}")
    private String port;

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redisson;

    @Override
    public void run(String... args) throws Exception {
        String currentWork = null;
        String workers = null;
        try {
            // 创建项目参数
            Option worker = Option.builder("w").longOpt("worker").hasArg(true).desc("total workers").required(true).build();
            Option currentWorker = Option.builder("cw").longOpt("currentWorker").hasArg(true).desc("current worker").required(true).build();
            Option help = Option.builder("h").longOpt("help").hasArg(false).desc("--worker=totalWorkers --currentWorker=currentWorker").build();

            // 整合所有参数
            Options opt = new Options();
            opt.addOption(help).addOption(worker).addOption(currentWorker);

            // 解析命令行参数
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(opt, args, Boolean.TRUE);
            if (!commandLine.hasOption("w") || !commandLine.hasOption("cw")) {
                log.error("must with --worker and --currentWorker args specified");
                System.exit(-1);
            }

            // 获取worker的值
            workers = commandLine.getOptionValue("worker");
            if (!StringUtils.isNumeric(workers)) {
                log.error("--worker must be a number");
                System.exit(-1);
            }

            // 获取currentWorker
            currentWork = commandLine.getOptionValue("currentWorker");
            if (!StringUtils.isNumeric(currentWork)) {
                log.error("--currentWoker must be a number");
                System.exit(-1);
            }

        } catch (Exception e) {
            log.error("解析项目启动参数异常,args:[{}]", args, e);
            System.exit(-1);
        }

        try {
            String ipAddress = getLocalIPAddress();
            log.info("当前服务的ip地址是:[{}],共{}台服务,当前编号是{}", ipAddress, workers, currentWork);
            if (StringUtils.isEmpty(ipAddress)) {
                throw new RuntimeException("ip地址获取失败，程序异常");
            }
            redisTemplate.opsForHash().put("OAUTH:IP", String.join("_", "w", workers, currentWork), String.join(":", ipAddress, port));
        } catch (Exception e) {
            log.error("初始化IP地址异常了", e);
            System.exit(-1);
        }
    }

    /**
     * id生成器(类支付宝订单号)
     * <pre>
     *     example:
     *          支付宝流水号:2018062021001001710502897238
     *          天猫订单号:165035137214303944
     * </pre>
     */
    public String generateId(String businessCode) {
        int delta = new Random().nextInt(50) + 1;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        RAtomicLong counter = redisson.getAtomicLong(businessCode);
        counter.addAndGet(delta);
        int length = String.valueOf(counter.get()).length();
        StringBuilder sb = new StringBuilder();
        if (length < COUNT) {
            sb.append(timestamp);
            for (int i = 0; i < COUNT - length; i++) {
                sb.append("0");
            }
            sb.append(counter.get());
        } else {
            sb.append(timestamp).append(counter.get());
        }
        redisTemplate.expire(businessCode, 24, TimeUnit.HOURS);
        return sb.toString();
    }

    /**
     * 获取本机ip
     */
    public static String getLocalIPAddress() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
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
