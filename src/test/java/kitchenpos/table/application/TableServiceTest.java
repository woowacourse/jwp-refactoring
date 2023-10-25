package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menu.ProductIdAndQuantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.MenuIdQuantityAndPrice;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.CannotChangeEmptyTableNumberOfGuestsException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.RequestOrderTableCountNotEnoughException;
import kitchenpos.table.exception.UnCompletedOrderExistsException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private TableService tableService;

    @Test
    void 테이블을_정상_생성한다() {
        // given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTableCreateRequest request = new OrderTableCreateRequest(numberOfGuests, empty);

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getTableGroupId()).isNull();
            assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
            assertThat(response.getEmpty()).isEqualTo(empty);
        });
    }

    @Test
    void 테이블을_전체_조회한다() {
        assertThat(tableService.list()).isInstanceOf(List.class);
    }

    @Nested
    class 비었는지_여부_변경_기능_테스트 {

        @Test
        void 테이블의_비었는지_여부를_변경한다() {
            // given
            OrderTable orderTable = new OrderTable(1, true);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // when
            OrderTableResponse response = tableService.changeEmpty(orderTable.getId(), request);

            // then
            assertThat(response.getEmpty()).isFalse();
        }

        @Test
        void 변경하려는_테이블_id가_존재하지_않으면_예외를_반환한다() {
            // given
            OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // when, then
            assertThrows(OrderTableNotFoundException.class,
                    () -> tableService.changeEmpty(-1L, request));
        }

        @Test
        void 변경하려는_테이블의_주문_상태가_변경_불가하면_예외를_반환한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            Product product = Product.of("product", new BigDecimal(2500));
            em.persist(menuGroup);
            em.persist(product);
            OrderTable orderTable = new OrderTable(4, false);
            em.persist(orderTable);
            Menu menu = Menu.of("menu", new BigDecimal(10_000), menuGroup.getId(),
                    List.of(new ProductIdAndQuantity(product.getId(), 4L)));
            em.persist(menu);
            Order order = Order.of(orderTable, OrderStatus.COOKING, LocalDateTime.now(),
                    List.of(new MenuIdQuantityAndPrice(menu.getId(), 1L, menu.getPrice())));
            em.persist(order);
            em.flush();
            em.clear();
            OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

            // when, then
            assertThrows(UnCompletedOrderExistsException.class,
                    () -> tableService.changeEmpty(orderTable.getId(), request));
        }
    }

    @Nested
    class 방문한_손님_수_변경_기능_테스트 {

        @Test
        void 방문한_손님_수를_변경한다() {
            // given
            int newNumberOfGuests = 4;
            OrderTable orderTable = new OrderTable(1, false);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(
                    newNumberOfGuests);

            // when
            OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(),
                    request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(response.getId()).isEqualTo(orderTable.getId());
                assertThat(response.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
            });
        }

        @Test
        void 손님_수가_음수인_경우_예외를_반환한다() {
            // given
            OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(
                    -1);

            // when, then
            assertThrows(RequestOrderTableCountNotEnoughException.class,
                    () -> tableService.changeNumberOfGuests(1L, request));
        }

        @Test
        void 손님_수를_변경하려는_테이블이_빈_테이블인_경우_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(0, true);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(
                    1);

            // when, then
            assertThrows(CannotChangeEmptyTableNumberOfGuestsException.class,
                    () -> tableService.changeNumberOfGuests(orderTable.getId(), request));
        }
    }
}
