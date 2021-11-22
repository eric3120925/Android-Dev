package com.ericapp.uber;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class StarterApplication extends Application {
    /* Find the ID and key in parse server
     *   The command:
     *   cd /opt/bitnami/parse
     *   then vi config.json*/

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myappID")
                // if defined
                .clientKey("b0YtiYNDxRXp")
                .server("http://13.58.81.222/parse/")
                .build()
        );

        /* connection Test
        ParseObject object = new ParseObject("ExampleObject");
        object.put("myString", "abcdefg");
        object.put("myNum", "123");

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.i("Result","Success");
                }else {
                    Log.i("Result","Fail");
                    e.printStackTrace();
                }
            }
        });

        */

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
