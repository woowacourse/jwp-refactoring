package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.MenuSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MenuSnapshotRepository extends JpaRepository<MenuSnapshot, Long> {

    Optional<MenuSnapshot> findByMenuIdAndMenuUpdatedAt(Long menuId, LocalDateTime updatedAt);
}
