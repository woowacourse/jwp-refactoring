package kitchenpos.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;

@WebMvcTest({
    MenuGroupRestController.class,
    MenuRestController.class,
    OrderRestController.class,
    ProductRestController.class,
    TableRestController.class,
    TableGroupRestController.class
})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected TableService tableService;

    @MockBean
    protected TableGroupService tableGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    protected String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
