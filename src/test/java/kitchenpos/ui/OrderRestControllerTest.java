package kitchenpos.ui;

import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.OrderFactory.order;
import static kitchenpos.fixture.OrderRequestFactory.orderRequestFrom;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.fixture.MenuFactory;
import kitchenpos.fixture.ProductFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class OrderRestControllerTest {

    @Autowired
    private OrderRestController orderRestController;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문 등록")
    @Test
    void create() {
        final var coke = productDao.save(ProductFactory.product("콜라", 1000));
        final var soda = menuGroupDao.save(menuGroup("탄산"));
        final var cokeMenu = menuDao.save(MenuFactory.menu("콜라 메뉴", soda, List.of(coke)));
        menuProductDao.save(new MenuProduct(null, cokeMenu.getId(), coke.getId(), 1));
        final var table = orderTableDao.save(notEmptyTable(2));

        final var order = order(table, cokeMenu);
        final var request = orderRequestFrom(order);

        final var response = orderRestController.create(request);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation()).isNotNull()
        );
    }
}
