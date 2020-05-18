package com.example.library.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;

@Log4j2
@Configuration
public class FtpConfiguration {

    @Bean
    public DefaultFtpSessionFactory sf(
            @Value("${ftp.username}") String username,
            @Value("${ftp.password}") String pw,
            @Value("${ftp.host}") String host,
            @Value("${ftp.port}") int port
    ) {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost(host);
        sf.setUsername(username);
        sf.setPassword(pw);
        sf.setPort(port);
        return sf;
    }

    @Bean
    public FtpRemoteFileTemplate template(DefaultFtpSessionFactory sf) {
        return new FtpRemoteFileTemplate(sf);
    }
}