package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목_생성;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
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
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Nested
    @DisplayName("주문 등록")
    class CreateTest {

        @DisplayName("정상적인 경우 주문을 등록할 수 있다.")
        @Test
        void createOrder() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

            final Order order = new Order();
            order.setOrderTableId(주문_테이블.getId());
            order.setOrderLineItems(List.of(주문_항목));

            final Order actual = sut.create(order);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(주문_테이블.getId()),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @DisplayName("주문 항목이 비어있으면 등록할 수 없다.")
        @Test
        void createOrderWithNullOrderLineItem() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

            final Order 주문 = 주문_생성(주문_테이블.getId(), List.of());

            assertThatThrownBy(() -> sut.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목에 쓰여진 메뉴가 존재하지 않으면 등록할 수 없다.")
        @Test
        void createOrderWithNotExistMenu() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Long 존재하지_않는_메뉴_ID = -1L;
            final OrderLineItem 주문_항목 = 주문_항목_생성(존재하지_않는_메뉴_ID, 1);

            final Order 주문 = 주문_생성(주문_테이블.getId(), List.of(주문_항목));

            assertThatThrownBy(() -> sut.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("해당 주문이 속한 주문 테이블이 존재하지 않으면 등록할 수 없다.")
        @Test
        void createOrderWithNotExistOrderTable() {
            final Long 존재하지_않는_주문_테이블_ID = -1L;
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

            final Order 주문 = 주문_생성(존재하지_않는_주문_테이블_ID, List.of(주문_항목));

            assertThatThrownBy(() -> sut.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("해당 주문이 속한 주문 테이블이 빈 테이블이면 등록할 수 없다.")
        @Test
        void createOrderWithEmptyTable() {
            final OrderTable 빈_테이블 = orderTableDao.save(빈_테이블_생성(5));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

            final Order 주문 = 주문_생성(빈_테이블.getId(), List.of(주문_항목));

            assertThatThrownBy(() -> sut.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void getOrders() {
        final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));
        final Product 짱구 = productDao.save(상품_생성("짱구", 100));
        final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
        final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
        final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
        final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

        final Order 주문 = 주문_생성(주문_테이블.getId(), List.of(주문_항목));

        orderDao.save(주문);

        assertThat(sut.list()).hasSize(1);
    }

    @Nested
    @DisplayName("주문 상태 변경")
    class OrderStatusChangeTest {

        @DisplayName("정상적인 경우 주문 상태를 변경할 수 있다.")
        @Test
        void changeOrderStatus() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

            final Order 주문 = orderDao.save(주문_생성(주문_테이블.getId(), List.of(주문_항목)));

            final Order 변경된_주문 = 주문_생성(주문_테이블.getId(), OrderStatus.MEAL.name(), List.of(주문_항목));

            final Order actual = sut.changeOrderStatus(주문.getId(), 변경된_주문);

            assertThat(actual.getOrderStatus()).isEqualTo(변경된_주문.getOrderStatus());
        }

        @DisplayName("주문이 존재하지 않으면 변경할 수 없다.")
        @Test
        void changeOrderStatusWithNotExistOrder() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

            final Long 존재하지_않는_주문_ID = -1L;

            final Order 변경된_주문 = 주문_생성(주문_테이블.getId(), OrderStatus.MEAL.name(), List.of(주문_항목));

            assertThatThrownBy(() -> sut.changeOrderStatus(존재하지_않는_주문_ID, 변경된_주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문이 계산 완료 상태면 변경할 수 없다.")
        @Test
        void changeOrderStatusWithCompletionOrder() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));
            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);

            final Order 주문 = orderDao.save(주문_생성(주문_테이블.getId(), OrderStatus.COMPLETION.name(), List.of(주문_항목)));
            final Order 변경된_주문 = 주문_생성(주문_테이블.getId(), OrderStatus.MEAL.name(), List.of(주문_항목));

            assertThatThrownBy(() -> sut.changeOrderStatus(주문.getId(), 변경된_주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
