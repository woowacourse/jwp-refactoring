package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTableResponse savedOrderTable1;
    private OrderTableResponse savedOrderTable2;

    @BeforeEach
    void setUp() {
        savedOrderTable1 = saveOrderTable(10, true);
        savedOrderTable2 = saveOrderTable(10, true);
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("단체 지정 정보를 생성한다.")
        @Test
        void Should_CreateTableGroup() {
            // given
            TableGroupRequest request = new TableGroupRequest(
                    List.of(savedOrderTable1.getId(), savedOrderTable2.getId()));

            // when
            TableGroupResponse actual = tableGroupService.create(request);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(request.getOrderTables()).hasSize(actual.getOrderTables().size());
            });
        }

        @DisplayName("단체 지정 정보의 테이블 목록이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTablesIsEmpty() {
            // given
            TableGroupRequest request = new TableGroupRequest(List.of());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록의 사이즈가 2보다 작다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTablesSizeIsLessThan2() {
            // given
            TableGroupRequest request = new TableGroupRequest(List.of(savedOrderTable1.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 존재하지 않는 테이블이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_TableGroupHasNotExistingTable() {
            // given
            TableGroupRequest request = new TableGroupRequest(
                    List.of(savedOrderTable2.getId() + 1, savedOrderTable2.getId() + 2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 비어있지 않은 테이블이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_TableIsNotEmpty() {
            // given
            OrderTableResponse emptyOrderTable = saveOrderTable(10, true);
            OrderTableResponse notEmptyOrderTable = saveOrderTable(10, false);
            TableGroupRequest request = new TableGroupRequest(
                    List.of(emptyOrderTable.getId(), notEmptyOrderTable.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 이미 단체 지정 정보가 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableHasTableGroup() {
            // given
            TableGroupRequest request = new TableGroupRequest(
                    List.of(savedOrderTable1.getId(), savedOrderTable2.getId()));
            tableGroupService.create(request);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("ungroup 메소드는")
    @Nested
    class UngroupMethod {

        @DisplayName("단체 지정을 해제한다.")
        @Test
        void Should_Ungroup() {
            // given
            TableGroupRequest request = new TableGroupRequest(
                    List.of(savedOrderTable1.getId(), savedOrderTable2.getId()));
            TableGroupResponse tableGroup = tableGroupService.create(request);

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            assertAll(() -> {
                assertThat(tableGroupRepository.findById(tableGroup.getId())).isNotEmpty();
                assertThat(orderTableRepository.findAll())
                        .allMatch(orderTable -> Objects.isNull(orderTable.getTableGroup()))
                        .allMatch(orderTable -> !orderTable.isEmpty());
            });
        }

        @DisplayName("주문 테이블 중 조리 혹은 식사 상태인 테이블이 있다면 IAE를 던진다.")
        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void Should_ThrowIAE_When_AnyStatusOfOrderTablesIsCookingOrMeal(final OrderStatus orderStatus) {
            // given
            Product savedProduct = saveProduct("상품", 10_000).toEntity();
            MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("메뉴 그룹"));
            MenuResponse menu = menuService.create(new MenuRequest(
                    "메뉴", BigDecimal.valueOf(10_000), menuGroup.getId(),
                    List.of(new MenuProductRequest(savedProduct.getId(), 1L))
            ));
            OrderTableResponse orderTable1 = tableService.create(new OrderTableRequest(10, true));
            OrderTableResponse orderTable2 = tableService.create(new OrderTableRequest(10, true));

            TableGroupResponse request = tableGroupService.create(
                    new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId())));

            OrderResponse order1 = orderService.create(
                    new OrderRequest(
                            orderTable1.getId(), List.of(new OrderLineItemRequest(menu.getId(), 1L)
                    ))
            );
            orderService.changeOrderStatus(order1.getId(), new OrderStatusUpdateRequest(orderStatus));

            OrderResponse order2 = orderService.create(
                    new OrderRequest(
                            orderTable2.getId(), List.of(new OrderLineItemRequest(menu.getId(), 1L)
                    ))
            );
            orderService.changeOrderStatus(order2.getId(), new OrderStatusUpdateRequest(OrderStatus.COMPLETION));
            // TODO: 리팩토링

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(request.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
