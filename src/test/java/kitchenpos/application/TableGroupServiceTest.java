package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
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
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.fixture.OrderFixture;
import kitchenpos.domain.fixture.OrderTableFixture;
import kitchenpos.domain.fixture.TableGroupFixture;

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
        final OrderTable orderTable1 = OrderTableFixture.주문_테이블()
            .빈_테이블(true)
            .build();
        final OrderTable orderTable2 = OrderTableFixture.주문_테이블()
            .빈_테이블(true)
            .build();
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.테이블_그룹()
            .주문_테이블들(List.of(savedOrderTable1, savedOrderTable2))
            .build();

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @DisplayName("테이블 그룹 등록 시 주문 테이블의 수가 2이상이어야 한다")
    @Test
    void createNumberOfOrderTableIsLowerTwo() {
        final TableGroup tableGroup = TableGroupFixture.테이블_그룹()
            .주문_테이블들(Collections.emptyList())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 등록하려는 테이블 그룹이 존재해야 한다")
    @Test
    void createOrderTableIsNotExist() {
        final OrderTable notSavedOrderTable1 = OrderTableFixture.주문_테이블().build();
        final OrderTable notSavedOrderTable2 = OrderTableFixture.주문_테이블().build();
        final OrderTable notSavedOrderTable3 = OrderTableFixture.주문_테이블().build();

        final TableGroup tableGroup = TableGroupFixture.테이블_그룹()
            .주문_테이블들(List.of(notSavedOrderTable1, notSavedOrderTable2, notSavedOrderTable3))
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 주문 테이블이 비어있으면 안된다")
    @Test
    void createOrderTableIsNotEmpty() {
        final OrderTable orderTable1 = OrderTableFixture.주문_테이블()
            .빈_테이블(false)
            .build();
        final OrderTable orderTable2 = OrderTableFixture.주문_테이블().build();
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.테이블_그룹()
            .주문_테이블들(List.of(savedOrderTable1, savedOrderTable2))
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹의 아이디가 null 이어야 한다")
    @Test
    void createOrderTableIsNotNull() {
        final OrderTable orderTable1 = OrderTableFixture.주문_테이블()
            .테이블_그룹_아이디(null)
            .build();
        final OrderTable orderTable2 = OrderTableFixture.주문_테이블().build();
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.테이블_그룹()
            .주문_테이블들(List.of(savedOrderTable1, savedOrderTable2))
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 그룹을 해제한다")
    @Test
    void ungroup() {
        final TableGroup tableGroup = TableGroupFixture.테이블_그룹()
            .그룹화한_시간(LocalDateTime.now())
            .build();
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final OrderTable orderTable = OrderTableFixture.주문_테이블()
            .테이블_그룹_아이디(savedTableGroup.getId())
            .build();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedOrderTable.getId())
            .주문_상태(OrderStatus.COMPLETION.name())
            .주문한_시간(LocalDateTime.now())
            .build();
        orderDao.save(order);

        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("테이블의 그룹을 해제할 때 테이블의 주문 상태가 요리중이거나 식사중일 경우 테이블을 비울 수 없다")
    @Test
    void ungroupOrderStatusIsCompletion() {
        final TableGroup tableGroup = TableGroupFixture.테이블_그룹()
            .그룹화한_시간(LocalDateTime.now())
            .build();
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final OrderTable orderTable = OrderTableFixture.주문_테이블()
            .테이블_그룹_아이디(savedTableGroup.getId())
            .build();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedOrderTable.getId())
            .주문_상태(OrderStatus.COOKING.name())
            .주문한_시간(LocalDateTime.now())
            .build();
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
