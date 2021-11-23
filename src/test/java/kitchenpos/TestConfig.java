package kitchenpos;

import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.request.TestAdapterContainer;
import kitchenpos.testtool.util.TestTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public RequestBuilder newRequestBuilder(TestAdapterContainer testAdapterContainer) {
        return new RequestBuilder(testAdapterContainer, TestTool.MOCK_MVC);
    }
}
