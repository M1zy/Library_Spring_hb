package com.example.library.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FtpClientWrapper {

    @Autowired
    private FtpRemoteFileTemplate template;

    public List<String> listFiles(String path) {
        FTPFile[] files = template.execute(session -> session.list(path));
        return Arrays.stream(files).map(x->x.getName()).collect(Collectors.toList());
    }

    public void putFileToPath(InputStream inputStream, String path) throws IOException {
        File file = new File(path);
        FileUtils.copyInputStreamToFile(inputStream, file);
        template.setRemoteDirectoryExpression(new LiteralExpression("Books"));
        template.send(new GenericMessage<>(file));
    }

    public void downloadFile(String source, String destination) {
        template.get(source,
                inputStream -> FileCopyUtils.copy(inputStream,
                        new FileOutputStream(new File(destination))));
    }
}