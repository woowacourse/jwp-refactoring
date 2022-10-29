package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.ui.apiservice.MenuApiService;
import kitchenpos.ui.apiservice.MenuGroupApiService;
import kitchenpos.ui.apiservice.OrderApiService;
import kitchenpos.ui.apiservice.ProductApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        ProductRestController.class,
        MenuGroupRestController.class,
        MenuRestController.class,
        OrderRestController.class
    }
)
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ProductApiService productApiService;

    @MockBean
    protected MenuGroupApiService menuGroupApiService;

    @MockBean
    protected MenuApiService menuApiService;

    @MockBean
    protected OrderApiService orderApiService;
}
