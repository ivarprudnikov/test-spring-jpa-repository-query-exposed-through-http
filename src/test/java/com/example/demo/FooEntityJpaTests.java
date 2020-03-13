package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class FooEntityJpaTests {

    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    FooRepository fooRepository;
    @Autowired
    BarRepository barRepository;

    FooEntity testFoo;

    @BeforeEach
    void setup() {
        FooEntity foo = new FooEntity();
        foo.setName("foo");
        testFoo = testEntityManager.persist(foo);

        BarEntity bar = new BarEntity();
        bar.setName("bar");
        bar.setFoo(testFoo);
        testEntityManager.persist(bar);
    }

    @Test
    void equals_and_hashcode_present() {
        FooEntity foo1 = new FooEntity();
        foo1.setId(1L);
        foo1.setName("x");

        FooEntity foo1Clone = new FooEntity();
        foo1Clone.setId(1L);
        foo1Clone.setName("x");

        FooEntity foo2 = new FooEntity();
        foo2.setId(2L);
        foo2.setName("y");

        assertThat("equal", foo1.equals(foo1Clone));
        assertThat("Hashcode is based on data props", foo1.hashCode() == foo1Clone.hashCode());
        assertThat("Different props - different instances", !foo1.equals(foo2));
        assertThat("Different props - different hashcode", foo1.hashCode() != foo2.hashCode());
    }

    @Test
    void repository_finds_by_id() {
        Optional<FooEntity> f = fooRepository.findById(testFoo.getId());
        assertThat("exists", f.isPresent());
    }

    @Test
    void uses_custom_projection() {
        Optional<ProjectedFooResult> f = fooRepository.getByIdToProjected(testFoo.getId());
        assertThat("exists", f.isPresent());
        assertThat("name matches", f.get().getName().equals("foo"));
        assertThat("bars found", f.get().getBarCount() == 1);
    }
}
