package ba.unsa.etf.cinebh.service.other;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

  public List<String[]> readCsv(MultipartFile file) throws Exception {
    List<String[]> rows = new ArrayList<>();
    try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
      String[] fields;
      while ((fields = csvReader.readNext()) != null) {
        rows.add(fields);
      }
    }
    return rows;
  }
}
