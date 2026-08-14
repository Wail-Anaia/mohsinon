package com.mohsinon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MohsinonApplicationTests {

    @Test
    @DisplayName("Application context should load successfully with Core module")
    void contextLoads() {
    }
}
