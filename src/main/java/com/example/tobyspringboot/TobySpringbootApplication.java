package com.example.tobyspringboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


public class TobySpringbootApplication {

    public static void main(String[] args) {
        // application context 생성 및 Servlet Container 초기화
        GenericWebApplicationContext applicationContext = new GenericWebApplicationContext() {
            @Override
            protected void onRefresh() {
                super.onRefresh();

                ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet",
                            new DispatcherServlet(this)
                    ).addMapping("/*");
                });
                webServer.start();
            }
        };
        // Bean 등록
        applicationContext.registerBean(HelloController.class);
        applicationContext.registerBean(SimpleHelloService.class);

        // Container 초기 -> application context가 빈 Object들을 모두 생성함.
        applicationContext.refresh();


    }
}
