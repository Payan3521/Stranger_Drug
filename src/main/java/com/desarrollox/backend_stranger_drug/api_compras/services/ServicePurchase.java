package com.desarrollox.backend_stranger_drug.api_compras.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_compras.controller.PurchaseDto;
import com.desarrollox.backend_stranger_drug.api_compras.exception.PurchaseNotFoundException;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;
import com.desarrollox.backend_stranger_drug.api_compras.repositories.IRepositoryPurchase;
import com.desarrollox.backend_stranger_drug.api_registro.exception.UserNotFoundException;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import com.desarrollox.backend_stranger_drug.api_registro.repositories.IRepositoryRegistro;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicePurchase implements IServicePurchase{

    private final IRepositoryPurchase repositoryPurchase;
    private final IRepositoryRegistro repositoryRegistro;

    @Override
    public Purchase create(PurchaseDto purchase) {

        if(!repositoryRegistro.existsById(purchase.getBuyerUserId())){
            throw new UserNotFoundException("El usuario con id: " + purchase.getBuyerUserId() + " No fue encontrado");
        }

        User buyerUser = repositoryRegistro.findById(purchase.getBuyerUserId()).get();

        Purchase compra = new Purchase(buyerUser, purchase.getVideoUrl(), purchase.getPricePaid());

        Purchase saved = repositoryPurchase.save(compra);

        return saved;
    }

    @Override
    public Optional<Purchase> softDeleteCliente(Long id) {
        Optional<Purchase> purchase = repositoryPurchase.findById(id);

        if (purchase.isEmpty()) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no fue encontrada");
        }

        int rows = repositoryPurchase.softDeleteCliente(id);

        if (rows == 0) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no pudo desactivarse");
        }

        purchase.get().setStatusPurchaseCliente(false);
        return purchase;
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        if (repositoryPurchase.existsById(id)) {
            return Optional.of(repositoryPurchase.findById(id).get());
        }else{
            throw new PurchaseNotFoundException("La compra con id: "+id+" no fue encontrada");
        }
    }

    @Override
    public List<Purchase> findAll() {
        return repositoryPurchase.findByStatusPurchaseAdminIsTrue();
    }

    @Override
    public List<Purchase> findByBuyerUserId(Long buyerId) {
        return repositoryPurchase.findByBuyerUser_IdAndStatusPurchaseClienteIsTrue(buyerId);
    }

    @Override
    public Optional<Purchase> softDeleteAdmin(Long id) {

        Optional<Purchase> purchase = repositoryPurchase.findById(id);

        if (purchase.isEmpty()) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no fue encontrada");
        }

        int rows = repositoryPurchase.softDeleteAdmin(id);

        if (rows == 0) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no pudo desactivarse");
        }

        purchase.get().setStatusPurchaseAdmin(false);
        return purchase;
    }

    @Override
    public Void clear() {
        repositoryPurchase.softDeleteAll();
        return null;
    }
}