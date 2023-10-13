package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void create_메서드는_tableGroup을_전달하면_tableGroup을_저장하고_반환한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(주문_테이블_생성(true));
        final OrderTable persistOrderTable2 = orderTableDao.save(주문_테이블_생성(true));
        final TableGroup tableGroup = 단체_지정_생성(Arrays.asList(persistOrderTable1, persistOrderTable2));

        // when
        final TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @ParameterizedTest(name = "tableGroup이 {0}이면 예외가 발생한다.")
    @NullAndEmptySource
    void create_메서드는_tableGroup이_없는_tableGroup을_전달하면_예외가_발생한다(final List<OrderTable> invalidOrderTables) {
        // given
        final TableGroup invalidTableGroup = 단체_지정_생성(invalidOrderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_tableGroup이_하나인_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성(true));
        final TableGroup invalidTableGroup = 단체_지정_생성(Arrays.asList(persistOrderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_tableGroup이_비어있지_않은_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(주문_테이블_생성(false));
        final OrderTable persistOrderTable2 = orderTableDao.save(주문_테이블_생성(false));
        final TableGroup invalidTableGroup = 단체_지정_생성(Arrays.asList(persistOrderTable1, persistOrderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup_메서드는_지정한_tableGroup의_id의_그룹을_해제한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(주문_테이블_생성(true));
        final OrderTable persistOrderTable2 = orderTableDao.save(주문_테이블_생성(true));
        final TableGroup persistTableGroup = tableGroupDao.save(
                단체_지정_생성(Arrays.asList(persistOrderTable1, persistOrderTable2))
        );

        // when
        tableGroupService.ungroup(persistTableGroup.getId());

        // then
        assertAll(
                () -> assertThat(persistOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(persistOrderTable2.getTableGroupId()).isNull()
        );
    }

    @ParameterizedTest(name = "orderStatus가 {0}일 때 예외가 발생한다.")
    @ValueSource(strings = {"MEAL", "COOKING"})
    void upgroup_메서드는_orderTable의_order의_상태가_COMPLETION이_아닌_tableGroup을_전달하면_예외가_발생한다(final String invalidOrderStatus) {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(주문_테이블_생성(true));
        final OrderTable persistOrderTable2 = orderTableDao.save(주문_테이블_생성(true));
        orderDao.save(주문_생성(persistOrderTable1.getId(), invalidOrderStatus));
        orderDao.save(주문_생성(persistOrderTable2.getId(), invalidOrderStatus));
        final TableGroup persistTableGroup = tableGroupDao.save(
                단체_지정_생성(Arrays.asList(persistOrderTable1, persistOrderTable2))
        );
        persistOrderTable1.setTableGroupId(persistTableGroup.getId());
        persistOrderTable2.setTableGroupId(persistTableGroup.getId());
        orderTableDao.save(persistOrderTable1);
        orderTableDao.save(persistOrderTable2);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(persistTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
