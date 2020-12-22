package ru.csu.videochat.model.utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_CATEGORIES = "categories";
    public static final String KEY_COLLECTION_CHATS = "chats";

    public static final String KEY_CATEGORY_ID = "category";
    public static final String KEY_CHAT_ID = "chat_id";
    public static final String KEY_USER_ID = "uidToken";
    public static final String KEY_AGE_ID = "yourAge";

    public static final String KEY_CHAT_UID_FIRST_TOKEN = "uid1Token";
    public static final String KEY_CHAT_UID_SECOND_TOKEN = "uid2Token";

    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_FCM_TOKEN = "fcm_token";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";

    public static final String REMOTE_MSG_INVITATION = "invitation";

    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";

    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";

    public static final String KEY_YOUR_AGE = "your_age";
    public static final String KEY_COMPANION_AGES = "companion_age";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN = "token";


    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAr8AqImI:APA91bHtpbIC41x-8k-1N__uEoXTbM9UlgpcfX06T0s_w6EylnzE1lqEGuTmlgyr9bhxtoDW0G9s-0qBo5aGdJOpUp6R-k-4tfA9gddqBZoviTZCu9boPI1Vi0QgrkJrrYEeuZkcyaS2");
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }
}
