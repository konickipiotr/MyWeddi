package com.myweddi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.user.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser",roles={"HOST","GUEST"})
@Transactional
class RegistrationAPIControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;


    @Autowired
    public RegistrationAPIControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserAuthRepository userAuthRepository, HostRepository hostRepository, GuestRepository guestRepository) {

        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    @BeforeEach
    void setUp() {
        this.userAuthRepository.deleteAll();
        this.hostRepository.deleteAll();
        this.guestRepository.deleteAll();
    }

    @Test
    void create_host_account() throws Exception {
        RegistrationForm rf = new RegistrationForm();
        rf.setUsertype("HOST");
        rf.setUsername("user1");
        rf.setPassword("111");
        rf.setBridefirstname("Zosia");
        rf.setBridelastname("Samosia");
        rf.setBridephone("789513");
        rf.setBrideemail("zosia.samosia@xx.pl");
        rf.setGroomfirstname("Antoni");
        rf.setGroomlastname("Nowak");
        rf.setGroomphone("753845");
        rf.setGroomemail("anotini.nowek@xx.pl");


        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rf)))
                .andExpect(status().isOk());

        UserAuth userA = this.userAuthRepository.findByUsername(rf.getUsername());
        Host host = this.hostRepository.findById(userA.getId()).get();

        assertEquals(rf.getUsername(), userA.getUsername());
        assertEquals(rf.getBridefirstname(), host.getBridefirstname());
        assertEquals(rf.getBridelastname(), host.getBridelastname());
        assertEquals(rf.getBrideemail(), host.getBrideemail());
        assertEquals(rf.getBridephone(), host.getBridephone());
        assertEquals(rf.getGroomfirstname(), host.getGroomfirstname());
        assertEquals(rf.getGroomlastname(), host.getGroomlastname());
        assertEquals(rf.getGroomemail(), host.getGroomemail());
        assertEquals(rf.getGroomphone(), host.getGroomphone());
        assertEquals(userA.getId(), host.getId());
    }

    @Test
    void create_guest_account() throws Exception {
        RegistrationForm rf = new RegistrationForm();
        rf.setUsertype("HOST");
        rf.setUsername("user1@com");
        rf.setPassword("111");
        rf.setBridefirstname("Zosia");
        rf.setBridelastname("Samosia");
        rf.setBridephone("789513");
        rf.setBrideemail("zosia.samosia@xx.pl");
        rf.setGroomfirstname("Antoni");
        rf.setGroomlastname("Nowak");
        rf.setGroomphone("753845");
        rf.setGroomemail("anotini.nowek@xx.pl");


        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rf)))
                .andExpect(status().isOk());

        UserAuth userA = this.userAuthRepository.findByUsername(rf.getUsername());
        Host host = this.hostRepository.findById(userA.getId()).get();

        RegistrationForm rf2 = new RegistrationForm();
        rf2.setUsertype("GUSET");
        rf2.setUsername("user2@com");
        rf2.setPassword("111");
        rf2.setFirstname("Adam");
        rf2.setLastname("Nowak");
        rf2.setGender("M");
        rf2.setWeddingcode(host.getWeddingcode());


        mockMvc.perform(post("/api/registration/guest")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rf2)))
                .andExpect(status().isOk());

        UserAuth userA2 = this.userAuthRepository.findByUsername(rf2.getUsername());
        Guest guest = this.guestRepository.findById(userA2.getId()).get();


        assertEquals(rf2.getUsername(), userA2.getUsername());
        assertEquals(userA2.getId(), guest.getId());

        assertEquals(rf2.getFirstname(), guest.getFirstname());
        assertEquals(rf2.getLastname(), guest.getLastname());
        assertEquals(GuestStatus.NOTCONFIRMED, guest.getStatus());
        assertEquals(userA2.getUsername(), guest.getEmail());
    }

    @Test
    void guest_has_wrong_weddingcode() throws Exception {

        RegistrationForm rf2 = new RegistrationForm();
        rf2.setUsertype("GUSET");
        rf2.setUsername("user2");
        rf2.setPassword("111");
        rf2.setFirstname("Adam");
        rf2.setLastname("Nowak");
        rf2.setGender("M");
        rf2.setWeddingcode("fdfer");


        mockMvc.perform(post("/api/registration/guest")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rf2)))
                .andExpect(status().isNotFound());
    }
}