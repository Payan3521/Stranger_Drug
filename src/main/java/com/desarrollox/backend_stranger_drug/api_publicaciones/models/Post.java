package com.desarrollox.backend_stranger_drug.api_publicaciones.models;

import java.time.LocalDateTime;
import com.desarrollox.backend_stranger_drug.api_secciones.models.Section;
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
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "main_video_id", referencedColumnName = "id", nullable = false)
    private Video videoUrl;

    @ManyToOne
    @JoinColumn(name = "preview_video_id", referencedColumnName = "id", nullable = false)
    private Video previewUrl;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "duration_minutes", nullable = false)
    private int duration;

    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id", nullable = false)
    private Section section;

    @PrePersist
    protected void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}