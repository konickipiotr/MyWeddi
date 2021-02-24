package com.myweddi.user;

import com.myweddi.user.reposiotry.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Guest extends User {

    @Id
    @GeneratedValue
    private Long id;
    private Long weddingid;
    private String email;
    private Long partner;
}
