package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.SpringServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest {

    @Nested
    class create_메소드는 {

        @Nested
        class 주문테이블이_비어있는_경우 extends SpringServiceTest {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블 2개 이상인 경우 단체 지정이 가능합니다.");
            }
        }

        @Nested
        class 주문테이블이_1개인_경우 extends SpringServiceTest {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                    createOrderTables(new OrderTable(1, true)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블 2개 이상인 경우 단체 지정이 가능합니다.");
            }
        }

        @Nested
        class 주문테이블이_실제_저장된_주문테이블이_아닌_경우 extends SpringServiceTest {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                    createOrderTables(
                            new OrderTable(0L, null, 1, true),
                            new OrderTable(1L, null, 0, true)
                    ));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("실제 주문 테이블 정보와 일치하지 않습니다.");
            }
        }

        @Nested
        class 주문테이블이_비어있지않는_경우 extends SpringServiceTest {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                    createOrderTables(
                            new OrderTable(1L, null, 0, true),
                            new OrderTable(2L, null, 0, false)
                    ));

            @BeforeEach
            void setUp() {
                orderTableDao.save(new OrderTable(2L, null, 0, false));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 비어있지 않거나 이미 단체지정되어있습니다.");
            }
        }

        @Nested
        class 주문테이블이_이미_단체지정이_된_경우 extends SpringServiceTest {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                    createOrderTables(
                            new OrderTable(1L, null, 0, false),
                            new OrderTable(2L, null, 0, false)
                    ));

            @BeforeEach
            void setUp() {
                Long tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()))
                        .getId();
                orderTableDao.save(new OrderTable(2L, tableGroupId, 0, false));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 비어있지 않거나 이미 단체지정되어있습니다.");
            }
        }

        private List<OrderTable> createOrderTables(final OrderTable... requestOrderTables) {
            return Arrays.stream(requestOrderTables)
                    .collect(Collectors.toList());
        }
    }
}
