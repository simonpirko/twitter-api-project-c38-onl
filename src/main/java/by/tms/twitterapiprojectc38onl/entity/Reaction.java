package by.tms.twitterapiprojectc38onl.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"account_id", "post_id"})})
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Post post;
}
