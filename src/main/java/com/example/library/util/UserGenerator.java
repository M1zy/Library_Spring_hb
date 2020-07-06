package com.example.library.util;
import com.example.library.domain.Report;
import com.example.library.domain.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class UserGenerator extends ExcelGenerator<User> {
    @Override
    public ByteArrayInputStream toExcel(User user)  {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("user");
            int rowIdx = 0;
            rowIdx = headerToExcel(workbook,userHeader,sheet,rowIdx);
            Row row = sheet.createRow(rowIdx++);
            fillRow(user,row);
            sheet.createRow(rowIdx++);
            rentsToExcel(workbook,sheet,user.getCartSet(),rowIdx);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Report report(User user) {
        Report report = new Report();
        try {
        ByteArrayInputStream in = toExcel(user);
        String date = dateFormat(new Date());
        StringBuilder name = new StringBuilder();
        name.append("User_");
        name.append(user.getName());
        name.append("_Email_");
        name.append(user.getEmail());
        name.append("_report_");
        name.append(date);
        report = new Report(name.toString(),
                byteArrayInputStreamToFile(in, name.toString()), date);
        }
        catch (Exception e){
            report.setStatus(e.getMessage());
        }
        finally {
            return report;
        }

    }
}
