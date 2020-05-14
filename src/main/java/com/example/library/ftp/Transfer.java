package com.example.library.ftp;

import com.example.library.domain.Book;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedInputStream;
import java.io.IOException;

public class Transfer {
    private FtpClient ftpClient = new FtpClient();

    public void uploadFile(MultipartFile file) throws IOException {
        ftpClient.open();
        ftpClient.putFileToPath(new BufferedInputStream(file.getInputStream()),"/Books/"+file.getOriginalFilename());
        ftpClient.close();
    }

    public void downloadFile(Book book) throws IOException {
        ftpClient.open();
        ftpClient.downloadFile("/Books/"+book.getFile(),book.getFile());
        ftpClient.close();
    }
}
