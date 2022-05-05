package com.server.amething.domain.question;

import com.server.amething.domain.member.Member;
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
    @JoinColumn(name = "owner_member_id")
    private Member ownerMember;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "visitor_member_id")
    private Member visitorMember;

    @Column(name = "question_description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType type;

}
