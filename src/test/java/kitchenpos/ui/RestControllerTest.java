package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {MenuGroupRestController.class, MenuRestController.class},
        includeFilters = @Filter(type = FilterType.REGEX, pattern = "kitchenpos.ui.mapper.*"))
public class RestControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    protected MenuGroupService menuGroupService;
    @MockBean
    protected MenuService menuService;
}
