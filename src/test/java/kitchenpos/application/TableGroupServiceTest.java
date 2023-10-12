package kitchenpos.application;

import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성_요청;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends IntegrationTestHelper {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 단체_지정을_저장한다() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 10, true));
        OrderTable otherTable = orderTableDao.save(주문_테이블_생성(null, 20, true));
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable, otherTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        // when
        TableGroup result = tableGroupService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getOrderTables()).hasSize(2);
            softly.assertThat(result.getOrderTables().get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(result.getOrderTables().get(0).isEmpty()).isEqualTo(false);
            softly.assertThat(result.getOrderTables().get(1).getNumberOfGuests()).isEqualTo(otherTable.getNumberOfGuests());
            softly.assertThat(result.getOrderTables().get(1).isEmpty()).isEqualTo(false);
        });
    }

    @Test
    void 주문_테이블이_2개보다_적다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 10, true));
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 다른_주문_테이블이_들어오면_예외를_발생시킨다() {
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 10, true));
        OrderTable otherTable = 주문_테이블_생성(null, 20, true);
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable, otherTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 10, true));
        OrderTable otherTable = orderTableDao.save(주문_테이블_생성(null, 20, true));
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable, otherTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        TableGroup savedTableGroup = tableGroupService.create(request);

        // when & then
        assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    @Test
    void 밥_먹는데_단체_지정을_풀어버리면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 10, true));
        OrderTable otherTable = orderTableDao.save(주문_테이블_생성(null, 20, true));
        TableGroup tableGroup = tableGroupService.create(단체_지정_생성_요청(단체_지정_생성(List.of(orderTable, otherTable))));
        orderDao.save(주문_생성(orderTable.getId(), COOKING.name(), LocalDateTime.now(), null));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
