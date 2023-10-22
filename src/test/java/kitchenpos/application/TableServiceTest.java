package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.CreateOrderTableRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderTableFixture.orderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

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
        final OrderTable expect1 = orderTableDao.save(orderTable(2, true));
        final OrderTable expect2 = orderTableDao.save(orderTable(4, true));

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getNumberOfGuests()).isEqualTo(expect1.getNumberOfGuests());
            softAssertions.assertThat(actual.get(1).getNumberOfGuests()).isEqualTo(expect2.getNumberOfGuests());
        });
    }

    @Test
    @DisplayName("테이블의 비어있음 정보를 변경한다")
    void changeEmpty() {
        // given
        final OrderTable 두명_테이블 = orderTableDao.save(orderTable(2, true));

        두명_테이블.setEmpty(false);
        final OrderTable tableEmptyChange = 두명_테이블;

        // when
        final OrderTable actual = tableService.changeEmpty(두명_테이블.getId(), tableEmptyChange);

        // then
        assertThat(actual.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블의 비어있음 정보를 변경할 때 테이블을 찾을 수 없으면 예외가 발생한다")
    void changeEmpty_invalidOrderTableId() {
        // given
        final Long invalidOrderTableId = -999L;
        final OrderTable 두명_테이블 = orderTableDao.save(orderTable(2, true));

        두명_테이블.setEmpty(false);
        final OrderTable tableEmptyChange = 두명_테이블;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, tableEmptyChange))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "주문 상태가 {0}일 때")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("테이블의 비어있음 정보를 변경할 때 테이블의 주문이 조리중이거나 식사중이면 예외가 발생한다")
    void changeEmpty_invalidOrderStatus(final OrderStatus orderStatus) {
        // given
        final OrderTable 두명_테이블 = orderTableDao.save(orderTable(2, true));

        orderDao.save(order(두명_테이블.getId(), orderStatus));

        두명_테이블.setEmpty(false);
        final OrderTable tableEmptyChange = 두명_테이블;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(두명_테이블.getId(), tableEmptyChange))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        final OrderTable 두명_테이블 = orderTableDao.save(orderTable(2, false));

        int newNumberOfGuests = 10;
        두명_테이블.setNumberOfGuests(newNumberOfGuests);
        final OrderTable tableNumberOfGuestsChange = 두명_테이블;

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(두명_테이블.getId(), tableNumberOfGuestsChange);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블을 찾을 수 없으면 예외가 발생한다")
    void changeNumberOfGuests_invalidNumberOfGuests() {
        // given
        final OrderTable 두명_테이블 = orderTableDao.save(orderTable(2, false));

        final int invalidNumberOfGuests = -1;
        두명_테이블.setNumberOfGuests(invalidNumberOfGuests);
        final OrderTable tableNumberOfGuestsChange = 두명_테이블;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(두명_테이블.getId(), tableNumberOfGuestsChange))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블을 찾을 수 없으면 예외가 발생한다")
    void changeNumberOfGuests_invalidOrderTableId() {
        // given
        final Long invalidOrderTableId = -999L;

        final OrderTable 두명_테이블 = orderTableDao.save(orderTable(2, false));

        int newNumberOfGuests = 10;
        두명_테이블.setNumberOfGuests(newNumberOfGuests);
        final OrderTable tableNumberOfGuestsChange = 두명_테이블;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, tableNumberOfGuestsChange))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블이 비어있으면 예외가 발생한다")
    void changeNumberOfGuests_emptyTable() {
        // given
        int newNumberOfGuests = 10;
        final OrderTable 두명_테이블 = orderTableDao.save(orderTable(2, true));

        두명_테이블.setNumberOfGuests(newNumberOfGuests);
        final OrderTable tableNumberOfGuestsChange = 두명_테이블;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(두명_테이블.getId(), tableNumberOfGuestsChange))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
