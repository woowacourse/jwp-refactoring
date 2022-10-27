package kitchenpos.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.ui.MenuGroupRestController;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.menu.ui.ProductRestController;
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
