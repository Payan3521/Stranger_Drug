package com.desarrollox.backend_stranger_drug.api_compras.controller;

import java.util.List;
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
    public ResponseEntity<Purchase> create(@Valid @RequestBody Purchase purchase){
        return null;
    }

    //Metodo para eliminar una compra
    //Util en vista de cliente, para eliminar una compra, si ya se aburrió
    //Elimina por completo de la base de datos
    @DeleteMapping("/{id}")
    public ResponseEntity<Purchase> delete(@PathVariable Long id){
        return null;
    }

    //Metodo para buscar una compra por su id
    @GetMapping("/{id}")
    public ResponseEntity<Purchase> findById(@PathVariable Long id){
        return null;
    }

    //Metodo para traer las compras de cierto comprador
    //Util en vista de cliente, para que vea sus compras y en bibioteca se conozca ello
    @GetMapping("/buyerUser/{id}")
    public ResponseEntity<List<Purchase>> findByBuyerUser(@PathVariable Long id){
        return null;
    }

    //Metodo para traer todas las compras sin importar el comprador
    //Util en vista de Administrador, para que vea todas sus ventas
    //Las compras deben de estar activas
    @GetMapping
    public ResponseEntity<List<Purchase>> findAll(){
        return null;
    }

    //Metodo para "Eliminar" las compras, hace soft delete
    //Util en vista de administrador cuando ya no quiere ver una compra en especifico
    //Solo desactiva la compra
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Purchase> softDelete(@PathVariable Long id){
        return null;
    }

    //Metodo para "Eliminar" todas las compras
    //Util en vista de Administrador, para limpiar la bandeja
    //Solo desactiva todas las compras
    @DeleteMapping("/clear")
    public ResponseEntity<Void> softDeleteAll(){
        return null;
    }

}