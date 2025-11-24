package com.desarrollox.backend_stranger_drug.api_compras.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_compras.controller.PurchaseDto;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;

public interface IServicePurchase {
    Purchase create(PurchaseDto purchase);
    Optional<Purchase> softDeleteCliente(Long id);
    Optional<Purchase> findById(Long id);
    List<Purchase> findByBuyerUserId(Long buyerId);
    List<Purchase> findAll();
    Optional<Purchase> softDeleteAdmin(Long id);
    Void clear();
}
