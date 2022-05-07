package com.server.amething.domain.question;

import com.server.amething.domain.user.User;
import com.server.amething.domain.question.enum_type.QuestionType;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "question")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "question_description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType type;

}
