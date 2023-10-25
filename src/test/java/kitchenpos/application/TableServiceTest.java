package kitchenpos.application;

import kitchenpos.domain.table.NumberOfGuests;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @PersistenceContext
    private EntityManager em;

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
            softAssertions.assertThat(actual.getTableGroup()).isNull();
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
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupRepository.save(new TableGroup());
        그룹화된_세명_네명_테이블.initOrderTables(List.of(세명_테이블, 네명_테이블));

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
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, false));
        orderRepository.save(new Order(두명_테이블, orderStatus));

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
