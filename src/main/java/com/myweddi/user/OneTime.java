package com.myweddi.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class OneTime {

    @Id
    private Long userid;
    private String password;

    public OneTime(Long userid, String password) {
        this.userid = userid;
        this.password = password;
    }
}
