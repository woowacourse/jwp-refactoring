package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;

class OrderCreateServiceTest extends ServiceTest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderCreateService orderCreateService;

    @DisplayName("유효한 주문을 생성한다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("test"));
        Product product = productRepository.save(new Product("test", BigDecimal.valueOf(1_000L)));
        OrderTable orderTable = tableRepository.save(new OrderTable(2, false));
        Menu menu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), menuGroup.getId(),
            Collections.singletonList(new MenuProduct(product.getId(), 2L))));

        assertThat(orderCreateService.create(orderTable.getId(),
            Collections.singletonList(new OrderLineItemCreateInfo(menu.getId(), 2L)))).isNotNull();
    }

    @DisplayName("등록되지 않은 메뉴를 주문할 경우 예외 처리한다.")
    @Test
    void createWithNotExistMenu() {
        OrderTable orderTable = tableRepository.save(new OrderTable(2, false));

        assertThatThrownBy(() -> orderCreateService.create(orderTable.getId(),
            Collections.singletonList(new OrderLineItemCreateInfo(1L, 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 테이블에 주문할 경우 예외 처리한다.")
    @Test
    void createWithNotExistTable() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("test"));
        Product product = productRepository.save(new Product("test", BigDecimal.valueOf(1_000L)));
        Menu menu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), menuGroup.getId(),
            Collections.singletonList(new MenuProduct(product.getId(), 2L))));

        assertThatThrownBy(() -> orderCreateService.create(1L,
            Collections.singletonList(new OrderLineItemCreateInfo(menu.getId(), 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 테이블에 주문이 발생할 경우 예외 처리한다.")
    @Test
    void createWithEmptyTable() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("test"));
        Product product = productRepository.save(new Product("test", BigDecimal.valueOf(1_000L)));
        Menu menu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), menuGroup.getId(),
            Collections.singletonList(new MenuProduct(product.getId(), 2L))));
        OrderTable orderTable = tableRepository.save(new OrderTable(2, true));

        assertThatThrownBy(() -> orderCreateService.create(orderTable.getId(),
            Collections.singletonList(new OrderLineItemCreateInfo(menu.getId(), 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}