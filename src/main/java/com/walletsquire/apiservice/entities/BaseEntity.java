package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "updated_datetimestamp")
    @UpdateTimestamp
    private Date updatedDatetimestamp;

    @Column(name = "created_datetimestamp")
    @CreationTimestamp
    private Date createdDatetimestamp;

}