package kitchenpos.ui.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.product.ProductService;
import kitchenpos.application.table.TableGroupService;
import kitchenpos.application.table.TableService;
import kitchenpos.ui.menu.MenuGroupRestController;
import kitchenpos.ui.menu.MenuRestController;
import kitchenpos.ui.order.OrderRestController;
import kitchenpos.ui.product.ProductRestController;
import kitchenpos.ui.table.TableGroupRestController;
import kitchenpos.ui.table.TableRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {MenuGroupRestController.class, MenuRestController.class, ProductRestController.class,
        TableRestController.class, TableGroupRestController.class, OrderRestController.class})
public class RestControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
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
