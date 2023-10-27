package kitchenpos.order.domain;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderValidatorTest extends ApplicationTestConfig {

    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(menuRepository, orderTableRepository);
    }


    @DisplayName("[SUCCESS] 주문 시작 준비가 가능한지 검증한다.")
    @Test
    void success_validatePrepare() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu savedMenu = menuRepository.save(
                Menu.withEmptyMenuProducts(
                        new Name("테스트용 메뉴명"),
                        Price.ZERO,
                        savedMenuGroup
                )
        );

        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 10, true));
        final Order newOrder = new Order(
                savedOrderTable.getId(),
                new OrderLineItems(List.of(
                        new OrderLineItem(savedMenu.getId(), new Quantity(10))
                )));

        // expect
        assertThatCode(() -> orderValidator.validatePrepare(newOrder))
                .doesNotThrowAnyException();
    }

    @DisplayName("[EXCEPTION] 주문 테이블 식별자값이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void throwException_validateOrderTable() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu savedMenu = menuRepository.save(
                Menu.withEmptyMenuProducts(
                        new Name("테스트용 메뉴명"),
                        Price.ZERO,
                        savedMenuGroup
                )
        );

        final Order newOrder = new Order(
                -1L,
                new OrderLineItems(List.of(
                        new OrderLineItem(savedMenu.getId(), new Quantity(10))
                )));

        // expect
        assertThatThrownBy(() -> orderValidator.validatePrepare(newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[EXCEPTION] 주문 항목 목록 수량이 실제 메뉴의 수량과 다를 경우 예외가 발생한다.")
    @Test
    void throwException_validateMenuSize() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 10, true));
        final Order newOrder = new Order(
                savedOrderTable.getId(),
                new OrderLineItems(List.of(
                        new OrderLineItem(-1L, new Quantity(10))
                )));

        // expect
        assertThatThrownBy(() -> orderValidator.validatePrepare(newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[EXCEPTION] 주문 항목이 비어있을 경우 예외가 발생한다.")
    @Test
    void throwException_validateOrderLineItemsEmpty() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu savedMenu = menuRepository.save(
                Menu.withEmptyMenuProducts(
                        new Name("테스트용 메뉴명"),
                        Price.ZERO,
                        savedMenuGroup
                )
        );

        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 10, true));
        final Order newOrder = new Order(
                savedOrderTable.getId(),
                new OrderLineItems(Collections.emptyList())
        );

        // expect
        assertThatThrownBy(() -> orderValidator.validatePrepare(newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
