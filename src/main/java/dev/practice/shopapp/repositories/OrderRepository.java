package dev.practice.shopapp.repositories;

import dev.practice.shopapp.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByReferenceCode(String referenceCode);

    List<OrderEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
