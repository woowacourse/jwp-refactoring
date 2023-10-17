package kitchenpos.api.config;

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
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@Tag("api-test")
@WebMvcTest({
        ProductRestController.class,
        MenuRestController.class,
        MenuGroupRestController.class,
        OrderRestController.class,
        TableRestController.class,
        TableGroupRestController.class
})
public class ApiTestConfig {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected TableService tableService;

    @MockBean
    protected TableGroupService tableGroupService;
}
