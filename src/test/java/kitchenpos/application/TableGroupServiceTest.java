package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 단체_지정을_할_수_있다() {
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(주문_테이블을_생성한다(null, 2, true));
        TableGroup tableGroup = 단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        orderTable1.setTableGroupId(savedTableGroup.getId());
        orderTable1.setEmpty(false);
        orderTable2.setTableGroupId(savedTableGroup.getId());
        orderTable2.setEmpty(false);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup).usingRecursiveComparison()
                        .ignoringFields("id", "orderTables")
                        .isEqualTo(tableGroup),
                () -> assertThat(savedTableGroup.getOrderTables())
                        .usingFieldByFieldElementComparator()
                        .containsExactly(orderTable1, orderTable2)
        );
    }

    @Test
    void 단체_지정하려는_테이블이_2개_미만이면_예외를_반환한다() {
        OrderTable orderTable = orderTableDao.save(주문_테이블을_생성한다(null, 1, true));
        TableGroup tableGroup = 단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정하려는_테이블이_존재하지_않으면_예외를_반환한다() {
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = 주문_테이블을_생성한다(null, 2, true);
        TableGroup tableGroup = 단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정하려는_테이블이_빈_테이블이_아니면_예외를_반환한다() {
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(주문_테이블을_생성한다(null, 2, false));
        TableGroup tableGroup = 단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(주문_테이블을_생성한다(null, 2, true));
        TableGroup tableGroup = 단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));
        tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_단체_지정을_해제할_수_있다() {
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(주문_테이블을_생성한다(null, 2, true));
        Long tableGroupId = tableGroupService.create(
                단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2))).getId();

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @Test
    void 단체_지정하려는_테이블의_주문_목록_중_식사_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(주문_테이블을_생성한다(null, 2, true));
        Long tableGroupId = tableGroupService.create(
                단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2))).getId();
        orderDao.save(주문을_생성한다(orderTable1.getId(), COOKING.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
    }
}
