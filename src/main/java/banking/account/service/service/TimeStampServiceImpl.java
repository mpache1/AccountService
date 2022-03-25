package banking.account.service.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TimeStampServiceImpl implements TimeStampService {

  @Override
  public String generateTimeStamp() {
    return new SimpleDateFormat("dd.MM.yyyy-HH:mm").format(new Date());
  }
}
