package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menugroup.application.dto.MenuGroupRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.support.ServiceTest;
import kitchenpos.tablegroup.application.dto.OrderTableResponse;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {


    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;


    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;

    @BeforeEach
    void init() {
        firstOrderTable = orderTableRepository.save(new OrderTable(0, true));
        secondOrderTable = orderTableRepository.save(new OrderTable(0, true));
    }

    @Nested
    class 단체_테이블을_등록할_때 {

        @Test
        void success() {
            // given
            TableGroupRequest tableGroupRequest = TableGroupRequest.from(
                    List.of(firstOrderTable.getId(), secondOrderTable.getId())
            );

            // when
            TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

            // then
            List<OrderTableResponse> updatedOrderTables = actual.getOrderTables();
            List<Long> updatedOrderTableIds = updatedOrderTables.stream()
                    .map(OrderTableResponse::getId)
                    .collect(Collectors.toList());

            assertThat(updatedOrderTables).hasSize(2);
            assertThat(updatedOrderTableIds).containsExactly(firstOrderTable.getId(), secondOrderTable.getId());
        }

        @Test
        void 테이블이_비어있으면_실패() {
            // given
            TableGroupRequest tableGroupRequest = TableGroupRequest.from(Collections.emptyList());

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_2개_미만이면_실패() {
            // given
            TableGroupRequest tableGroupRequest = TableGroupRequest.from(List.of(firstOrderTable.getId()));

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_주문테이블이_존재할_경우_실패() {
            // given
            Long invalidOrderTableId = 0L;
            TableGroupRequest tableGroupRequest = TableGroupRequest.from(List.of(invalidOrderTableId));

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_이미_주문을_받았거나_다른_단체_테이블에_등록된_경우_실패() {
            // given
            TableGroupRequest tableGroupRequest = TableGroupRequest.from(
                    List.of(firstOrderTable.getId(), secondOrderTable.getId())
            );
            TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 단체_테이블을_해제할_때 {

        @Test
        void success() {
            // given
            TableGroupRequest tableGroupRequest = TableGroupRequest.from(
                    List.of(firstOrderTable.getId(), secondOrderTable.getId())
            );
            TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

            BigDecimal price = BigDecimal.valueOf(1000);
            ProductResponse productResponse = productService.create(new ProductRequest("치킨", price));
            MenuGroupResponse menuGroupResponse = menuGroupService.create(new MenuGroupRequest("Leo's Pick"));
            MenuResponse menuResponse = menuService.create(new MenuRequest(
                    "후라이드",
                    price,
                    menuGroupResponse.getId(),
                    List.of(new MenuProductRequest(productResponse.getId(), 1L))
            ));

            OrderResponse firstOrder = orderService.create(new OrderRequest(firstOrderTable.getId(), List.of(
                    new OrderLineItemRequest(menuResponse.getId(), 1L)
            )));
            OrderResponse secondOrder = orderService.create(new OrderRequest(secondOrderTable.getId(), List.of(
                    new OrderLineItemRequest(menuResponse.getId(), 1L)
            )));

            OrderStatusRequest meal = new OrderStatusRequest(OrderStatus.MEAL);

            orderService.changeOrderStatus(firstOrder.getId(), meal);
            orderService.changeOrderStatus(secondOrder.getId(), meal);

            OrderStatusRequest completion = new OrderStatusRequest(OrderStatus.COMPLETION);

            orderService.changeOrderStatus(firstOrder.getId(), completion);
            orderService.changeOrderStatus(secondOrder.getId(), completion);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            OrderTable ungroupedOrderTable = orderTableRepository.findAll().stream()
                    .filter(orderTable -> orderTable.getId().equals(firstOrderTable.getId()))
                    .findFirst()
                    .get();

            assertThat(ungroupedOrderTable.getTableGroupId()).isNull();
        }


        @Test
        void 주문_상태가_조리_중이거나_식사_중인_경우_실패() {
            //given
            TableGroupRequest tableGroupRequest = TableGroupRequest.from(
                    List.of(firstOrderTable.getId(), secondOrderTable.getId())
            );
            TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

            BigDecimal price = BigDecimal.valueOf(1000);
            ProductResponse productResponse = productService.create(new ProductRequest("치킨", price));
            MenuGroupResponse menuGroupResponse = menuGroupService.create(new MenuGroupRequest("Leo's Pick"));
            MenuResponse menuResponse = menuService.create(new MenuRequest(
                    "후라이드",
                    price,
                    menuGroupResponse.getId(),
                    List.of(new MenuProductRequest(productResponse.getId(), 1L))
            ));

            OrderResponse firstOrder = orderService.create(new OrderRequest(firstOrderTable.getId(), List.of(
                    new OrderLineItemRequest(menuResponse.getId(), 1L)
            )));
            OrderResponse secondOrder = orderService.create(new OrderRequest(secondOrderTable.getId(), List.of(
                    new OrderLineItemRequest(menuResponse.getId(), 1L)
            )));

            Long tableGroupId = savedTableGroup.getId();

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
