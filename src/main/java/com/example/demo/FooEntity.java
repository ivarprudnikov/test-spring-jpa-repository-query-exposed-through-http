package com.example.demo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "foo")
public class FooEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "foo")
    private Set<BarEntity> bars = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BarEntity> getBars() {
        return bars;
    }

    public void setBars(Set<BarEntity> bars) {
        this.bars = bars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FooEntity)) return false;
        FooEntity fooEntity = (FooEntity) o;
        return Objects.equals(id, fooEntity.id) &&
                Objects.equals(name, fooEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
