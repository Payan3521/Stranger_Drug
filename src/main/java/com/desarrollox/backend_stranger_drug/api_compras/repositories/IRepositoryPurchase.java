package com.desarrollox.backend_stranger_drug.api_compras.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;
import jakarta.transaction.Transactional;

@Repository
public interface IRepositoryPurchase extends JpaRepository<Purchase, Long>{

    List<Purchase> findByBuyerUser_IdAndStatusPurchaseClienteIsTrue(Long buyerId);

    List<Purchase> findByStatusPurchaseAdminIsTrue();

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE purchases SET status_purchase_cliente = false WHERE id = :id",
        nativeQuery = true
    )
    int softDeleteCliente(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE purchases SET status_purchase_admin = false WHERE id = :id",
        nativeQuery = true
    )
    int softDeleteAdmin(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE purchases SET status_purchase_admin = false",
        nativeQuery = true
    )
    int softDeleteAll();
    
}