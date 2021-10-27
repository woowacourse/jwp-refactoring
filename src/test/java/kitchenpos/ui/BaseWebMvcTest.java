package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = {
        MenuGroupRestController.class,
        MenuRestController.class,
        ProductRestController.class
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

    protected String parseJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected MockHttpServletRequestBuilder getMethodRequestBase(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8");
    }

    protected MockHttpServletRequestBuilder postMethodRequestBase(String url, String content) {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(content);
    }
}
