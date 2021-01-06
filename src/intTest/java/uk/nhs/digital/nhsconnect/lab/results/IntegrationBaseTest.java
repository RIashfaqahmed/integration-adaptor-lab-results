package uk.nhs.digital.nhsconnect.lab.results;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, SoftAssertionsExtension.class, IntegrationTestsExtension.class})
@SpringBootTest
@Slf4j
public abstract class IntegrationBaseTest {

}
