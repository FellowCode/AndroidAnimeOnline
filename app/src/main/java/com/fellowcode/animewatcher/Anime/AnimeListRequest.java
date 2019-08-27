package com.fellowcode.animewatcher.Anime;

import com.fellowcode.animewatcher.Api.Link;

public interface AnimeListRequest {
    Link getUrl();

    void onResponse(String response);
}
