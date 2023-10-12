package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class ControllerTest {

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected TableGroupService tableGroupService;

    @MockBean
    protected TableService tableService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MenuGroup 메뉴_그룹(String name) {
        return 메뉴_그룹(null, name);
    }

    protected MenuGroup 메뉴_그룹(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    protected Menu 메뉴() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(menuProduct));

        return menu;
    }
}
