package com.walletsquire.apiservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "category")
@Entity
public class Category extends BaseEntity {

    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Getter
    @Setter
    private Category parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Category> children = new HashSet<>();

    @JsonIgnore
    public Set<Category> getChildren() {
        return children;
    }

}