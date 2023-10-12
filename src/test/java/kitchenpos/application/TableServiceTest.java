package kitchenpos.application;

import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderTableFixture.테이블1;
import static kitchenpos.fixture.OrderTableFixture.테이블9;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Autowired
    private DataSource dataSource;

    private TableService tableService;

    @Mock
    private OrderDao mockedOrderDao;
    @Mock
    private OrderTableDao mockedOrderTableDao;

    @BeforeEach
    void setUp() {
        var orderDao = new JdbcTemplateOrderDao(dataSource);
        var orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    void 테이블을_등록한다() {
        var unsavedTable = 테이블9();

        var saved = tableService.create(unsavedTable);

        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(테이블9());
    }

    @Test
    void 테이블_등록시_테이블_ID와_테이블그룹_ID를_지정할_수_없다() {
        var desiredId = Long.MAX_VALUE;
        var unsavedTable = 테이블9();
        unsavedTable.setId(desiredId);
        unsavedTable.setTableGroupId(desiredId);

        var saved = tableService.create(unsavedTable);

        assertThat(saved.getId()).isNotEqualTo(desiredId);
        assertThat(saved.getTableGroupId()).isNotSameAs(desiredId);
    }

    @Test
    void 모든_테이블들을_가져온다() {
        assertThat(tableService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(OrderTableFixture.listAllInDatabase());
    }

    @Test
    void 테이블_비움_변경시_기존_테이블이어야한다() {
        var unsavedTable = 테이블9();
        var newState = new OrderTable();
        newState.setEmpty(false);

        assertThatThrownBy(() -> tableService.changeEmpty(unsavedTable.getId(), newState))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_비움_변경시_조리중이나_식사중이면_안된다() {
        var table = 테이블1();
        this.tableService = new TableService(mockedOrderDao, new JdbcTemplateOrderTableDao(dataSource));
        when(mockedOrderDao.existsByOrderTableIdAndOrderStatusIn(
                eq(table.getId()),
                eq(List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
        ).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_비움_변경시_테이블그룹이_없어야한다() {
        this.tableService = new TableService(new JdbcTemplateOrderDao(dataSource), mockedOrderTableDao);

        var table = 테이블1();
        table.setTableGroupId(1L);
        when(mockedOrderTableDao.findById(eq(table.getId()))).thenReturn(Optional.of(table));

        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_채운다() {
        var table = 테이블1();

        var newState = new OrderTable();
        newState.setEmpty(false);

        assertThatNoException()
                .isThrownBy(() -> tableService.changeEmpty(table.getId(), newState));
    }

    @Test
    void 테이블을_비운다() {
        var table = 테이블1();

        var newState = new OrderTable();
        newState.setEmpty(true);

        assertThatNoException()
                .isThrownBy(() -> tableService.changeEmpty(table.getId(), newState));
    }

    @Test
    void 테이블_비움_변경시_저장된_테이블을_반환한다() {
        var expected = 테이블1();
        expected.setEmpty(false);

        var newState = new OrderTable();
        newState.setEmpty(false);
        var actual = tableService.changeEmpty(테이블1().getId(), newState);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 손님수_변경시_손님수는_0이상이어야한다() {
        var newState = new OrderTable();
        newState.setEmpty(false);
        newState.setNumberOfGuests(-1);
        tableService.changeEmpty(테이블1().getId(), newState);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블1().getId(), newState))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수_변경시_기존_테이블을_사용해야한다() {
        var unsaved = 테이블9();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(unsaved.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수_변경시_빈_테이블이면_안된다() {
        var emptyTable = 테이블1();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수_변경시_저장된_테이블을_반환한다() {
        var expected = 테이블1();
        expected.setEmpty(false);
        expected.setNumberOfGuests(Integer.MAX_VALUE);

        var newState = new OrderTable();
        newState.setEmpty(false);
        newState.setNumberOfGuests(Integer.MAX_VALUE);
        tableService.changeEmpty(테이블1().getId(), newState);
        var actual = tableService.changeNumberOfGuests(테이블1().getId(), newState);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
