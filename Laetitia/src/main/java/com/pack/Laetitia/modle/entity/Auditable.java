package com.pack.Laetitia.modle.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pack.Laetitia.packManager.exceptio.ApiException;
import com.pack.Laetitia.packManager.domin.RequestContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {

    @Id
    @SequenceGenerator(name = "primary_key_seq", sequenceName = "primary_key_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_key_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    // Static generator for performance optimization
    private static final AlternativeJdkIdGenerator ID_GENERATOR = new AlternativeJdkIdGenerator();
    private String referenceId = ID_GENERATOR.generateId().toString();

    @NonNull
    private Long createdBy;

    @NonNull
    private Long updatedBy;

    @NonNull
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforePersist() {
        var userId = 0L;//RequestContext.getUserId();
//        if (userId == null) {
//            throw new ApiException("Cannot persist entity without user ID Request Context for this thread");
//        }
        setCreatedAt(now());
        setCreatedBy(userId);
        setUpdatedBy(userId);
        setUpdatedAt(now());
    }

    @PreUpdate
    public void beforeUpdate() {
        var userId = 0L;//RequestContext.getUserId();
//        if (userId == null) {
//            throw new ApiException("Cannot update entity without user ID Request Context for this thread");
//        }
        setUpdatedAt(now());
        setUpdatedBy(userId);
    }
}
