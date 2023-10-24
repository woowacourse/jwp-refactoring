package kitchenpos.application;

import static kitchenpos.application.KitchenposFixture.메뉴그룹만들기;
import static kitchenpos.application.KitchenposFixture.상품만들기;
import static kitchenpos.application.KitchenposFixture.주문테이블만들기;
import static kitchenpos.application.KitchenposFixture.주문할메뉴만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.dao.MenuCustomDao;
import kitchenpos.dao.OrderCustomDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.ordertable.NumberOfGuests;
import kitchenpos.domain.product.Price;
import kitchenpos.application.request.OrderLineItemsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import({TableService.class, OrderCustomDao.class, ProductService.class, MenuService.class, MenuCustomDao.class, OrderService.class, MenuGroupService.class})
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Nested
    @DisplayName("주문 테이블의 비어있음 상태를 변경하는 경우")
    class EmptyTable {

        @Test
        @DisplayName("orderTableId에 해당하는 pathVariable에 주문 테이블 식별자를 제공하여 테이블의 비어있음 여부를 변경할 수 있다.")
        void givenOrderTableId() {
            final Long savedTableId = tableService.create(new NumberOfGuests(6), true);

            final OrderTableResponse changedTable = tableService.changeEmpty(savedTableId, false);
            assertThat(changedTable).extracting("empty").isEqualTo(false);
        }

        @Test
        @DisplayName("주문 상태가 COOKING 또는 MEAL인 주문 테이블의 상태를 변경할 수 없다.")
        void invalidTableStatus(
                @Autowired ProductService productService,
                @Autowired MenuService menuService,
                @Autowired OrderService orderService,
                @Autowired MenuGroupService menuGroupService,
                @Autowired TableService tableService
        ) {
            // given : 상품
            final Product savedProduct = 상품만들기("상품!", "4000", productService);

            // given : 메뉴 그룹
            final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

            // given : 메뉴
            final MenuResponse savedMenu
                    = menuService.create("메뉴!", new Price(new BigDecimal("4000")), savedMenuGroup.getId(),
                    List.of(new MenuProductRequest(savedProduct.getId(), 4L)));
            final MenuResponse savedMenu2
                    = menuService.create("메뉴 2!", new Price(new BigDecimal("9000")), savedMenuGroup.getId(),
                    List.of(new MenuProductRequest(savedProduct.getId(), 4L)));

            final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu.getId(), 4);
            final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2.getId(), 3);

            // given : 주문 테이블
            final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

            // given : 주문
            final OrderResponse savedOrder = orderService.create(
                    savedOrderTable.getId(),
                    List.of(
                            new OrderLineItemsRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()),
                            new OrderLineItemsRequest(orderLineItem2.getMenuId(), orderLineItem2.getQuantity())
                    )
            );

            orderService.changeOrderStatus(savedOrder.getId());

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), false))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("손님 수를 변경하는 경우")
    class changeGuestNumber {

        @Test
        @DisplayName("orderTableId에 해당하는 pathVariable에 주문 테이블 식별자를, requestBody에 변경하려는 손님 수를 제공하여 변경할 수 있다.")
        void given(@Autowired OrderTableDao orderTableDao) {
            final Long savedTableId = tableService.create(new NumberOfGuests(6), false);

            tableService.changeNumberOfGuests(savedTableId, new NumberOfGuests(9));

            final Optional<OrderTable> changedOrderTable = orderTableDao.findById(savedTableId);
            assertThat(changedOrderTable.get().getNumberOfGuests()).isEqualTo(new NumberOfGuests(9));
        }

        @Test
        @DisplayName("실재하지 않는 주문 테이블의 손님 수를 변경할 수 없다.")
        void notExistingTable() {
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(0L, new NumberOfGuests(9)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
