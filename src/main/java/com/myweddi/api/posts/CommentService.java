package com.myweddi.api.posts;

import com.myweddi.conf.Global;
import com.myweddi.db.CommentRepository;
import com.myweddi.model.Comment;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserAuthRepository userAuthRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserAuthRepository userAuthRepository) {
        this.commentRepository = commentRepository;
        this.userAuthRepository = userAuthRepository;
    }

    public ResponseEntity deleteComment(Long comId, String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        Comment comment = commentRepository.findById(comId).get();

        if(!user.getRole().equals("HOST") && !user.getId().equals(comment.getUserid()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        commentRepository.deleteById(comId);
        return new ResponseEntity(HttpStatus.OK);
    }

    public void addComment(Comment comment, String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        comment.setUserid(user.getId());
        comment.setCreationdate(LocalDateTime.now(Global.zid));
        this.commentRepository.save(comment);
    }

}
