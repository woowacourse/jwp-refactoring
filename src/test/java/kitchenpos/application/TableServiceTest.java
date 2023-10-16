package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTableFixture.주문테이블(null, 0, true);
    }

    @Test
    void 주문_테이블을_생성한다() {
        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @Test
    void 전체_주문_테이블을_조회한다() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables.get(orderTables.size() - 1))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedOrderTable);
    }

    @Test
    void 주문_테이블_비움_상태_변경() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable newOrderTable = OrderTableFixture.주문테이블_비움상태_변경(savedOrderTable, false);

        // when
        OrderTable emptyOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

        // then
        assertThat(emptyOrderTable.isEmpty()).isFalse();
    }

    @Test
    void 주문_테이블_비움상태_변경시_존재하지_않는_주문_테이블id로_조회할_경우_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable newOrderTable = OrderTableFixture.주문테이블_비움상태_변경(savedOrderTable, false);
        Long notExistId = 1000L;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(notExistId, newOrderTable));
    }

    @Test
    void 주문_테이블_비움상태_변경시_그룹화되어있으면_예외가_발생한다() {
        // given
        TableGroup tableGroup = tableGroupDao.save(TableGroupFixture.테이블그룹_생성(LocalDateTime.now(), null));
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setTableGroupId(tableGroup.getId());

        // when
        OrderTable savedOrderTableWithGroup = orderTableDao.save(savedOrderTable);
        OrderTable newOrderTable = OrderTableFixture.주문테이블_비움상태_변경(savedOrderTableWithGroup, false);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTableWithGroup.getId(), newOrderTable));
    }

    @Test
    void 주문_테이블_비움상태_변경시_주문_상태가_조리중_또는_식사중이면_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable newOrderTable = OrderTableFixture.주문테이블_비움상태_변경(savedOrderTable, false);
        orderDao.save(OrderFixture.주문_상품_없이_생성(savedOrderTable.getId(), "COOKING", LocalDateTime.now(), null));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), newOrderTable));
    }

    @Test
    void 주문_테이블의_손님수를_변경한다() {
        // given
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable guestChangeOrderTable = OrderTableFixture.주문테이블_손님수_변경(savedOrderTable, 5);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), guestChangeOrderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 주문_테이블의_손님수를_음수로_지정하면_예외가_발생한다() {
        // given
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable guestChangeOrderTable = OrderTableFixture.주문테이블_손님수_변경(savedOrderTable, -1);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), guestChangeOrderTable));
    }

    @Test
    void 주문_테이블의_손님수_변경시_존재하지않는_주문테이블id로_조회하면_예외가_발생한다() {
        // given
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        Long notExistId = 1000L;

        // when
        OrderTable guestChangeOrderTable = OrderTableFixture.주문테이블_손님수_변경(savedOrderTable, 5);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(notExistId, guestChangeOrderTable));
    }

    @Test
    void 주문_테이블의_손님수_변경시_테이블이_비어있으면_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable guestChangeOrderTable = OrderTableFixture.주문테이블_손님수_변경(savedOrderTable, 5);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), guestChangeOrderTable));
    }
}
