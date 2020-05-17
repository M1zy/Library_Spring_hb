package com.example.library.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;

@Log4j2
@Configuration
public class FtpConfiguration {

    @Bean
    public DefaultFtpSessionFactory sf() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("192.168.43.5");
        sf.setUsername("Mizy");
        sf.setPassword("pagani12");
        return sf;
    }

    @Bean
    public FtpRemoteFileTemplate template(DefaultFtpSessionFactory sf) {
        return new FtpRemoteFileTemplate(sf);
    }
}