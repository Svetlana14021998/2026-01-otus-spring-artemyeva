package org.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "questions", "course"})
@Table(name = "tests")
@Builder
@NamedEntityGraph(
    name = "testWithQuestionsAndAnswers",
    attributeNodes = @NamedAttributeNode(
        value = "questions",
        subgraph = "questionsWithAnswers"
    ),
    subgraphs = {
        @NamedSubgraph(
            name = "questionsWithAnswers",
            attributeNodes = @NamedAttributeNode("answers")
        )
    }
)

@NamedEntityGraph(
    name = "testWithQuestionsCourseAndAuthor",
    attributeNodes = {
        @NamedAttributeNode("questions"),
        @NamedAttributeNode(value = "course", subgraph = "courseWithAuthor")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "courseWithAuthor",
            attributeNodes = @NamedAttributeNode("author")
        )
    }
)
@NamedEntityGraph(
    name = "testWithAuthor",
    attributeNodes = {
        @NamedAttributeNode(value = "course", subgraph = "courseWithAuthor")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "courseWithAuthor",
            attributeNodes = @NamedAttributeNode("author")
        )
    }
)
public class Test {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "need_correct_answer_count")
    private int needCorrectAnswerCount;

    @OneToMany(orphanRemoval = true,
        fetch = FetchType.LAZY,
        mappedBy = "test",
        cascade = CascadeType.ALL)
    List<Question> questions = new ArrayList<>();

    @OneToOne(mappedBy = "test")
    private Course course;

    public Map<Long, List<Long>> getCorrectAnswersMap() {
        Map<Long, List<Long>> result = new HashMap<>();

        for (Question question : questions) {
            List<Long> correctAnswerIds = question.getAnswers().stream()
                .filter(Answer::isCorrect)
                .map(Answer::getId)
                .collect(Collectors.toList());

            if (!correctAnswerIds.isEmpty()) {
                result.put(question.getId(), correctAnswerIds);
            }
        }

        return result;
    }
}
