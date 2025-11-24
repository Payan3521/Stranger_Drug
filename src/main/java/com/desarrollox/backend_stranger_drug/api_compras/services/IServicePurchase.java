package com.desarrollox.backend_stranger_drug.api_compras.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;

public interface IServicePurchase {
    Purchase create(Purchase purchase);
    Optional<Purchase> delete(Long id);
    Optional<Purchase> findById(Long id);
    List<Purchase> findByBuyerUserId(Long buyerId);
    List<Purchase> findAll();
    Optional<Purchase> softDelete(Long id);
    Void clear();
}
