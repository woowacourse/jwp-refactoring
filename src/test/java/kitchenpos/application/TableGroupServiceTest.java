package kitchenpos.application;

import static kitchenpos.application.fixture.OrderFixtures.generateOrder;
import static kitchenpos.application.fixture.OrderTableFixtures.*;
import static kitchenpos.application.fixture.TableGroupFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableGroupSaveRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupServiceTest {


    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableGroupService tableGroupService;

    @Autowired
    public TableGroupServiceTest(final OrderDao orderDao,
                                 final OrderTableDao orderTableDao,
                                 final TableGroupDao tableGroupDao,
                                 final TableGroupService tableGroupService) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableGroupService = tableGroupService;
    }

    @Test
    void tableGroup을_생성한다() {
        OrderTable 테이블_1번 = orderTableDao.save(generateOrderTable(0, true));
        OrderTable 테이블_2번 = orderTableDao.save(generateOrderTable(0, true));

        TableGroupResponse actual = tableGroupService.create(generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번)));

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @Test
    void orderTables가_비어있으면_예외를_던진다() {
        TableGroupSaveRequest request = generateTableGroupSaveRequest(List.of());

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTables의_사이즈가_2미만인_경우_예외를_던진다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, true));

        assertThatThrownBy(() -> tableGroupService.create(generateTableGroupSaveRequest(List.of(orderTable))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderGroup이_가진_orderTables의_사이즈와_저장된_orderTables의_사이즈가_다른_경우_예외를_던진다() {
        OrderTable 테이블_1번 = orderTableDao.save(generateOrderTable(0, true));
        OrderTable 테이블_2번 = orderTableDao.save(generateOrderTable(0, true));
        OrderTable 테이블_3번 = generateOrderTable(0, true);

        assertThatThrownBy(
                () -> tableGroupService.create(generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번, 테이블_3번))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTables_중_비어있지_않은_table이_존재하는_경우_예외를_던진다() {
        OrderTable 테이블_1번 = orderTableDao.save(generateOrderTable(0, true));
        OrderTable 테이블_2번 = orderTableDao.save(generateOrderTable(0, true));
        OrderTable 비어있지_않은_테이블 = orderTableDao.save(generateOrderTable(0, false));

        assertThatThrownBy(
                () -> tableGroupService.create(generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번, 비어있지_않은_테이블))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTables_중_tableGroupId가_null이_아닌_경우_예외를_던진다() {
        TableGroup tableGroup = tableGroupDao.save(generateTableGroup(List.of()));
        OrderTable 테이블_1번 = orderTableDao.save(generateOrderTable(tableGroup.getId(), 0, true));
        OrderTable 테이블_2번 = orderTableDao.save(generateOrderTable(tableGroup.getId(), 0, true));

        OrderTable 테이블_3번 = orderTableDao.save(generateOrderTable(0, true));

        assertThatThrownBy(
                () -> tableGroupService.create(generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번, 테이블_3번))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void tableGroup을_해제한다() {
        TableGroup tableGroup = tableGroupDao.save(generateTableGroup(List.of()));
        orderTableDao.save(generateOrderTable(tableGroup.getId(), 0, true));
        orderTableDao.save(generateOrderTable(tableGroup.getId(), 0, true));

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(() -> {
            List<OrderTable> actual = orderTableDao.findAll();
            assertThat(actual.get(0).getTableGroupId()).isNull();
            assertThat(actual.get(0).isEmpty()).isFalse();
            assertThat(actual.get(1).getTableGroupId()).isNull();
            assertThat(actual.get(1).isEmpty()).isFalse();
        });
    }

    @ParameterizedTest(name = "orderTables의 orderStatus가 {0} 인 경우 예외를 던진다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void orderTables의_orderStatus가_COOKING_MEAL인_경우_예외를_던진다(final OrderStatus orderStatus) {
        TableGroup tableGroup = tableGroupDao.save(generateTableGroup(List.of()));
        OrderTable 테이블_1번 = orderTableDao.save(generateOrderTable(tableGroup.getId(), 0, true));
        OrderTable 테이블_2번 = orderTableDao.save(generateOrderTable(tableGroup.getId(), 0, true));

        orderDao.save(generateOrder(테이블_1번.getId(), orderStatus, List.of()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
