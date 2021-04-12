package com.myweddi.api.posts;

import com.myweddi.db.CommentRepository;
import com.myweddi.db.LikeRepository;
import com.myweddi.db.PhotoRepository;
import com.myweddi.db.PostRepository;
import com.myweddi.exception.ForbiddenException;
import com.myweddi.model.*;
import com.myweddi.user.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import com.myweddi.view.CommentView;
import com.myweddi.view.PostView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    private PostService postService;
    private UserAuthRepository userAuthRepository;
    private LikeRepository likeRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private PhotoRepository photoRepository;

    private UserAuth ua1 = new UserAuth("host1", "11", "HOST", UserStatus.ACTIVE);
    private UserAuth ua2= new UserAuth("guest1", "11", "GUEST", UserStatus.ACTIVE);
    private UserAuth ua22= new UserAuth("guest11", "11", "GUEST", UserStatus.ACTIVE);
    private UserAuth ua3 = new UserAuth("host2", "11", "HOST", UserStatus.ACTIVE);
    private UserAuth ua4 = new UserAuth("guest2", "11", "GUEST", UserStatus.ACTIVE);

    @Autowired
    public PostServiceTest(PostService postService, UserAuthRepository userAuthRepository, LikeRepository likeRepository, PostRepository postRepository, CommentRepository commentRepository, HostRepository hostRepository, GuestRepository guestRepository, PhotoRepository photoRepository) {
        this.postService = postService;
        this.userAuthRepository = userAuthRepository;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.photoRepository = photoRepository;
    }

    @BeforeEach
    void setUp() {
        this.userAuthRepository.deleteAll();
        this.likeRepository.deleteAll();
        this.postRepository.deleteAll();
        this.commentRepository.deleteAll();
        this.guestRepository.deleteAll();
        this.hostRepository.deleteAll();
        this.photoRepository.deleteAll();

        this.userAuthRepository.save(ua1);
        this.userAuthRepository.save(ua2);
        this.userAuthRepository.save(ua22);
        this.userAuthRepository.save(ua3);
        this.userAuthRepository.save(ua4);

        Host h1 = new Host();
        Host h2 = new Host();
        Guest g1 = new Guest(ua2.getId(), ua1.getId(), "jola.patola@xxx.pl", "Jola", "Patola", "GUEST", GuestStatus.CONFIRMED);
        Guest g11 = new Guest(ua22.getId(), ua1.getId(), "mariola.patola@xxx.pl", "Mariola", "Patola", "GUEST", GuestStatus.CONFIRMED);
        Guest g2 = new Guest(ua4.getId(), ua3.getId(), "adam.nowak@xxx.pl", "Adam", "Nowak", "GUEST", GuestStatus.CONFIRMED);
        h1.setId(ua1.getId());
        h2.setId(ua3.getId());
        this.hostRepository.save(h1);
        this.hostRepository.save(h2);
        this.guestRepository.save(g1);
        this.guestRepository.save(g11);
        this.guestRepository.save(g2);
    }

    @Test
    void check_method_newPostInDb() {
        List<Post> all = this.postRepository.findAll();
        assertTrue(all.isEmpty());

        this.postService.newPostInDb(new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.LOCAL), ua2.getUsername());

        all = this.postRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());

        this.postService.newPostInDb(new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.LOCAL), ua2.getUsername());
        all = this.postRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(2, all.size());
    }

    @Test
    void user_try_to_get_private_a_post_from_other_wedding() {
        Post post = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.LOCAL);
        this.postRepository.save(post);
        assertThrows(ForbiddenException.class, ()-> this.postService.getPost(post.getId(), ua4.getUsername()));
        assertThrows(ForbiddenException.class, ()-> this.postService.getPost(post.getId(), ua3.getUsername()));

        assertEquals(HttpStatus.OK, this.postService.getPost(post.getId(), ua1.getUsername()).getStatusCode());
        assertEquals(HttpStatus.OK, this.postService.getPost(post.getId(), ua2.getUsername()).getStatusCode());
    }

    @Test
    void guests_cannot_get_public_post_hosts_can() {
        Post post = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.PUBLIC);
        this.postRepository.save(post);
        assertThrows(ForbiddenException.class, ()-> this.postService.getPost(post.getId(), ua2.getUsername()));
        assertThrows(ForbiddenException.class, ()-> this.postService.getPost(post.getId(), ua4.getUsername()));

        assertEquals(HttpStatus.OK, this.postService.getPost(post.getId(), ua1.getUsername()).getStatusCode());
        assertEquals(HttpStatus.OK, this.postService.getPost(post.getId(), ua3.getUsername()).getStatusCode());
    }

    @Test
    void wedding_member_get_wedding_post() {
        Post post = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.LOCAL);
        this.postRepository.save(post);
        ResponseEntity<PostView> postview_e = postService.getPost(post.getId(), ua2.getUsername());
        assertEquals(HttpStatus.OK, postview_e.getStatusCode());

        PostView postview = postview_e.getBody();

        assertEquals(ua1.getId(), postview.getWeddingid());
        assertEquals(ua2.getId(), postview.getUserid());
        assertTrue("bla bla".equals(postview.getDescription()));
        assertTrue(postview.getComments().isEmpty());
    }

    @Test
    void get_post_with_its_comments() {
        Post post = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.LOCAL);
        this.postRepository.save(post);
        Comment c1 = new Comment(post.getId(), ua2.getId(), "elo elo", LocalDateTime.of(2020, 01, 01,01,20));
        Comment c2 = new Comment(post.getId(), ua2.getId(), "elo elo", LocalDateTime.of(2020, 01, 01,01,10));
        Comment c3 = new Comment(post.getId(), ua2.getId(), "elo elo", LocalDateTime.of(2020, 01, 01,01,15));
        this.commentRepository.saveAll(Arrays.asList(c1, c2, c3));

        ResponseEntity<PostView> postview_e = postService.getPost(post.getId(), ua2.getUsername());
        assertEquals(HttpStatus.OK, postview_e.getStatusCode());

        PostView postview = postview_e.getBody();
        List<CommentView> comments = postview.getComments();
        assertFalse(comments.isEmpty());

        assertEquals(c2.getId(), comments.get(0).getId());
        assertEquals(c3.getId(), comments.get(1).getId());
        assertEquals(c1.getId(), comments.get(2).getId());
    }

    @Test
    void is_post_created_by_user() {
        Post post = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.LOCAL);
        this.postRepository.save(post);

        ResponseEntity<PostView> postview_e = postService.getPost(post.getId(), ua2.getUsername());
        PostView postview = postview_e.getBody();
        assertTrue(postview.isMyPost());

        postview_e = postService.getPost(post.getId(), ua22.getUsername());
        postview = postview_e.getBody();
        assertFalse(postview.isMyPost());
    }

    @Test
    void deletePost_should_delete_post_photos_and_all_comment() {

        assertTrue(this.postRepository.findAll().isEmpty());
        assertTrue(this.commentRepository.findAll().isEmpty());
        assertTrue(this.photoRepository.findAll().isEmpty());

        Post post = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "post1", Posttype.LOCAL);
        Post post2 = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "post2", Posttype.LOCAL);
        this.postRepository.save(post);
        this.postRepository.save(post2);
        Comment c1 = new Comment(post.getId(), ua2.getId(), "elo elo", LocalDateTime.of(2020, 01, 01,01,20));
        Comment c2 = new Comment(post.getId(), ua2.getId(), "elo elo", LocalDateTime.of(2020, 01, 01,01,10));
        Comment c3 = new Comment(post.getId(), ua2.getId(), "elo elo", LocalDateTime.of(2020, 01, 01,01,15));
        Comment c4 = new Comment(post2.getId(), ua2.getId(), "elo elo", LocalDateTime.of(2020, 01, 01,01,15));
        this.commentRepository.saveAll(Arrays.asList(c1, c2, c3, c4));

        Photo p1 = new Photo(post.getId(), ua2.getId());
        Photo p2 = new Photo(post.getId(), ua2.getId());
        Photo p3 = new Photo(post2.getId(), ua3.getId());
        this.photoRepository.saveAll(Arrays.asList(p1, p2, p3));


        assertEquals(2, this.postRepository.findAll().size());
        assertEquals(4, this.commentRepository.findAll().size());
        assertEquals(3, this.photoRepository.findAll().size());

        postService.deletePost(ua2.getUsername(), post.getId());

        assertFalse(this.postRepository.existsById(post.getId()));

        assertEquals(1, this.postRepository.findAll().size());
        assertEquals(1, this.commentRepository.findAll().size());
        assertEquals(1, this.photoRepository.findAll().size());

        assertEquals(post2.getId(), this.postRepository.findAll().get(0).getId());
        assertEquals(c4.getId(), this.commentRepository.findAll().get(0).getId());
        assertEquals(p3.getId(), this.photoRepository.findAll().get(0).getId());
    }

    @Test
    void changePostStar() {
        Post post = new Post(ua1.getId(), ua2.getId(), LocalDateTime.now(), "bla bla", Posttype.LOCAL);
        this.postRepository.save(post);
        ResponseEntity<PostView> postview_e = postService.getPost(post.getId(), ua2.getUsername());
        assertEquals(HttpStatus.OK, postview_e.getStatusCode());
        PostView postview = postview_e.getBody();

        assertEquals(ua1.getId(), postview.getWeddingid());
        assertEquals(ua2.getId(), postview.getUserid());
        assertFalse(postview.isWeddiLike());
        assertEquals(0, postview.getLikeNumber());

        WeddiLike like = new WeddiLike(post.getId(), ua2.getId());
        postService.changePostStar(like);

        postview_e = postService.getPost(post.getId(), ua2.getUsername());
        assertEquals(HttpStatus.OK, postview_e.getStatusCode());
        postview = postview_e.getBody();

        assertEquals(ua1.getId(), postview.getWeddingid());
        assertEquals(ua2.getId(), postview.getUserid());
        assertTrue(postview.isWeddiLike());
        assertEquals(1, postview.getLikeNumber());

        postService.changePostStar(like);

        postview_e = postService.getPost(post.getId(), ua2.getUsername());
        assertEquals(HttpStatus.OK, postview_e.getStatusCode());
        postview = postview_e.getBody();

        assertEquals(ua1.getId(), postview.getWeddingid());
        assertEquals(ua2.getId(), postview.getUserid());
        assertFalse(postview.isWeddiLike());
        assertEquals(0, postview.getLikeNumber());
    }

    @Test
    void get_public_postList() {
        Post post1 = new Post(ua1.getId(), ua1.getId(), LocalDateTime.of(2020, 01,01,01,30), "host1 private", Posttype.LOCAL);
        Post post2 = new Post(0l, ua1.getId(), LocalDateTime.of(2020, 01,01,01,10), "host1 public", Posttype.PUBLIC);
        Post post3 = new Post(ua1.getId(), ua2.getId(), LocalDateTime.of(2020, 01,01,01,40), "guest1 private", Posttype.LOCAL);
        Post post4 = new Post(ua3.getId(), ua3.getId(), LocalDateTime.of(2020, 01,01,02,30), "host2 private", Posttype.LOCAL);
        Post post5 = new Post(0l, ua3.getId(), LocalDateTime.of(2020, 01,01,02,10), "host2 public", Posttype.PUBLIC);
        Post post6 = new Post(ua3.getId(), ua4.getId(), LocalDateTime.of(2020, 01,01,02,30), "guest3 private", Posttype.LOCAL);
        this.postRepository.saveAll(Arrays.asList(post1, post2, post3, post4, post5, post6));

        ResponseEntity<ListWrapper<PostView>> listWrapperEntity = postService.getLastPublicPosts(1, ua1.getUsername());

        List<PostView> posts = listWrapperEntity.getBody().getList();
        assertEquals(2, posts.size());
        assertEquals(post5.getId(), posts.get(0).getId());
        assertEquals(post2.getId(), posts.get(1).getId());

        assertThrows(ForbiddenException.class, ()-> this.postService.getLastPublicPosts(1, ua2.getUsername()));
    }

    @Test
    void get_postList_from_my_wedding() {
        Post post1 = new Post(ua1.getId(), ua1.getId(), LocalDateTime.of(2020, 01,01,01,30), "host1 private", Posttype.LOCAL);
        Post post2 = new Post(0l, ua1.getId(), LocalDateTime.of(2020, 01,01,01,10), "host1 public", Posttype.PUBLIC);
        Post post3 = new Post(ua1.getId(), ua2.getId(), LocalDateTime.of(2020, 01,01,01,40), "guest1 private", Posttype.LOCAL);
        Post post4 = new Post(ua3.getId(), ua3.getId(), LocalDateTime.of(2020, 01,01,02,30), "host2 private", Posttype.LOCAL);
        Post post5 = new Post(0l, ua3.getId(), LocalDateTime.of(2020, 01,01,02,10), "host2 public", Posttype.PUBLIC);
        Post post6 = new Post(ua3.getId(), ua4.getId(), LocalDateTime.of(2020, 01,01,02,30), "guest3 private", Posttype.LOCAL);
        this.postRepository.saveAll(Arrays.asList(post1, post2, post3, post4, post5, post6));

        ListWrapper<PostView> listWrapper = postService.getLastWeddingPosts(1, ua1.getUsername());
        List<PostView> posts = listWrapper.getList();
        assertEquals(2, posts.size());
        assertEquals(post3.getId(), posts.get(0).getId());
        assertEquals(post1.getId(), posts.get(1).getId());
    }
}
