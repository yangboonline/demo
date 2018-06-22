package com.bert.plugins.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * @author yangbo
 * @date 2018/6/21
 */
@Slf4j
public class SoapUtil {

    /**
     * 将String格式成SOAPMessage
     *
     * @param soapString
     * @return
     */
    public static SOAPMessage format(String soapString) {
        MessageFactory msgFactory;
        try {
            msgFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = msgFactory.createMessage(new MimeHeaders(),
                    new ByteArrayInputStream(soapString.getBytes(Charset.forName("UTF-8"))));
            soapMessage.saveChanges();
            return soapMessage;
        } catch (Exception e) {
            log.error("format Soap String error.", e);
        }
        return null;
    }

    /**
     * 将SOAPBody解析成String
     *
     * @param soapMessage
     * @return
     */
    public static String parseBody(SOAPMessage soapMessage) {
        StringWriter sw = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Document document = soapMessage.getSOAPBody().extractContentAsDocument();
            transformer.transform(new DOMSource(document), new StreamResult(sw));
        } catch (Exception e) {
            log.error("parse SoapBody error.", e);
        }
        return sw.toString();
    }

    /**
     * 将SOAPHeader解析成String
     *
     * @param soapMessage
     * @return
     */
    public static String parseHeader(SOAPMessage soapMessage, String tagName) {
        StringWriter sw = new StringWriter();
        try {
            Node node = soapMessage.getSOAPHeader().getElementsByTagName(tagName).item(NumberUtils.INTEGER_ZERO);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(sw));
        } catch (Exception e) {
            log.error("parse SoapHeader error.", e);
        }
        return sw.toString();
    }

    /**
     * 创建SoapXml
     *
     * @param xmlHeader
     * @param xmlBody
     * @return
     */
    public static String createSoapXml(String xmlHeader, String xmlBody) {
        String soapXml = null;
        ByteArrayOutputStream bos;
        try {
            // 创建SOAPMessage
            SOAPMessage message = MessageFactory.newInstance().createMessage();
            // 添加xml描述信息
            message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
            // SOAPBody添加子节点
            SOAPBody soapBody = message.getSOAPBody();
            soapBody.addTextNode(xmlBody);
            // SOAPHeader添加子节点
            SOAPHeader soapHeader = message.getSOAPHeader();
            soapHeader.addTextNode(xmlHeader);
            // 保存SOAPMessage
            message.saveChanges();
            // 初始化输出流
            bos = new ByteArrayOutputStream();
            // SOAPMessage写入输出流
            message.writeTo(bos);
            // 去除转义
            soapXml = StringEscapeUtils.unescapeXml(bos.toString());
        } catch (Exception e) {
            log.error("create SoapXml error.", e);
        }
        return soapXml;
    }

}
