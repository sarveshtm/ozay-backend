package com.ozay.resultsetextractor;

import com.ozay.model.AccountInformation;
import com.ozay.model.Member;
import com.ozay.model.NotificationRecord;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by naofumiezaki on 6/12/15.
 */
public class NotificationRecordResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<NotificationRecord> list = new ArrayList<NotificationRecord>();


        long previousId = 0;

        NotificationRecord notificationRecord = null;
        Member member = null;
        while(rs.next()){
            if(previousId == 0 || previousId != rs.getLong("id") ){
                notificationRecord = new NotificationRecord();
            }
            member = new Member();


        }



        return list;
    }
}
