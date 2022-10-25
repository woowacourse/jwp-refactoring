package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        ProductRestController.class,
        MenuGroupRestController.class,
        MenuRestController.class,
        TableGroupRestController.class,
        TableRestController.class,
        OrderRestController.class
})
@Import(MockMvcConfig.class)
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected TableGroupService tableGroupService;

    @MockBean
    protected TableService tableService;

    @MockBean
    protected OrderService orderService;
}
