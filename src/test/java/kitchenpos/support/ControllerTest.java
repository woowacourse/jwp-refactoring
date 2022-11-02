package kitchenpos.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.product.ProductService;
import kitchenpos.ui.menu.MenuGroupRestController;
import kitchenpos.ui.menu.MenuRestController;
import kitchenpos.ui.product.ProductRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        ProductRestController.class,
        MenuGroupRestController.class,
        MenuRestController.class
})
@MockBean({
        ProductService.class,
        MenuGroupService.class,
        MenuService.class
})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
