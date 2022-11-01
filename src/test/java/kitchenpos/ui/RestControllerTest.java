package kitchenpos.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;

@WebMvcTest(controllers = {
    ProductRestController.class,
    MenuRestController.class,
    MenuGroupRestController.class,
    TableRestController.class,
    TableGroupRestController.class,
    OrderRestController.class
})
public class RestControllerTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    @MockBean protected ProductService productService;
    @MockBean protected MenuService menuService;
    @MockBean protected MenuGroupService menuGroupService;
    @MockBean protected TableService tableService;
    @MockBean protected TableGroupService tableGroupService;
    @MockBean protected OrderService orderService;
}
