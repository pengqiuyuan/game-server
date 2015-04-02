package com.enlight.game.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author pengqiuyuan
 * jackson 利用Jackson中的ObjectMapper对象，转为对象为json字符串，springside对Jackson的简单封装 JsonBinder
 * 在ElasticSearch中使用,把 从索引index中取出的  map对象转换成 json字符串,再将json字符串转换成我们需要的 object对象
 */
public class JsonBinder {  
	  
    private static Logger logger = LoggerFactory.getLogger(JsonBinder.class);  
    
    private ObjectMapper mapper;  
  
    public JsonBinder(Inclusion inclusion) {  
        mapper = new ObjectMapper();  
        //设置输出包含的属性  
/*        mapper.getSerializationConfig().setSerializationInclusion(inclusion);  
        //设置输入时忽略JSON字符串中存在而Java对象实际没有的属性  
        mapper.getDeserializationConfig().set(  
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
    */
    }  
  
    /** 
     * 创建输出全部属性到Json字符串的Binder. 
     */  
    public static JsonBinder buildNormalBinder() {  
        return new JsonBinder(Inclusion.ALWAYS);  
    }  
  
    /** 
     * 创建只输出非空属性到Json字符串的Binder. 
     */  
    public static JsonBinder buildNonNullBinder() {  
        return new JsonBinder(Inclusion.NON_NULL);  
    }  
  
    /** 
     * 创建只输出初始值被改变的属性到Json字符串的Binder. 
     */  
    public static JsonBinder buildNonDefaultBinder() {  
        return new JsonBinder(Inclusion.NON_DEFAULT);  
    }  
  
    /** 
     * 如果JSON字符串为Null或"null"字符串,返回Null. 
     * 如果JSON字符串为"[]",返回空集合. 
     *  
     * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句: 
     * List<MyBean> beanList = binder.getMapper().readValue(listString, new TypeReference<List<MyBean>>() {}); 
     */  
    public <T> T fromJson(String jsonString, Class<T> clazz) {  
        if (StringUtils.isEmpty(jsonString)) {
            return null;  
        }  
  
        try {  
            return mapper.readValue(jsonString, clazz);  
        } catch (IOException e) {  
            logger.warn("parse json string error:" + jsonString, e); 
            return null;  
        }  
    }  
  
    /** 
     * 如果对象为Null,返回"null". 
     * 如果集合为空集合,返回"[]". 
     */  
    public String toJson(Object object) {  
  
        try {  
            return mapper.writeValueAsString(object);  
        } catch (IOException e) {  
            logger.warn("write to json string error:" + object, e);  
            return null;  
        }  
    }  
  
    /** 
     * 设置转换日期类型的format pattern,如果不设置默认打印Timestamp毫秒数. 
     */  
    public void setDateFormat(String pattern) {  
        if (StringUtils.isNotBlank(pattern)) {  
            DateFormat df = new SimpleDateFormat(pattern);  
/*            mapper.getSerializationConfig().setDateFormat(df);  
            mapper.getDeserializationConfig().setDateFormat(df);  */
        }  
    }  
  
    /** 
     * 取出Mapper做进一步的设置或使用其他序列化API. 
     */  
    public ObjectMapper getMapper() {  
        return mapper;  
    }  
}  