package kitchenpos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.ui.MenuGroupRestController;
import kitchenpos.ui.MenuRestController;
import kitchenpos.ui.OrderRestController;
import kitchenpos.ui.ProductRestController;
import kitchenpos.ui.TableGroupRestController;
import kitchenpos.ui.TableRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest({
        MenuGroupRestController.class,
        MenuRestController.class,
        OrderRestController.class,
        ProductRestController.class,
        TableGroupRestController.class,
        TableRestController.class
})
@MockBean({
        MenuGroupService.class,
        MenuService.class,
        OrderService.class,
        ProductService.class,
        TableGroupService.class,
        TableService.class
})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions postRequestWithJson(final String url, final Object object) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(object)));
    }

    protected ResultActions getRequest(final String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    protected ResultActions putRequestWithJson(final String url, final Object object) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(object)));
    }

    protected ResultActions deleteRequest(final String url) throws Exception {
        return mockMvc.perform(delete(url));
    }
}
