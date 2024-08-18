package com.example.tobyspringboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloControllerTest {
    @Test
    void helloController() {
        HelloController helloController = new HelloController(name -> name);

        String ret = helloController.hello("test");

        Assertions.assertThat(ret).isEqualTo("test");
    }

    @Test
    void failHelloController() {
        HelloController helloController = new HelloController(name -> name);

        // NullPointerException이 발생하면 테스트 성공
        // 발생하지 않는다면 테스트 실패
        Assertions.assertThatThrownBy(() -> {
            String ret = helloController.hello(null);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            String ret = helloController.hello("");
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
