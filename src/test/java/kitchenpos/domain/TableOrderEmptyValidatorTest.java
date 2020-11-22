package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;

class TableOrderEmptyValidatorTest extends ServiceTest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableOrderEmptyValidator tableOrderEmptyValidator;

    @DisplayName("테이블에 완료되지 않은 주문이 있는 경우 예외 처리한다.")
    @Test
    void validate() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("test_group"));
        Product savedProduct1 = productRepository.save(new Product("후라이드 치킨", BigDecimal.valueOf(10_000L)));
        Product savedProduct2 = productRepository.save(new Product("양념 치킨", BigDecimal.valueOf(20_000L)));
        Product savedProduct3 = productRepository.save(new Product("시원한 아이스 아메리카노", BigDecimal.valueOf(5_000L)));

        OrderTable orderTable = tableRepository.save(new OrderTable(2, false));
        Menu savedMenu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), savedMenuGroup.getId(),
            Arrays.asList(new MenuProduct(savedProduct1.getId(), 2L), new MenuProduct(savedProduct2.getId(), 1L),
                new MenuProduct(savedProduct3.getId(), 1L))));

        orderRepository.save(
            Order.place(orderTable.getId(), Collections.singletonList(new OrderLineItem(savedMenu.getId(), 2L))));

        assertThatThrownBy(() -> tableOrderEmptyValidator.validate(orderTable.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블들에 완료되지 않은 주문이 있는 경우 예외 처리한다.")
    @Test
    void validateTables() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("test_group"));
        Product savedProduct1 = productRepository.save(new Product("후라이드 치킨", BigDecimal.valueOf(10_000L)));
        Product savedProduct2 = productRepository.save(new Product("양념 치킨", BigDecimal.valueOf(20_000L)));
        Product savedProduct3 = productRepository.save(new Product("시원한 아이스 아메리카노", BigDecimal.valueOf(5_000L)));

        OrderTable orderTable = tableRepository.save(new OrderTable(2, false));
        Menu savedMenu = menuRepository.save(new Menu("test", BigDecimal.valueOf(2_000L), savedMenuGroup.getId(),
            Arrays.asList(new MenuProduct(savedProduct1.getId(), 2L), new MenuProduct(savedProduct2.getId(), 1L),
                new MenuProduct(savedProduct3.getId(), 1L))));

        orderRepository.save(
            Order.place(orderTable.getId(), Collections.singletonList(new OrderLineItem(savedMenu.getId(), 2L))));

        assertThatThrownBy(() -> tableOrderEmptyValidator.validate(Collections.singletonList(orderTable.getId())))
            .isInstanceOf(IllegalArgumentException.class);
    }
}