package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Objects;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
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
    private TableGroupService tableGroupService;


    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

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
            MenuProduct menuProduct = new MenuProduct(savedProduct, 1L);
            MenuGroupResponse savedMenuGroup = saveMenuGroup("메뉴 그룹");
            MenuResponse savedMenu = saveMenu("메뉴", 10_000, savedMenuGroup.toEntity(), List.of(menuProduct));

            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(12, false));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(13, false));

            Order order1 = new Order(orderTable1, List.of(new OrderLineItem(savedMenu.getId(), 1L)));
            order1.changeOrderStatus(orderStatus);

            Order order2 = new Order(orderTable2, List.of(new OrderLineItem(savedMenu.getId(), 1L)));
            order2.changeOrderStatus(OrderStatus.COMPLETION);

            orderRepository.save(order1);
            orderRepository.save(order2);

            orderTable1.changeEmpty(true);
            orderTable2.changeEmpty(true);

            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);

            TableGroupResponse request = tableGroupService.create(
                    new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId())));
            // TODO: 리팩토링

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(request.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
