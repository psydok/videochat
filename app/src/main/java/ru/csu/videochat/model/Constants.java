package ru.csu.videochat.model;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_CATEGORIES = "categories";
    public static final String KEY_COLLECTION_CATEGORIES_CHATS = "chats";

    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_CHAT_ID = "chat_id";

    //    public static final String KEY_CATEGORY_NAME = "name";

    public static final String KEY_CHAT_UID_FIRST_TOKEN = "uid1_token";
    public static final String KEY_CHAT_UID_SECOND_TOKEN = "uid2_token";

    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_FCM_TOKEN = "fcm_token";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";


    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION,
                "AAAAr8AqImI:APA91bHtpbIC41x-8k-1N__uEoXTbM9UlgpcfX06T0s_w6EylnzE1lqEGuTmlgyr9bhxtoDW0G9s-0qBo5aGdJOpUp6R-k-4tfA9gddqBZoviTZCu9boPI1Vi0QgrkJrrYEeuZkcyaS2");
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }
}
