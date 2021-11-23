package kitchenpos.testtool.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import kitchenpos.testtool.response.HttpResponse;
import kitchenpos.testtool.response.MockMvcResult;
import kitchenpos.testtool.util.RequestDto;
import kitchenpos.testtool.util.TestTool;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

public class MockMvcExecutor implements TestAdapter {

    private final WebApplicationContext context;
    private final ObjectMapper objectMapper;

    public MockMvcExecutor(
            WebApplicationContext context,
            ObjectMapper objectMapper
    ) {
        this.context = context;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isAssignable(RequestDto requestDto) {
        return TestTool.MOCK_MVC.equals(requestDto.getTestTool());
    }

    @Override
    public HttpResponse execute(RequestDto requestDto) {
        final MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        try {
            MockHttpServletRequestBuilder servletRequestBuilder = urlRequest(requestDto);
            ResultActions resultActions = mockMvc.perform(servletRequestBuilder);
            return new MockMvcResult(resultActions, objectMapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("MockMvcExecutor execute 문제");
        }
    }

    private MockHttpServletRequestBuilder urlRequest(RequestDto requestDto) throws Exception {
        final HttpMethod httpMethod = requestDto.getHttpMethod();
        if (httpMethod.matches("GET")) {
            return MockMvcRequestBuilders.get(requestDto.getUrl(), requestDto.getPathVariables());
        }

        if (httpMethod.matches("POST")) {
            if (Objects.nonNull(requestDto.getData())) {
                return MockMvcRequestBuilders
                        .post(requestDto.getUrl(), requestDto.getPathVariables())
                        .content(objectMapper.writeValueAsBytes(requestDto.getData()))
                        .contentType(MediaType.APPLICATION_JSON);
            }
            return MockMvcRequestBuilders.post(requestDto.getUrl(), requestDto.getPathVariables());
        }

        if (httpMethod.matches("PUT")) {
            if (Objects.nonNull(requestDto.getData())) {
                return MockMvcRequestBuilders
                        .put(requestDto.getUrl(), requestDto.getPathVariables())
                        .content(objectMapper.writeValueAsBytes(requestDto.getData()))
                        .contentType(MediaType.APPLICATION_JSON);
            }
            return MockMvcRequestBuilders.put(requestDto.getUrl(), requestDto.getPathVariables());
        }
        if (httpMethod.matches("DELETE")) {
            return MockMvcRequestBuilders
                    .delete(requestDto.getUrl(), requestDto.getPathVariables());
        }
        throw new IllegalArgumentException("not matched http method");
    }
}
