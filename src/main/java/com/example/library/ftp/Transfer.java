package com.example.library.ftp;

import com.example.library.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class Transfer {
    @Autowired
    FtpClientWrapper ftpClientWrapper;

    public void uploadFile(MultipartFile file) throws IOException {
        ftpClientWrapper.putFileToPath(new BufferedInputStream(file.getInputStream()),
                file.getOriginalFilename());
    }

    public void downloadFile(Book book) throws IOException {
        ftpClientWrapper.downloadFile("/Books/"+book.getFile(),book.getFile());
    }

    public List<String> files(String path) throws IOException {
        return ftpClientWrapper.listFiles(path);
    }
}
