package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.OrderTable;
import kitchenpos.service.common.ServiceTest;

@DisplayName("TableService 단위 테스트")
class TableServiceTest extends ServiceTest {

	@Autowired
	TableService tableService;

	@DisplayName("주문 테이블을 등록할 수 있다.")
	@Test
	void create() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(0);
		orderTable.setEmpty(true);

		OrderTable created = tableService.create(orderTable);

		assertAll(
			() -> assertThat(created.getId()).isEqualTo(10),
			() -> assertThat(created.getTableGroupId()).isNull(),
			() -> assertThat(created.getNumberOfGuests()).isEqualTo(0),
			() -> assertThat(created.isEmpty()).isTrue()
		);
	}

	@DisplayName("주문 테이블의 목록을 조회할 수 있다.")
	@Test
	void list() {
		List<OrderTable> list = tableService.list();

		assertThat(list).hasSize(9);
	}

	@DisplayName("빈 테이블을 설정할 수 있다.")
	@Test
	void changeEmpty_WhenSetTrue() {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(true);

		OrderTable result = tableService.changeEmpty(2L, orderTable);

		assertThat(result.isEmpty()).isTrue();
	}

	@DisplayName("빈 테이블을 해지할 수 있다.")
	@Test
	void changeEmpty_WhenSetFalse() {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(false);

		OrderTable result = tableService.changeEmpty(1L, orderTable);

		assertThat(result.isEmpty()).isFalse();
	}

	@DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정할 수 없다.")
	@Test
	void changeEmpty_WhenTableIsGrouped_CanNotSetTrue() {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(true);

		assertThatThrownBy(() -> tableService.changeEmpty(9L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정된 주문 테이블은 빈 테이블 해지할 수 없다.")
	@Test
	void changeEmpty_WhenTableIsGrouped_CanNotSetFalse() {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(false);

		assertThatThrownBy(() -> tableService.changeEmpty(6L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
	@ParameterizedTest
	@CsvSource(value = {"4,true", "4,false", "3,true", "3,false"})
	void changeEmpty_WhenOrderStatusIsCookingOrMeal_CanNot(long id, boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(empty);

		assertThatThrownBy(() -> tableService.changeEmpty(id, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("방문한 손님 수를 입력할 수 있다.")
	@Test
	void changeNumberOfGuests() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(4);

		OrderTable result = tableService.changeNumberOfGuests(2L, orderTable);

		assertThat(result.getNumberOfGuests()).isEqualTo(4);
	}

	@DisplayName("방문한 손님 수는 0명 이상이어야 한다.")
	@Test
	void changeNumberOfGuests_WhenNumberOfGuestsLessThanZero() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(-1);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(2L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
	@Test
	void changeNumberOfGuests_WhenTableIsEmpty() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(4);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}
}