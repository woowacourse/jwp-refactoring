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
