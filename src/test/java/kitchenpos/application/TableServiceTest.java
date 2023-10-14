package kitchenpos.application;

import kitchenpos.application.dto.OrderTableEmptyRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fake.InMemoryOrderDao;
import kitchenpos.fake.InMemoryOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    private TableService tableService;
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    @BeforeEach
    void before() {
        orderDao = new InMemoryOrderDao();
        orderTableDao = new InMemoryOrderTableDao();
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTableRequest orderTable = new OrderTableRequest(10, false);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable).usingRecursiveComparison()
                    .ignoringFields("id", "tableGroupId")
                    .isEqualTo(orderTable);
            softly.assertThat(savedOrderTable.getId()).isNotNull();
            softly.assertThat(savedOrderTable.getTableGroupId()).isNull();
        });
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경한다() {
        // given
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 10, false));
        Order order = new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.of(2002, 3, 3, 3, 3));
        orderDao.save(order);

        // when
        OrderTable emptyTable = tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyRequest(true));

        // then
        assertThat(emptyTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경할_때_단체_지정이_있으면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableDao.save(new OrderTable(1L, 10, false));

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 주문_테이블을_빈_테이블로_변경할_때_주문_상태가_완료가_아니면_예외가_발생한다(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 10, false));
        Order order = new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.of(2002, 3, 3, 3, 3));
        orderDao.save(order);

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
