package kitchenpos.application;

import static kitchenpos.fixtures.domain.OrderFixture.createOrder;
import static kitchenpos.fixtures.domain.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.fixtures.domain.TableGroupFixture.TableGroupRequestBuilder;
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
            TableGroupRequest request = new TableGroupRequestBuilder()
                    .addOrderTables(savedOrderTable1, savedOrderTable2)
                    .build();

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
            TableGroupRequest request = new TableGroupRequestBuilder().build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록의 사이즈가 2보다 작다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTablesSizeIsLessThan2() {
            // given
            TableGroupRequest request = new TableGroupRequestBuilder()
                    .addOrderTables(savedOrderTable1)
                    .build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 존재하지 않는 테이블이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_TableGroupHasNotExistingTable() {
            // given
            OrderTableRequest notSavedTable1 = new OrderTableRequest(10, true);
            OrderTableRequest notSavedTable2 = new OrderTableRequest(15, true);
            TableGroupRequest request = new TableGroupRequestBuilder()
                    .addOrderTables(notSavedTable1, notSavedTable2)
                    .build();

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
            TableGroupRequest request = new TableGroupRequestBuilder()
                    .addOrderTables(emptyOrderTable, notEmptyOrderTable)
                    .build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 이미 단체 지정 정보가 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableHasTableGroup() {
            // given
            TableGroupRequest request = new TableGroupRequestBuilder()
                    .addOrderTables(savedOrderTable1, savedOrderTable2)
                    .build();
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
            TableGroupRequest request = new TableGroupRequestBuilder()
                    .addOrderTables(savedOrderTable1, savedOrderTable2)
                    .build();
            TableGroupResponse tableGroup = tableGroupService.create(request);

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            assertAll(() -> {
                assertThat(tableGroupRepository.findById(tableGroup.getId())).isNotEmpty();
                assertThat(orderTableRepository.findAll())
                        .allMatch(orderTable -> Objects.isNull(orderTable.getTableGroupId()))
                        .allMatch(orderTable -> !orderTable.isEmpty());
            });
        }

        @DisplayName("주문 테이블 중 조리 혹은 식사 상태인 테이블이 있다면 IAE를 던진다.")
        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void Should_ThrowIAE_When_AnyStatusOfOrderTablesIsCookingOrMeal(final OrderStatus orderStatus) {
            // given
            OrderTable orderTable1 = orderTableRepository.save(createOrderTable(10, true));
            OrderTable orderTable2 = orderTableRepository.save(createOrderTable(10, true));

            orderRepository.save(createOrder(orderTable1.getId(), orderStatus, LocalDateTime.now(), List.of()));
            orderRepository.save(
                    createOrder(orderTable1.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), List.of()));

            TableGroupResponse request = tableGroupService.create(
                    new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId())));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(request.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
