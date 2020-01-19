package com.yangbo.es.model.common;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * 分页插件
 *
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @version 1.0
 * @date 2019/12/17 9:38
 */
@Getter
@Setter
@NoArgsConstructor
public class Pagination implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String uri = null;
    protected int pn = 1;
    protected int ps = 14;
    protected int maxPs = 1000;
    protected int total = 0;
    protected int pageTotal = 0;
    protected String currentPage = null;
    protected Map<String, Object> paramMap = Maps.newHashMap();
    private int showPageCount = 9;
    protected String pager = null;

    private Pagination(int ps) {
        this.ps = ps;
    }

    private Pagination(int pn, int ps) {
        this.pn = pn;
        this.ps = ps;
    }

    public void addParam(String paramKey, Object paramValue) {
        this.paramMap.put(paramKey, paramValue);
    }

    public void removeParam(String paramKey) {
        this.paramMap.remove(paramKey);
    }

    public Object getParam(String paramKey) {
        return this.paramMap.get(paramKey);
    }

    public String getPager() {
        if (this.pager == null) {
            this.pager = this.buildPage();
        }
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public int getStart() {
        return (pn - 1) * this.getPs();
    }

    public String getCurrentPage() {
        if (this.pager == null) {
            this.pager = this.buildPage();
        }
        return currentPage == null ? null : Base64.encodeBase64URLSafeString(currentPage.getBytes(Charsets.UTF_8));
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    private String buildPage() {
        pageTotal = (int) Math.ceil(this.total * 1.0 / this.getPs());
        StringBuilder url = new StringBuilder(uri + "?");
        if (paramMap != null && paramMap.size() > 0) {
            Set<String> paramKeys = paramMap.keySet();
            for (String paramKey : paramKeys) {
                if ("pn".equals(paramKey) || "ps".equals(paramKey)) {
                    continue;
                }
                Object paramValue = paramMap.get(paramKey);
                if (paramValue == null || "".equals(paramValue)) {
                    continue;
                }
                String encodeValue = paramValue.toString();
                try {
                    encodeValue = URLEncoder.encode(encodeValue, Charsets.UTF_8.name());
                } catch (UnsupportedEncodingException ignored) {
                }
                url.append(paramKey).append("=").append(encodeValue).append("&");
            }
        }
        url.append("ps=").append(this.getPs()).append("&");

        // 开始生成分页条
        StringBuilder pagerBuffer = new StringBuilder();
        pagerBuffer.append("<ul class='pagination'>");
        if (pn == 1) {
            pagerBuffer.append("<li class='disabled'><a href='#'>首页</a></li>");
        } else if (pn > 1) {
            pagerBuffer.append("<li><a href='").append(url).append("&pn=").append(1).append("'>首页</a></li>");
        }
        int showNoStart = pn - (getShowPageCount() / 2);
        showNoStart = showNoStart <= 0 ? 1 : showNoStart;
        int showNoEnd = showNoStart + getShowPageCount() - 1;
        showNoEnd = Math.min(showNoEnd, pageTotal);
        showNoStart = showNoEnd - getShowPageCount() + 1;
        showNoStart = showNoStart <= 0 ? 1 : showNoStart;

        for (int i = showNoStart; i <= showNoEnd; i++) {
            if (pn == i) {
                // 生成当前页URL (带参数)
                currentPage = url + "pn=" + i;
            }
            pagerBuffer.append(pn == i ? "<li class='active'" : "<li").append("><a href='").append(url)
                    .append("pn=").append(i).append("'").append(">").append(i).append("</a></li>");
        }
        if (pn >= pageTotal) {
            pagerBuffer.append("<li class='disabled'><a href='#'>末页</a></li>");
        } else {
            pagerBuffer.append("<li><a href='").append(url).append("&pn=").append(pageTotal).append("'>末页</a></li>");
        }
        pagerBuffer.append("</ul>");
        return pagerBuffer.toString();
    }

    public static Pagination duplicate(Pagination pgn) {
        Pagination pagination = new Pagination();
        pagination.setUri(pgn.getUri());
        pagination.setPn(pgn.getPn());
        pagination.setPs(pgn.getPs());
        pagination.setTotal(0);
        return pagination;
    }

    public static Pagination create(int pn, int ps) {
        Pagination p = new Pagination();
        p.pn = pn;
        p.ps = ps;
        return p;
    }

    public static Pagination create(int pn, int ps, int maxPs) {
        Pagination p = new Pagination();
        p.pn = pn;
        p.ps = ps;
        p.maxPs = maxPs;
        return p;
    }

}
