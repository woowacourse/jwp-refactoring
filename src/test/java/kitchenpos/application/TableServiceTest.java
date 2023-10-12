package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest implements ServiceTest {

    private TableGroup tableGroup;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableService tableService;

    @BeforeEach
    void setUp() {
        this.tableGroup = tableGroupDao.save(테이블_그룹());
    }

    @Test
    void 주문_테이블은_생성될_수_있다() {
        // given
        final OrderTable orderTable = 주문_테이블(10, false);

        // expected
        assertDoesNotThrow(() -> tableService.create(orderTable));
    }

    @Test
    void 주문_테이블이_없다면_비울_수_없다() {
        // given
        final OrderTable orderTable = 주문_테이블(10, false);

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블은_테이블_그룹에_귀속_되었다면_비울_수_없다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(tableGroup, 10, false));

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블은_테이블_그룹에_귀속_되지_않았다면_비울_수_있다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));

        // expected
        assertDoesNotThrow(() -> tableService.changeEmpty(orderTable.getId(), orderTable));
    }

    @Test
    void 주문_테이블은_주문_상태가_조리_중_일_때_빈자리로_변경할_수_없다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        orderDao.save(주문(orderTable.getId(), OrderStatus.COOKING));

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블은_주문_상태가_식사_중_일_때_빈자리로_변경할_수_없다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        orderDao.save(주문(orderTable.getId(), OrderStatus.MEAL));

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블은_주문_상태가_완료라면_빈자리로_변경할_수_있다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, true));
        orderDao.save(주문(orderTable.getId(), OrderStatus.COMPLETION));

        // when
        OrderTable savedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        // then
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블은_빈_자리일_경우_손님_수를_변경할_수_없다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, true));

        // expected
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블은_빈_자리가_아니라면_손님_수를_변경할_수_있다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final OrderTable changeOrderTable = 주문_테이블(12, false);

        // expected
        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable));
    }
}
