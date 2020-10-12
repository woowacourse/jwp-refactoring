package kitchenpos.service;

import static kitchenpos.service.step.TableGroupServiceTestStep.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.TableGroup;
import kitchenpos.service.common.ServiceTest;

@DisplayName("TableGroup 단위 테스트")
class TableGroupServiceTest extends ServiceTest {

	@Autowired
	TableGroupService tableGroupService;

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

	@Test
	void ungroup() {
	}
}