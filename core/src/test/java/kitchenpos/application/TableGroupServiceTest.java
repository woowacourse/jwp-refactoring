package kitchenpos.application;

import static kitchenpos.application.KitchenposFixture.메뉴그룹만들기;
import static kitchenpos.application.KitchenposFixture.상품만들기;
import static kitchenpos.application.KitchenposFixture.주문테이블만들기;
import static kitchenpos.application.KitchenposFixture.주문할메뉴만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.core.menu.application.dto.MenuProductRequest;
import kitchenpos.core.order.application.dto.OrderLineItemsRequest;
import kitchenpos.core.menu.application.dto.MenuResponse;
import kitchenpos.core.tablegroup.application.TableGroupService;
import kitchenpos.core.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.core.tablegroup.application.TableGroupCustomDao;
import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.order.application.OrderService;
import kitchenpos.core.order.application.validator.OrderStatusValidator;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.ordertable.application.TableService;
import kitchenpos.core.ordertable.domain.OrderTable;
import kitchenpos.core.product.application.ProductService;
import kitchenpos.core.product.domain.Product;
import kitchenpos.core.ordertable.domain.NumberOfGuests;
import kitchenpos.core.product.domain.Price;
import kitchenpos.core.menu.application.MenuService;
import kitchenpos.core.menugroup.application.MenuGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import({TableGroupService.class, TableService.class, ProductService.class, TableGroupCustomDao.class, MenuService.class,
        MenuGroupService.class, OrderService.class, OrderStatusValidator.class})
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    @DisplayName("주문 테이블을 그룹화할 때")
    class GroupOrderTables {
        @Test
        @DisplayName("그룹화할 주문 테이블들의 식별자를 제공하여 그룹화할 수 있다.")
        void given(@Autowired TableService tableService) {
            final OrderTable savedOrderTable = 주문테이블만들기(tableService, true);
            final OrderTable savedOrderTable2 = 주문테이블만들기(tableService, true);

            final TableGroupResponse savedTableGroup = tableGroupService.create(
                    List.of(savedOrderTable.getId(), savedOrderTable2.getId()));

            assertThat(savedTableGroup).isNotNull();
            assertThat(savedTableGroup.getId()).isNotNull();
            assertThat(savedTableGroup.getCreatedDate())
                    .as("식별자와 생성일자는 자동 생성된다")
                    .isNotNull();
        }

        @Test
        @DisplayName("그룹화하려는 주문 테이블의 수가 2개 미만이면 그룹화할 수 없다.")
        void invalidOrderTableSize(@Autowired TableService tableService) {
            final OrderTable savedOrderTable = 주문테이블만들기(tableService, true);

            assertThatThrownBy(() -> tableGroupService.create(List.of(savedOrderTable.getId())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화하려는 주문 테이블 중 실재하지 않는 주문 테이블이 있다면 그룹화할 수 없다.")
        void notExistingOrderTable(@Autowired TableService tableService) {
            final OrderTable orderTable = 주문테이블만들기(tableService, true);

            final OrderTable orderTable2 = new OrderTable(0L, 0L, new NumberOfGuests(0), true);
            // 저장하지 않은 orderTable

            assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화하려는 주문 테이블 중 이미 그룹화 되어있는 주문 테이블이 있다면 그룹화할 수 없다.")
        void alreadyGrouped(@Autowired TableService tableService) {
            // given : 이미 그룹화된 주문 테이블
            final OrderTable savedOrderTable = 주문테이블만들기(tableService, true);
            final OrderTable savedOrderTable2 = 주문테이블만들기(tableService, true);

            tableGroupService.create(List.of(savedOrderTable.getId(), savedOrderTable2.getId()));

            final OrderTable savedOrderTable3 = 주문테이블만들기(tableService, true);

            assertThatThrownBy(
                    () -> tableGroupService.create(List.of(savedOrderTable2.getId(), savedOrderTable3.getId())))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 테이블의 그룹화를 해제할 때")
    class UngroupOrderTables {
        @Test
        @DisplayName("tableGroupId에 해당하는 pathVariable에 단체 식별자를 제공하여 그룹화를 해제할 수 있다.")
        void given(@Autowired TableService tableService) {
            final OrderTable savedOrderTable = 주문테이블만들기(tableService, true);
            final OrderTable savedOrderTable2 = 주문테이블만들기(tableService, true);

            final TableGroupResponse savedTableGroup = tableGroupService.create(
                    List.of(savedOrderTable.getId(), savedOrderTable2.getId()));

            assertThat(savedTableGroup.getOrderTables())
                    .extracting("id")
                    .contains(savedOrderTable.getId(), savedOrderTable2.getId());
        }

        @Test
        @DisplayName("그룹에 속한 주문 테이블 중 주문의 상태가 COOKING 또는 MEAL인 경우 그룹화를 해제할 수 없다.")
        void invalidOrderStatus(
                @Autowired ProductService productService,
                @Autowired MenuService menuService,
                @Autowired MenuGroupService menuGroupService,
                @Autowired TableService tableService,
                @Autowired OrderService orderService
        ) {

            // given : 주문 테이블
            final OrderTable savedOrderTable = 주문테이블만들기(tableService, true);
            final OrderTable savedOrderTable2 = 주문테이블만들기(tableService, true);

            final TableGroupResponse savedTableGroup = tableGroupService.create(
                    List.of(savedOrderTable.getId(), savedOrderTable2.getId()));

            주어진_OrderTable로_주문하기(productService, menuService, menuGroupService, orderService, savedOrderTable);

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private void 주어진_OrderTable로_주문하기(
                final ProductService productService,
                final MenuService menuService,
                final MenuGroupService menuGroupService,
                final OrderService orderService,
                final OrderTable orderTable) {

            // given : 상품
            final Product savedProduct = 상품만들기("상품!", "4000", productService);

            // given : 메뉴 그룹
            final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

            // given : 메뉴
            final MenuResponse savedMenu
                    = menuService.create("메뉴!", new Price(new BigDecimal("4000")), savedMenuGroup.getId(), List.of(new MenuProductRequest(savedProduct.getId(), 4L)));
            final MenuResponse savedMenu2
                    = menuService.create("메뉴 2!", new Price(new BigDecimal("4000")), savedMenuGroup.getId(), List.of(new MenuProductRequest(savedProduct.getId(), 4L)));

            // given : 주문 메뉴
            final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu.getId(), 4);
            final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2.getId(), 3);

            // given : 주문
            orderService.create(
                    orderTable.getId(),
                    List.of(
                            new OrderLineItemsRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()),
                            new OrderLineItemsRequest(orderLineItem2.getMenuId(), orderLineItem2.getQuantity())
                    )
            );
        }
    }
}
