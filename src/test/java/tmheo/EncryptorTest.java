package tmheo;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by taemyung on 2016. 10. 21..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class EncryptorTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void testPropertyEncrypt() throws Exception {

        // Given
        String user = "user";
        String password = "test";
        String clientId = "gog";

        // When
        String encryptedUser = stringEncryptor.encrypt(user);
        String encryptedPassword = stringEncryptor.encrypt(password);
        String encryptedClientId = stringEncryptor.encrypt(clientId);

        log.debug("encrypted user[{}] for user[{}]", encryptedUser, user);
        log.debug("encrypted password[{}] for password[{}]", encryptedPassword, password);
        log.debug("encrypted clientId[{}] for clientId[{}]", encryptedClientId, clientId);

        // Then
        assertThat(user, is(stringEncryptor.decrypt(encryptedUser)));
        assertThat(password, is(stringEncryptor.decrypt(encryptedPassword)));
        assertThat(clientId, is(stringEncryptor.decrypt(encryptedClientId)));

    }
}
