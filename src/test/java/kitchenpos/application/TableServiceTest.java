package kitchenpos.application;

import static kitchenpos.test.fixture.OrderFixture.주문;
import static kitchenpos.test.fixture.TableFixture.테이블;
import static kitchenpos.test.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 테이블을_추가한다() {
        OrderTable orderTable = 테이블(null, 5, false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable.getId()).isNotNull();
            softly.assertThat(savedOrderTable.getTableGroupId()).isNull();
            softly.assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(5);
            softly.assertThat(savedOrderTable.isEmpty()).isFalse();
        });
    }

    @Nested
    class 테이블_목록_조회_시 {

        @Test
        void 모든_테이블_목록을_조회한다() {
            //given
            OrderTable orderTableA = 테이블(null, 5, false);
            OrderTable orderTableB = 테이블(null, 7, false);
            OrderTable savedOrderTableA = tableService.create(orderTableA);
            OrderTable savedOrderTableB = tableService.create(orderTableB);

            //when
            List<OrderTable> orderTables = tableService.list();

            //then
            assertThat(orderTables).usingRecursiveComparison().isEqualTo(List.of(savedOrderTableA, savedOrderTableB));
        }

        @Test
        void 테이블이_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<OrderTable> orderTables = tableService.list();

            //then
            assertThat(orderTables).isEmpty();
        }
    }

    @Nested
    class 테이블을_빈_테이블로_수정_시 {

        @Test
        void 정상적인_테이블이라면_테이블을_빈_테이블로_수정한다() {
            //given
            OrderTable orderTable = tableService.create(테이블(null, 5, false));
            OrderTable updateTable = 테이블(null, 0, true);

            //when
            OrderTable updatedTable = tableService.changeEmpty(orderTable.getId(), updateTable);

            //then
            assertSoftly(softly -> {
                softly.assertThat(updatedTable.getId()).isEqualTo(orderTable.getId());
                softly.assertThat(updatedTable.getTableGroupId()).isNull();
                softly.assertThat(updatedTable.isEmpty()).isTrue();
            });
        }

        @Test
        void 존재하지_않는_테이블이라면_예외를_던진다() {
            //given
            OrderTable updateTable = 테이블(null, 10, false);

            //when, then
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 수정_대상_테이블에_테이블_그룹이_존재하면_예외를_던진다() {
            //given
            TableGroup tableGroup = tableGroupDao.save(테이블_그룹(LocalDateTime.now(), Collections.emptyList()));
            OrderTable orderTable = orderTableDao.save(테이블(tableGroup.getId(), 5, false));
            OrderTable updateTable = 테이블(null, 10, false);

            //when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 조리중이거나_식사중인_테이블이라면_예외를_던진다(OrderStatus orderStatus) {
            //given
            OrderTable orderTable = tableService.create(테이블(null, 5, false));
            OrderTable updateTable = 테이블(null, 10, false);
            orderDao.save(주문(orderTable.getId(), orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));

            //when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_인원_수_수정_시 {

        @Test
        void 정상적인_테이블이라면_테이블_인원_수를_수정한다() {
            //given
            OrderTable orderTable = tableService.create(테이블(null, 5, false));
            OrderTable updateTable = 테이블(null, 10, false);

            //when
            OrderTable updatedTable = tableService.changeNumberOfGuests(orderTable.getId(), updateTable);

            //then
            assertSoftly(softly -> {
                softly.assertThat(updatedTable.getId()).isEqualTo(orderTable.getId());
                softly.assertThat(updatedTable.getTableGroupId()).isNull();
                softly.assertThat(updatedTable.getNumberOfGuests()).isEqualTo(updatedTable.getNumberOfGuests());
                softly.assertThat(updatedTable.isEmpty()).isFalse();
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1})
        void 수정되는_인원_수가_0보다_작으면_예외를_던진다(int numberOfGuests) {
            //given
            OrderTable orderTable = tableService.create(테이블(null, 5, false));
            OrderTable updateTable = 테이블(null, numberOfGuests, false);

            //when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외를_던진다() {
            //given
            OrderTable updateTable = 테이블(null, 10, false);

            //when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있으면_예외를_던진다() {
            //given
            OrderTable orderTable = tableService.create(테이블(null, 0, true));
            OrderTable updateTable = 테이블(null, 10, false);

            //when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
