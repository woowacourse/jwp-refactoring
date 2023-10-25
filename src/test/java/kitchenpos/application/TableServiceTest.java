package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_상품;
import static kitchenpos.fixture.MenuFixture.메뉴_상품들;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderFixture.주문_상품;
import static kitchenpos.fixture.OrderFixture.주문_상품들;
import static kitchenpos.fixture.OrderTableFixture.손님_정보;
import static kitchenpos.fixture.OrderTableFixture.주문_태이블_손님_수_변경_요청;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_빈자리_변경_요청;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성_요청;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table_group.TableGroup;
import kitchenpos.exception.TableException;
import kitchenpos.exception.TableException.NotCompletionTableCannotChangeEmptyException;
import kitchenpos.exception.TableException.TableEmptyException;
import kitchenpos.exception.TableException.TableGroupedTableCannotChangeEmptyException;
import kitchenpos.repositroy.MenuGroupRepository;
import kitchenpos.repositroy.MenuRepository;
import kitchenpos.repositroy.OrderRepository;
import kitchenpos.repositroy.OrderTableRepository;
import kitchenpos.repositroy.ProductRepository;
import kitchenpos.repositroy.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest implements ServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableService tableService;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        this.tableGroup = tableGroupRepository.save(테이블_그룹());
    }

    private Order 주문하다(final OrderTable orderTable) {
        final Product product = productRepository.save(상품("상품", BigDecimal.valueOf(1000)));

        final MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("메뉴 그룹"));
        final MenuProducts menuProducts = 메뉴_상품들(메뉴_상품(product, 1L));
        final Menu menu = menuRepository.save(메뉴("메뉴", 1000L, menuProducts, menuGroup));

        final OrderLineItems orderLineItems = 주문_상품들(주문_상품(menu, 1L));
        return orderRepository.save(주문(orderTable, orderLineItems));
    }

    @Test
    void 주문_테이블은_생성될_수_있다() {
        // given
        final OrderTableCreateRequest request = 주문_테이블_생성_요청(10, false);

        // when
        final OrderTableResponse response = tableService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 존재하지_않는_주문_테이블은_빈자리_변경을_할_수_없다() {
        // given
        final OrderTableChangeEmptyRequest request = 주문_테이블_빈자리_변경_요청(true);

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(Long.MAX_VALUE, request))
                .isInstanceOf(TableException.NotFoundException.class);
    }

    @Test
    void 주문_테이블은_테이블_그룹에_귀속_되었다면_비울_수_없다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        orderTable.order(주문하다(orderTable));
        orderTable.group(tableGroup);

        final OrderTableChangeEmptyRequest request = 주문_테이블_빈자리_변경_요청(true);

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(TableGroupedTableCannotChangeEmptyException.class);
    }

    @Test
    void 주문_테이블은_테이블_그룹에_귀속_되지_않았다면_비울_수_있다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        final OrderTableChangeEmptyRequest request = 주문_테이블_빈자리_변경_요청(true);

        // expected
        assertDoesNotThrow(() -> tableService.changeEmpty(orderTable.getId(), request));
    }

    @Test
    void 주문_테이블은_주문_상태가_조리_중_일_때_빈자리로_변경할_수_없다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        orderTable.order(주문하다(orderTable));
        final OrderTableChangeEmptyRequest request = 주문_테이블_빈자리_변경_요청(true);

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(NotCompletionTableCannotChangeEmptyException.class);
    }

    @Test
    void 주문_테이블은_주문_상태가_식사_중_일_때_빈자리로_변경할_수_없다() {
        // given
        final OrderTable orderTable = 주문_테이블(손님_정보(10, false));
        final Order order = 주문하다(orderTable);
        order.changeOrderStatus(OrderStatus.MEAL);
        orderTable.order(order);
        orderTableRepository.save(orderTable);
        final OrderTableChangeEmptyRequest request = 주문_테이블_빈자리_변경_요청(true);

        // expected
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(NotCompletionTableCannotChangeEmptyException.class);
    }

    @Test
    void 주문_테이블은_주문_상태가_완료라면_빈자리로_변경할_수_있다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        final Order order = 주문하다(orderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);
        orderTable.order(order);
        final OrderTableChangeEmptyRequest request = 주문_테이블_빈자리_변경_요청(true);

        // expected
        assertDoesNotThrow(() -> tableService.changeEmpty(orderTable.getId(), request));
    }

    @Test
    void 주문_테이블은_빈_자리일_경우_손님_수를_변경할_수_없다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(0, true)));
        final OrderTableChangeNumberOfGuestsRequest request = 주문_태이블_손님_수_변경_요청(10);

        // expected
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(TableEmptyException.class);
    }

    @Test
    void 주문_테이블은_빈_자리가_아니라면_손님_수를_변경할_수_있다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        final OrderTableChangeNumberOfGuestsRequest request = 주문_태이블_손님_수_변경_요청(10);

        // expected
        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(orderTable.getId(), request));
    }

    @Test
    void 주문테이블을_전체_조회한다() {
        // given
        orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        orderTableRepository.save(주문_테이블(손님_정보(10, false)));

        // when
        final List<OrderTableResponse> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
