package kitchenpos.order.domain;

import kitchenpos.config.RepositoryTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("[SUCCESS] 주문 식별자값으로 주문 조회를 실패할 경우 예외가 발생한다.")
    @Test
    void success_findOrderById() {
        // given
        final OrderTable savedOrderTable = persistOrderTable(OrderTable.withoutTableGroup(5, true));
        final Order expected = persistOrder(Order.ofEmptyOrderLineItems(savedOrderTable.getId()));

        em.flush();
        em.close();

        // when
        final Order actual = orderRepository.findOrderByOrderTableId(savedOrderTable.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
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
        final OrderTable savedOrderTable = persistOrderTable(OrderTable.withoutTableGroup(5, true));
        final Order expected = persistOrder(Order.ofEmptyOrderLineItems(savedOrderTable.getId()));

        em.flush();
        em.close();

        // when
        final Order actual = orderRepository.findOrderByOrderTableId(savedOrderTable.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
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

    private Menu createMenu() {
        final Product savedProduct = persistProduct(new Product(new Name("테스트용 상품명"), Price.from("10000")));
        final MenuGroup savedMenuGroup = persistMenuGroup(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu savedMenu = persistMenu(Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, savedMenuGroup));
        savedMenu.addMenuProducts(List.of(
                MenuProduct.withoutMenu(savedProduct, new Quantity(1))
        ));

        return savedMenu;
    }

    @DisplayName("[SUCCESS] 주문 테이블 식별자값 목록으로 주문 목록을 조회한다.")
    @Test
    void success_findInOrderTableIds() {
        // given
        final OrderTable savedOrderTable = persistOrderTable(OrderTable.withoutTableGroup(10, true));
        final Order savedOrder = persistOrder(
                new Order(
                        savedOrderTable.getId(),
                        new OrderLineItems(List.of())
                )
        );

        em.flush();
        em.close();

        // when
        final List<Order> actual = orderRepository.findInOrderTableIds(List.of(savedOrderTable.getId()));

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final Order actualOrder = actual.get(0);

            softly.assertThat(actualOrder).isEqualTo(savedOrder);
        });
    }
}
