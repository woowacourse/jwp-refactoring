package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.양념치킨;
import static kitchenpos.fixture.OrderTableFixture.테이블1;
import static kitchenpos.fixture.OrderTableFixture.테이블2;
import static kitchenpos.fixture.OrderTableFixture.테이블9;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TableServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

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

        orderFromTable1(양념치킨());

        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_비움_변경시_테이블그룹이_없어야한다() {
        var table = 테이블1();
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table, 테이블2()));
        tableGroupService.create(tableGroup);

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

    private Order orderFromTable1(Menu menu) {
        var fullTable = 테이블1();
        fullTable.setEmpty(false);
        try {
            tableService.changeEmpty(fullTable.getId(), fullTable);
        } catch (IllegalArgumentException ignored) {

        }
        var order = new Order();
        order.setOrderTableId(fullTable.getId());
        var item = new OrderLineItem();
        item.setMenuId(menu.getId());
        order.setOrderLineItems(List.of(item));

        return orderService.create(order);
    }
}
