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
    @JoinColumn(name = "receiver_member_id")
    private Member receiverMember;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sent_member_id")
    private Member sentMember;

    @Column(name = "question_description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType type;

}
