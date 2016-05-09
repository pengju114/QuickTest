/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pj.rest;

import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

/**
 * REST URI分为3个域：/操作对象/操作/[条件运算或ID[?参数]] 即: /target/operation/[condition[?queryString]]; 
 * 多个REST URI可以拼接组成父子关系。
 * @author luzhenwen
 */
public class RestURIParser {
    private static final String ALIAS_TABLE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    /**
     * 解析rest uri,当rest uri不符合规则时抛出错误,仅判断组合规则,不对每个单元进行检测.
     * @param restUri rest uri字符串
     * @param charset 解析时的字符编码,可选,默认为utf-8.
     * @return 
     * @throws java.lang.Exception 
     */
    public static List<RestURIPart> parse(String restUri, String charset) throws Exception{
        LinkedList<RestURIPart> parts = new LinkedList<RestURIPart>();
        if (restUri != null) {
            try {
                restUri = URLDecoder.decode(restUri, charset == null?"UTF-8":charset);
            } catch (Exception e) {
            }
            
            System.out.println("parse rest uri:"+restUri);
            int queryStringIndex = restUri.lastIndexOf("?");
            if (queryStringIndex != -1) {
                // 去掉查询字符串
                restUri = restUri.substring(0, queryStringIndex);
            }
            String[] units = restUri.replaceAll("^\\/|\\/$", "").split("\\/");
            
            if (units.length < 2) {// 至少要包含 path 和 operation 两个单元
                throw new IllegalArgumentException("illegal rest uri format:"+restUri+"; it should at least containing two parts.");
            }
            
            int lastIndex = units.length - 1;
            List<String> operations = RestURIPart.OPERATIONS;
            for (int i = units.length - 1; i > -1 ; i--) {
                String unit = units[i];
                if (operations.contains(unit)) {
                    // 操作单元
                    if (lastIndex - i > 1) {
                        // illegal unit
                        throw new IllegalArgumentException("illegal rest uri format:"+restUri);
                    }
                    if (i < 1 || operations.contains(units[i-1])) {
                        // 操作单元前面必须是path单元,而且不能是操作单元
                        throw new IllegalArgumentException("illegal rest uri format:"+restUri+"; before the operation unit is must be a path unit.");
                    }
                    
                    RestURIPart child = null;
                    if (parts.size() > 0) {
                        child = parts.getFirst();
                    }
                    RestURIPartImpl impl = new RestURIPartImpl(units[i - 1], Character.toString(ALIAS_TABLE.charAt(i)) , unit, lastIndex == i?null:units[lastIndex]);
                    if (child != null) {
                        // parent只能是list或者get
                        if (!impl.getOperation().equalsIgnoreCase(RestURIPart.OPERATION_LIST) && !impl.getOperation().equalsIgnoreCase(RestURIPart.OPERATION_GET)) {
                            throw new IllegalArgumentException("illegal rest uri format:"+restUri+"; parent uri's operation can only be 'list' and 'get'");
                        }
                        ((RestURIPartImpl)child).setParent(impl);
                        if (!child.getCondition().isRequireParent()) {
                            throw new IllegalArgumentException("illegal rest uri format:"+restUri+"; child rest uri must have a var param");
                        }
                    }
                    parts.addFirst(impl);
                    
                    i--;// 跳过当前操作单元前面的path单元
                    lastIndex = i - 1;// 此时lastIndex移到path前一单元的位置
                }
            }
        }else{
            throw new IllegalArgumentException("restUri should not be null.");
        }
        return parts;
    }
}
