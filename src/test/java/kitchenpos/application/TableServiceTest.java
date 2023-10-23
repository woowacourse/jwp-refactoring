package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

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
        var saved = tableService.create();

        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(테이블9());
    }

    @Test
    @Transactional
    void 모든_테이블들을_가져온다() {
        assertThat(tableService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(OrderTableFixture.listAllInDatabase());
    }

    @Test
    void 테이블_비움_변경시_기존_테이블이어야한다() {
        var unsavedTable = 테이블9();

        assertThatThrownBy(() -> tableService.changeEmpty(unsavedTable.getId(), false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_비움_변경시_조리중이나_식사중이면_안된다() {
        orderOneFromTable1(양념치킨());

        assertThatThrownBy(() -> tableService.changeEmpty(테이블1().getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_비움_변경시_테이블그룹이_없어야한다() {
        var table = 테이블1();
        tableGroupService.create(new TableGroupRequest(List.of(
                new OrderTableRequest(table.getId()),
                new OrderTableRequest(테이블2().getId())
        )));

        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_채운다() {
        var table = 테이블1();

        assertThatNoException()
                .isThrownBy(() -> tableService.changeEmpty(table.getId(), false));
    }

    @Test
    void 테이블을_비운다() {
        var table = 테이블1();

        assertThatNoException()
                .isThrownBy(() -> tableService.changeEmpty(table.getId(), true));
    }

    @Test
    void 테이블_비움_변경시_저장된_테이블을_반환한다() {
        var expected = false;

        var actual = tableService.changeEmpty(테이블1().getId(), false);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.isEmpty()).isEqualTo(expected);
    }

    @Test
    void 손님수_변경시_손님수는_0이상이어야한다() {
        tableService.changeEmpty(테이블1().getId(), false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블1().getId(), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수_변경시_기존_테이블을_사용해야한다() {
        var unsaved = 테이블9();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(unsaved.getId(), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수_변경시_빈_테이블이면_안된다() {
        var emptyTable = 테이블1();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수_변경시_저장된_테이블을_반환한다() {
        tableService.changeEmpty(테이블1().getId(), false);
        var actual = tableService.changeNumberOfGuests(테이블1().getId(), Integer.MAX_VALUE);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getNumberOfGuests()).isEqualTo(Integer.MAX_VALUE);
    }

    private Order orderOneFromTable1(Menu menu) {
        var fullTable = tableService.changeEmpty(테이블1().getId(), false);
        var item = new OrderLineItemRequest(menu.getId(), 1);
        var order = new OrderRequest(fullTable.getId(), List.of(item));

        return orderService.create(order);
    }
}
