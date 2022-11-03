package kitchenpos.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.presentation.MenuGroupRestController;
import kitchenpos.menu.presentation.MenuRestController;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.presentation.OrderRestController;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.presentation.ProductRestController;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.presentation.TableGroupRestController;
import kitchenpos.table.presentation.TableRestController;
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
