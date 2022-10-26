package kitchenpos.application;

import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_TRUE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.table.domain.dao.JdbcTemplateOrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import kitchenpos.table.application.TableGroupService;
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
            TableGroup tableGroup = TableGroupFixture.getTableGroup(List.of(orderTable1, orderTable2));

            TableGroup actual = tableGroupService.create(tableGroup);

            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id", "orderTables")
                .isEqualTo(tableGroup);
        }
    }

    @NestedApplicationTest
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        private TableGroup tableGroup;
        private OrderTable orderTable1;

        @BeforeEach
        void setUp() {
            orderTable1 = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable());
            OrderTable orderTable2 = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable());
            tableGroup = tableGroupService.create(TableGroupFixture.getTableGroup(List.of(orderTable1, orderTable2)));
        }

        @Test
        @DisplayName("테이블 그룹 아이디를 받으면 포함되어 있는 주문 테이블들을 그룹 해제한다.")
        void success() {
            Long tableGroupId = tableGroup.getId();

            tableGroupService.ungroup(tableGroupId);

            assertThat(orderTable1.getTableGroupId()).isNull();
        }
    }
}
