package com.example.demo.integration;

import java.io.File;

import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class IntegrationConfig {

     @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory(SftpConfiguration configuration) {

        log.info(configuration.toString());
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(configuration.getHost());
        factory.setPort(configuration.getPort());
        factory.setUser(configuration.getUser());
        factory.setAllowUnknownKeys(true);
        Resource resource = new DefaultResourceLoader().getResource(configuration.getPrivateKey());

        factory.setPrivateKey(resource);

        return new CachingSessionFactory<SftpClient.DirEntry>(factory);
    
    }

    @Bean
    @ServiceActivator(inputChannel = "toSftpChannel")
    public Message handler(SftpConfiguration configuration) {

        SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory(configuration));
        handler.setRemoteDirectoryExpression(new LiteralExpression(configuration.getDirectory()));
        handler.setLoggingEnabled(true);
        handler.setFileNameGenerator(new FileNameGenerator() {

            @Override
            public String generateFileName(Message<?> message) {
                if (message.getPayload() instanceof File) {
                    return ((File) message.getPayload()).getName();
                } else throw new IllegalArgumentException("File expected in message payload");
            }
        });

        return handler(configuration);
    }

    @MessagingGateway
    public interface CustomGateway {

        @Gateway(requestChannel = "toSftpChannel")
        void sendToSftp(File file);
    }
}
