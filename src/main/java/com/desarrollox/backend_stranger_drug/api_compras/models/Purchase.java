package com.desarrollox.backend_stranger_drug.api_compras.models;

import java.time.LocalDateTime;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import com.desarrollox.backend_stranger_drug.api_videos.models.Video;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_user_id", referencedColumnName = "id")
    private User buyerUser;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "id", nullable = false)
    private Video videoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "price_paid", nullable = false)
    private double pricePaid;

    @Column(name = "status_purchase_admin", nullable = false)
    private boolean statusPurchaseAdmin;

    @Column(name = "status_purchase_cliente", nullable = false)
    private boolean statusPurchaseCliente;

    public Purchase(User buyerUser2, Video videoUrl2, double pricePaid2) {
        this.buyerUser=buyerUser2;
        this.videoUrl=videoUrl2;
        this.pricePaid=pricePaid2;
    }

    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        statusPurchaseAdmin=true;
        statusPurchaseCliente=true;
    }

}