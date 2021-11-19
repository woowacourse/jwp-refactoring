package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import org.junit.jupiter.api.BeforeEach;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menugroup.ui.MenuGroupRestController;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.ui.ProductRestController;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.ui.TableGroupRestController;
import kitchenpos.table.ui.TableRestController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest({
        MenuGroupRestController.class,
        ProductRestController.class,
        MenuRestController.class,
        TableRestController.class,
        OrderRestController.class,
        TableGroupRestController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected TableService tableService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                      .addFilter(new CharacterEncodingFilter("UTF-8", true))
                                      .alwaysDo(print())
                                      .build();
    }

    protected String objectToJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
