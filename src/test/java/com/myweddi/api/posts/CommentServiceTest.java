package com.myweddi.api.posts;

import com.myweddi.db.CommentRepository;
import com.myweddi.db.LikeRepository;
import com.myweddi.db.PhotoRepository;
import com.myweddi.db.PostRepository;
import com.myweddi.model.Comment;
import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    private UserAuthRepository userAuthRepository;
    private CommentRepository commentRepository;
    private CommentService commentService;

    private UserAuth ua1 = new UserAuth("host1", "11", "HOST", UserStatus.ACTIVE);
    private UserAuth ua2= new UserAuth("guest1", "11", "GUEST", UserStatus.ACTIVE);
    private UserAuth ua3 = new UserAuth("host2", "11", "HOST", UserStatus.ACTIVE);
    private UserAuth ua4 = new UserAuth("guest2", "11", "GUEST", UserStatus.ACTIVE);

    @Autowired
    public CommentServiceTest(UserAuthRepository userAuthRepository, CommentRepository commentRepository, CommentService commentService) {
        this.userAuthRepository = userAuthRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @BeforeEach
    void setUp() {
        this.userAuthRepository.deleteAll();
        this.commentRepository.deleteAll();

        this.userAuthRepository.save(ua1);
        this.userAuthRepository.save(ua2);
        this.userAuthRepository.save(ua3);
        this.userAuthRepository.save(ua4);
    }

    @Test
    void addComment_method_should_prepare_comment() {
        Comment c = new Comment(1l, null, "elo elo", null);
        this.commentService.addComment(c, ua2.getUsername());

        List<Comment> all = this.commentRepository.findAll();
        Comment c2 = all.get(0);

        assertEquals(1l, c2.getPostid());
        assertEquals(ua2.getId(), c2.getUserid());
        assertTrue("elo elo".equals(c2.getContent()));
        assertNotNull(c2.getCreationdate());
    }

    @Test
    void test_method_deleteComment() {
        Comment c = new Comment(1l, null, "elo elo", null);
        this.commentService.addComment(c, ua2.getUsername());

        List<Comment> all = this.commentRepository.findAll();
        assertFalse(all.isEmpty());

        this.commentService.deleteComment(c.getId(), ua2.getUsername());

        all = this.commentRepository.findAll();
        assertTrue(all.isEmpty());
    }
}