package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.OrderTableFixture.비지않은_테이블;
import static kitchenpos.fixture.OrderTableFixture.빈테이블;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderService orderService;

    @Nested
    class 주문하기 {

        @Test
        void 주문을_할_수_있다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블 = orderTableDao.save(비지않은_테이블());
            OrderLineItem 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            Order order = 주문(테이블.getId(), List.of(주문상품));

            // when
            Order created = orderService.create(order);

            // then
            Order find = orderDao.findById(created.getId()).get();
            assertThat(find.getOrderTableId()).isEqualTo(테이블.getId());
            assertThat(find.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @Test
        void 주문상품이_없으면_주문할_수_없다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블 = orderTableDao.save(비지않은_테이블());

            Order order = 주문(테이블.getId(), List.of());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문상품속_메뉴가_중복되면_주문할_수_없다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블 = orderTableDao.save(비지않은_테이블());
            OrderLineItem 주문상품1 = 주문상품(후라이드메뉴.getId(), 3);
            OrderLineItem 주문상품2 = 주문상품(후라이드메뉴.getId(), 1);

            Order order = 주문(테이블.getId(), List.of(주문상품1, 주문상품2));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_주문할_수_없다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            Long wrongTableId = 999L;
            OrderLineItem 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            Order order = 주문(wrongTableId, List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있으면_주문할_수_없다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 빈테이블 = orderTableDao.save(빈테이블());
            OrderLineItem 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            Order order = 주문(빈테이블.getId(), List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경할_수_있다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블 = orderTableDao.save(비지않은_테이블());
            OrderLineItem 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            Order order = orderDao.save(주문(테이블.getId(), OrderStatus.COOKING.name()));
            orderLineItemDao.save(주문상품(order.getId(), 후라이드메뉴.getId(), 1));

            Order changeRequest = 주문(OrderStatus.MEAL.name());

            // when
            Order updated = orderService.changeOrderStatus(order.getId(), changeRequest);

            // then
            assertThat(updated.getOrderStatus()).isEqualTo(changeRequest.getOrderStatus());
        }

        @Test
        void 주문이_존재하지_않을_경우_변경할_수_없다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            Long wrongOrderId = 999L;
            Order changeRequest = 주문(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당_주문이_이미_완료_상태일_경우_변경할_수_없다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블 = orderTableDao.save(비지않은_테이블());
            OrderLineItem 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            Order order = orderDao.save(주문(테이블.getId(), OrderStatus.COMPLETION.name()));
            orderLineItemDao.save(주문상품(order.getId(), 후라이드메뉴.getId(), 1));

            Order changeRequest = 주문(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_목록_조회 {

        @Test
        void 주문_목록을_조회할_수_있다() {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블 = orderTableDao.save(비지않은_테이블());
            OrderLineItem 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            Order order1 = orderDao.save(주문(테이블.getId(), OrderStatus.COOKING.name()));
            OrderLineItem 주문상품1 = orderLineItemDao.save(주문상품(order1.getId(), 후라이드메뉴.getId(), 1));
            order1.setOrderLineItems(List.of(주문상품1));

            Order order2 = orderDao.save(주문(테이블.getId(), OrderStatus.COOKING.name()));
            OrderLineItem 주문상품2 = orderLineItemDao.save(주문상품(order2.getId(), 후라이드메뉴.getId(), 3));
            order2.setOrderLineItems(List.of(주문상품2));

            // when
            List<Order> findList = orderService.list();

            // then
            assertThat(findList).usingRecursiveComparison()
                    .isEqualTo(List.of(order1, order2));
        }
    }
}
