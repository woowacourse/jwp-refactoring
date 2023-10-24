package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.dto.TableEmptyRequest;
import kitchenpos.ordertable.application.dto.TableGuestRequest;
import kitchenpos.ordertable.application.dto.TableRequest;
import kitchenpos.ordertable.application.dto.TableResponse;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.ServiceTest;
import kitchenpos.tablegroup.application.dto.OrderTableResponse;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {


    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;


    @Test
    void 테이블_등록() {
        // given
        TableRequest tableRequest = new TableRequest(0, true);

        // when
        TableResponse tableResponse = tableService.create(tableRequest);

        // then
        assertThat(orderTableRepository.findById(tableResponse.getId())).isPresent();
    }

    @Test
    void 테이블_목록_조회() {
        // given
        TableRequest tableRequest = new TableRequest(0, true);
        TableResponse tableResponse1 = tableService.create(tableRequest);
        TableResponse tableResponse2 = tableService.create(tableRequest);

        // when
        List<TableResponse> tableResponses = tableService.list();

        // then
        assertThat(tableResponses)
                .usingRecursiveComparison()
                .isEqualTo(List.of(tableResponse1, tableResponse2));
    }

    @Nested
    class 주문_테이블을_빈_테이블로_변경할_때 {

        @Test
        void success() {
            // given
            TableRequest tableRequest = new TableRequest(0, false);
            TableResponse tableResponse = tableService.create(tableRequest);

            Product savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
            MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));

            // when
            MenuResponse menuResponse = menuService.create(
                    new MenuRequest(
                            "후라이드 치킨",
                            new BigDecimal("1000"),
                            savedMenuGroup.getId(),
                            List.of(new MenuProductRequest(savedProduct.getId(), 1L))
                    )
            );

            OrderResponse orderResponse = orderService.create(new OrderRequest(tableResponse.getId(),
                    List.of(new OrderLineItemRequest(menuResponse.getId(), 1L))));

            orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.MEAL));
            orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.COMPLETION));

            TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(false);

            // when
            TableResponse actual = tableService.changeEmpty(tableResponse.getId(), tableEmptyRequest);

            // then
            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        void 주문_상태가_조리중이거나_식사_중이면_실패() {
            // given
            TableRequest tableRequest = new TableRequest(0, false);
            TableResponse tableResponse = tableService.create(tableRequest);

            Product savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
            MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));

            // when
            MenuResponse menuResponse = menuService.create(
                    new MenuRequest(
                            "후라이드 치킨",
                            new BigDecimal("1000"),
                            savedMenuGroup.getId(),
                            List.of(new MenuProductRequest(savedProduct.getId(), 1L))
                    )
            );

            OrderResponse orderResponse = orderService.create(new OrderRequest(tableResponse.getId(),
                    List.of(new OrderLineItemRequest(menuResponse.getId(), 1L))));

            orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.MEAL));
            TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(false);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeEmpty(tableResponse.getId(), tableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_테이블에_소속되어_있으면_실패() {
            // given
            List<TableResponse> tableResponses = List.of(
                    tableService.create(new TableRequest(0, true)),
                    tableService.create(new TableRequest(0, true)),
                    tableService.create(new TableRequest(0, true))
            );

            List<Long> tableIds = tableResponses.stream()
                    .map(TableResponse::getId)
                    .collect(Collectors.toList());

            TableGroupResponse tableGroupResponse = tableGroupService.create(TableGroupRequest.from(tableIds));

            OrderTableResponse orderTableResponse = tableGroupResponse.getOrderTables().get(0);
            Long orderTableId = orderTableResponse.getId();

            TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(false);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, tableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    class 주문_테이블의_고객_수를_변경할_때 {


        @Test
        void success() {
            // given
            TableRequest tableRequest = new TableRequest(2, false);
            TableResponse tableResponse = tableService.create(tableRequest);

            TableGuestRequest tableGuestRequest = new TableGuestRequest(4);

            // when
            TableResponse result = tableService.changeNumberOfGuests(tableResponse.getId(), tableGuestRequest);

            // then
            assertThat(result.getNumberOfGuests()).isEqualTo(4);
        }

        @Test
        void 변경할_고객의_수가_0_미만이면_실패() {
            // given
            TableRequest tableRequest = new TableRequest(2, false);
            TableResponse tableResponse = tableService.create(tableRequest);
            Long orderTableId = tableResponse.getId();

            TableGuestRequest tableGuestRequest = new TableGuestRequest(-1);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_주문_테이블이면_실패() {
            // given
            Long invalidOrderTableId = 0L;
            TableGuestRequest tableGuestRequest = new TableGuestRequest(3);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, tableGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_빈_테이블이면_실패() {
            // given
            TableRequest tableRequest = new TableRequest(2, true);
            TableResponse tableResponse = tableService.create(tableRequest);
            Long orderTableId = tableResponse.getId();

            TableGuestRequest tableGuestRequest = new TableGuestRequest(3);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }


}
