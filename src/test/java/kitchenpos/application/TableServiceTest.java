package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table.NumberOfGuests;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.dto.CreateOrderTableRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.domain.order.OrderFixture.order;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("테이블을 등록한다")
    void create() {
        // given
        final CreateOrderTableRequest orderTable = new CreateOrderTableRequest(2, true);

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @Test
    @DisplayName("테이블 목록을 조회한다")
    void list() {
        // given
        final OrderTable expect1 = orderTableRepository.save(new OrderTable(2, true));
        final OrderTable expect2 = orderTableRepository.save(new OrderTable(4, true));

        em.flush();
        em.clear();

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(expect1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(expect2);
        });
    }

    @Test
    @DisplayName("테이블의 비어있음 정보를 변경한다")
    void changeEmpty() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));

        em.flush();
        em.clear();

        final ChangeOrderTableEmptyRequest orderTable = new ChangeOrderTableEmptyRequest(false);

        // when
        final OrderTable actual = tableService.changeEmpty(두명_테이블.getId(), orderTable);

        // then
        assertThat(actual.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블의 비어있음 정보를 변경할 때 테이블을 찾을 수 없으면 예외가 발생한다")
    void changeEmpty_invalidOrderTableId() {
        // given
        final Long invalidOrderTableId = -999L;

        final ChangeOrderTableEmptyRequest orderTable = new ChangeOrderTableEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("테이블의 비어있음 정보를 변경할 때 테이블이 그룹화되어 있으면 예외가 발생한다.")
    void changeEmpty_groupedTable() {
        // given
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, true));
        final TableGroup 세명_네명_테이블_그룹 = tableGroupRepository.save(new TableGroup());
        세명_테이블.groupBy(세명_네명_테이블_그룹.getId());
        네명_테이블.groupBy(세명_네명_테이블_그룹.getId());

        em.flush();
        em.clear();

        final ChangeOrderTableEmptyRequest orderTable = new ChangeOrderTableEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(세명_테이블.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화된 테이블의 상태를 변경할 수 없습니다.");
    }

    @ParameterizedTest(name = "주문 상태가 {0}일 때")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("테이블의 비어있음 정보를 변경할 때 테이블의 주문이 조리중이거나 식사중이면 예외가 발생한다")
    void changeEmpty_invalidOrderStatus(final OrderStatus orderStatus) {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드, 2L);
        final MenuProducts 메뉴상품_목록 = new MenuProducts(List.of(후라이드_2개));
        final Menu 후라이드_2개_메뉴 = menuRepository.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), 메뉴상품_목록));
        final OrderLineItem 주문_항목 = new OrderLineItem(후라이드_2개_메뉴.getId(), 2);

        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, false));
        orderRepository.save(order(두명_테이블.getId(), orderStatus, new OrderLineItems(List.of(주문_항목))));

        em.flush();
        em.clear();

        final ChangeOrderTableEmptyRequest orderTable = new ChangeOrderTableEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(두명_테이블.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리중이거나 식사중인 주문이 남아있다면 테이블 상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, false));

        em.flush();
        em.clear();

        int newNumberOfGuests = 10;
        final ChangeNumberOfGuestsRequest orderTable = new ChangeNumberOfGuestsRequest(newNumberOfGuests);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(두명_테이블.getId(), orderTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(new NumberOfGuests(newNumberOfGuests));
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 손님 수가 음수이면 예외가 발생한다")
    void changeNumberOfGuests_invalidNumberOfGuests() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, false));

        em.flush();
        em.clear();

        final int invalidNumberOfGuests = -1;
        final ChangeNumberOfGuestsRequest invalidOrderTable = new ChangeNumberOfGuestsRequest(invalidNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(두명_테이블.getId(), invalidOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블을 찾을 수 없으면 예외가 발생한다")
    void changeNumberOfGuests_invalidOrderTableId() {
        // given
        final Long invalidOrderTableId = -999L;

        int newNumberOfGuests = 10;
        final ChangeNumberOfGuestsRequest orderTable = new ChangeNumberOfGuestsRequest(newNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블이 비어있으면 예외가 발생한다")
    void changeNumberOfGuests_emptyTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));

        em.flush();
        em.clear();

        int newNumberOfGuests = 10;
        final ChangeNumberOfGuestsRequest orderTable = new ChangeNumberOfGuestsRequest(newNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(두명_테이블.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
    }
}
