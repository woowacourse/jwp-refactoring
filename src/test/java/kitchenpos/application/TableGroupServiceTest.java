package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.fixture.ProductFixture.치킨_8000원;
import static kitchenpos.fixture.ProductFixture.피자_8000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.RequestParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("단체 테이블 등록 시 지정된 빈 테이블들을 주문 테이블로 변경해 저장한다.")
    void 단체_테이블_지정_성공_주문_테이블로_변경() {
        // given
        final List<OrderTable> existingTables = List.of(
                tableService.create(빈_테이블_생성()),
                tableService.create(빈_테이블_생성()),
                tableService.create(빈_테이블_생성())
        );
        final long emptyCountBefore = countEmpty(existingTables);

        // when
        final TableGroup saved = tableGroupService.create(RequestParser.from(existingTables));

        // then
        final long emptyCountAfter = countEmpty(saved.getOrderTables());
        assertThat(emptyCountBefore).isEqualTo(existingTables.size());
        assertThat(emptyCountAfter).isZero();
    }

    private long countEmpty(final List<OrderTable> tables) {
        return tables.stream()
                .filter(OrderTable::isEmpty)
                .count();
    }

    @Test
    @DisplayName("기존에 존재하는 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_존재하지_않는_테이블() {
        // given
        final OrderTable existingTable = tableService.list().get(0);
        final OrderTable newTable = new OrderTable(0, true);

        // when
        final List<OrderTable> tablesInGroup = List.of(existingTable, newTable);

        // then
        assertThatThrownBy(() -> tableGroupService.create(RequestParser.from(tablesInGroup)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 개별의 주문 테이블로 분할할 수 있다.")
    void 단체_테이블_분할_성공_저장() {
        // given
        final TableGroup tableGroup = tableGroupService.create(RequestParser.from(tableService.list()));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> tablesAfterUngroup = tableService.list();
        assertAll(() -> assertThat(tablesAfterUngroup)
                        .map(OrderTable::getTableGroup)
                        .allMatch(Objects::isNull),
                () -> assertThat(tablesAfterUngroup)
                        .noneMatch(OrderTable::isEmpty));
    }

    // TODO 도메인 테스트로 이동
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 '조리', '식사'인 테이블이 있으면 분할할 수 없다.")
    void 단체_테이블_분할_실패_주문_상태(String orderStatus) {
        // given
        final OrderTable notAbleToSplitStatusTable = tableService.create(빈_테이블_생성());
        final OrderTable ableToSplitStatusTable = tableService.create(빈_테이블_생성());
        final List<OrderTable> orderTablesInGroup = List.of(notAbleToSplitStatusTable, ableToSplitStatusTable);
        final TableGroup tableGroup = tableGroupService.create(RequestParser.from(orderTablesInGroup));

        // when
        final Product chicken = productService.create(치킨_8000원());
        final Product pizza = productService.create(피자_8000원());
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("양식"));
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(10000), menuGroup, List.of(chicken, pizza))
        );
        orderService.create(RequestParser.of(notAbleToSplitStatusTable, List.of(menu)));

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
