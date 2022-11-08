package kitchenpos.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.product.application.ProductService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.menu.ui.MenuGroupRestController;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.product.ui.ProductRestController;
import kitchenpos.table.ui.TableGroupRestController;
import kitchenpos.table.ui.TableRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        MenuGroupRestController.class,
        ProductRestController.class,
        TableRestController.class,
        TableGroupRestController.class,
        MenuRestController.class,
        OrderRestController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected TableService tableService;

    @MockBean
    protected TableGroupService tableGroupService;

    @MockBean
    protected OrderService orderService;
}
