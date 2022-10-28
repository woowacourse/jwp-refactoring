package kitchenpos.application;

import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_FALSE;
import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_TRUE;
import static kitchenpos.support.fixture.dto.TableGroupDtoFixture.단체_지정_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.dao.JdbcTemplateOrderTableDao;
import kitchenpos.table.domain.dao.JdbcTemplateTableGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @NestedApplicationTest
    @DisplayName("create 메서드는")
    class Create {

        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            orderTable1 = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable());
            orderTable2 = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable());
        }

        @Test
        @DisplayName("테이블 그룹을 생성한다.")
        void success() {
            TableGroupResponse tableGroupResponse = tableGroupService.create(
                단체_지정_생성_요청(new OrderTables(List.of(orderTable1, orderTable2))));
            assertThat(tableGroupResponse).usingRecursiveComparison()
                .ignoringFields("id", "orderTables.id")
                .isEqualTo(tableGroupResponse);
        }
    }

    @NestedApplicationTest
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        private TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            tableGroup = jdbcTemplateTableGroupDao.save(TableGroup.from());
            jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_FALSE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_FALSE.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("테이블 그룹 아이디를 받으면 포함되어 있는 주문 테이블들을 그룹 해제한다.")
        void success() {
            tableGroupService.ungroup(tableGroup.getId());

            List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId());
            assertThat(orderTables).hasSize(0);
        }
    }
}
