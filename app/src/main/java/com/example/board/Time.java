package com.example.board;

import android.os.Handler;
import android.os.Message;

public class Time extends Thread {



    boolean isRun=true;

    Handler handler;

    int milliSeconds, seconds, minutes;//מילי שניות,שניות,דקות
    public Time(Handler handler)
    {
        this.handler=handler;
        milliSeconds = 0;
        seconds = 0;
        minutes = 0;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public void run() {//מימוש הפעולה run שרצה מתי שהthread מתחיל
        super.run();
        while(true)


        {
            if(isRun)
            {
                try {
                    Thread.sleep(100);//מחכה עשירית שניה
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isRun) {
                    Message msg = new Message();
                    milliSeconds++;

                    //העלאת שניה אחרי 10 מילי שניות
                    if (milliSeconds == 10) {
                        milliSeconds = 0;
                        seconds++;
                    }
                    //העלאת דקה אחרי 60 שניות
                    if (seconds == 60) {
                        minutes++;
                        seconds = 0;
                    }
                    msg.arg1 = milliSeconds;
                    msg.arg2 = seconds;

                    handler.sendMessage(msg);//שליחת הזמן המעודכן לhandler
                }

            }
        }
    }
}
