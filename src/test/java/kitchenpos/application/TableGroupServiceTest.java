package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest implements ServiceTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 테이블_그룹의_주문_테이블은_2_이상이_아니라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(0, true));
        final TableGroup tableGroup = 테이블_그룹(orderTable);

        // expected
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹의_생성_시_존재하지_않는_주문_테이블이_있다면_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(주문_테이블(10, true));
        final OrderTable orderTable2 = 주문_테이블(10, true);
        orderTable2.setId(Long.MAX_VALUE);
        final TableGroup tableGroup = 테이블_그룹(orderTable1, orderTable2);

        // expected
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹의_생성_시_해당_하는_주문_테이블_중_빈자리가_아닌게_있다면_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(주문_테이블(10, false));
        final OrderTable orderTable2 = orderTableDao.save(주문_테이블(10, true));
        final TableGroup tableGroup = 테이블_그룹(orderTable1, orderTable2);

        // expected
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹의_생성_시_이미_다른_그룹에_속한_테이블이_있다면_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(주문_테이블(10, true));
        final OrderTable orderTable2 = orderTableDao.save(주문_테이블(10, true));
        TableGroup otherTableGroup = tableGroupDao.save(테이블_그룹(orderTable1));
        orderTable1.setTableGroupId(otherTableGroup.getId());
        orderTableDao.save(orderTable1);

        final TableGroup tableGroup = 테이블_그룹(orderTable1, orderTable2);

        // expected
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(주문_테이블(10, true));
        final OrderTable orderTable2 = orderTableDao.save(주문_테이블(10, true));

        final TableGroup tableGroup = 테이블_그룹(orderTable1, orderTable2);

        // when
        TableGroup savedtableGroup = tableGroupService.create(tableGroup);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedtableGroup.getCreatedDate()).isNotNull();
            softly.assertThat(savedtableGroup.getOrderTables()).hasSize(2);
            softly.assertThat(savedtableGroup.getOrderTables())
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(List.of(
                            주문_테이블(savedtableGroup, 10, false),
                            주문_테이블(savedtableGroup, 10, false))
                    );
        });
    }

    @Test
    void 테이블_그룹에서_요리_중인_상태의_주문이_있을_경우_그룹화를_해제할_수_없다() {
        // given
        final TableGroup tableGroup = tableGroupDao.save(테이블_그룹());
        final OrderTable orderTable1 = orderTableDao.save(주문_테이블(tableGroup, 10, false));
        final OrderTable orderTable2 = orderTableDao.save(주문_테이블(tableGroup, 10, false));

        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);

        // when
        orderDao.save(주문(orderTable1.getId(), OrderStatus.COOKING));

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹에서_식사_중인_상태의_주문이_있을_경우_그룹화를_해제할_수_없다() {
        // given
        final TableGroup tableGroup = tableGroupDao.save(테이블_그룹());
        final OrderTable orderTable1 = orderTableDao.save(주문_테이블(tableGroup, 10, false));
        final OrderTable orderTable2 = orderTableDao.save(주문_테이블(tableGroup, 10, false));

        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);

        // when
        orderDao.save(주문(orderTable1.getId(), OrderStatus.MEAL));

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final TableGroup tableGroup = tableGroupDao.save(테이블_그룹());
        final OrderTable orderTable1 = orderTableDao.save(주문_테이블(tableGroup, 10, false));
        final OrderTable orderTable2 = orderTableDao.save(주문_테이블(tableGroup, 10, false));

        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(
                List.of(orderTable1.getId(), orderTable2.getId()));
        assertThat(orderTables)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(
                        주문_테이블(10, false),
                        주문_테이블(10, false)));

    }
}
