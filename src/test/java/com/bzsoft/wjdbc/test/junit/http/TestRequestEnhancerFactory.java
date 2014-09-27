package com.bzsoft.wjdbc.test.junit.http;

import com.bzsoft.wjdbc.servlet.RequestEnhancer;
import com.bzsoft.wjdbc.servlet.RequestEnhancerFactory;

public class TestRequestEnhancerFactory implements RequestEnhancerFactory {
    public RequestEnhancer create() {
        return new TestRequestEnhancer();
    }
}
