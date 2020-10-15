package kitchenpos.service;

import static kitchenpos.service.step.TableGroupServiceTestStep.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.service.common.ServiceTest;

@DisplayName("TableGroup 단위 테스트")
class TableGroupServiceTest extends ServiceTest {

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private OrderTableDao orderTableDao;

	@DisplayName("2개 이상의 빈 테이블을 단체로 지정할 수 있다.")
	@Test
	void create() {
		TableGroup tableGroup = createValidTableGroup();

		TableGroup result = tableGroupService.create(tableGroup);

		assertThat(result.getId()).isEqualTo(5L);

		assertAll(
			() -> assertThat(result.getId()).isEqualTo(5L),
			() -> assertThat(result.getCreatedDate()).isNotNull(),
			() -> assertThat(result.getOrderTables()).hasSize(2)
		);
	}

	@DisplayName("단체 지정 시 테이블을 중복될 수 없다.")
	@Test
	void create_WhenHasDuplicatedTable() {
		TableGroup tableGroup = createTableGroupThatHasDuplicatedTables();

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정을 해지할 수 있다.")
	@Test
	void ungroup() {
		tableGroupService.ungroup(1L);

		List<OrderTable> allByTableGroupId = orderTableDao.findAllByTableGroupId(1L);

		assertThat(allByTableGroupId).isEmpty();
	}

	@DisplayName("주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
	@ParameterizedTest
	@ValueSource(longs = {2, 3})
	void ungroup_WhenOrderStatusIsMealOrCooking(long id) {
		assertThatThrownBy(() -> tableGroupService.ungroup(id))
			.isInstanceOf(IllegalArgumentException.class);
	}
}