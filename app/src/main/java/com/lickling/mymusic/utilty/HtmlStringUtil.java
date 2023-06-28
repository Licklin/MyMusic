package com.lickling.mymusic.utilty;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Function:  返回各种样式的字符串{Spanned类型}的工具类
 */
public class HtmlStringUtil {

    public static Spanned SongSingerName(String title, String artist){
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(artist))
            return Html.fromHtml("<font color = \"#1e1e1e\">快去听听音乐吧</font>",
                                                Html.FROM_HTML_OPTION_USE_CSS_COLORS);
        if (TextUtils.isEmpty(artist) || artist.equals("<unknown>")) artist = "Unknown";
        String highColor = "#1e1e1e", lowColor = "#34373d";

        String SongInformation = "<font color = "+highColor+"><bold>"+title+"</bold></font>"+
                "<font color = "+lowColor+"><small><bold> - </bold>"+artist+"</small></font>";
        return Html.fromHtml(SongInformation,Html.FROM_HTML_OPTION_USE_CSS_COLORS);
    }

    public static Spanned songName(String title){
        if (TextUtils.isEmpty(title))
            return Html.fromHtml("<font color = \"#EEEEEE\">快去听听音乐吧</font>",
                    Html.FROM_HTML_OPTION_USE_CSS_COLORS);
        String color = "#1e1e1e";
        String SongInformation = "<font color = "+color+"><bold>"+title+"</bold></font>";
        return Html.fromHtml(SongInformation,Html.FROM_HTML_OPTION_USE_CSS_COLORS);
    }

    public static Spanned songArtist(String artist) {
        if (TextUtils.isEmpty(artist))
            return Html.fromHtml("<font color = \"#EEEEEE\">快去听听音乐吧</font>",
                    Html.FROM_HTML_OPTION_USE_CSS_COLORS);
        String color = "#EEEEEE";
        String SongInformation = "<font color = "+color+">"+artist+"</font>";
        return Html.fromHtml(SongInformation,Html.FROM_HTML_OPTION_USE_CSS_COLORS);
    }
    public static String SheetTips(int count){
        return "已有歌单("+count+"个)";
    }
}