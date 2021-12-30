package com.example.board;

import android.os.Handler;
import android.os.Message;

public class Time extends Thread {



    boolean isRun=true;

    Handler handler;

    int milliSeconds, seconds;
    public Time(Handler handler)
    {
        this.handler=handler;
        milliSeconds = 0;
        seconds = 0;
    }

    @Override
    public void run() {
        super.run();
        while(true)

        {
            if(isRun)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                milliSeconds++;
                if (milliSeconds == 10) {
                    milliSeconds = 0;
                    seconds++;
                }
                msg.arg1 = milliSeconds;
                msg.arg2 = seconds;
                handler.sendMessage(msg);

            }
        }
    }
}
