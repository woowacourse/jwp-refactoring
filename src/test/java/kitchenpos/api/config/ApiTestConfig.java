package kitchenpos.api.config;

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
import org.springframework.test.web.servlet.MockMvc;

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

    @MockBean
    protected ProductService productService;

    @MockBean
    private MenuService menuService;

    @MockBean
    private MenuGroupService menuGroupService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private TableService tableService;

    @MockBean
    private TableGroupService tableGroupService;
}
