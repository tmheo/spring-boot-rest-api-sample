package tmheo;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import tmheo.cache.MultiCaffeineCacheManager;
import tmheo.entity.Person;
import tmheo.repository.PersonRepository;

import java.util.Arrays;

@SpringBootApplication
@EnableCaching
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PersonRepository personRepository) {
        return args -> {
            personRepository.save(new Person("first name1", "last name1", "test1@test.com"));
            personRepository.save(new Person("first name2", "last name2", "test2@test.com"));
            personRepository.save(new Person("first name3", "last name3", "test3@test.com"));
        };
    }

    @Bean
    public CacheManager cacheManager() {
        MultiCaffeineCacheManager cacheManager = new MultiCaffeineCacheManager();

        cacheManager.setCacheNames(Arrays.asList("person, personList"));
        cacheManager.setCacheSpecification("person", "maximumSize=100,expireAfterWrite=1m");
        cacheManager.setCacheSpecification("personList", "maximumSize=100,expireAfterAccess=20s");

        log.debug("caffeine cache manager initialized");

        return cacheManager;
    }

    @Bean
    public StringEncryptor jasyptStringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("mysecret");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");

        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        pooledPBEStringEncryptor.setConfig(config);

        return pooledPBEStringEncryptor;
    }

}
