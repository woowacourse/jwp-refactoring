package kitchenpos.application;

import static kitchenpos.fixtures.domain.OrderFixture.createOrder;
import static kitchenpos.fixtures.domain.OrderTableFixture.createOrderTable;
import static kitchenpos.fixtures.domain.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixtures.domain.TableGroupFixture.TableGroupRequestBuilder;
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
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        this.orderTables = List.of(
                orderTableDao.save(createOrderTable(10, true)),
                orderTableDao.save(createOrderTable(10, true))
        );
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("단체 지정 정보를 생성한다.")
        @Test
        void Should_CreateTableGroup() {
            // given
            TableGroup tableGroup = new TableGroupRequestBuilder()
                    .addOrderTables(orderTables)
                    .build();

            // when
            TableGroup actual = tableGroupService.create(tableGroup);

            // then
            assertAll(() -> {
                assertThat(tableGroup.getCreatedDate()).isEqualTo(actual.getCreatedDate());
                assertThat(tableGroup.getOrderTables()).hasSize(actual.getOrderTables().size());
            });
        }

        @DisplayName("단체 지정 정보의 테이블 목록이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTablesIsEmpty() {
            // given
            TableGroup tableGroup = new TableGroupRequestBuilder().build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록의 사이즈가 2보다 작다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTablesSizeIsLessThan2() {
            // given
            TableGroup tableGroup = new TableGroupRequestBuilder()
                    .addOrderTables(orderTables.get(0))
                    .build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 존재하지 않는 테이블이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_TableGroupHasNotExistingTable() {
            // given
            OrderTable notSavedTable1 = createOrderTable(10, true);
            OrderTable notSavedTable2 = createOrderTable(15, true);
            TableGroup tableGroup = new TableGroupRequestBuilder()
                    .addOrderTables(notSavedTable1, notSavedTable2)
                    .build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 비어있지 않은 테이블이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_TableIsNotEmpty() {
            // given
            OrderTable orderTable1 = orderTableDao.save(createOrderTable(10, true));
            OrderTable orderTable2 = orderTableDao.save(createOrderTable(10, false));
            TableGroup tableGroup = new TableGroupRequestBuilder()
                    .addOrderTables(orderTable1, orderTable2)
                    .build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 정보의 테이블 목록에 이미 단체 지정 정보가 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableHasTableGroup() {
            // given
            OrderTable orderTable1 = orderTableDao.save(createOrderTable(10, true));
            OrderTable orderTable2 = orderTableDao.save(createOrderTable(10, true));

            TableGroup request = new TableGroupRequestBuilder()
                    .addOrderTables(orderTable1, orderTable2)
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
            TableGroup request = new TableGroupRequestBuilder()
                    .addOrderTables(orderTables).build();
            TableGroup tableGroup = tableGroupService.create(request);

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            assertAll(() -> {
                assertThat(tableGroupDao.findById(tableGroup.getId())).isNotEmpty();
                assertThat(orderTableDao.findAll())
                        .allMatch(orderTable -> Objects.isNull(orderTable.getTableGroupId()));
                List<OrderTable> all = orderTableDao.findAll();
                System.out.println("all = " + all);
                assertThat(all)
                        .allMatch(orderTable -> !orderTable.isEmpty());
            });
        }

        @DisplayName("주문 테이블 중 조리 혹은 식사 상태인 테이블이 있다면 IAE를 던진다.")
        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void Should_ThrowIAE_When_AnyStatusOfOrderTablesIsCookingOrMeal(final OrderStatus orderStatus) {
            // given
            OrderTable orderTable1 = orderTableDao.save(createOrderTable(10, true));
            OrderTable orderTable2 = orderTableDao.save(createOrderTable(10, true));

            orderDao.save(createOrder(orderTable1.getId(), orderStatus, LocalDateTime.now(), List.of()));
            orderDao.save(createOrder(orderTable1.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), List.of()));

            TableGroup tableGroup = tableGroupService.create(
                    createTableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
