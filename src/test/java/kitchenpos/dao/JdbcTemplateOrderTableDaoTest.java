package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("OrderTable Dao 테스트")
class JdbcTemplateOrderTableDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @DisplayName("OrderTable을 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 OrderTable은 저장에 성공한다.")
        @Test
        void success() {
            // given
            OrderTable orderTable = OrderTable을_생성한다(10);

            // when
            OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

            // then
            assertThat(jdbcTemplateOrderTableDao.findById(savedOrderTable.getId())).isPresent();
            assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            assertThat(savedOrderTable.isEmpty()).isFalse();
        }

        @DisplayName("OrderTable과 DB에 ID가 존재하는 OrderTable은 수정을 진행한다.")
        @Test
        void createdThenUpdate() {
            // given
            OrderTable beforeSaveOrderTable = OrderTable을_생성한다(5);
            OrderTable afterSaveOrderTable = jdbcTemplateOrderTableDao.save(beforeSaveOrderTable);

            assertThat(jdbcTemplateOrderTableDao.findById(afterSaveOrderTable.getId())).isPresent();
            assertThat(afterSaveOrderTable.getNumberOfGuests()).isEqualTo(beforeSaveOrderTable.getNumberOfGuests());
            assertThat(afterSaveOrderTable.isEmpty()).isFalse();

            // when
            afterSaveOrderTable.setNumberOfGuests(10);
            OrderTable afterUpdateOrderTable = jdbcTemplateOrderTableDao.save(afterSaveOrderTable);

            // then
            assertThat(afterUpdateOrderTable).usingRecursiveComparison()
                .isEqualTo(afterSaveOrderTable);
            assertThat(afterSaveOrderTable.getNumberOfGuests()).isNotEqualTo(beforeSaveOrderTable.getNumberOfGuests());
        }

        @DisplayName("OderTable엔 ID가 존재하지만 DB에는 존재하지 않는 경우 저장에 실패한다.")
        @Test
        void nonCreatedUpdateFailed() {
            // given
            OrderTable orderTable = OrderTable을_생성한다(5);
            orderTable.setId(Long.MAX_VALUE);

            // when
            OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

            // then
            assertThat(jdbcTemplateOrderTableDao.findById(savedOrderTable.getId())).isNotPresent();
        }

        @DisplayName("TableGroupId가 DB에 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void tableGroupIdNullException() {
            // given
            OrderTable orderTable = OrderTable을_생성한다(5, Long.MAX_VALUE);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateOrderTableDao.save(orderTable))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 OrderTable을 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 OrderTable 조회에 성공한다.")
        @Test
        void present() {
            // given
            OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(10));

            // when
            Optional<OrderTable> foundOrderTable = jdbcTemplateOrderTableDao.findById(savedOrderTable.getId());

            // then
            assertThat(foundOrderTable).isPresent();
            assertThat(foundOrderTable.get()).usingRecursiveComparison()
                .isEqualTo(savedOrderTable);
        }

        @DisplayName("ID가 존재하지 않는다면 OrderTable 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<OrderTable> foundOrderTable = jdbcTemplateOrderTableDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundOrderTable).isNotPresent();
        }
    }

    @DisplayName("모든 OrderTable을 조회한다.")
    @Test
    void findAll() {
        // given
        List<OrderTable> beforeSavedOrderTables = jdbcTemplateOrderTableDao.findAll();

        beforeSavedOrderTables.add(jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(10)));
        beforeSavedOrderTables.add(jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(11)));
        beforeSavedOrderTables.add(jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(12)));

        // when
        List<OrderTable> afterSavedOrderTables = jdbcTemplateOrderTableDao.findAll();

        // then
        assertThat(afterSavedOrderTables).hasSize(beforeSavedOrderTables.size());
        assertThat(afterSavedOrderTables).usingRecursiveComparison()
            .isEqualTo(beforeSavedOrderTables);
    }

    @DisplayName("ID가 일치하는 모든 OrderTable을 조회한다.")
    @Test
    void findAllByIdIn() {
        // given
        OrderTable savedOrderTable1 = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2));
        OrderTable savedOrderTable2 = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(3));
        OrderTable savedOrderTable3 = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(4));

        // when
        List<OrderTable> foundOrderTables = jdbcTemplateOrderTableDao.findAllByIdIn(
            Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId(), savedOrderTable3.getId())
        );

        // then
        assertThat(foundOrderTables).extracting("id")
            .containsExactly(savedOrderTable1.getId(), savedOrderTable2.getId(), savedOrderTable3.getId());
    }

    @DisplayName("TableGroupID가 일치하는 모든 OrderTable을 조회한다.")
    @Test
    void findAllByTableGroupIdIn() {
        // given
        TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다(LocalDateTime.now()));
        OrderTable savedOrderTable1 = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
        OrderTable savedOrderTable2 = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(3, tableGroup.getId()));
        OrderTable savedOrderTable3 = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(4));

        // when
        List<OrderTable> foundOrderTables = jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId());

        // then
        assertThat(foundOrderTables).extracting("id")
            .containsExactly(savedOrderTable1.getId(), savedOrderTable2.getId());
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests) {
        return OrderTable을_생성한다(numberOfGuests, null);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);

        return orderTable;
    }

    private TableGroup TableGroup을_생성한다(LocalDateTime localDateTime) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(localDateTime);

        return tableGroup;
    }
}