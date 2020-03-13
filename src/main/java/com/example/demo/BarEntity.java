package com.example.demo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bar")
public class BarEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(targetEntity = FooEntity.class)
    @JoinColumn(name = "foo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bar_foo"))
    private FooEntity foo;

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

    public FooEntity getFoo() {
        return foo;
    }

    public void setFoo(FooEntity foo) {
        this.foo = foo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BarEntity)) return false;
        BarEntity barEntity = (BarEntity) o;
        return Objects.equals(id, barEntity.id) &&
                Objects.equals(name, barEntity.name) &&
                Objects.equals(foo, barEntity.foo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, foo);
    }
}
