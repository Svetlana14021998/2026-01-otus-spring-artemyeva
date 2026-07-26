package org.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.CourseType;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "materials", "author", "test"})
@Table(name = "courses")
@Builder
@NamedEntityGraph(
    name = "courseWithMaterials",
    attributeNodes = @NamedAttributeNode("materials")
)
@NamedEntityGraph(
    name = "courseWithAuthor",
    attributeNodes = @NamedAttributeNode("author")
)
@NamedEntityGraph(
    name = "courseWithMaterialsAndAuthor",
    attributeNodes = {
        @NamedAttributeNode("materials"),
        @NamedAttributeNode("author")
    }
)
public class Course {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "course_type")
    private CourseType courseType;

    @Column(name = "description")
    private String description;

    @OneToMany(orphanRemoval = true,
        fetch = FetchType.LAZY,
        mappedBy = "course",
        cascade = CascadeType.ALL)
    private List<TheoryMaterial> materials = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private AppUser author;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", unique = true)
    private Test test;
}
