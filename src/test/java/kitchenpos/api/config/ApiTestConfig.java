package kitchenpos.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.product.application.ProductService;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.menugroup.ui.MenuGroupRestController;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.product.ui.ProductRestController;
import kitchenpos.tablegroup.ui.TableGroupRestController;
import kitchenpos.ordertable.ui.TableRestController;
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
