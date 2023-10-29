package kitchenpos.table.application;

import kitchenpos.configuration.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.ChangeNumberOfGuestsDto;
import kitchenpos.table.dto.ChangeOrderTableEmptyDto;
import kitchenpos.table.dto.CreateOrderTableDto;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.menu.domain.MenuFixture.menu;
import static kitchenpos.order.domain.OrderFixture.order;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

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
        final CreateOrderTableDto request = new CreateOrderTableDto(2, true);

        // when
        final OrderTable actual = tableService.create(request);

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

        final Long tableId = 두명_테이블.getId();
        final ChangeOrderTableEmptyDto request = new ChangeOrderTableEmptyDto(false);

        // when
        final OrderTable actual = tableService.changeEmpty(tableId, request);

        // then
        assertThat(actual.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블의 비어있음 정보를 변경할 때 테이블을 찾을 수 없으면 예외가 발생한다")
    void changeEmpty_invalidOrderTableId() {
        // given
        final Long invalidOrderTableId = -999L;

        final ChangeOrderTableEmptyDto request = new ChangeOrderTableEmptyDto(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, request))
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
        orderTableRepository.save(세명_테이블);
        orderTableRepository.save(네명_테이블);

        final Long groupedTableId = 세명_테이블.getId();
        final ChangeOrderTableEmptyDto request = new ChangeOrderTableEmptyDto(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(groupedTableId, request))
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
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드.getId(), 2L);
        final Menu 후라이드_2개_메뉴 = menuRepository.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));
        final OrderLineItem 주문_항목 = new OrderLineItem(후라이드_2개_메뉴.getId(), 2);

        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, false));
        orderRepository.save(order(두명_테이블.getId(), orderStatus, new OrderLineItems(List.of(주문_항목))));

        final Long notCompleteTableId = 두명_테이블.getId();
        final ChangeOrderTableEmptyDto request = new ChangeOrderTableEmptyDto(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(notCompleteTableId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리중이거나 식사중인 주문이 남아있다면 테이블 상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, false));

        final Long tableId = 두명_테이블.getId();
        int newNumberOfGuests = 10;
        final ChangeNumberOfGuestsDto request = new ChangeNumberOfGuestsDto(newNumberOfGuests);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(tableId, request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(new NumberOfGuests(newNumberOfGuests));
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 손님 수가 음수이면 예외가 발생한다")
    void changeNumberOfGuests_invalidNumberOfGuests() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, false));

        final Long tableId = 두명_테이블.getId();
        final int invalidNumberOfGuests = -1;
        final ChangeNumberOfGuestsDto invalidRequest = new ChangeNumberOfGuestsDto(invalidNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블을 찾을 수 없으면 예외가 발생한다")
    void changeNumberOfGuests_invalidOrderTableId() {
        // given
        final Long invalidOrderTableId = -999L;

        int newNumberOfGuests = 10;
        final ChangeNumberOfGuestsDto request = new ChangeNumberOfGuestsDto(newNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블이 비어있으면 예외가 발생한다")
    void changeNumberOfGuests_emptyTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));

        int newNumberOfGuests = 10;
        final ChangeNumberOfGuestsDto request = new ChangeNumberOfGuestsDto(newNumberOfGuests);
        final Long emptyTableId = 두명_테이블.getId();

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTableId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
    }
}
