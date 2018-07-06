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
import java.time.LocalDate;
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

    /**
     * 支付宝订单尾号默认长度
     */
    private static final long ALIPAY_COUNT = 11;

    /**
     * 天猫订单尾号默认长度
     */
    private static final long TMALL_COUNT = 8;

    /**
     * 天猫订单号前缀基准值
     */
    private static final long ORDER_PREFIX = 1000;

    /**
     * 随机数最大偏移量
     */
    private static final int MAX_OFFSET = 5000;

    /**
     * 日期时间毫秒
     */
    private static final String DATE_TIME_MILLISECOND = "yyyyMMddHHmmssSSS";

    /**
     * 日期
     */
    private static final String DATE = "yyyyMMdd";

    /**
     * 时间
     */
    private static final String TIME = "HHmmss";

    /**
     * 基准日期
     */
    private static final LocalDate BASE_DATE = LocalDate.of(2018, 1, 1);

    @Value("${server.port}")
    private String port;

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redisson;

    @Override
    public void run(String... args) {
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
     * 支付宝订单号生成器
     * <pre>
     *     example:
     *          支付宝流水号:2018062021001001710502897238
     * </pre>
     */
    public String generateAlipayOrderNumber(String businessCode) {
        // 随机生成大于0小于MAX_OFFSET的订单号偏量值
        int delta = new Random().nextInt(MAX_OFFSET) + 1;
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化当前时间到毫秒
        String timestamp = now.format(DateTimeFormatter.ofPattern(DATE_TIME_MILLISECOND));
        // 格式化当前时间到天
        String keySuffix = now.format(DateTimeFormatter.ofPattern(DATE));
        // 创建Redis联合键值
        String unionKey = String.join(":", businessCode, "Alipay", keySuffix);
        // 通过RedissonClient获取分布式原子类,生成订单尾号的初始值
        RAtomicLong counter = redisson.getAtomicLong(unionKey);
        // 执行递增操作
        counter.addAndGet(delta);
        // 获取counter的长度
        int length = String.valueOf(counter.get()).length();
        // 如果长度小于COUNT值,需要补位"0"
        StringBuilder sb = new StringBuilder();
        if (length < ALIPAY_COUNT) {
            sb.append(timestamp);
            for (int i = 0; i < ALIPAY_COUNT - length; i++) {
                sb.append("0");
            }
            sb.append(counter.get());
        } else {
            sb.append(timestamp).append(counter.get());
        }
        // 历史Redis联合键值最多在缓存中保留24小时
        redisTemplate.expire(unionKey, 24, TimeUnit.HOURS);
        return sb.toString();
    }

    /**
     * 天猫订单号生成器
     * <pre>
     *     example:
     *          天猫订单号:165035137214303944
     * </pre>
     */
    public String generateTMallOrderNumber(String businessCode) {
        // 随机生成大于0小于MAX_OFFSET的订单号偏量值
        int delta = new Random().nextInt(MAX_OFFSET) + 1;
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 获取当前日期
        LocalDate today = now.toLocalDate();
        // 与基准日期相差的天数
        long days = today.toEpochDay() - BASE_DATE.toEpochDay();
        // 订单号前缀
        String prefix = String.valueOf(ORDER_PREFIX + days);
        // 格式化当前时间为时分秒
        String middle = now.format(DateTimeFormatter.ofPattern(TIME));
        // 格式化当前时间到天
        String keySuffix = now.format(DateTimeFormatter.ofPattern(DATE));
        // 创建Redis联合键值
        String unionKey = String.join(":", businessCode, "TMall", keySuffix);
        // 通过RedissonClient获取分布式原子类,生成订单尾号的初始值
        RAtomicLong counter = redisson.getAtomicLong(unionKey);
        // 执行递增操作
        counter.addAndGet(delta);
        // 获取counter的长度
        int length = String.valueOf(counter.get()).length();
        // 如果长度小于COUNT值,需要补位"0"
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(middle);
        if (length < TMALL_COUNT) {
            for (int i = 0; i < TMALL_COUNT - length; i++) {
                sb.append("0");
            }
            sb.append(counter.get());
        } else {
            sb.append(counter.get());
        }
        // 历史Redis联合键值最多在缓存中保留24小时
        redisTemplate.expire(unionKey, 24, TimeUnit.HOURS);
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
