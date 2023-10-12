package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.Fixture.Fixture.orderFixtrue;
import static kitchenpos.Fixture.Fixture.orderTableFixture;
import static kitchenpos.Fixture.Fixture.tableGroupFixture;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_단체지정 {
        @Test
        void 테이블들을_단체_지정한다() {
            OrderTable orderTable1 = orderTableDao.save(orderTableFixture(null, 1, true));
            OrderTable orderTable2 = orderTableDao.save(orderTableFixture(null, 2, true));
            TableGroup tableGroup = tableGroupFixture(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            orderTable1.setTableGroupId(savedTableGroup.getId());
            orderTable1.setEmpty(false);
            orderTable2.setTableGroupId(savedTableGroup.getId());
            orderTable2.setEmpty(false);

            assertSoftly(softly -> {
                softly.assertThat(savedTableGroup.getId()).isNotNull();
                softly.assertThat(savedTableGroup).extracting("createdDate")
                        .isEqualTo(tableGroup.getCreatedDate());
                softly.assertThat(savedTableGroup.getOrderTables())
                        .extracting("id", "tableGroupId")
                        .containsOnly(tuple(orderTable1.getId(), savedTableGroup.getId()),
                                tuple(orderTable2.getId(), savedTableGroup.getId()));
            });
        }

        @Test
        void 단체_지정하려는_테이블이_2개_미만이면_지정할_수_없다() {
            OrderTable orderTable = orderTableDao.save(orderTableFixture(null, 1, true));
            TableGroup tableGroup = tableGroupFixture(LocalDateTime.now(), List.of(orderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정 테이블의 개수는 2개 이상이어야 합니다.");
        }

        @Test
        void 존재하지_않는_테이블을_단체로_지정할_수_없다() {
            OrderTable savedOrderTable = orderTableDao.save(orderTableFixture(null, 1, true));
            OrderTable notSavedOrderTable = orderTableFixture(null, 2, true);
            TableGroup tableGroup = tableGroupFixture(LocalDateTime.now(), List.of(savedOrderTable, notSavedOrderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블을 지정했습니다. 단체 지정 할 수 없습니다.");
        }

        @Test
        void 비어있지_않은_테이블을_단체로_지정할_수_없다() {
            OrderTable emptyTable = orderTableDao.save(orderTableFixture(null, 1, true));
            OrderTable notEmptyTable = orderTableDao.save(orderTableFixture(null, 2, false));
            TableGroup tableGroup = tableGroupFixture(LocalDateTime.now(), List.of(emptyTable, notEmptyTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않은 테이블이거나 이미 단체지정된 테이블입니다. 단체 지정할 수 없습니다.");
        }

        @Test
        void 이미_지정된_테이블을_단체_지정할_수_없다() {
            OrderTable alreadyGrouped1 = orderTableDao.save(orderTableFixture(null, 1, true));
            OrderTable alreadyGrouped2 = orderTableDao.save(orderTableFixture(null, 2, true));
            OrderTable orderTable = orderTableDao.save(orderTableFixture(null, 3, true));
            tableGroupService.create(tableGroupFixture(LocalDateTime.now(), List.of(alreadyGrouped1, alreadyGrouped2)));

            TableGroup tableGroup = tableGroupFixture(LocalDateTime.now(), List.of(alreadyGrouped1, alreadyGrouped2, orderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않은 테이블이거나 이미 단체지정된 테이블입니다. 단체 지정할 수 없습니다.");
        }
    }


    @Test
    void 단체_지정을_삭제한다() {
        OrderTable orderTable1 = orderTableDao.save(orderTableFixture(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(orderTableFixture(null, 2, true));
        TableGroup tableGroup = tableGroupService.create(tableGroupFixture(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        assertSoftly(softly -> {
            softly.assertThat(orderTable1.getTableGroupId()).isNull();
            softly.assertThat(orderTable2.getTableGroupId()).isNull();
        });
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 단체_지정된_주문_테이블의_주문_상태가_조리_또는_식사인_경우_단체_지정을_삭제할_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable1 = orderTableDao.save(orderTableFixture(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(orderTableFixture(null, 2, true));
        TableGroup tableGroup = tableGroupService.create(tableGroupFixture(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        orderDao.save(orderFixtrue(orderTable1.getId(), orderStatus.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문이 이미 조리 혹은 식사 중입니다. 단체 지정을 삭제할 수 없습니다.");
    }
}
