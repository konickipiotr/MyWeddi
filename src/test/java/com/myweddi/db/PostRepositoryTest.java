package com.myweddi.db;

import com.myweddi.model.Post;
import com.myweddi.model.Posttype;
import com.myweddi.user.Guest;
import com.myweddi.user.GuestStatus;
import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    private PostRepository postRepository;
    private UserAuthRepository  userAuthRepository;
    private PasswordEncoder passwordEncoder;
    private GuestRepository guestRepository;

    private Long host1Id, host2Id, host3Id, guest1Id, guest2Id, guest3Id, guest4Id, guest5Id;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, GuestRepository guestRepository) {
        this.postRepository = postRepository;
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.guestRepository = guestRepository;
    }

    @BeforeEach
    void setUp() {
        this.postRepository.deleteAll();
        this.userAuthRepository.deleteAll();
        this.guestRepository.deleteAll();

        UserAuth ua11 = new UserAuth("host1", passwordEncoder.encode("11"), "HOST", UserStatus.ACTIVE);
        UserAuth ua12 = new UserAuth("host2", passwordEncoder.encode("11"), "HOST", UserStatus.ACTIVE);
        UserAuth ua13 = new UserAuth("host3", passwordEncoder.encode("11"), "HOST", UserStatus.ACTIVE);
        UserAuth ua2 = new UserAuth("guest1", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        UserAuth ua3 = new UserAuth("guest2", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        UserAuth ua4 = new UserAuth("guest3", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        UserAuth ua5 = new UserAuth("guest4", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        UserAuth ua6 = new UserAuth("guest5", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);

        this.userAuthRepository.saveAll(Arrays.asList(ua11, ua12, ua13, ua2, ua3, ua4, ua5, ua6));

        Guest g1 = new Guest(ua2.getId(), ua11.getId(), "jola.patola@xxx.pl", "Jola", "Patola", GuestStatus.CONFIRMED);
        Guest g2 = new Guest(ua3.getId(), ua11.getId(), "adam.nowak@xxx.pl", "Adam", "Nowak", GuestStatus.CONFIRMED);
        Guest g3 = new Guest(ua4.getId(), ua12.getId(), "zosia.samosia@xxx.pl", "Zosia", "Samosia", GuestStatus.CONFIRMED);
        Guest g4 = new Guest(ua5.getId(), ua12.getId(), "marian.kowalski@xxx.pl", "Marian", "Kowalski", GuestStatus.CONFIRMED);
        Guest g5 = new Guest(ua6.getId(), ua13.getId(), "halina.hubska@xxx.pl", "Halina", "Hubska", GuestStatus.CONFIRMED);

        this.guestRepository.saveAll(Arrays.asList(g1, g2, g3, g4, g5));

        host1Id = ua11.getId();
        host2Id = ua12.getId();
        host3Id = ua13.getId();

        guest1Id = g1.getId();
        guest2Id = g2.getId();
        guest3Id = g3.getId();
        guest4Id = g4.getId();
        guest5Id = g5.getId();
    }

    @Test
    public void find_last_post_from_current_wedding(){
        List<Post> result = postRepository.findByWeddingidOrderByCreationdateDesc(host1Id, PageRequest.of(0, 20));
        assertTrue(result.isEmpty());

        this.postRepository.save(new Post(host1Id, guest1Id, LocalDateTime.now(), "elo elo", Posttype.LOCAL));

        result = postRepository.findByWeddingidOrderByCreationdateDesc(host1Id, PageRequest.of(0, 20));
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(guest1Id, result.get(0).getUserid());
        assertEquals(host1Id, result.get(0).getWeddingid());
        assertEquals(Posttype.LOCAL, result.get(0).getPosttype());
        assertTrue(result.get(0).getCreationdate().isBefore(LocalDateTime.now()));
    }

    @Test
    public void findByWeddingidOrderByCreationdateDesc_order_is_ok(){

        this.postRepository.save(new Post(host1Id, guest1Id, LocalDateTime.of(2020, 04, 20, 4, 20), "two", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 3, 20), "one", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest1Id, LocalDateTime.of(2020, 04, 20, 4, 50), "four", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 4, 30), "three", Posttype.LOCAL));

        List<Post> result = postRepository.findByWeddingidOrderByCreationdateDesc(host1Id, PageRequest.of(0, 20));

        assertEquals(4, result.size());
        assertTrue("four".equals(result.get(0).getDescription()));
        assertTrue("three".equals(result.get(1).getDescription()));
        assertTrue("two".equals(result.get(2).getDescription()));
        assertTrue("one".equals(result.get(3).getDescription()));
    }

    @Test
    public void return_pagesize_num_of_elements(){

        int pagesize = 5;
        this.postRepository.save(new Post(host1Id, guest1Id, LocalDateTime.of(2020, 04, 20, 4, 20), "two", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 3, 20), "one", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest1Id, LocalDateTime.of(2020, 04, 20, 4, 50), "four", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 4, 30), "three", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 4, 30), "three", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 4, 30), "three", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 4, 30), "three", Posttype.LOCAL));

        List<Post> result = postRepository.findByWeddingidOrderByCreationdateDesc(host1Id, PageRequest.of(0, pagesize));
        List<Post> result2 = postRepository.findAll();

        assertEquals(pagesize, result.size());
        assertEquals(7, result2.size());
    }

    @Test
    public void local_posts_available_for_appropriate_users(){

        int pagesize = 20;
        this.postRepository.save(new Post(host1Id, guest1Id, LocalDateTime.of(2020, 04, 20, 4, 10), "two", Posttype.LOCAL));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 4, 20), "one", Posttype.LOCAL));
        this.postRepository.save(new Post(host2Id, guest3Id, LocalDateTime.of(2020, 04, 20, 4, 30), "four", Posttype.LOCAL));
        this.postRepository.save(new Post(host2Id, guest4Id, LocalDateTime.of(2020, 04, 20, 4, 40), "three", Posttype.LOCAL));
        this.postRepository.save(new Post(host3Id, guest5Id, LocalDateTime.of(2020, 04, 20, 4, 50), "three", Posttype.LOCAL));

        List<Post> result1 = postRepository.findByWeddingidOrderByCreationdateDesc(host1Id, PageRequest.of(0, pagesize));
        List<Post> result2 = postRepository.findByWeddingidOrderByCreationdateDesc(host2Id, PageRequest.of(0, pagesize));
        List<Post> result3 = postRepository.findByWeddingidOrderByCreationdateDesc(host3Id,  PageRequest.of(0, pagesize));

        assertEquals(2, result1.size());
        assertEquals(2, result2.size());
        assertEquals(1, result3.size());

        assertEquals(guest2Id, result1.get(0).getUserid());
        assertEquals(guest1Id, result1.get(1).getUserid());

        assertEquals(guest4Id, result2.get(0).getUserid());
        assertEquals(guest3Id, result2.get(1).getUserid());

        assertEquals(guest5Id, result3.get(0).getUserid());
    }

    @Test
    public void public_posts_available_for_all_hosts(){

        int pagesize = 20;
        this.postRepository.save(new Post(host1Id, guest1Id, LocalDateTime.of(2020, 04, 20, 4, 10), "one", Posttype.PUBLIC));
        this.postRepository.save(new Post(host1Id, guest2Id, LocalDateTime.of(2020, 04, 20, 4, 20), "two", Posttype.LOCAL));
        this.postRepository.save(new Post(host2Id, guest3Id, LocalDateTime.of(2020, 04, 20, 4, 30), "three", Posttype.PUBLIC));
        this.postRepository.save(new Post(host2Id, guest4Id, LocalDateTime.of(2020, 04, 20, 4, 40), "four", Posttype.LOCAL));
        this.postRepository.save(new Post(host3Id, guest5Id, LocalDateTime.of(2020, 04, 20, 4, 50), "five", Posttype.PUBLIC));

        List<Post> result1 = postRepository.findByPosttypeOrderByCreationdateDesc(Posttype.PUBLIC, PageRequest.of(0, pagesize));
        List<Post> result2 = postRepository.findByPosttypeOrderByCreationdateDesc(Posttype.LOCAL, PageRequest.of(0, pagesize));

        assertEquals(3, result1.size());
        assertEquals(2, result2.size());

        assertTrue("five".equals(result1.get(0).getDescription()));
        assertTrue("three".equals(result1.get(1).getDescription()));
        assertTrue("one".equals(result1.get(2).getDescription()));

        assertTrue("four".equals(result2.get(0).getDescription()));
        assertTrue("two".equals(result2.get(1).getDescription()));
    }
}