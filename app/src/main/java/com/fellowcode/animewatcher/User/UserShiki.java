package com.fellowcode.animewatcher.User;

import android.content.Context;
import android.util.Log;

import com.fellowcode.animewatcher.Utils.Serialize;

import java.io.Serializable;

public class UserShiki implements Serializable {

    public String accessToken;
    public String refreshToken;
    public int endDateTime;
    public int id;

    public String imageUrl;
    public String nickname;

    public UserShiki(Context context){
        load(context);
    }

    void load(Context context){
        UserShiki userShiki = Serialize.read(context, "userShiki");
        if (userShiki != null) {
            accessToken = userShiki.accessToken;
            refreshToken = userShiki.refreshToken;
            endDateTime = userShiki.endDateTime;
            id = userShiki.id;
            imageUrl = userShiki.imageUrl;
            nickname = userShiki.nickname;
        }
    }

    public void save(Context context){
        Serialize.write(context,"userShiki", this);
    }

    public boolean isAuthenticated(){
        return accessToken != null && refreshToken != null && endDateTime != 0 && id != 0;
    }
}
