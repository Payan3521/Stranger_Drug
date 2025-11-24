package com.desarrollox.backend_stranger_drug.api_compras.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;
import com.desarrollox.backend_stranger_drug.api_compras.services.IServicePurchase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
public class ControllerPurchase {

    private final IServicePurchase servicePurchase;

    //Metodo para generar una nueva compra
    //Util en vista de cliente, para que pueda comprar
    @PostMapping
    public ResponseEntity<Purchase> create(@Valid @RequestBody PurchaseDto purchase){
        Purchase saved = servicePurchase.create(purchase);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    //Metodo para eliminar una compra
    //Util en vista de cliente, para eliminar una compra, si ya se aburrió
    //Desactiva para cliente esa compra
    @DeleteMapping("/soft-delete-cliente/{id}")
    public ResponseEntity<Purchase> softDeleteCliente(@PathVariable Long id){
        servicePurchase.softDeleteCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Metodo para buscar una compra por su id
    @GetMapping("/{id}")
    public ResponseEntity<Purchase> findById(@PathVariable Long id){
        Optional<Purchase> purchase = servicePurchase.findById(id);
        return new ResponseEntity<>(purchase.get(), HttpStatus.OK);
    }

    //Metodo para traer las compras de cierto comprador
    //Util en vista de cliente, para que vea sus compras y en bibioteca se conozca ello
    //Deben estar activas para cliente, no importa si esta desactivada para admin
    @GetMapping("/buyerUser/{id}")
    public ResponseEntity<List<Purchase>> findByBuyerUser(@PathVariable Long id){
        List<Purchase> purchaseList = servicePurchase.findByBuyerUserId(id);
        if(purchaseList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(purchaseList, HttpStatus.OK);
    }

    //Metodo para traer todas las compras sin importar el comprador
    //Util en vista de Administrador, para que vea todas sus ventas
    //Las compras deben de estar activas para administrador, no importa si estan desactivadas para cliente
    @GetMapping
    public ResponseEntity<List<Purchase>> findAll(){
        List<Purchase> purchaseList = servicePurchase.findAll();
        if(purchaseList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(purchaseList, HttpStatus.OK);
    }

    //Metodo para "Eliminar" las compras, hace soft delete
    //Util en vista de administrador cuando ya no quiere ver una compra en especifico
    //Solo desactiva la compra para admin
    @DeleteMapping("/soft-delete-admin/{id}")
    public ResponseEntity<Purchase> softDeleteAdmin(@PathVariable Long id){
        Optional<Purchase> purchase = servicePurchase.softDeleteAdmin(id);
        return new ResponseEntity<>(purchase.get(), HttpStatus.OK);
    }

    //Metodo para "Eliminar" todas las compras
    //Util en vista de Administrador, para limpiar la bandeja
    //Solo desactiva todas las compras para admin
    @DeleteMapping("/clear")
    public ResponseEntity<Void> softDeleteAll(){
        servicePurchase.clear();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}