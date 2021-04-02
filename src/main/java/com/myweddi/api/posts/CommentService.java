package com.myweddi.api.posts;

import com.myweddi.conf.Global;
import com.myweddi.db.CommentRepository;
import com.myweddi.model.Comment;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean deleteComment(Principal principal, Long comId){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Comment comment = commentRepository.findById(comId).get();

        if(!user.getId().equals(comment.getUserid()))
            return false;

        commentRepository.deleteById(comId);
        return true;
    }

    public void addComment(Comment comment){
        comment.setCreationdate(LocalDateTime.now(Global.zid));
        this.commentRepository.save(comment);
    }

}
