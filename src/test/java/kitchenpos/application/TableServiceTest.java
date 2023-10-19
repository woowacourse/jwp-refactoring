package kitchenpos.application;

import kitchenpos.domain.order.OrderDao;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.TableGroup;
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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupDao;

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
        savedOrderTable.setEmpty(false);

        // when
        OrderTable emptyOrderTable = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

        // then
        assertThat(emptyOrderTable.isEmpty()).isFalse();
    }

    @Test
    void 주문_테이블_비움상태_변경시_존재하지_않는_주문_테이블id로_조회할_경우_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setEmpty(false);
        Long notExistId = -1L;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(notExistId, savedOrderTable));
    }

    @Test
    void 주문_테이블_비움상태_변경시_그룹화되어있으면_예외가_발생한다() {
        // given
        TableGroup tableGroup = tableGroupDao.save(TableGroupFixture.테이블그룹_생성(LocalDateTime.now(), null));
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setTableGroupId(tableGroup.getId());

        // when
        OrderTable savedOrderTableWithGroup = orderTableRepository.save(savedOrderTable);
        savedOrderTableWithGroup.setEmpty(false);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTableWithGroup.getId(), savedOrderTableWithGroup));
    }

    @Test
    void 주문_테이블_비움상태_변경시_주문_상태가_조리중_또는_식사중이면_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setEmpty(false);
        orderDao.save(OrderFixture.주문(savedOrderTable.getId(), "COOKING", LocalDateTime.now(), null));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable));
    }

    @Test
    void 주문_테이블의_손님수를_변경한다() {
        // given
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        savedOrderTable.setNumberOfGuests(5);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 주문_테이블의_손님수를_음수로_지정하면_예외가_발생한다() {
        // given
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        savedOrderTable.setNumberOfGuests(-1);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable));
    }

    @Test
    void 주문_테이블의_손님수_변경시_존재하지않는_주문테이블id로_조회하면_예외가_발생한다() {
        // given
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        Long notExistId = -1L;

        // when
        savedOrderTable.setNumberOfGuests(5);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(notExistId, savedOrderTable));
    }

    @Test
    void 주문_테이블의_손님수_변경시_테이블이_비어있으면_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        savedOrderTable.setNumberOfGuests(5);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable));
    }
}
