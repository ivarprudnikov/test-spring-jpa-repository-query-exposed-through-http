# About

[![Build Status](https://travis-ci.org/ivarprudnikov/test-spring-jpa-repository-query-exposed-through-http.svg?branch=master)](https://travis-ci.org/ivarprudnikov/test-spring-jpa-repository-query-exposed-through-http)

Example dealing with custom native query in JPA repository

Before running locally:
- Create MySQL database `mysql -h127.0.0.1 -uroot -e "drop database if exists demo; create database demo;"`

Run locally:
- To run locally `$ ./gradlew bootRun`
- Access results from custom JPA interface method `http://localhost:8080/foo/search/getByIdToProjected{?id}`

Run tests: `$ ./gradlew test`

## From stackoverflow response:

Question URL - https://stackoverflow.com/questions/60622337/using-arbitrary-query-as-projection-in-spring-data-rest-project

* custom JPA repository method using `@Query`
* selecting results in your `@Query`
* mapping `@Query` results to an interface
* exposing new repository method through `@RepositoryRestResource`

### Custom JPA repository method using `@Query`

As you have mentioned it is quite straightforward, just annotate a method with `@Query` and make sure your return type corresponds to what is being returned from the query, eg:

```java
public interface FooRepository extends JpaRepository<FooEntity, Long> {
    @Query(nativeQuery = true, value = "select f from foo f where f.name = :myParam")
    Optional<FooEntity> getInSomeAnotherWay(String myParam);
}
```

### Selecting results in your `@Query`

You have given an example already but I'll simplify to make it easier and shorter.

Given entities `FooEntity.java` and `BarEntity.java`:

```java
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
    
    // getter setters excluded for brevity
}

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

    // getter setters excluded for brevity
}
```

We want now to return custom result set which contains `FooEntity.name` and count of `FooEntity.bars`:

```
SELECT f.name as name, count(b.id) as barCount FROM foo f, bar b WHERE f.id = :id AND b.foo_id = :id


+-----------------+----------+
| name            | barCount |
+-----------------+----------+
| Jonny tables    | 1        |
+-----------------+----------+
```

### Mapping `@Query` results to an interface

To map above result set we need an interface where getters nicely reflect what is being selected:

```java
public interface ProjectedFooResult {
    String getName();
    Long getBarCount();
}
```

Now we can rewrite our repository method to:

```java
@Query(nativeQuery = true, 
    value = "SELECT f.name as name, count(b.id) as barCount FROM foo f, bar b WHERE f.id = :id AND b.foo_id = :id")
Optional<ProjectedFooResult> getByIdToProjected(Long id);
```

### Exposing new repository method through `@RepositoryRestResource`

I am not very familiar with this but after adding `org.springframework.data:spring-data-rest-hal-browser` dependency I got this nice interface that exposed available methods after repository was annotated with `@RepositoryRestResource`. For a given repository which contains above mentioned details:

```java
@RepositoryRestResource(path = "foo")
public interface FooRepository extends JpaRepository<FooEntity, Long> {
    @Query(nativeQuery = true, value = "SELECT f.name as name, count(b.id) as barCount FROM foo f, bar b WHERE f.id = :id AND b.foo_id = :id")
    Optional<ProjectedFooResult> getByIdToProjected(Long id);
}
```

the method will be exposed through `http://localhost:8080/foo/search/getByIdToProjected?id=1` when running locally.
