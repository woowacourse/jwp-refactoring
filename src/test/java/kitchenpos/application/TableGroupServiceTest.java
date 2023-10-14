package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static kitchenpos.fixture.Fixture.orderFixture;
import static kitchenpos.fixture.Fixture.orderTableFixture;
import static kitchenpos.fixture.Fixture.tableGroupFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = tableService.create(orderTableFixture(null, 4, true));
        OrderTable orderTable2 = tableService.create(orderTableFixture(null, 4, true));
        tableGroup = tableGroupFixture(now(), List.of(orderTable1, orderTable2));
    }


    @Nested
    @DisplayName("테이블 생성 테스트")
    class CreateTableGroupTest {

        @Test
        @DisplayName("테이블 그룹을 생성한다.")
        void create() {
            //given & when
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            //then
            assertSoftly(softAssertions -> {
                assertThat(savedTableGroup.getOrderTables()).hasSize(2);
                assertThat(savedTableGroup.getId()).isNotNull();
            });
        }

        @Test
        @DisplayName("OrderTables 가 빈 리스트인 경우 예외가 발생한다.")
        void createWithEmptyCreateTables() {
            //given
            tableGroup.setOrderTables(List.of());

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("orderTables 갯수가 2 이하입니다.");
        }

        @Test
        @DisplayName("OrderTables의 size 1인경우 예외가 발생한다.")
        void createWithSingleOrderTable() {
            //given
            OrderTable orderTable = tableService.create(orderTableFixture(null, 4, true));
            tableGroup.setOrderTables(List.of(orderTable));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("orderTables 갯수가 2 이하입니다.");
        }

        @Test
        @DisplayName("저장 되지 않은 OrderTable을 포함한 경우 예외가 발생한다.")
        void createdWithUnSavedOrderTable() {
            //given
            final OrderTable orderTable1 = orderTableFixture(null, 4, true);
            final OrderTable orderTable2 = orderTableFixture(null, 4, true);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("유효하지 않은 OrderTable을 포함하고 있습니다.");
        }

        @Test
        @DisplayName("비어있지 않은 OrderTable을 포함한 경우 예외 발생")
        void createWithFullOrderTable() {
            //given
            final OrderTable orderTable1 = tableService.create(orderTableFixture(null, 4, false));
            final OrderTable orderTable2 = tableService.create(orderTableFixture(null, 4, true));
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 그룹화된 테이블을 포함하고 있습니다.");
        }

        @Test
        @DisplayName("이미 그룹화 되어있는 테이블을 포함하고 있으면 예외가 발생한다.")
        void createWithAlreadyGroupedTable() {
            //given
            final OrderTable orderTable1 = tableService.create(orderTableFixture(null, 4, true));
            final OrderTable orderTable2 = tableService.create(orderTableFixture(null, 4, true));
            final OrderTable orderTable3 = tableService.create(orderTableFixture(null, 4, true));
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            tableGroupService.create(tableGroup);

            final TableGroup tableGroup2 = tableGroupFixture(now(), List.of(orderTable2, orderTable3));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 그룹화된 테이블을 포함하고 있습니다.");
        }
    }

    @Nested
    @DisplayName("테이블 그룹해제 테스트")
    class UnGroupTest {

        @Test
        @DisplayName("테이블의 그룹을 해제한다")
        void unGroupTables() {
            //given
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
            final List<OrderTable> orderTables = savedTableGroup.getOrderTables();
            final Long id1 = orderTables.get(0).getId();
            final Long id2 = orderTables.get(0).getId();

            //when
            tableGroupService.ungroup(savedTableGroup.getId());

            //then
            final OrderTable orderTable1 = orderTableDao.findById(id1).get();
            final OrderTable orderTable2 = orderTableDao.findById(id2).get();

            assertSoftly(softAssertions -> {
                assertThat(orderTable1.isEmpty()).isFalse();
                assertThat(orderTable2.isEmpty()).isFalse();
                assertThat(orderTable1.getTableGroupId()).isNull();
                assertThat(orderTable2.getTableGroupId()).isNull();
            });
        }

        @ParameterizedTest(name = "status = {0}")
        @ValueSource(strings = {"MEAL", "COOKING"})
        void unGroupWithInvalidStatusTable(final String status) {
            //given
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
            final OrderTable orderTable = savedTableGroup.getOrderTables().get(0);

            orderDao.save(orderFixture(orderTable.getId(), status, now(), null));

            //when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("그룹해제 할수 없는 상태의 테이블을 포함하고 있습니다.");
        }
    }
}
