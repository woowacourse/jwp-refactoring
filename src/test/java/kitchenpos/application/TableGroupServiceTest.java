package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩_상태_설정;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_저장;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_저장;
import static kitchenpos.fixture.TableGroupFixture.단체_테이블_저장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

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
        final TableGroup saved = tableGroupService.create(new TableGroup(existingTables));

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
        final OrderTable newTable = new OrderTable();
        newTable.setEmpty(true);

        // when
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(existingTable, newTable));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소속된 단체가 없는 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_이미_소속_단체가_있는_테이블() {
        // given
        final List<OrderTable> tablesWithGroup = List.of(
                빈_테이블_저장(tableService::create),
                빈_테이블_저장(tableService::create)
        );
        단체_테이블_저장(tableGroupService::create, tablesWithGroup);

        // when
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(tablesWithGroup);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_주문_테이블() {
        // given
        final List<OrderTable> tablesWithGroup = List.of(
                주문_테이블_저장(tableService::create),
                주문_테이블_저장(tableService::create)
        );

        // when
        // then
        assertThatThrownBy(() -> 단체_테이블_저장(tableGroupService::create, tablesWithGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체에 지정할 테이블은 2개 이상이어야 한다.")
    void 단체_테이블_지정_실패_주문_테이블_개수_미달() {
        assertThatThrownBy(() -> 단체_테이블_저장(tableGroupService::create, List.of(주문_테이블_저장(tableService::create))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 개별의 주문 테이블로 분할할 수 있다.")
    void 단체_테이블_분할_성공_저장() {
        // given
        /// TODO: 2023/10/19  service, repository 사용 통일
        final TableGroup tableGroup = tableGroupService.create(new TableGroup(tableService.list()));

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

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 '조리', '식사'인 테이블이 있으면 분할할 수 없다.")
    void 단체_테이블_분할_실패_주문_상태(String orderStatus) {
        // given
        final OrderTable notAbleToSplitStatusTable = tableService.create(빈_테이블_생성());
        final OrderTable ableToSplitStatusTable = tableService.create(빈_테이블_생성());
        final List<OrderTable> orderTablesInGroup = List.of(notAbleToSplitStatusTable, ableToSplitStatusTable);
        final TableGroup tableGroup = tableGroupService.create(new TableGroup(orderTablesInGroup));

        // when
        final Product chicken = productRepository.save(ProductFixture.치킨_8000원());
        final Product pizza = productRepository.save(ProductFixture.피자_8000원());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));
        final Menu menu = menuRepository.save(
                세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(10000), menuGroup, List.of(chicken, pizza))
        );
        orderService.create(
                주문_생성_메뉴_당_1개씩_상태_설정(notAbleToSplitStatusTable, OrderStatus.valueOf(orderStatus), List.of(menu)))
        ;

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
