package com.example.library.ftp;

import com.example.library.domain.Book;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FtpService {
    @Value("${ftp.defaultDirectory}") String path;

    @Autowired
    private FtpRemoteFileTemplate template;

    public List<String> listFilenames(String path) {
        FTPFile[] files = template.execute(session -> session.list(path));
        return Arrays.stream(files).map(x->x.getName()).collect(Collectors.toList());
    }

    public void uploadFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        template.setRemoteDirectoryExpression(new LiteralExpression(path));
        template.send(new GenericMessage<>(file));
    }

    public void downloadFile(Book book) {
        String source = "/" + path + "/" + book.getPath();
        String destination = book.getPath();
        template.get(source,
                inputStream -> FileCopyUtils.copy(inputStream,
                        new FileOutputStream(new File(destination))));
    }
}