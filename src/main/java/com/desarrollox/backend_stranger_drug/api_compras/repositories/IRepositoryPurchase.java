package com.desarrollox.backend_stranger_drug.api_compras.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;

@Repository
public interface IRepositoryPurchase extends JpaRepository<Purchase, Long>{
    
}