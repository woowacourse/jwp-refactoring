package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static kitchenpos.fixture.FixtureFactory.메뉴_그룹_생성;
import static kitchenpos.fixture.FixtureFactory.메뉴_생성;
import static kitchenpos.fixture.FixtureFactory.주문_생성;
import static kitchenpos.fixture.FixtureFactory.주문_테이블_생성;
import static kitchenpos.fixture.FixtureFactory.주문_항목_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_저장할_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴 그룹"));
        final Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 5, false));
        final OrderLineItem orderLineItem = 주문_항목_생성(null, menu.getId(), 10);

        final Order expected = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));

        // when
        final Order actual = orderService.create(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isNotNull(),
                () -> assertThat(orderLineItem.getOrderId()).isEqualTo(actual.getId())
        );
    }

    @Nested
    class 주문_생성_실패 {

        @Test
        void 주문_항목이_비어있다면_예외가_발생한다() {
            // given
            final OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
            final Order expected = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of());

            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_개수와_메뉴_개수가_일치하지_않으면_에외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴 그룹"));
            final Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
            final OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
            final OrderLineItem firstOrderLineItem = 주문_항목_생성(null, menu.getId(), 2);
            final OrderLineItem secondOrderLineItem = 주문_항목_생성(null, menu.getId(), 2);

            final Order expected = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(firstOrderLineItem, secondOrderLineItem));

            // expected
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있을_경우_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴 그룹"));
            final Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
            final OrderLineItem firstOrderLineItem = 주문_항목_생성(null, menu.getId(), 2);
            final OrderLineItem secondOrderLineItem = 주문_항목_생성(null, menu.getId(), 2);

            final Order expected = 주문_생성(null, null, LocalDateTime.now(), List.of(firstOrderLineItem, secondOrderLineItem));

            // expected
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴 그룹"));
        final Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 5, false));
        final OrderLineItem orderLineItem = 주문_항목_생성(null, menu.getId(), 10);

        final Order expected = orderDao.save(주문_생성(orderTable.getId(), String.valueOf(OrderStatus.COOKING), LocalDateTime.now(), List.of(orderLineItem)));

        // when
        final Order actual = orderService.changeOrderStatus(expected.getId(), expected);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("COOKING");
    }

    @Test
    void 주문_상태가_완료라면_상태_변경시_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴 그룹"));
        final Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 5, false));
        final OrderLineItem orderLineItem = 주문_항목_생성(null, menu.getId(), 10);

        final Order expected = orderDao.save(주문_생성(orderTable.getId(), String.valueOf(OrderStatus.COMPLETION), LocalDateTime.now(), List.of(orderLineItem)));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(expected.getId(), expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_가져올_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴 그룹"));
        final Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 5, false));
        final OrderLineItem orderLineItem = 주문_항목_생성(null, menu.getId(), 10);

        orderDao.save(주문_생성(orderTable.getId(), String.valueOf(OrderStatus.COMPLETION), LocalDateTime.now(), List.of(orderLineItem)));

        // when
        final List<Order> expected = orderService.list();

        // then
        assertThat(expected).hasSize(1);
    }

}
