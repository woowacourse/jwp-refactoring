package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class OrderServiceTest {

    private static final Long WRONG_ID = -1L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private OrderTable notEmptyTable;
    private Menu testMenu;

    @BeforeEach
    void setup() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        notEmptyTable = orderTableDao.save(orderTable);

        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트 메뉴 그룹");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setName("테스트 메뉴");
        menu.setMenuGroupId(savedMenuGroup.getId());
        testMenu = menuDao.save(menu);
    }

    @Nested
    @DisplayName("주문 생성테스트")
    class CreateTest {

        @Test
        @DisplayName("주문 생성에 성공한다.")
        void success() {
            // given
            final Order request = getOrder(notEmptyTable.getId(), List.of(getOrderLineItem(testMenu.getId())));

            // when
            final Order response = orderService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getOrderStatus()).isEqualTo("COOKING");
            });
        }

        @Test
        @DisplayName("메뉴가 없이 주문을 하면 예외가 발생한다.")
        void throwExceptionWithEmptyMenuList() {
            // given
            final Order request = getOrder(notEmptyTable.getId(), Collections.emptyList());

            // when
            // then
            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("잘못된 메뉴로 주문을 하면 예외가 발생한다.")
        void throwExcpetionWithWorngMenuId() {
            // given
            final Order request = getOrder(notEmptyTable.getId(), List.of(getOrderLineItem(WRONG_ID)));

            // when
            // then
            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비어있는 테이블에서 주문 생성시 예외가 발생한다.")
        void throwExceptionWithEmptyTable() {
            // given
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            orderTable.setNumberOfGuests(0);
            final OrderTable savedEmptyTable = orderTableDao.save(orderTable);

            final Order request = getOrder(savedEmptyTable.getId(), List.of(getOrderLineItem(testMenu.getId())));

            // when
            // then
            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class ChangeStatusTest {

        @ParameterizedTest(name = "초기상태 : {0}, 변경상태 : {1}")
        @CsvSource(value = {"COOKING,MEAL", "MEAL,COMPLETION", "COOKING,COMPLETION"})
        @DisplayName("성공 테스트")
        void success(final String originalStatus, final String statusToChange) {
            // given
            final Order original = getOrder(notEmptyTable.getId(), List.of(getOrderLineItem(testMenu.getId())));
            original.setOrderStatus(originalStatus);
            original.setOrderedTime(LocalDateTime.now());
            final Order savedOrder = orderDao.save(original);

            final Order changeRequest = new Order();
            changeRequest.setOrderStatus(statusToChange);

            // when
            final Order response = orderService.changeOrderStatus(savedOrder.getId(), changeRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(savedOrder.getId());
                softly.assertThat(response.getOrderStatus()).isEqualTo(statusToChange);
            });
        }

        @ParameterizedTest(name = "초기상태 : {0}, 변경상태 : {1}")
        @CsvSource(value = {"COMPLETION,MEAL", "COMPLETION,COOKING", "COMPLETION,COMPLETION"})
        @DisplayName("실패 테스트")
        void fail(final String originalStatus, final String statusToChange) {
            // given
            final Order original = getOrder(notEmptyTable.getId(), List.of(getOrderLineItem(testMenu.getId())));
            original.setOrderStatus(originalStatus);
            original.setOrderedTime(LocalDateTime.now());
            final Order savedOrder = orderDao.save(original);

            final Order changeRequest = new Order();
            changeRequest.setOrderStatus(statusToChange);

            // when
            // then
            Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("주문들 리스트 조회 테스트")
    void getOrderList() {
        // given
        final Order order = getOrder(notEmptyTable.getId(), List.of(getOrderLineItem(testMenu.getId())));
        order.setOrderStatus("COOKING");
        order.setOrderedTime(LocalDateTime.now());
        final Order savedOrder = orderDao.save(order);

        // when
        final List<Order> response = orderService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotEmpty();
            final Order savedResult = response.get(response.size() - 1);
            softly.assertThat(savedResult.getId()).isEqualTo(savedResult.getId());
        });
    }

    private OrderLineItem getOrderLineItem(final Long menuId) {
        final OrderLineItem orderMenuRequest = new OrderLineItem();
        orderMenuRequest.setMenuId(menuId);
        orderMenuRequest.setQuantity(1L);
        return orderMenuRequest;
    }

    private Order getOrder(final Long tableId, final List<OrderLineItem> orderLineItems) {
        final Order request = new Order();
        request.setOrderTableId(tableId);
        request.setOrderLineItems(orderLineItems);
        return request;
    }
}
