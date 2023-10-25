package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupRequest.OrderTableDto;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.OrderTableCannotBeGroupedException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.RequestOrderTableCountNotEnoughException;
import kitchenpos.table.exception.TableGroupNotFoundException;
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
            Order order = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now());
            em.persist(tableGroup);
            em.persist(orderTable1);
            em.persist(orderTable2);
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
