package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
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
import kitchenpos.table.exception.OrderTableCannotBeGroupedException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest.OrderTableDto;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.OrderTableNotFoundException;
import kitchenpos.tablegroup.exception.RequestOrderTableCountNotEnoughException;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import kitchenpos.tablegroup.exception.UnCompletedOrderExistsException;
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
class TableGroupServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private TableGroupService tableGroupService;


    @Nested
    class 단체_테이블_생성_테스트 {

        @Test
        void 단체_테이블을_생성한다() {
            // given
            OrderTable orderTable1 = new OrderTable(1, false);
            OrderTable orderTable2 = new OrderTable(2, false);
            em.persist(orderTable1);
            em.persist(orderTable2);
            em.flush();
            em.clear();
            TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableDto(orderTable1.getId()), new OrderTableDto(
                            orderTable2.getId())));

            // when, then
            TableGroupResponse response = tableGroupService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(response.getId()).isNotNull();
                assertThat(response.getOrderTables()).hasSize(2);
            });
        }

        @Test
        void 단체_테이블_생성_요청에_테이블_id가_포함되지_않은_경우_예외를_반환한다() {
            // given
            TableGroupRequest request = new TableGroupRequest(Collections.emptyList());

            // when, then
            assertThrows(RequestOrderTableCountNotEnoughException.class,
                    () -> tableGroupService.create(request));
        }

        @Test
        void 단체_테이블_생성_요청에_둘_미만의_테이블_id가_포함된_경우_예외를_반환한다() {
            // given
            TableGroupRequest request = new TableGroupRequest(List.of(new OrderTableDto(1L)));

            // when, then
            assertThrows(RequestOrderTableCountNotEnoughException.class,
                    () -> tableGroupService.create(request));
        }

        @Test
        void 단체로_지정될_테이블이_존재하지_않는_경우_예외를_반환한다() {
            // given
            TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableDto(-1L), new OrderTableDto(-2L)));

            // when, then
            assertThrows(OrderTableNotFoundException.class,
                    () -> tableGroupService.create(request));
        }

        @Test
        void 단체로_지정될_테이블이_빈_테이블인_경우_예외를_반환한다() {
            // given
            OrderTable orderTable1 = new OrderTable(0, true);
            OrderTable orderTable2 = new OrderTable(1, false);
            em.persist(orderTable1);
            em.persist(orderTable2);
            em.flush();
            em.clear();
            TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableDto(orderTable1.getId()), new OrderTableDto(
                            orderTable2.getId())));

            // when, then
            assertThrows(OrderTableCannotBeGroupedException.class,
                    () -> tableGroupService.create(request));
        }

        @Test
        void 단체로_지정될_테이블이_이미_단체_테이블에_소속된_경우_예외를_반환한다() {
            // given
            OrderTable orderTable1 = new OrderTable(1, false);
            OrderTable orderTable2 = new OrderTable(2, false);
            OrderTable orderTable3 = new OrderTable(3, false);
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());
            em.persist(orderTable1);
            em.persist(orderTable2);
            em.persist(orderTable3);
            em.persist(tableGroup);
            orderTable1.addToTableGroup(tableGroup.getId());
            orderTable2.addToTableGroup(tableGroup.getId());
            em.flush();
            em.clear();
            TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableDto(orderTable1.getId()), new OrderTableDto(
                            orderTable2.getId())));

            // when, then
            assertThrows(OrderTableCannotBeGroupedException.class,
                    () -> tableGroupService.create(request));
        }
    }

    @Nested
    class 단체_테이블_해제_테스트 {

        @Test
        void 단체_지정을_정상_해제한다() {
            // given
            OrderTable orderTable1 = new OrderTable(1, false);
            OrderTable orderTable2 = new OrderTable(2, false);
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());
            em.persist(orderTable1);
            em.persist(orderTable2);
            em.persist(tableGroup);
            em.flush();
            em.clear();

            // when, then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @Test
        void 지정을_해제하려는_단체_테이블이_존재하지_않는_경우_예외를_반환한다() {
            // when, then
            assertThrows(TableGroupNotFoundException.class, () -> tableGroupService.ungroup(1L));
        }

        @Test
        void 주문이_완료되지_않은_테이블의_단체_테이블을_해제하면_예외를_반환한다() {
            // given
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());
            OrderTable orderTable1 = new OrderTable(1, false);
            OrderTable orderTable2 = new OrderTable(2, false);
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            Product product = Product.of("product", new BigDecimal(2500));
            em.persist(menuGroup);
            em.persist(product);
            Menu menu = Menu.of("menu", new BigDecimal(10_000), menuGroup.getId(),
                    List.of(new ProductIdAndQuantity(product.getId(), 4L)));
            em.persist(menu);
            em.persist(tableGroup);
            em.persist(orderTable1);
            em.persist(orderTable2);
            Order order = Order.of(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                    List.of(new MenuIdQuantityAndPrice(menu.getId(), 1L, menu.getPrice())));
            em.persist(order);
            orderTable1.addToTableGroup(tableGroup.getId());
            orderTable2.addToTableGroup(tableGroup.getId());
            em.flush();
            em.clear();

            // when, then
            assertThrows(UnCompletedOrderExistsException.class, () -> tableGroupService.ungroup(
                    tableGroup.getId()));
        }
    }
}
