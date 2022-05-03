package com.server.amething.domain.answer;

import com.server.amething.domain.question.Question;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "answer")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "answer_description")
    private String description;
}

