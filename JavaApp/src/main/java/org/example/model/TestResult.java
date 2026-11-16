package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Table(name = "test_results")
@Builder
@NamedEntityGraph(
    name = "testResultWithTestAndCourse",
    attributeNodes = @NamedAttributeNode(
        value = "test",
        subgraph = "testWithCourse"
    ),
    subgraphs = {
        @NamedSubgraph(
            name = "testWithCourse",
            attributeNodes = @NamedAttributeNode("course")
        )
    }
)
public class TestResult {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "correct_answers")
    private int correctAnswers;

    @Column(name = "total_question_count")
    private int totalQuestions;

    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;

    @CreationTimestamp
    @Column(name = "passed_date")
    private LocalDateTime passedDate;

    @Column(name = "is_passed")
    private boolean isPassed;
}
