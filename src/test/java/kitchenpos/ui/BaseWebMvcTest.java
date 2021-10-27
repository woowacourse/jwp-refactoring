package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = {
        MenuGroupRestController.class,
        MenuRestController.class,
        ProductRestController.class,
        OrderRestController.class,
        TableRestController.class,
        TableGroupRestController.class
})
public class BaseWebMvcTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected TableService tableService;

    @MockBean
    protected TableGroupService tableGroupService;

    protected String parseJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8");
    }

    protected MockHttpServletRequestBuilder postRequest(String url, String content) {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(content);
    }

    protected MockHttpServletRequestBuilder putRequest(String url, String content) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(content);
    }

    protected MockHttpServletRequestBuilder putRequest(String url, String content, Object... pathVariables) {
        return put(url, pathVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(content);
    }

    protected MockHttpServletRequestBuilder deleteRequest(String url, Object... pathVariables) {
        return delete(url, pathVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8");
    }
}
