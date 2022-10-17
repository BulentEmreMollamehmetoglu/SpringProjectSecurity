package com.project.questapp.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "post")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    //Long userId; this line represents user table
    @ManyToOne(fetch = FetchType.EAGER) // fetch type lazy means just wait the object user and just get object post
    @JoinColumn(name = "user_id",nullable = false) // it connects the column user_id
    @OnDelete(action = OnDeleteAction.CASCADE) // if one user has deleted then all posted that related with that user is going to be deleted
    //@JsonIgnore
    User user;//a lot of posts have one user

    String title;
    @Lob
    @Column(columnDefinition = "text") // spring detect as a string if we don't write that it's going to be varchar
    String text;
}
