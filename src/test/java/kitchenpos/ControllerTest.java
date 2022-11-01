package kitchenpos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.application.ProductService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.menu.ui.MenuGroupRestController;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.menu.ui.ProductRestController;
import kitchenpos.table.ui.TableGroupRestController;
import kitchenpos.table.ui.TableRestController;
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
