package kitchenpos.application;

import static kitchenpos.domain.fixture.OrderFixture.완료된_주문;
import static kitchenpos.domain.fixture.OrderFixture.요리중인_주문;
import static kitchenpos.domain.fixture.OrderTableFixture.비어있는_테이블;
import static kitchenpos.domain.fixture.OrderTableFixture.비어있지_않는_테이블;
import static kitchenpos.domain.fixture.OrderTableFixture.새로운_테이블;
import static kitchenpos.domain.fixture.TableGroupFixture.새로운_테이블_그룹;
import static kitchenpos.domain.fixture.TableGroupFixture.테이블_그룹의_주문_테이블들은;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.dao.fake.FakeOrderDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.dao.fake.FakeTableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("TableGroup 서비스 테스트")
class TableGroupServiceTest {

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        orderDao = new FakeOrderDao();
        orderTableDao = new FakeOrderTableDao();
        tableGroupDao = new FakeTableGroupDao();

        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹을 등록한다")
    @Test
    void create() {
        final OrderTable savedOrderTable1 = orderTableDao.save(비어있는_테이블());
        final OrderTable savedOrderTable2 = orderTableDao.save(비어있는_테이블());
        final TableGroup tableGroup = 테이블_그룹의_주문_테이블들은(List.of(savedOrderTable1, savedOrderTable2));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @DisplayName("테이블 그룹 등록 시 주문 테이블의 수가 2이상이어야 한다")
    @Test
    void createNumberOfOrderTableIsLowerTwo() {
        final TableGroup tableGroup = 테이블_그룹의_주문_테이블들은(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 등록하려는 테이블 그룹이 존재해야 한다")
    @Test
    void createOrderTableIsNotExist() {
        final OrderTable notSavedOrderTable1 = 새로운_테이블();
        final OrderTable notSavedOrderTable2 = 새로운_테이블();
        final OrderTable notSavedOrderTable3 = 새로운_테이블();

        final TableGroup tableGroup = 테이블_그룹의_주문_테이블들은(
            List.of(notSavedOrderTable1, notSavedOrderTable2, notSavedOrderTable3)
        );

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 주문 테이블이 비어있으면 안된다")
    @Test
    void createOrderTableIsNotEmpty() {
        final OrderTable savedOrderTable1 = orderTableDao.save(비어있지_않는_테이블());
        final OrderTable savedOrderTable2 = orderTableDao.save(새로운_테이블());

        final TableGroup tableGroup = 테이블_그룹의_주문_테이블들은(List.of(savedOrderTable1, savedOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹의 아이디가 null 이어야 한다")
    @Test
    void createOrderTableIsNotNull() {
        final OrderTable savedOrderTable1 = orderTableDao.save(새로운_테이블(null));
        final OrderTable savedOrderTable2 = orderTableDao.save(새로운_테이블());

        final TableGroup tableGroup = 테이블_그룹의_주문_테이블들은(List.of(savedOrderTable1, savedOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 그룹을 해제한다")
    @Test
    void ungroup() {
        final TableGroup savedTableGroup = tableGroupDao.save(새로운_테이블_그룹());
        final OrderTable savedOrderTable = orderTableDao.save(새로운_테이블(savedTableGroup.getId()));

        orderDao.save(완료된_주문(savedOrderTable.getId()));

        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("테이블의 그룹을 해제할 때 테이블의 주문 상태가 요리중이거나 식사중일 경우 테이블을 비울 수 없다")
    @Test
    void ungroupOrderStatusIsCompletion() {
        final TableGroup savedTableGroup = tableGroupDao.save(새로운_테이블_그룹());
        final OrderTable savedOrderTable = orderTableDao.save(새로운_테이블(savedTableGroup.getId()));

        orderDao.save(요리중인_주문(savedOrderTable.getId()));

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
