package com.myweddi.api.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.db.ChurchRepository;
import com.myweddi.model.ChurchInfo;
import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser",roles={"HOST","GUEST"})
@Transactional
class ChurchInfoControllerTest {


    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ChurchInfoAPIController churchInfoAPIController;
    private ChurchRepository churchRepository;
    private UserAuthRepository userAuthRepository;
    private UserAuth user;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ChurchInfoControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ChurchInfoAPIController churchInfoAPIController, ChurchRepository churchRepository, UserAuthRepository userAuthRepository,  PasswordEncoder passwordEncoder) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.churchInfoAPIController = churchInfoAPIController;
        this.churchRepository = churchRepository;
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        this.userAuthRepository.deleteAll();
        this.churchRepository.deleteAll();

        user = new UserAuth("user1", passwordEncoder.encode("123"), "HOST", com.myweddi.user.UserStatus.ACTIVE);
        this.userAuthRepository.save(user);
    }

    @Test
    void saveNewChurchInfo() throws Exception {

        assertEquals(0,this.churchRepository.findAll().size());
        ChurchInfo churchInfo = new ChurchInfo(user.getId());
        churchInfo.setName("Kościół świętego krzyża");
        churchInfo.setLatitude(23.44);
        churchInfo.setLongitude(34);


        mockMvc.perform(post("/api/churchinfo")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(churchInfo)))
                .andExpect(status().isOk());

        assertEquals(1,this.churchRepository.findAll().size());
    }

    @Test
    void updateWeddingInfo() throws Exception {

        assertEquals(0,this.churchRepository.findAll().size());

        ChurchInfo churchInfo = new ChurchInfo(user.getId());
        churchInfo.setName("Kościół świętego krzyża");
        churchInfo.setLatitude(23.44);
        churchInfo.setLongitude(34);
        churchInfo.setWebAppPath("/xx/zz/vv.jpg");
        churchInfo.setRealPath("/xx/zz/vv.jpg");
        this.churchRepository.save(churchInfo);

        ChurchInfo chi = this.churchRepository.findAll().get(0);

        assertEquals(churchInfo.getWeddingid(), chi.getWeddingid());
        assertEquals(churchInfo.getName(), chi.getName());
        assertEquals(churchInfo.getWebAppPath(), chi.getWebAppPath());
        assertEquals(churchInfo.getRealPath(), chi.getRealPath());
        assertEquals(1,this.churchRepository.findAll().size());

        ChurchInfo chi2 = new ChurchInfo(user.getId());
        chi2.setName("Kościół 2");
        chi2.setWebAppPath(null);
        chi2.setRealPath(null);

        mockMvc.perform(post("/api/churchinfo")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(chi2)))
                .andExpect(status().isOk());

        List<ChurchInfo> all = this.churchRepository.findAll();
        ChurchInfo chi3 = all.get(0);
        assertEquals(1, all.size());
        assertEquals(chi2.getName(), chi3.getName());
        assertEquals(churchInfo.getWebAppPath(), chi3.getWebAppPath());
        assertEquals(churchInfo.getRealPath(), chi3.getRealPath());
    }
}