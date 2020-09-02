package com.demo.payo.utils;


import com.demo.payo.model.SmsDto;

import java.util.List;

public interface IsNewSMS {
    void IsReceivedSMS( List<SmsDto> smsDtoList);
}
