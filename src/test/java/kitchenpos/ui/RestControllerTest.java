package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menuGroup.applicaiton.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menuGroup.ui.MenuGroupRestController;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.product.application.ProductService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.product.ui.ProductRestController;
import kitchenpos.table.ui.TableGroupRestController;
import kitchenpos.table.ui.TableRestController;
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
