package com.moefilm.web;

import com.moefilm.web.util.MybatisPlusUtil;

public class Generator {
    public static void main(String[] args) {
        MybatisPlusUtil.generator("moefilm-web", "osf_users", "com.moefilm.web", "moefilm.com", "root", "123456", "127.0.0.1", "osf", true);
    }
}
