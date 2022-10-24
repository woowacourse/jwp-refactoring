package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class OrderTableDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;


    @Nested
    class save_메서드는 {

        private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);
        private final OrderTable orderTable = new OrderTable(null, 5, false);
        private TableGroup savedTableGroup;

        @BeforeEach
        void setUp() {
            savedTableGroup = tableGroupDao.save(tableGroup);
            orderTable.setTableGroupId(savedTableGroup.getId());
        }

        @Nested
        class 주문_테이블이_주어지면 {

            @Test
            void id를_채운다() {
                final OrderTable savedOrderTable = orderTableDao.save(orderTable);

                assertThat(savedOrderTable.getId()).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        @Nested
        class 주문_테이블_id가_주어지면 {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);
            private final OrderTable orderTable = new OrderTable(null, 5, false);
            private OrderTable savedOrderTable;

            @BeforeEach
            void setUp() {
                final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
                orderTable.setTableGroupId(savedTableGroup.getId());
                savedOrderTable = orderTableDao.save(orderTable);
            }

            @Test
            void 해당_주문_테이블을_반환한다() {
                final Optional<OrderTable> foundOrderTable = orderTableDao.findById(savedOrderTable.getId());

                assertAll(
                        () -> assertThat(foundOrderTable).isPresent(),
                        () -> assertThat(foundOrderTable.get()).usingRecursiveComparison()
                                .isEqualTo(savedOrderTable)
                );
            }
        }
    }

    @Nested
    class findAll_메서드는 {

        @Nested
        class 호출되면 {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);
            private final OrderTable orderTable = new OrderTable(null, 5, false);
            private OrderTable savedOrderTable;

            @BeforeEach
            void setUp() {
                final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
                orderTable.setTableGroupId(savedTableGroup.getId());
                savedOrderTable = orderTableDao.save(orderTable);
            }

            @Test
            void 모든_주문_테이블을_반환한다() {
                final List<OrderTable> orderTables = orderTableDao.findAll();

                assertThat(orderTables).usingFieldByFieldElementComparator()
                        .containsAll(List.of(savedOrderTable));
            }
        }
    }
}
