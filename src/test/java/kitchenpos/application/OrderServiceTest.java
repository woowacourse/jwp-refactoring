package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목_생성;
import static kitchenpos.fixture.OrderTableFixtures.빈_테이블1;
import static kitchenpos.fixture.OrderTableFixtures.주문_테이블9;
import static kitchenpos.fixture.OrderTableFixtures.테이블_생성;
import static kitchenpos.fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class OrderServiceTest {

    @Autowired
    private OrderService sut;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Nested
    @DisplayName("주문 등록")
    class CreateTest {

        @DisplayName("정상적인 경우 주문을 등록할 수 있다.")
        @Test
        void createOrder() {
            final OrderTable orderTable = orderTableRepository.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct)));
            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final Order actual = sut.create(order);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId()),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @DisplayName("주문 항목이 비어있으면 등록할 수 없다.")
        @Test
        void createOrderWithNullOrderLineItem() {
            final Order order = 주문_생성(주문_테이블9.getId(), List.of());

            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목에 쓰여진 메뉴가 존재하지 않으면 등록할 수 없다.")
        @Test
        void createOrderWithNotExistMenu() {
            final Long 존재하지_않는_메뉴_ID = -1L;
            final OrderLineItem orderLineItem = 주문_항목_생성(존재하지_않는_메뉴_ID, 1);

            final Order order = 주문_생성(주문_테이블9.getId(), List.of(orderLineItem));

            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("해당 주문이 속한 주문 테이블이 존재하지 않으면 등록할 수 없다.")
        @Test
        void createOrderWithNotExistOrderTable() {
            final Long 존재하지_않는_주문_테이블_ID = -1L;
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct)));
            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);

            final Order order = 주문_생성(존재하지_않는_주문_테이블_ID, List.of(orderLineItem));

            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("해당 주문이 속한 주문 테이블이 빈 테이블이면 등록할 수 없다.")
        @Test
        void createOrderWithEmptyTable() {
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct)));
            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);

            final Order order = 주문_생성(빈_테이블1.getId(), List.of(orderLineItem));

            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void getOrders() {
        final MenuProduct menuProduct1 = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
        final Menu menu1 = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct1)));
        final OrderLineItem orderLineItem1 = 주문_항목_생성(menu1.getId(), 1);

        final MenuProduct menuProduct2 = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
        final Menu menu2 = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct2)));
        final OrderLineItem orderLineItem2 = 주문_항목_생성(menu2.getId(), 1);

        final OrderTable orderTable = orderTableRepository.save(테이블_생성(5, false));
        final Order order = 주문_생성(orderTable.getId(), List.of(orderLineItem1, orderLineItem2));

        orderDao.save(order);

        assertThat(sut.list()).hasSize(1);
    }

    @Nested
    @DisplayName("주문 상태 변경")
    class OrderStatusChangeTest {

        @DisplayName("정상적인 경우 주문 상태를 변경할 수 있다.")
        @Test
        void changeOrderStatus() {
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct)));
            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);

            final OrderTable orderTable = orderTableRepository.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));
            final Order order = orderDao.save(주문_생성(orderTable.getId(), List.of(orderLineItem)));
            final Order changedOrder = 주문_생성(order.getId(), OrderStatus.MEAL.name(), List.of(orderLineItem));

            final Order actual = sut.changeOrderStatus(order.getId(), changedOrder);

            assertThat(actual.getOrderStatus()).isEqualTo(changedOrder.getOrderStatus());
        }

        @DisplayName("주문이 존재하지 않으면 변경할 수 없다.")
        @Test
        void changeOrderStatusWithNotExistOrder() {
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct)));
            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);

            final OrderTable orderTable = orderTableRepository.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));
            final Long 존재하지_않는_주문_ID = -1L;
            final Order changedOrder = 주문_생성(orderTable.getId(), OrderStatus.MEAL.name(), List.of(orderLineItem));

            assertThatThrownBy(() -> sut.changeOrderStatus(존재하지_않는_주문_ID, changedOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문이 계산 완료 상태면 변경할 수 없다.")
        @Test
        void changeOrderStatusWithCompletionOrder() {
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct)));
            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);

            final OrderTable orderTable = orderTableRepository.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));
            final Order order = orderDao.save(주문_생성(orderTable.getId(), OrderStatus.COMPLETION.name(), List.of(orderLineItem)));
            final Order changedOrder = 주문_생성(order.getId(), OrderStatus.MEAL.name(), List.of(orderLineItem));

            assertThatThrownBy(() -> sut.changeOrderStatus(order.getId(), changedOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
