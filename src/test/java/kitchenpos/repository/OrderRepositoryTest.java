package kitchenpos.repository;

import kitchenpos.config.RepositoryTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("[SUCCESS] 주문 식별자값으로 주문 조회를 실패할 경우 예외가 발생한다.")
    @Test
    void success_findOrderById() {
        // given
        final OrderTable savedOrderTable = persistOrderTable(new OrderTable(null, 5, true));
        final Order expected = persistOrder(Order.ofEmptyOrderLineItems(savedOrderTable));

        em.flush();
        em.close();

        // when
        final Order actual = orderRepository.findOrderByOrderTableId(savedOrderTable.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getOrderTable()).isEqualTo(expected.getOrderTable());
            softly.assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
            softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
            softly.assertThat(actual.getOrderLineItems()).isEqualTo(expected.getOrderLineItems());
        });
    }

    @DisplayName("[EXCEPTION] 주문 식별자값으로 주문 조회를 실패할 경우 예외가 발생한다.")
    @Test
    void throwException_findOrderById_when_notFount() {
        assertThatThrownBy(() -> orderRepository.findOrderById(0L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("[SUCCESS] 주문 테이블 식별자값으로 주문을 조회한다.")
    @Test
    void success_findOrderByOrderTableId() {
        // given
        final OrderTable savedOrderTable = persistOrderTable(new OrderTable(null, 5, true));
        final Order expected = persistOrder(Order.ofEmptyOrderLineItems(savedOrderTable));

        em.flush();
        em.close();

        // when
        final Order actual = orderRepository.findOrderByOrderTableId(savedOrderTable.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getOrderTable()).isEqualTo(expected.getOrderTable());
            softly.assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
            softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
            softly.assertThat(actual.getOrderLineItems()).isEqualTo(expected.getOrderLineItems());
        });
    }

    @DisplayName("[EXCEPTION] 주문 테이블 식별자값으로 주문 조회를 실패할 경우 예외가 발생한다.")
    @Test
    void throwException_findOrderByOrderTableId_when_notFount() {
        assertThatThrownBy(() -> orderRepository.findOrderByOrderTableId(0L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("[SUCCESS] 주문 테이블 식별자값과 주문 상태 목록 조건에 해당하는 주문이 존재하는지 확인한다.")
    @Test
    void success_existsByOrderTableIdAndOrderStatusIn() {
        // given
        final Product savedProduct = persistProduct(new Product(new Name("테스트용 상품명"), new Price("10000")));
        final MenuGroup savedMenuGroup = persistMenuGroup(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu savedMenu = persistMenu(Menu.ofEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, savedMenuGroup));
        persistMenuProduct(new MenuProduct(savedMenu, savedProduct, new Quantity(1)));
        final OrderTable savedOrderTable = persistOrderTable(new OrderTable(null, 10, true));
        final Order savedOrder = persistOrder(Order.ofEmptyOrderLineItems(savedOrderTable));
        final OrderLineItem orderLineItem = OrderLineItem.ofWithoutOrder(savedMenu, new Quantity(1));
        orderLineItem.assignOrder(savedOrder);
        persistOrderLineItem(orderLineItem);

        em.flush();
        em.close();

        // when
        final boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId(), List.of(OrderStatus.COOKING));

        // then
        assertThat(actual).isTrue();
    }
}
